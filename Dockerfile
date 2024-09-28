FROM alpine/java:21-jdk
RUN mkdir -p /uploads
RUN chmod -R 777 /uploads
ADD ./target/*.jar file-server-api.jar
EXPOSE 8085
CMD ["java", "-jar", "file-server-api.jar"]
