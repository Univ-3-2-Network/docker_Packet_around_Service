# --- Build stage ---
  FROM gradle:8.10.2-jdk21-alpine AS Build
  WORKDIR /workspace
  COPY . .
  RUN gradle clean bootJar --no-daemon

# --- Run Stage ---
  From eclipse-temurin:21-jre-alpine
  WORKDIR /app
  COPY --from=build /workspace/build/libs/*-SNAPSHOT.jar app.jar
  # JAVA Tunning option || 1. env, 2. mem 변동 조정
  ENV JAVA_OPTS="-XX:MaxRAMPercentages=75 -XX:+UseZGC"
  EXPOSE 8080
  HEALTHCHECK --interval=10s --timeout=3s --retries=10 CMD wget -qO- http://localhost:8080/actuator/health | grep -q '"status":"UP' || exit 1
  ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]