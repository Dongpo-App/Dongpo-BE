# 베이스 이미지 설정
FROM gradle:8.6-jdk17 AS build

# 컨테이너의 작업 경로 설정
WORKDIR /app

# Gradle Wrapper와 관련 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 실제 소스 코드와 JAR 파일 복사
COPY . .

# Gradle 의존성 다운로드 및 빌드
RUN ./gradlew clean build --no-daemon

# 최종 이미지 설정
FROM openjdk:17

# 컨테이너의 작업 경로 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/dongpo-0.1.jar .

EXPOSE 8080

# 컨테이너가 시작될 때 실행될 명령어 설정
CMD ["java", "-Dspring.profiles.active=default,credentials", "-jar", "/app/dongpo-0.1.jar"]