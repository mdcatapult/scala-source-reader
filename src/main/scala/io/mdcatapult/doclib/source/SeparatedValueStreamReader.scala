package io.mdcatapult.doclib.source

import java.io.InputStream
import java.nio.charset.Charset

import org.apache.commons.csv.{CSVFormat, CSVParser, CSVRecord}
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.txt.UniversalEncodingDetector

import scala.jdk.CollectionConverters._

/**
  * Reads CSV and TSV files.  This implementation allow us to define handling of escape characters which differs
  * from the Tika.
  *
  * @param defaultReader delegated to if InputStream is not TSV or CSV
  */
class SeparatedValueStreamReader(defaultReader: StreamReader) extends StreamReader {

  private val encodingDetector = new UniversalEncodingDetector()

  override def readText(input: InputStream, name: String): String =
    if (name.endsWith(".tsv"))
      separatedValueExtract(input, fieldDelimiter = '\t')
    else if (name.endsWith(".csv"))
      separatedValueExtract(input, fieldDelimiter = ',')
    else
      defaultReader.readText(input, name)

  private def separatedValueExtract(input: InputStream, fieldDelimiter: Char): String = {
    val lineDelimiter = "\n"
    val escape = '\\'

    val charset: Charset = encodingDetector.detect(input, new Metadata())
    val p = CSVParser.parse(input, charset, CSVFormat.DEFAULT.withEscape(escape).withDelimiter(fieldDelimiter))

    try {
      val rows: Iterator[CSVRecord] = p.iterator.asScala
      val lines: Iterator[String] = rows.map(_.iterator().asScala.mkString(fieldDelimiter.toString))
      lines.mkString(lineDelimiter)
    } finally {
      p.close()
    }
  }
}
