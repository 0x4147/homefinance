FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/homefinance-0.0.1-SNAPSHOT.jar homefinance.jar

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005" , "-jar", "homefinance.jar"]
