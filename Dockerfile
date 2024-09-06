# Use an official Maven image to build the application
FROM maven AS build

# Set the working directory for the build
WORKDIR /app

# Copy the pom.xml and install dependencies
COPY facility/pom.xml .

# Download dependencies (This will be cached unless pom.xml changes)
RUN mvn dependency:go-offline

# Copy the source code
COPY facility/src ./src

# Package the application
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory for the runtime
WORKDIR /app

# Copy the packaged jar file from the build stage
COPY --from=build /app/target/facility-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]

