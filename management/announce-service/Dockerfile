# 베이스 이미지 설정
FROM openjdk:17-jdk-slim


#타임존(서울)
ENV TZ=Asia/Seoul
RUN apt-get update && apt-get install -y tzdata && rm -rf /var/lib/apt/lists/*


# 작업 디렉토리 생성
WORKDIR /app


# JAR 파일 복사
COPY build/libs/*-SNAPSHOT.jar app.jar
#COPY vacation.hwp vacation.hwp



# 시스템 업데이트 및 curl 설치 (헬스체크용)
#RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*


# 헬스체크 설정
# HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
#  CMD curl -f http://192.168.0.69:8080/actuator/health || exit 1

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]