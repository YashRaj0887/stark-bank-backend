# 1. Build Stage (Use Java 21)
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
# Keep the memory limit just to be safe
ENV MAVEN_OPTS="-Xmx350m"
RUN mvn clean package -DskipTests

# 2. Run Stage (Use Java 21)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/banking-app-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]