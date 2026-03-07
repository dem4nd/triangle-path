package triangle

import org.scalatest.funsuite.AnyFunSuite

class TrianglePathImplSpec extends AnyFunSuite:

  val trianglePathFinder = new TrianglePathFinder

  test("findPath returns the expected path") {
    assert(trianglePathFinder.findPath(List.empty) == Right(List.empty))
  }
