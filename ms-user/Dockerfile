FROM eclipse-temurin:17-jdk-alpine

WORKDIR /user

COPY target/ms-user-0.0.1-SNAPSHOT.jar user.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "user.jar"]