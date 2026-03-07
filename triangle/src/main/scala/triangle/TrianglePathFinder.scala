package triangle

import triangle.TrianglePathFinder.PathNode

import scala.annotation.tailrec

object TrianglePathFinder:
  case class PathNode(
    value: Int,
    bestSum: Int,
    next: Option[PathNode] // None for bottom layer
  )

class TrianglePathFinder:

  // TODO Add parse error handling
  def parseInput(rows: List[String]): Either[String, List[List[Int]]] =
    Right(rows.map { s =>
      s.split("\\s+").toList.map(_.toInt)
    })

  def foldLayers(layers: List[List[Int]]): Option[PathNode] = {
    layers.reverse match {
      case bottomLayer :: upLayers =>
        val bottomPathNodes = bottomLayer.map(w => PathNode(w, w, Option.empty[PathNode]))
        upLayers
          .foldLeft(bottomPathNodes) { case (prevPathLayer, weightsLayer) =>
            weightsLayer
              .lazyZip(prevPathLayer)
              .lazyZip(prevPathLayer.tail)
              .map { case (w, left, right) =>
                val betterSide = if (left.bestSum < right.bestSum) left else right
                PathNode(w, w + betterSide.bestSum, Some(betterSide))
              }
          }
          .headOption
      case _ => Option.empty[PathNode]
    }
  }

  def collectPath(topNode: Option[PathNode]): List[Int] = {
    @tailrec
    def moveDownPath(pn: PathNode, collected: List[PathNode]): List[PathNode] =
      pn.next match {
        case Some(next) => moveDownPath(next, pn :: collected)
        case _ => pn :: collected
      }

    topNode.fold(List.empty[Int])(
      moveDownPath(_, List.empty[PathNode])
        .map(_.value)
        .reverse)
  }

  def findPath(rows: List[String]): Either[String, List[Int]] = {
    parseInput(rows) // If parsing fails, the error will propagate through the map chain
      .map(foldLayers)
      .map(collectPath)
  }
