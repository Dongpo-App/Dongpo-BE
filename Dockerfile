# 베이스 이미지 설정
FROM openjdk:17

# 컨테이너의 작업 경로 설정
WORKDIR /app

# 빌드 결과물을 Docker 컨테이너에 추가 (프로젝트 디렉터리 -> 컨테이너)
COPY ./build/libs/dongpo-0.1.jar /app

EXPOSE 8080

# 컨테이너가 시작될 때 실행될 명령어 설정
CMD ["java", "-Dspring.profiles.active=default,deploy", "-jar", "/app/dongpo-0.1.jar"]