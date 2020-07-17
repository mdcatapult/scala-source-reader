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
