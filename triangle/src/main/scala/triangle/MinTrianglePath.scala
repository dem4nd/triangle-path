package triangle

object MinTrianglePath:
  def main(args: Array[String]): Unit =
    println(
      (new TrianglePathFinder).findPath(Seq.empty).mkString("Minimal path is: ", " ", "")
    )
