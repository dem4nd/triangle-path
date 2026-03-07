FROM gradle:9.3.0-jdk21 AS build
WORKDIR /workspace

COPY gradle gradle
COPY gradlew gradlew
COPY gradlew.bat gradlew.bat
COPY settings.gradle.kts build.gradle.kts gradle.properties ./
COPY triangle triangle

RUN chmod +x gradlew && ./gradlew :triangle:shadowJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/triangle/build/libs/triangle-all.jar ./triangle-path.jar

ENTRYPOINT ["java", "-jar", "triangle-path.jar"]