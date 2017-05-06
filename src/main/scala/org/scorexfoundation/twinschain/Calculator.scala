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

}
