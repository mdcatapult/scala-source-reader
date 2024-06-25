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

/**
  * Loader object to allow easy instantiation and detection of appropriate loader
  */
object SourceReader {

  /** list of valid extensions to be handled in archive format **/
  val validExtensions: List[String] = List(
    "bz2", "csv", "docx", "htm", "html", "nam", "nam.gz", "nxml",
    "odp", "ods", "odt", "pdf", "pptx", "sgm", "sgml", "shtm", "shtml",
    "txt", "txt.gz", "tar", "tar.001", "tar.bz2", "tar.gz", "tbz2", "tgz", "tsv",
    "xlsx", "xml", "xml.gz", "zip")

  private val defaultStreamReaders =
    new SeparatedValueStreamReader(
      new TikaTextExtractingStreamReader()
    )

  def apply(): SourceReader =
    new ArchiveSourceReader(defaultStreamReaders, LimitedInputStream.maxArraySize)
}

/**
  * Reads text from a [[Source]].
  */
trait SourceReader {

  /**
    * Override the maximum number of bytes that can be read from this InputStream.
    * The default is the maximum size of an array.
    * @param maxBytes maximum number of bytes that can be read
    * @return loader with maxBytes overridden
    */
  def withMaxBytes(maxBytes: Int): SourceReader

  /**
    * Read text from the configured input stream.  This input will get buffered and be prevented from exceeding
    * the maxBytes limit.
    * @return list of text read/extracted from the source.
    *         For a simple file this will have a single entry.
    *         If it is an archive of some kind that can be broken up into sub-sources then there will be 1 String for
    *         each sub-source.
    */
  def read(source: Source): List[String]

}
