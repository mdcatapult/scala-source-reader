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

import com.typesafe.scalalogging.LazyLogging
import org.apache.commons.compress.archivers.{ArchiveInputStream, ArchiveStreamFactory}

import java.io.{BufferedInputStream, InputStream}
import scala.util.{Failure, Success, Try}

/**
  * Recursively parse an archive to discover all [[Source]]s which are read into a list of strings - one for each Source
  * discovered.  The text is read through a [[StreamReader]] which is free to manipulate the read bytes from its
  * [[InputStream]] as its implementation sees fit.
  *
  * @param reader converts the InputStream of a single Source into a string
  * @param maxBytes maximum number of bytes that can be read from an InputStream
  */
private class ArchiveSourceReader(
                                   reader: StreamReader,
                                   maxBytes: Int,
                                 ) extends SourceReader with LazyLogging {

  import SourceReader.validExtensions

  def withMaxBytes(maxBytes: Int): SourceReader =
    new ArchiveSourceReader(reader, maxBytes)

  /**
    * Read text from the configured input stream.  This input will get buffered.
    * @return
    */
  def read(source: Source): List[String] = {
    val constrainedInput = {
      new LimitedInputStream(
        new BufferedInputStream(source.input),
        maxBytes
      )
    }
    archiveStream(constrainedInput) match {

      case ais: ArchiveInputStream[_] =>
        Iterator.continually(ais.getNextEntry)
          .takeWhile(_ != null)
          .filterNot(_.isDirectory)
          .filter(entry =>
            validExtensions.exists(ext => entry.getName.matches(s".*\\.$ext$$"))
          )
          .map(entry =>
            read(Source(ais, entry.getName)).head
          )
          .toList

      case _ =>
        try {
          List(
            reader.readText(source.copy(input = constrainedInput)).trim
          )
        } catch {
          case e: Exception => throw e.getCause
        }
    }
  }

  /**
    * test if input is an archive and return appropriate stream of type
    * @return
    */
  private def archiveStream(input: InputStream): InputStream = {
    Try(new ArchiveStreamFactory().createArchiveInputStream(input)) match {
      case Success(ais) => ais
      case Failure(_) => input
    }
  }
}
