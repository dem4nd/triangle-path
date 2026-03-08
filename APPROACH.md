# Approach

## Problem Overview

The task is to find a minimal path from the top of a numeric triangle to its bottom.

Starting from the top element, each step can move to one of the two adjacent elements in the next row.

The goal is to determine:

- the minimal sum of values along such a path
- the sequence of values forming that path

## Why Not Brute Force

For a triangle with n rows, a path from the top to the bottom consists of (n-1) steps.

At each step there are two possible choices: move to the left child or to the right child.

Therefore the set of all possible paths forms a complete binary decision tree
of depth (n-1), which contains: `2^(n-1)` possible paths.

For example, with 500 rows the number of possible paths is:

2^499 ~ 1.6 x 10^150

Enumerating all possible paths becomes impractical.

Instead, the solution uses a **bottom-up dynamic programming approach** that computes the optimal path in **O(n^2)** time.

## Processing Pipeline

The main processing flow is expressed as a simple pipeline:

```scala
def findPath(rows: List[String]): Either[TriangleError, (Int, List[Int])] =
  for
    triangle <- parseInput(rows)
    validTriangle <- validateTriangle(triangle)
  yield
    validTriangle
      .pipe(foldLayers)
      .pipe(collectPath)
```

The stages are:

- `parseInput`: convert input strings into a triangle structure
- `validateTriangle`: ensure the triangle shape is correct
- `foldLayers`: compute the optimal path using bottom-up dynamic programming
- `collectPath`: reconstruct the path and return the minimal sum

This keeps parsing, validation, solving, and result reconstruction clearly separated.

The first two stages may return errors wrapped in `Either`.

- `parseInput` may fail if some values cannot be parsed as integers
- `validateTriangle` verifies the triangle shape

Once these steps succeed, the remaining stages operate on already validated data.  
`foldLayers` and `collectPath` therefore assume valid input and do not perform additional error handling.

## Core Data Model

The dynamic programming state is represented by `PathNode` objects.

Each node stores:

- the current value
- the best sum achievable from this node to the bottom
- a reference to the next node in the optimal path

Conceptually:

```scala
case class PathNode(
  value: Int,
  bestSum: Int,
  next: Option[PathNode] = None // None for bottom layer
)
```

This allows the algorithm to compute optimal sums while also preserving the path structure.

## Bottom-Up Folding Strategy

The triangle is processed **from the bottom row upward**.

The bottom row is first converted into initial `PathNode` objects.
Each higher row then computes its optimal nodes using the two child nodes below it.

```scala
def foldLayers(layers: Triangle): PathNode =
  val itr = layers.reverseIterator
  val bottomPathNodes = itr.next().map(v => PathNode(v, v))

  itr
    .foldLeft(bottomPathNodes) { (prevLayer, rowValues) =>
      rowValues
        .lazyZip(prevLayer)
        .lazyZip(prevLayer.tail)
        .map { (v, left, right) => left.better(right).goUp(v) }
    }
    .head
```

For each element `v` in a row:

1. the better of the two nodes below is selected using `left.better(right)`
2. a new parent node is created above it using `goUp()`, extending the selected child node

The `goUp()` method creates a new parent node by adding `v` to the child's `bestSum`
and linking to that child.

The use of `lazyZip` avoids creating intermediate collections. 
Values are combined lazily and consumed directly by `map`, reducing temporary allocations.

## Path Reconstruction

After folding all layers, only the **top `PathNode`** remains.

The final path is reconstructed by following the `next` references until the bottom of the triangle is reached.

This operation takes **O(n)** time.

## Complexity Analysis

Let *n* be the number of rows.

The number of triangle elements is:

```
1 + 2 + ... + n = n(n+1)/2 = O(n^2)
```

### Time Complexity

- parsing input: `O(n^2)`
- folding layers: `O(n^2)`
- path reconstruction: `O(n)`

Overall:

```
Time complexity: O(n^2)
```

### Space Complexity

Each triangle element can produce a `PathNode`.  
Because nodes reference lower layers through `next`, earlier nodes may remain reachable.

Therefore:

```
Space complexity: O(n^2) in the worst case
```

## Design Choices

Key design decisions in this implementation:

- immutable data structures
- explicit typed error handling using `Either`
- clear separation between parsing, validation, solving, and result extraction

These choices improve readability, correctness, and testability of the solution.

## Testing Strategy

Unit tests cover:

- parsing correctness
- triangle validation
- path calculation
- edge cases such as empty input, invalid shape, and invalid numbers

Additionally, large generated triangles were used to verify performance on large inputs.

## Notes

The repository includes a helper script `gen_descending` that generates test triangles of arbitrary size.

Example:

```bash
./util/gen_descending 5
```

A large example input with **5000 rows** is included for performance testing (`example_5000_rows.txt.gz`). The file is provided in compressed form.

Example run with the large input without unpacking and with execution time measurement:

```bash
gzip -dc data-sample/example_5000_rows.txt.gz | time java -jar triangle/build/libs/triangle-all.jar
```

Example execution time on a typical developer machine (5000-row input): **~1.2 s**
