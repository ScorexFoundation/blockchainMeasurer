package org.scorexfoundation.twinschain

trait Settings {

  val seedNode = "52.40.210.252"
  val Nodes = seedNode +: Seq("52.88.43.127", "35.167.200.197", "35.163.27.226", "35.167.74.210", "54.70.0.50",
    "52.89.211.42", "35.167.43.13", "35.167.184.105", "35.165.178.93", "54.191.32.214", "52.26.217.230",
    "52.24.190.196", "34.209.224.39", "52.25.11.49", "52.41.61.115", "35.163.92.164", "35.161.164.5", "52.40.158.125",
    "52.24.217.224")
  val Port = 9085


}
