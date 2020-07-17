package io.mdcatapult.doclib.source

import java.io.InputStream

import org.apache.tika.Tika
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.ParseContext
import org.apache.tika.sax.BodyContentHandler

/**
  * Read text from InputStream using Tika.
  */
class TikaTextExtractingStreamReader extends StreamReader {

  private val tika = new Tika()

  override def readText(input: InputStream, name: String): String = {
    val handler = new BodyContentHandler(-1)

    tika.getParser.parse(
      input,
      handler,
      new Metadata(),
      new ParseContext)

    handler.toString
  }
}
