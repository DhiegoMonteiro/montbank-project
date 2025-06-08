FROM eclipse-temurin:17-jdk-alpine

WORKDIR /email

COPY target/email-0.0.1-SNAPSHOT.jar email.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "email.jar"]