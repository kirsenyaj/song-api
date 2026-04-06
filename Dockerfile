# ==========================================
# Stage 1: Build the Application
# ==========================================
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy the Maven wrapper and pom.xml first to cache dependencies
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Grant execution rights to the Maven wrapper
RUN chmod +x ./mvnw

# Download dependencies (this step is cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline

# Copy the actual source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# ==========================================
# Stage 2: Run the Application
# ==========================================
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Copy the compiled .jar file from the 'build' stage
COPY --from=build /app/target/*.jar app.jar

# Expose the standard port
EXPOSE 8080

# Execute the application
ENTRYPOINT ["java", "-jar", "app.jar"]