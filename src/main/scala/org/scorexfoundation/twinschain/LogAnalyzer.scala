package org.scorexfoundation.twinschain

import scala.sys.process._

/**
  * Analyze stats from log format
  */
object LogAnalyzer extends App with Calculator with Settings {

  logDownloader("/home/ubuntu/data/data/tails.data")

  def logDownloader(logPath: String): Unit = {
    Nodes.foreach { n =>
      val command = s"scp ubuntu@$n:$logPath data/newLogs/$n.stats"
      println(command)
      command.!
    }
  }

}
