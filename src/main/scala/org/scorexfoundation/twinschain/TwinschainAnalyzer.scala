package org.scorexfoundation.twinschain

import org.scorexfoundation.ApiClient

object TwinschainAnalyzer extends App with Settings {

  val LastBlockNum = 10

  def loop(): Unit = {
//    val Nodes = Seq("127.0.0.1")
    val tails = Nodes.map{ n =>
      ApiClient.getTail(n, Port, LastBlockNum)
    }
    println(tails)

    Thread.sleep(1000)
    loop()
  }

  loop()

}
