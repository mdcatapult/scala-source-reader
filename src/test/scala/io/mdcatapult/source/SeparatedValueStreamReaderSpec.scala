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

import java.util.concurrent.atomic.AtomicBoolean

import io.mdcatapult.util.io.stringToInputStream

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class SeparatedValueStreamReaderSpec extends AnyFreeSpec with Matchers {

  private class MockStreamReader extends StreamReader {
    val executed = new AtomicBoolean(false)

    override def readText(source: Source): String = {
      executed.set(true)
      s"MockStreamReader executed ${source.name}"
    }
  }

  "A SeparatedValueStreamReader" - {
    "when reading from a source" - {
      "given the name has .tsv extension" - {
        val mockReader = new MockStreamReader()
        val reader = new SeparatedValueStreamReader(mockReader)

        val line = reader.readText(Source(stringToInputStream("1,000\t\"sep\\\tarated\""), "test.tsv"))

        "should give TSV text with escaped tab characters unescaped" in {
          line should be ("1,000\tsep\tarated")
        }
        "should not execute the default reader" in {
          mockReader.executed.get should be (false)
        }
      }

      "given the name has .csv extension" - {
        val mockReader = new MockStreamReader()
        val reader = new SeparatedValueStreamReader(mockReader)

        val line = reader.readText(Source(stringToInputStream("1\t000,\"sep\\,arated\""), "test.csv"))

        "should give CSV text with escaped commas characters unescaped" in {
          line should be ("1\t000,sep,arated")
        }
        "should not execute the default reader" in {
          mockReader.executed.get should be (false)
        }
      }

      "given the name has a non-separated value extension" - {
        val mockReader = new MockStreamReader()
        val reader = new SeparatedValueStreamReader(mockReader)

        val line = reader.readText(Source(stringToInputStream(""), "test.txt"))

        "should give text from default reader" in {
          line should be ("MockStreamReader executed test.txt")
        }
      }

      "given the name has no extension" - {
        val mockReader = new MockStreamReader()
        val reader = new SeparatedValueStreamReader(mockReader)

        val line = reader.readText(Source(stringToInputStream(""), "test"))

        "should give text from default reader" in {
          line should be ("MockStreamReader executed test")
        }
      }
    }
  }
}
