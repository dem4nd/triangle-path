package triangle

import scala.io.Source
import scala.util.chaining._


object MinTrianglePath:
  def main(args: Array[String]): Unit =
    Source.stdin.getLines().toList
      .pipe((new TrianglePathFinder).findPath)
      .pipe {
        case Right(total, path) => path.mkString("Minimal path is: ", " + ", s" = $total")
        case Left(error) => error.message
      }
      .pipe(println)
