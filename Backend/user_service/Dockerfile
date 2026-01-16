# Use multi-stage build for smaller final image
FROM maven:3.9.4-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -B
COPY src ./src
RUN ./mvnw package -DskipTests -B

# Final image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Install curl for health checks
RUN apk add --no-cache curl

# Copy the built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Azure-specific optimizations (kept for compatibility)
EXPOSE 9020
ENV PORT=9020

# Render-compatible health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:${PORT}/actuator/health || exit 1

# Use environment variables passed at runtime
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar /app/app.jar"]