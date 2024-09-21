FROM alpine/java:21-jdk
ADD ./target/*.jar image-server-api.jar
EXPOSE 8085
CMD ["java", "-jar", "image-server-api.jar"]
