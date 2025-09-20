FROM maven:3.9.9-eclipse-temurin-17 AS builder

# Store all info in app folder 
WORKDIR /app

COPY pom.xml .

# catch dependency when we agin start the server offline menas
RUN mvn dependency:go-offline -B  

COPY src ./src
RUN mvn clean package -DskipTests

# Runner Stage (Java 17 JDK)
FROM openjdk:17-jdk AS runner
 
WORKDIR /app

# Copy jar (any version jar will be renamed to app.jar)
# copy snpashot jar creted in the builder step and copy in to a runner step places at app.jar 
COPY --from=builder ./app/target/*.jar ./app.jar
#COPY target/analytics-service-0.0.1-SNAPSHOT.jar analytics-service.jar

# Expose ports app runs on
EXPOSE 4005


ENTRYPOINT ["java", "-jar", "app.jar"]
