# 베이스 이미지 설정
FROM openjdk:17-jdk-slim

# 작업 디렉토리 생성
WORKDIR /app

# JAR 파일 복사
COPY build/libs/management-0.0.1-SNAPSHOT.jar app.jar
COPY vacation.hwp vacation.hwp


# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
