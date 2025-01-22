# Use an OpenJDK 17 image as the base
FROM 522814707388.dkr.ecr.us-east-2.amazonaws.com/java-image:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY build/libs/TafUserMS.jar app.jar

# Expose the port your application runs on (optional but recommended)
EXPOSE 8081

# Set the command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]