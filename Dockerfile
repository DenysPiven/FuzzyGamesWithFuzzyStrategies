# Use the official Maven image to create a build artifact
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -f pom.xml clean package

# Use the official OpenJDK image to run the application
FROM openjdk:17
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
