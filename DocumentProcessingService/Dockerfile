FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml ./
COPY DocumentProcessingService/pom.xml ./DocumentProcessingService/pom.xml
RUN mvn -B -N install
COPY . .
RUN mvn -B -pl DocumentProcessingService -am -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/DocumentProcessingService/target/DocumentProcessingService-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]