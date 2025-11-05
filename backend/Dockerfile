# --- Build stage ---
FROM gradle:8.10.2-jdk21-alpine AS build
WORKDIR /workspace
COPY . .
RUN if [ -f ./gradlew ]; then chmod +x ./gradlew && ./gradlew clean bootJar --no-daemon; else gradle clean bootJar --no-daemon; fi

# --- Run stage ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /workspace/build/libs/*-SNAPSHOT.jar app.jar
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseZGC"
EXPOSE 8080
HEALTHCHECK --interval=10s --timeout=3s --retries=10 CMD wget -qO- http://localhost:8080/actuator/health | grep -q '"status":"UP"' || exit 1
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
