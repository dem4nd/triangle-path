# triangle-path

A Gradle project for a triangle path task.

## Stack

- Java 21
- Scala 3
- Gradle 9
- module: `triangle`

## Setup

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
echo input.txt | java -jar triangle/build/libs/triangle-all.jar
```

## Run in docker image

Build:

```bash
docker build -t triangle-path .
```

Run:

```bash
docker run --rm -i triangle-path < input.txt
```
