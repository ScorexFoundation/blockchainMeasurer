package org.scorexfoundation.twinschain

import org.scorexfoundation.ApiClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object TwinschainAnalyzer extends App with Settings {

  val LastBlockNum = 50

  def loop(): Unit = {
    Future.sequence(Nodes.map { n =>
      Future {
        ApiClient.getTail(n, Port, LastBlockNum)
      }
    }).onComplete {
      case Success(tails) =>
        val diffs = tails.map { t =>
          tails.map(_.count(s => !t.contains(s)))
        }
        val persentiles: Seq[(Int, Int)] = diffs.map { d =>
          (persentile(0.5, d), persentile(0.9, d))
        }
        println(persentiles.map(_._1).mkString(",") + ",|," + persentiles.map(_._2).mkString(","))
      case Failure(e) =>
      //        e.printStackTrace()
    }

    Thread.sleep(1000 * 10)
    loop()
  }

  loop()

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
