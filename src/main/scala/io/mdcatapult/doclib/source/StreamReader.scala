package io.mdcatapult.doclib.source

import java.io.InputStream

trait StreamReader {

  /**
    * Read an InputStream and convert its bytes into text.
    *
    * @param input bytes to convert
    * @param name name of stream, such as file name or name in an archive
    * @return extracted text
    */
  def readText(input: InputStream, name: String): String
}
