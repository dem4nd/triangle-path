package triangle

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.shouldBe
import triangle.TrianglePathFinder.PathNode

import scala.util.chaining.*


class TrianglePathFinderSpec extends AnyFunSuite:

  val trianglePathFinder = new TrianglePathFinder

  test("parseInput parses a valid triangle") {
    val rows = List(
      "2",
      "3 4",
      "6 5 7"
    )

    trianglePathFinder.parseInput(rows) shouldBe Right(
      List(
        2 :: Nil,
        3 :: 4 :: Nil,
        6 :: 5 ::7 :: Nil
      )
    )
  }

  test("parseInput fails on invalid token in a row") {
    val rows = List(
      "2",
      "3 4",
      "5 X 6"
    )

    trianglePathFinder.parseInput(rows) shouldBe Left(ParseError(2))
  }

  test("parseInput parses empty input as empty triangle") {
    trianglePathFinder.parseInput(Nil) shouldBe Right(Nil)
  }

  test("parseInput parses a row with empty string as empty row") {
    trianglePathFinder.parseInput("" :: Nil) shouldBe Right(List(Nil))
  }

  test("validateTriangle returns error on empty triangle") {
    trianglePathFinder.validateTriangle(Nil) shouldBe Left(EmptyTriangle)
  }

  test("validateTriangle returns error on triangle with single empty row") {
    trianglePathFinder.validateTriangle(List(Nil)) shouldBe Left(InvalidShape(0, 0))
  }

  test("validateTriangle returns error when a row is shorter than expected") {
    val triangle = List(
      1 :: Nil,
      2 :: 3 :: Nil,
      4 :: 6 :: 7 :: Nil,
      8 :: 10 :: Nil, // <--- error is here (2 tokens are omitted)
      13 :: 14 :: 15 :: 16 :: 17 :: Nil
    )

    trianglePathFinder.validateTriangle(triangle) shouldBe Left(InvalidShape(3, 2))
  }

  test("validateTriangle returns error when a row is longer than expected") {
    val triangle = List(
      1 :: Nil,
      2 :: 3 :: Nil,
      4 :: 6 :: 7 :: Nil,
      8 :: 10 :: 11 :: 12 :: 20 :: Nil, // <--- error is here (1 extra token)
      13 :: 14 :: 15 :: 16 :: 17 :: Nil
    )

    trianglePathFinder.validateTriangle(triangle) shouldBe Left(InvalidShape(3, 5))
  }

  test("validateTriangle returns error when triangle contains empty row in the middle") {
    val triangle = List(
      1 :: Nil,
      2 :: 3 :: Nil,
      List.empty[Int], // <--- error is here (empty row)
      8 :: 10 :: 11 :: 12 :: 20 :: Nil,
      13 :: 14 :: 15 :: 16 :: 17 :: Nil
    )

    trianglePathFinder.validateTriangle(triangle) shouldBe Left(InvalidShape(2, 0))
  }

  test("validateTriangle returns Right when triangle shape is valid") {
    val triangle = List(
      1 :: Nil,
      2 :: 3 :: Nil,
      4 :: 5 :: 6 :: Nil,
      7 :: 8 :: 9 :: 10 :: Nil
    )

    trianglePathFinder.validateTriangle(triangle) shouldBe Right(triangle)
  }

  test("foldLayers computes correct bestSum for multi-layer triangle") {
    val triangle = List(
      7 :: Nil,
      6 :: 3 :: Nil,
      3 :: 8 :: 5 :: Nil,
      11 :: 2 :: 10 :: 9 :: Nil
    )

    val node = trianglePathFinder.foldLayers(triangle)

    node.value shouldBe 7
    node.bestSum shouldBe 18
    node.next.isDefined shouldBe true
  }

  test("foldLayers computes correct result for single-layer triangle") {
    val triangle = List(
      7 :: Nil
    )

    val node = trianglePathFinder.foldLayers(triangle)

    node.value shouldBe 7
    node.bestSum shouldBe 7
    node.next.isDefined shouldBe false
  }

  test("foldLayers and collectPath compute correct minimal path") {
    val triangle = List(
      7 :: Nil,
      6 :: 3  :: Nil,
      3 :: 8 :: 5 :: Nil,
      11 :: 2 :: 10 ::  9 ::Nil
    )

    val result =
      trianglePathFinder.foldLayers(triangle)
        .pipe(trianglePathFinder.collectPath)

    result shouldBe 18 -> List(7, 6, 3, 2)
  }

  test("foldLayers selects correct child near the right edge") {
    val triangle = List(
      1 :: Nil,
      9 :: 2 :: Nil,
      9 :: 9 :: 1 :: Nil,
      9 :: 9 :: 9 :: 0 :: Nil
    )

    val result =
      trianglePathFinder.foldLayers(triangle)
        .pipe(trianglePathFinder.collectPath)

    result shouldBe 4 -> List(1, 2, 1, 0)
  }
