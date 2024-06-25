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

import org.apache.tika.Tika
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.ParseContext
import org.apache.tika.sax.BodyContentHandler

/**
  * Read text from InputStream using Tika.
  */
class TikaTextExtractingStreamReader extends StreamReader {

  private val tika = new Tika()

  override def readText(source: Source): String = {
    val handler = new BodyContentHandler(-1)

    tika.getParser.parse(
      source.input,
      handler,
      new Metadata(),
      new ParseContext)

    handler.toString
  }
}
