# Stage 1 Build
FROM maven:3.8.4-openjdk-17-slim AS build

# Change work directory inside container
WORKDIR /app

# Copy repository to the container
COPY . .

# Build the application using Maven
RUN mvn package -Dmaven.test.skip

# Stage 2 Package
FROM openjdk:17-jdk-alpine

# Copy the built JAR file from the "build" container to a new container
COPY --from=build /app/target/Redi-0.0.1-SNAPSHOT.jar /app/Redi.jar

# Define entry point for running the container
ENTRYPOINT ["java","-jar","/app/Redi.jar"]
