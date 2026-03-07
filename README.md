# triangle-path

A minimal multi-module Gradle project for a triangle path task.

## Stack

- Java 21
- Scala 3
- Gradle 9
- module: `triangle`

## Local setup

Using mise:

```bash
mise install
```

If `gradle-wrapper.jar` is not committed yet, generate it once:

```bash
gradle wrapper
```

Then use Gradle normally:

```bash
./gradlew build
./gradlew test
./gradlew :triangle:run
./gradlew :triangle:shadowJar
```

## Run locally

Build the fat jar:

```bash
./gradlew :triangle:shadowJar
```

Run it:

```bash
java -jar triangle/build/libs/triangle-all.jar
```

You can also run the main class directly with classpath:

```bash
java -cp triangle/build/libs/triangle-all.jar triangle.MinTrianglePath
```

`java MinTrianglePath` is not the standard way to run a Gradle/Scala project, because Java needs either a classpath or a jar with a manifest.

## Production Docker image

Build:

```bash
docker build -t triangle-path .
```

Run:

```bash
docker run --rm triangle-path
```
