FROM eclipse-temurin:17-jdk-alpine

WORKDIR /api

COPY target/gateway-0.0.1-SNAPSHOT.jar gateway.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "gateway.jar"]