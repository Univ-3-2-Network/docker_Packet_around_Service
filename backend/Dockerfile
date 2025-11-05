# --- Build stage ---
FROM gradle:8.10.2-jdk21-alpine AS build
WORKDIR /workspace
COPY . .

# 오래된 gradle-wrapper(4.x)를 무시하고 새로 세팅
RUN gradle wrapper --gradle-version 8.10.2 --distribution-type bin

# wrapper 버전 확인 (로그 확인용)
RUN ./gradlew --version

# jar 빌드
RUN ./gradlew clean bootJar --no-daemon

# --- Run stage ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /workspace/build/libs/*-SNAPSHOT.jar /app/app.jar
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseZGC"
EXPOSE 8080
HEALTHCHECK --interval=10s --timeout=3s --retries=10 \
  CMD wget -qO- http://localhost:8080/actuator/health | grep -q '"status":"UP"' || exit 1
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]

