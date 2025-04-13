# Use a lightweight Java image
FROM openjdk:17-jdk-alpine

# Set environment variables (optional for future use)
ENV JAVA_OPTS=""

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file (make sure the path is correct after build)
COPY target/*.jar app.jar

# Expose the port your app runs on
EXPOSE 8089

# Run the app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
