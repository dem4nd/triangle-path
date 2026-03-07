package triangle

import triangle.TrianglePathFinder.PathNode

object TrianglePathFinder:
  case class PathNode(
    sum: Int,
    nextLayerNode: Option[PathNode] // None for bottom layer
  )

class TrianglePathFinder:

  // TODO Add parse error handling
  private def parseInput(rows: List[String]): Either[String, List[List[Int]]] =
    Right(rows.map { s =>
      s.split("\\s+").map(_.toInt)
        .toList
    })

  private def foldLayers(layers: Seq[List[Int]]): List[List[PathNode]] = {
    List.empty
  }

  private def collectPath(layers: List[List[PathNode]]): List[Int] = {
    List.empty
  }

  def findPath(rows: List[String]): Either[String, List[Int]] = {
    parseInput(rows) // If parsing fails, the error will propagate through the map chain
      .map(foldLayers)
      .map(collectPath)
  }
