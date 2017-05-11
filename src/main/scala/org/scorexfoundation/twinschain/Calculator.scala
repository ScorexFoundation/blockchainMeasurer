package org.scorexfoundation.twinschain

trait Calculator {
  //find minimal i, that $perSent or less of data is less or equal to i
  def persentile(perSent: Double, data: Seq[Int]): Int = {
    val length = data.length
    def loop(i: Int): Int = {
      val count = data.count(_ <= i)
      if (count.toDouble / length > perSent) {
        i
      } else {
        loop(i + 1)
      }
    }
    loop(0)
  }

  def analyzeTails(tails: Seq[Seq[String]]): String = {
    val diffs = tails.map { t =>
      tails.map(_.count(s => !t.contains(s)))
    }
    val persentiles: Seq[(Int, Int)] = diffs.map { d =>
      (persentile(0.5, d), persentile(0.9, d))
    }
    persentiles.map(_._1).mkString(",") + ",|," + persentiles.map(_._2).mkString(",") + ",|," +
      persentile(0.5, persentiles.map(_._1)) + ",|," + persentile(0.9, persentiles.map(_._2))
  }

}
