FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
RUN apt-get update && apt-get install -y git
COPY . .
RUN mvn clean install

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/DocumentProcessingService-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]