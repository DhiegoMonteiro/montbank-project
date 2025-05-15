FROM eclipse-temurin:17-jdk-alpine

WORKDIR /transaction

COPY target/transaction-0.0.1-SNAPSHOT.jar transaction.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "transaction.jar"]