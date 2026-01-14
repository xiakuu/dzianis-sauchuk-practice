FROM openjdk:26-ea-trixie AS build
WORKDIR /app
COPY .. .
RUN ./mvnw clean package -DskipTests

FROM openjdk:26-ea-trixie
WORKDIR /app
COPY --from=build /app/target/practice-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "practice-0.0.1-SNAPSHOT.jar"]

EXPOSE 8000