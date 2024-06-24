/*
 * Copyright 2024 Medicines Discovery Catapult
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.mdcatapult.source

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

  override def readText(source: Source): String =
    if (source.name.endsWith(".tsv"))
      separatedValueExtract(source.input, fieldDelimiter = '\t')
    else if (source.name.endsWith(".csv"))
      separatedValueExtract(source.input, fieldDelimiter = ',')
    else
      defaultReader.readText(source)

  private def separatedValueExtract(input: InputStream, fieldDelimiter: Char): String = {
    val lineDelimiter = "\n"
    val escape = '\\'

    val charset: Charset = encodingDetector.detect(input, new Metadata())
    val csvFormat = CSVFormat.Builder.create().setEscape(escape).setDelimiter(fieldDelimiter).build()
    val p = CSVParser.parse(input, charset, csvFormat)

    try {
      val rows: Iterator[CSVRecord] = p.iterator.asScala
      val lines: Iterator[String] = rows.map(_.iterator().asScala.mkString(fieldDelimiter.toString))
      lines.mkString(lineDelimiter)
    } finally {
      p.close()
    }
  }
}
