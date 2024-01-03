# Build stage
FROM maven:3.8-jdk-17 AS build

LABEL maintainer="Wesley Bruno" version="0.0.1-SNAPSHOT"

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY . .
RUN mvn clean install

# Final stage
FROM gcr.io/distroless/java17-debian11

COPY --from=build /app/target/samurai-cars-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

USER 1001

ENTRYPOINT ["java", "-jar", "app.jar"]
