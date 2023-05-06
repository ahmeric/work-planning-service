FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/work-planning-service-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
