package org.scorexfoundation.twinschain

import org.scorexfoundation.ApiClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object TwinschainAnalyzer extends App with Settings with Calculator {

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
        println(persentiles.map(_._1).mkString(",") + ",|," + persentiles.map(_._2).mkString(",") + ",|," +
          persentile(0.5, persentiles.map(_._1)) + ",|," + persentile(0.9, persentiles.map(_._2)))
      case Failure(e) =>
        e.printStackTrace()
    }

    Thread.sleep(1000 * 10)
    loop()
  }

  loop()
}
