# Base image for the compilation container
FROM maven:3.9.9-eclipse-temurin-21-jammy AS builder

# Defines the working directory where to execute commands
WORKDIR /project

# Copies the project dependencies
COPY ../pom.xml /project/

# Downloads the project dependencies
RUN mvn dependency:go-offline

# Copies the project code
COPY ../src /project/src

# Compiles the project
RUN mvn -B package -DskipTests

# Base image for the application container
FROM eclipse-temurin:21-jre

# Defines the working directory where the JAR is located
WORKDIR /usr/src/app/

# Copy the JAR from the compilation container
COPY --from=builder /project/target/*.jar /usr/src/app/app.jar

# Indicates the port that exposes the container
EXPOSE 8443

# Command to be executed when running docker run
CMD [ "java", "-jar", "app.jar" ]
