FROM maven:3.9.15-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY src /app/src
COPY pom.xml /app

RUN mvn clean install -DskipTests

FROM eclipse-temurin:21-jre-alpine
LABEL authors="ezequiel"

COPY --from=build /app/target/supportdesk-api-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
