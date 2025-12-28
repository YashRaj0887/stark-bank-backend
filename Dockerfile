# 1. Build Stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
# Limit Maven to 350MB so Render doesn't kill it
ENV MAVEN_OPTS="-Xmx350m"
RUN mvn clean package -DskipTests

# 2. Run Stage
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build /app/target/banking-app-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]