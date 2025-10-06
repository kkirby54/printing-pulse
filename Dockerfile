# Multi-stage build for Spring Boot (Gradle Wrapper)

FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace

# Copy gradle wrapper and build files first for better layer caching
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle

# Copy source
COPY src ./src

# Build fat jar
RUN chmod +x ./gradlew \
  && ./gradlew clean bootJar --no-daemon

# Runtime image
FROM eclipse-temurin:17-jre
ENV JAVA_OPTS="" \
    TZ=Asia/Seoul \
    SERVER_PORT=8080 \
    SPRING_PROFILES_ACTIVE=prod

WORKDIR /app

# Copy boot jar
COPY --from=build /workspace/build/libs/*-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Duser.timezone=Asia/Seoul $JAVA_OPTS -jar /app/app.jar"]