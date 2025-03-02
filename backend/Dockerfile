# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the target folder to the working directory
COPY target/backend-0.0.1-SNAPSHOT.jar /app/backend-0.0.1-SNAPSHOT.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]

