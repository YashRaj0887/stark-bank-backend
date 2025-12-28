# 1. Use an official Java image to build the app
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# 2. Use a smaller Java image to run the app
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/banking-app-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]