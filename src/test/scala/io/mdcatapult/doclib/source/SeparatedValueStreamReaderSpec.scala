package io.mdcatapult.doclib.source

import java.io.InputStream
import java.util.concurrent.atomic.AtomicBoolean

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class SeparatedValueStreamReaderSpec extends AnyFreeSpec with Matchers {

  private class MockStreamReader extends StreamReader {
    val executed = new AtomicBoolean(false)

    override def readText(input: InputStream, name: String): String = {
      executed.set(true)
      s"MockStreamReader executed $name"
    }
  }

  "A SeparatedValueStreamReader" - {
    "when reading from a source" - {
      "given the name has .tsv extension" - {
        val mockReader = new MockStreamReader()
        val reader = new SeparatedValueStreamReader(mockReader)

        reader.readText()

        "should escape tab characters" in {}
        "should not execute the default reader" ignore {}
      }

      "given the name has .csv extension" - {
        "should escape tab characters" ignore {}
        "should not execute the default reader" ignore {}
      }

      "given the name has a non-separated value extension" - {
        "should escape tab characters" ignore {}
        "should not execute the default reader" ignore {}
      }

      "given the name has no extension" - {
        "should escape tab characters" ignore {}
        "should not execute the default reader" ignore {}
      }
    }
  }
}
