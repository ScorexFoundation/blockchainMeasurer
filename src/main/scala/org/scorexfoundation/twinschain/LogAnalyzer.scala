package org.scorexfoundation.twinschain

import scala.io.Source
import scala.sys.process._
import scala.util.{Failure, Success, Try}

/**
  * Analyze stats from log format
  */
object LogAnalyzer extends App with Calculator with Settings {


  val InitialBlock = 500
  val R = "02"
  val RootPath = s"data/newLogs/$R"
  val ResultPath = s"data/stats/$R.stats"

//  logDownloader("/home/ubuntu/data/data/tails.data", RootPath)
  calculateStats()

  def calculateStats(): Unit = {
    val statsLines: Seq[Seq[(Long, Array[String])]] = Nodes.map(n => s"$RootPath/$n.stats").map { fn =>
      val lines: Seq[String] = Source.fromFile(fn).getLines().toSeq
      lines.map(_.split(":")).map(l => (BigInt(l.head).toLong, l.last.split(",")))
    }
    val logger = new FileLogger(ResultPath)
    val initialTime = statsLines.head.toSeq(InitialBlock)._1

//    logger.clear()
    logger.appendString(s"time,bf50%,bf90%,consensusDelay50%,consensusDelay90%")

    def timeLoop(time: Long): Unit = {
      Try {
        println(s"processing chain at time $time")
        val tails: Seq[Seq[String]] = statsLines.map(a => getTail(a, time)).map(_._2.toSeq)
        val bf = calcBlockDiff(tails)
        val consensusDelay = calcConsensusDelay(statsLines, tails, time)
        logger.appendString(s"${time.toString},${bf._1},${bf._2},${consensusDelay._1},${consensusDelay._2}")
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
