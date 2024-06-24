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

import io.mdcatapult.util.io.stringToInputStream

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class LimitedInputStreamSpec extends AnyFreeSpec with Matchers {

  private val stream = new LimitedInputStream(stringToInputStream(""), 10) {
    // raise protection to public to isolate and give a clear test
    override def raiseError(pSizeMax: Long, pCount: Long): Unit =
      super.raiseError(pSizeMax, pCount)
  }

  "A LimitedInputStream" - {
    "when checking if an error tis to be raised" - {
      "should throw InputStreamTooLongException" - {
        "given maximum bytes have been read" in {
          val e = the [InputStreamTooLongException] thrownBy stream.raiseError(10, 10)
          e.maxLength should be (10)
        }

        "given maximum bytes to read has been exceeded" in {
          val e = the [InputStreamTooLongException] thrownBy stream.raiseError(10, 10 + 1)
          e.maxLength should be (10)
        }
      }

      "should throw nothing" - {
        "given less than the maximum number of bytes have been read" in {
          stream.raiseError(10, 10 - 1)
        }
      }
    }
  }
}
