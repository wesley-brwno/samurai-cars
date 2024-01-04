# Build stage
FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN apt-get maven:3.8-jdk-17
RUN mvn clean install

# Final stage
FROM openjdk:17-jdk-slim-buster

COPY --from=build /target/samurai-cars-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
