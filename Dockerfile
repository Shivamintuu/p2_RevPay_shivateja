# Build stage
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/RevpayP2-0.0.1-SNAPSHOT.jar app.jar

# The application listens on port 8099 by default (as configured in application.properties)
# but will respect the PORT environment variable injected by Render.
EXPOSE 8099

CMD ["java", "-jar", "app.jar"]