FROM eclipse-temurin:25-jdk-alpine
RUN apk add --no-cache tzdata && \
    ln -snf /usr/share/zoneinfo/Asia/Manila /etc/localtime && \
    echo "Asia/Manila" > /etc/timezone
WORKDIR /app
COPY build/libs/file-server-api.jar ./file-server-api.jar
ENTRYPOINT ["java", "-jar", "file-server-api.jar"]