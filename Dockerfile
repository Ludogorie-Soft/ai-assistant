FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage (Eclipse Temurin - openjdk image is deprecated)
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/broker-bot-0.0.1-SNAPSHOT.jar app.jar

RUN chmod +x /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
