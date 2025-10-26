# =========================================
# Production-Grade Multi-Stage Dockerfile
# For: Invoice Rules API (Spring Boot)
# =========================================

# Stage 1: Build
FROM eclipse-temurin:17-jdk as builder
WORKDIR /app

# Copy only pom.xml first (layer caching for deps)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre

# Metadata
LABEL maintainer="Sivasankar Thalavai <developer@example.com>" \
      project="Invoice Rules API" \
      version="1.0.0"

# Set working directory
WORKDIR /app

# Copy executable JAR from build stage
COPY --from=build /app/target/invoice-rules-api*.jar invoice-rules-api.jar

# Set runtime env vars
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Expose app port
EXPOSE 8080

# Health check (optional for Docker/Kubernetes)
HEALTHCHECK --interval=30s --timeout=5s CMD curl -f http://localhost:8080/actuator/health || exit 1

# Default entrypoint
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]