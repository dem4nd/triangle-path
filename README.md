# Triangle Minimal Path Finder

## Problem Description

This project implements a solution for finding the minimal path from the top to the bottom of a number triangle.

At each step you may move only to one of the two adjacent numbers in the row directly below.

Example triangle:

```
7
6 3
3 8 5
11 2 10 9
```

Minimal path:

```
7 + 6 + 3 + 2 = 18
```

The solution uses a bottom-up dynamic programming approach, which computes the minimal path efficiently without exploring all possible paths.

Additional details are documented in:

- **ASSUMPTIONS.md** — input assumptions and constraints
- **APPROACH.md** — algorithm explanation, complexity analysis, and implementation details

---

## Setup

### Requirements

- Java 21

The project uses the **Gradle Wrapper**, which is included in the repository,
so no local Gradle installation is required.

### Optional: Environment Setup with mise

The project includes a `mise.toml` configuration that allows automatic installation of required tools.

[`mise`](https://mise.jdx.dev/) is a development environment manager that can install and manage tool versions automatically.

Install tools:

```bash
mise install
```

Activate the environment:

```bash
mise activate
```

## Build

Compile the project:

```bash
./gradlew build
```

## Run Unit Tests

Run all unit tests:

```bash
./gradlew test
```

Force re-run all tests:

```bash
./gradlew test --rerun-tasks
```

## Run Application

The application reads the triangle **from standard input**.

Example input file:

```
data-sample/example_4_rows.txt
```

### Option 1 — Run with Gradle

```bash
./gradlew -q :triangle:run < data-sample/example_4_rows.txt
```

The `-q` flag suppresses Gradle output so that only the program result is printed.

Example output:

```
Minimal path is: 7 + 6 + 3 + 2 = 18
```

#### Example: Empty Input

The application also handles empty input.

```bash
./gradlew -q :triangle:run < /dev/null
```

Example output:

```
Triangle is empty
```

### Option 2 — Run Compiled JAR with Java

Build the executable JAR:

```bash
./gradlew :triangle:shadowJar
```

Run the application:

```bash
java -jar triangle/build/libs/triangle-all.jar < data-sample/example_4_rows.txt
```

### Option 3 — Run with Docker (no Java or Gradle required)

Build the Docker image:

```bash
docker build -t triangle-path .
```

Run the container:

```bash
docker run -i triangle-path < data-sample/example_4_rows.txt
```

or

```bash
cat data-sample/example_4_rows.txt | docker run -i triangle-path
```

The `-i` flag keeps STDIN open so the application can read the triangle input.

## Project Structure

```
triangle/
  src/main/scala/triangle/
    MinTrianglePath.scala          # CLI entry point
    TrianglePathFinder.scala       # core algorithm implementation

  src/test/scala/triangle/
    TrianglePathFinderSpec.scala   # unit tests

data-sample/
  example_4_rows.txt               # example triangle input
  example_5000_rows.txt.gz         # example triangle input (5K rows, gzipped)
  
util/
  gen_descending                   # bash script generating triangle test data 

README.md                          # project overview and usage
ASSUMPTIONS.md                     # input assumptions and constraints
APPROACH.md                        # algorithm explanation and design notes
```

## Algorithm Complexity

Let **n** be the number of rows in the triangle.

- Time complexity: O(n^2)
- Space complexity: O(n^2) in the worst case

The algorithm processes the triangle **bottom-up**, computing minimal partial sums for each layer.

## Implementation Notes

For a detailed explanation of the algorithm and design decisions, see **APPROACH.md**.

The implementation focuses on:

- immutable data structures
- clear separation of parsing, validation, and solving stages
- deterministic path reconstruction
- comprehensive unit testing
