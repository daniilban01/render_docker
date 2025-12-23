# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Leverage Docker layer caching
COPY pom.xml .
COPY src ./src

# Build the application (runs tests by default)
RUN mvn -B clean package

# ---- Run stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built jar (adjust the pattern if you change the artifact name)
COPY --from=build /workspace/target/*.jar /app/app.jar

# Render will route to the port you set in the service settings.
# This also supports Render's PORT env var if you prefer.
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dserver.port=\${PORT:-8080} -jar /app/app.jar"]
