FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/test.jar /app/test.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/test.jar"]