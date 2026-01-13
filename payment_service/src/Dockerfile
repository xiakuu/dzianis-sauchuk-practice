FROM openjdk:17.0.2-jdk-slim-buster
WORKDIR /app
COPY /target/orderservice-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "orderservice-0.0.1-SNAPSHOT.jar"]

EXPOSE 9193