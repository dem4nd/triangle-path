FROM gradle:9.3.0-jdk21 AS build
WORKDIR /workspace

COPY settings.gradle.kts build.gradle.kts gradle.properties ./

# Pre-download dependencies (cached Docker layer)
RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle :triangle:dependencies --no-daemon || true

COPY triangle triangle

RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle :triangle:shadowJar --no-daemon

FROM gcr.io/distroless/java21-debian12
WORKDIR /app

COPY --from=build /workspace/triangle/build/libs/triangle-all.jar ./triangle-path.jar

ENTRYPOINT ["java", "-jar", "triangle-path.jar"]