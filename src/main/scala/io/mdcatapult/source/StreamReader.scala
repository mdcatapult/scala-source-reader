package io.mdcatapult.source

trait StreamReader {

  /**
    * Read from an InputStream of a Source and convert its bytes into text.
    *
    * @param source to read from
    * @return extracted text
    */
  def readText(source: Source): String
}
