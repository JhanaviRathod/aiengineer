
# syntax=docker/dockerfile:1

# Stage 1: Build the application
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy the complete Gradle project
COPY . .

# Build the executable Spring Boot JAR
RUN chmod +x gradlew && \
    ./gradlew bootJar --no-daemon

# Stage 2: Run the application
FROM eclipse-temurin:21-jdk AS final

WORKDIR /app

# Run as a non-root user
RUN useradd --system --uid 10001 appuser

COPY --from=build /app/build/libs/*.jar /tmp/app.jar

RUN mv /tmp/app.jar /app/app.jar && \
    chown appuser:appuser /app/app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
