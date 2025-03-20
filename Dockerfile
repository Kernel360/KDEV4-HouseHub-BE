FROM openjdk:17-jdk-slim

WORKDIR /app

COPY ./build/libs/*.jar app.jar

RUN ls -l

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]