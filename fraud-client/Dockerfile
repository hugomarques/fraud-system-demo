FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean bootJar -x test

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar fraud-client.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "fraud-client.jar"]