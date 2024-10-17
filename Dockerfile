FROM jelastic/maven:3.9.5-openjdk-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

FROM alpine/java:21-jdk
RUN mkdir -p /uploads
RUN chmod -R 777 /uploads
WORKDIR /app
COPY --from=build /app/target/*.jar .
CMD ["java", "-jar", "file-server-api.jar"]
