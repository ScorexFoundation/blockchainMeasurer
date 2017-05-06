package org.scorexfoundation.twinschain

import scala.io.Source

object FileParser extends App with Calculator {

  def oldFormatParser(filename: String): Unit = {
    for (line <- Source.fromFile(filename).getLines()) {
      val values: Seq[Int] = line.split(",").map(_.toInt)
      val per50 = (0 until values.length / 2) map (i => values(i * 2))
      val per90 = (0 until values.length / 2) map (i => values(i * 2 + 1))
      println(per50.mkString(",") + ",|," + per90.mkString(",") + ",|," +
        persentile(0.5, per50) + ",|," + persentile(0.9, per90))
    }
  }

}
