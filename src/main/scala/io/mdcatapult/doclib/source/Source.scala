package io.mdcatapult.doclib.source

import java.io.{File, FileInputStream, InputStream}

object Source {

  /**
    * Create a Source from a file.  The name of the source is the path to the file.
    * @param f file to read
    * @return the source for the file
    */
  def fromFile(f: File): Source =
    Source(new FileInputStream(f), f.getPath)
}

/**
  * Defines some source of text to be read by [[SourceReader]].
  * @param input stream of bytes to convert into text
  * @param name name of stream, such as file name or name in an archive
  */
case class Source(input: InputStream, name: String)
