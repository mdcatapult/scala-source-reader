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

import org.apache.commons.fileupload.util.{LimitedInputStream => ApacheLimitedInputStream}

object LimitedInputStream {

  /** Maximum number of bytes than we want to read into a single array. */
  val maxArraySize: Int = Int.MaxValue / 2
}

/** Implements Apache's LimitedInputStream in a simple way that throws a InputStreamTooLongException
  * if we try to read more bytes than the maximum allowed
  */
class LimitedInputStream(in: InputStream, pSizeMax: Long) extends ApacheLimitedInputStream(in, pSizeMax) {

  override def raiseError(pSizeMax: Long, pCount: Long): Unit =
    if (pCount >= pSizeMax) throw InputStreamTooLongException(pSizeMax)
}
