package org.scorexfoundation.twinschain

import scala.io.Source
import scala.sys.process._
import scala.util.{Failure, Success, Try}

/**
  * Analyze stats from log format
  */
object LogAnalyzer extends App with Calculator with Settings {


  val initialTime = 1494504700000L
  val statsLines: Seq[Iterator[(Long, Array[String])]] = Nodes.map(n => s"data/newLogs/$n.stats").map { fn =>
    val lines: Iterator[String] = Source.fromFile(fn).getLines()
    lines.map(_.split(":")).map(l => (BigInt(l.head).toLong, l.last.split(",")))
  }
  val logger = new FileLogger("data/R06_2.stats")

//  logDownloader("/home/ubuntu/data/data/tails.data")
  timeLoop(initialTime)

  def timeLoop(time: Long): Unit = {
    Try {
      val tails: Seq[Seq[String]] = statsLines.map(_.find(_._1 >= time).get).map(_._2.toSeq)
      logger.appendString(time.toString + "," + analyzeTails(tails))
    } match {
      case Success(_) =>
        timeLoop(time + 10000)
      case Failure(e) =>
        e.printStackTrace()
    }
  }


  def logDownloader(logPath: String): Unit = {
    Nodes.foreach { n =>
      val command = s"scp ubuntu@$n:$logPath data/newLogs/$n.stats"
      println(command)
      command.!
    }
  }

}
