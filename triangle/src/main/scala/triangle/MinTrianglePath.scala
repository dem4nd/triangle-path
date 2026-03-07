package triangle

import scala.io.Source
import scala.util.chaining._

object MinTrianglePath:
  def main(args: Array[String]): Unit =
    Source.stdin.getLines().toList
      .pipe((new TrianglePathFinder).findPath)
      .pipe {
        case Right(Nil)  => "No path for empty triangle"
        case Right(path) => path.mkString("Minimal path is: ", " ", "")
        case Left(error) => error
      }
      .pipe(println)
