FROM eclipse-temurin:24-jdk-alpine
WORKDIR /app

# Copy the Maven Wrapper files
COPY .mvn/ .mvn/
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd
COPY pom.xml .

# Make mvnw executable
RUN chmod +x mvnw

# Install netcat for health checks
RUN apk add --no-cache netcat-openbsd

# Download dependencies (this layer can be cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline -B

# Copy the project source
COPY src ./src

# Copy .env if it exists (ignore if not present)
RUN cp .env . 2>/dev/null || true

# Build the application
RUN ./mvnw package -DskipTests

# Copy the JAR to WORKDIR
RUN cp target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "--enable-native-access=ALL-UNNAMED", "-jar", "/app/app.jar"]
