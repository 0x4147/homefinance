# Build Stage
FROM maven:3.9.5-eclipse-temurin-17-alpine AS build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

# Runtime Stage
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /build/target/homefinance-0.0.1-SNAPSHOT.jar homefinance.jar
EXPOSE 8585 5005
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8585/health || exit 1
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "homefinance.jar"]
