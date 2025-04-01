# 빌드 스테이지
FROM gradle:7.6.1-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle bootJar # 빌드 실행

# 런타임 스테이지
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
RUN ls -l
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]