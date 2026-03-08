# Assumptions

This document describes the assumptions and constraints regarding the input format expected by the application.

## Triangle Shape

The input must represent a valid triangle.

Row `i` must contain exactly `i` integer values.

If this condition is violated, the input is considered invalid.

## Empty Input

An empty input stream is treated as an empty triangle.

In this case the application reports an error.

## Numbers

All triangle elements are parsed as integers.

The implementation does not impose additional constraints on the numeric values.

However, it is assumed that the sum of values along any path from the top of the triangle to the bottom does not exceed `Int.MaxValue`, since path sums are accumulated using the Scala `Int` type.

## Whitespace

Numbers in a row are separated by whitespace.

Multiple spaces between numbers are allowed.

## Triangle Size

The implementation is designed to handle triangles with several hundred rows efficiently.

In particular, it comfortably supports the **500-row triangle** mentioned in the assignment.

As a practical benchmark, a generated triangle with **5000 rows** (included in the repository as a compressed example) was processed in about **1.2 seconds** on a typical developer machine.

No strict upper bound is enforced by the application, but extremely large inputs may lead to increased memory usage and computation time due to the `O(n^2)` algorithmic complexity.

## Error Handling

The application validates the input and reports errors for the following cases:

- empty triangle
- invalid triangle shape
- invalid integer values