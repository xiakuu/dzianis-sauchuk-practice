FROM openjdk:17.0.2-jdk-slim-buster
WORKDIR /app
COPY ../target/apigateway-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
EXPOSE 8989