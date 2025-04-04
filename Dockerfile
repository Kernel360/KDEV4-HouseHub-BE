# 빌드 스테이지
FROM gradle:7.6.1-jdk17 AS builder
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew	#gradlew 실행 권한 부여
RUN ./gradlew bootJar	#gradlew를 통해 실행 가능한 jar파일 생성

# 런타임 스테이지
FROM openjdk:17-jdk-slim
WORKDIR /app
RUN ls -l
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
