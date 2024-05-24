FROM openjdk:17-alpine
MAINTAINER Elleined

# Docker MySQL Credentials
ENV MYSQL_HOST=mysql_server
ENV MYSQL_USER=root
ENV MYSQL_PASSWORD=root
ENV MYSQL_PORT=3306
ENV MYSQL_DATABASE=image_server_api_db
ENV PORT=8085

ADD ./target/*.jar image-server-api.jar
EXPOSE 8085
CMD ["java", "-jar", "image-server-api.jar"]
