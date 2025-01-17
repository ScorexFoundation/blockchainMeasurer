package org.scorexfoundation.twinschain

import java.nio.file.{Files, Path, Paths, StandardOpenOption}

class FileLogger(filePath: String) {

  val path: Path = Paths.get(filePath)
  val f = path.toFile
  f.getParentFile().mkdirs()
  f.createNewFile()

  def appendString(string: String): Unit = {
    Files.write(path, (string + "\n").getBytes(), StandardOpenOption.APPEND)
  }

  def clear(): Unit = {
    Files.write(path, "".getBytes(), StandardOpenOption.WRITE)
  }

}
