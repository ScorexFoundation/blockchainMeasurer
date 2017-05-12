package org.scorexfoundation.twinschain

import scala.io.Source
import scala.sys.process._
import scala.util.{Failure, Success, Try}

/**
  * Analyze stats from log format
  */
object LogAnalyzer extends App with Calculator with Settings {


  val R = "02"
  val RootPath = s"data/newLogs/$R"
  val ResultPath = s"data/stats/$R.stats"

  logDownloader("/home/ubuntu/data/data/tails.data", RootPath)
  calculateStats()

  def calculateStats(): Unit = {
    val statsLines: Seq[Iterator[(Long, Array[String])]] = Nodes.map(n => s"$RootPath/$n.stats").map { fn =>
      val lines: Iterator[String] = Source.fromFile(fn).getLines()
      lines.map(_.split(":")).map(l => (BigInt(l.head).toLong, l.last.split(",")))
    }
    val logger = new FileLogger(ResultPath)
    val initialTime = statsLines.head.toSeq(100)._1

    def timeLoop(time: Long): Unit = {
      Try {
        println(s"processing chain at time $time")
        val tails: Seq[Seq[String]] = statsLines.map(_.find(_._1 >= time).get).map(_._2.toSeq)
        val bf = calcBlockDiff(tails)
//        val consensusDelay = ???
        logger.appendString(s"${time.toString},${bf._1},${bf._2}")
      } match {
        case Success(_) =>
          timeLoop(time + 10000)
        case Failure(e) =>
          e.printStackTrace()
      }
    }

    timeLoop(initialTime)
  }


  def logDownloader(logPath: String, downloadPath: String): Unit = {
    Nodes.foreach { n =>
      val command = s"scp ubuntu@$n:$logPath $downloadPath/$n.stats"
      println(command)
      command.!
    }
  }

}
