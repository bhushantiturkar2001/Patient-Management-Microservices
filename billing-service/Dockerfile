# FROM maven:3.9.9-eclipse-temurin-21 AS builder 

# WORKDIR /app

# COPY  pom.xml

# RUN mvn dependency:go-offline -B

# COPY src ./src

# RUN mvn clean package

# FROM openjdk:17-jdk AS runner

# WORKDIR /app

# COPY --from=builder ./app/target/billing-service-0.0.1-SNAPSHOT.jar ./app.jar

# EXPOSE 4001
# EXPOSE 9001

# ENTRYPOINT ["java", "-jar","app.jar"]

# Builder Stage (Java 17 + Maven 3.9.9)
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# Runner Stage (Java 17 JDK)
FROM openjdk:17-jdk AS runner

WORKDIR /app

# Copy jar (any version jar will be renamed to app.jar)
COPY --from=builder /app/target/*.jar ./app.jar

# Expose ports
EXPOSE 4001
EXPOSE 9001

ENTRYPOINT ["java", "-jar", "app.jar"]
