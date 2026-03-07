package triangle

import org.scalatest.funsuite.AnyFunSuite
import scala.util.chaining._


class TrianglePathImplSpec extends AnyFunSuite:

  val trianglePathFinder = new TrianglePathFinder

  test("findPath returns empty path for empty triangle") {
    assert(trianglePathFinder.findPath(List.empty) == Right(List.empty))
  }

  test("findPath returns non-empty path for triangle") {
    val triangle = List(
      List(7),
      List(6, 3),
      List(3, 8, 5),
      List(11, 2, 10, 9)
    )

    val path =
      trianglePathFinder.foldLayers(triangle)
        .pipe(trianglePathFinder.collectPath)

    assert(path == List(7, 6, 3, 2))
  }
