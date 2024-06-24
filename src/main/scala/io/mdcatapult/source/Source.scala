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
