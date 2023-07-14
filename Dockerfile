# --- Image ---

FROM eclipse-temurin
 
WORKDIR /app

# Copy files required by Maven
COPY .mvn/ .mvn

# Copy Maven config
COPY mvnw pom.xml ./

# Pull all dependencies for offline-work
RUN ./mvnw dependency:go-offline

# Copy app source files
COPY src ./src

# --- Container ---

# Run app
CMD ["./mvnw", "spring-boot:run"]