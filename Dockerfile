# --- Image ---
#FROM eclipse-temurin
#WORKDIR /app
# Copy files required by Maven
#COPY .mvn/ .mvn
# Copy Maven config
#COPY mvnw pom.xml ./
# Pull all dependencies for offline-work
#RUN ./mvnw dependency:go-offline
# Copy app source files
#COPY src ./src
# --- Container ---
# Run app
#CMD ["./mvnw", "spring-boot:run"]
#
#
#
FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install
 
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
EXPOSE 8080
COPY --from=builder /app/target/*.jar /app/*.jar
ENTRYPOINT ["java", "-jar", "/app/*.jar" ]