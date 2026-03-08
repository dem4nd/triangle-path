package triangle

import triangle.TrianglePathFinder.*

import scala.annotation.tailrec
import scala.util.chaining.*


type Triangle = List[List[Int]]

sealed trait TriangleError:
  def message: String

case class ParseError(rowIndex: Int) extends TriangleError:
  override def message: String = s"Invalid value in row ${rowIndex + 1}"

case object EmptyTriangle extends TriangleError:
  override def message: String = "Triangle is empty"

case class InvalidShape(rowIndex: Int, actualSize: Int) extends TriangleError:
  override def message: String = s"Invalid triangle shape at row ${rowIndex + 1}: expected ${rowIndex + 1} elements, got ${actualSize}"


object TrianglePathFinder:
  case class PathNode(
    value: Int,
    bestSum: Int,
    next: Option[PathNode] = None // None for bottom layer
  ):
    def better(other: PathNode): PathNode =
      if bestSum <= other.bestSum then this else other

    def goUp(v: Int): PathNode = PathNode(v, bestSum + v, Some(this))


class TrianglePathFinder:

  // Parse input rows into triangle layers of integers
  // Returns ParseError if any token cannot be converted to Int
  def parseInput(rows: List[String]): Either[TriangleError, Triangle] =
    rows.zipWithIndex
      .foldLeft[Either[TriangleError, Triangle]](Right(Nil)) {
        case (Left(err), _) => Left(err)

        case (Right(acc), (row, rowIndex)) =>
          val parsed =
            row.split("\\s+").iterator
              .filter(_.nonEmpty)
              .map(_.toIntOption)
              .toList

          if parsed.exists(_.isEmpty) then
            Left(ParseError(rowIndex))
          else
            Right(parsed.flatten :: acc)
      }
      .map(_.reverse)


  // Validate that the triangle is non-empty and that each row has the expected size (1, 2, 3, ...)
  // Returns the original triangle if valid, otherwise the first encountered validation error
  def validateTriangle(triangle: Triangle): Either[TriangleError, Triangle] =
    if triangle.isEmpty then Left(EmptyTriangle)
    else
      triangle.iterator
        .zipWithIndex
        .collectFirst {
          case (row, rowIndex) if row.size != rowIndex + 1 =>
            InvalidShape(rowIndex, row.size)
        }
        .toLeft(triangle)


  // Fold triangle layers from bottom to top to build the optimal path
  // `layers` list is guaranteed to be non-empty because the triangle is validated beforehand
  def foldLayers(layers: Triangle): PathNode =
    val itr = layers.reverseIterator
    val bottomPathNodes = itr.next().map(v => PathNode(v, v))

    itr
      .foldLeft(bottomPathNodes) { (prevLayer, rowValues) =>
        rowValues
          .lazyZip(prevLayer)
          .lazyZip(prevLayer.tail)
          .map { (v, left, right) => left.better(right).goUp(v) }
      }.head


  // Reconstruct optimal path by following `next` pointers from the top node
  def collectPath(topNode: PathNode): (Int, List[Int]) =
    @tailrec
    def moveDownPath(pn: PathNode, collected: List[Int]): List[Int] =
      pn.next match {
        case Some(next) => moveDownPath(next, pn.value :: collected)
        case _ => pn.value :: collected
      }

    topNode.bestSum -> moveDownPath(topNode, Nil).reverse


  // Main processing pipeline
  // parse -> validate -> solve -> collect path
  // Returns (bestSum, pathFromTopToBottom)
  def findPath(rows: List[String]): Either[TriangleError, (Int, List[Int])] =
    for
      triangle <- parseInput(rows)
      validTriangle <- validateTriangle(triangle)
    yield
      validTriangle
        .pipe(foldLayers)
        .pipe(collectPath)

