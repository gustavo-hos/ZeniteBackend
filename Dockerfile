FROM gradle:8.10.2-jdk21-alpine AS build
WORKDIR /workspace
COPY gradlew gradle/ ./
RUN chmod +x gradlew
COPY settings.gradle.kts build.gradle.kts ./
COPY src ./src

RUN --mount=type=cache,target=/home/gradle/.gradle \
    ./gradlew --no-daemon clean bootJar

FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar app.jar
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -Duser.timezone=Europe/Lisbon"
EXPOSE 8080
USER app
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]