FROM eclipse-temurin:17-jdk-alpine

WORKDIR /cards

COPY target/cards-0.0.1-SNAPSHOT.jar cards.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "cards.jar"]