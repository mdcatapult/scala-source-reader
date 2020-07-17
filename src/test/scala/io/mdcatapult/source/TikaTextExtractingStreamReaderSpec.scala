package io.mdcatapult.source

import java.io.File

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class TikaTextExtractingStreamReaderSpec extends AnyFreeSpec with Matchers {

  private val reader = new TikaTextExtractingStreamReader()

  "A TikaTextExtractingStreamReader" - {
    "when reading from a source" - {
      "should extract text from that source" - {
        "given it is PDF" in {
          val source = Source.fromFile(new File("sources/sample.pdf"))
          val text = reader.readText(source)

          text should include ("A Simple PDF File")
          text should include ("This is a small demonstration .pdf file")
          text should include ("continued from page 1. Yet more text. And more text. And more text.")
        }

        "given it is MS DOC" in {
          val source = Source.fromFile(new File("sources/sample.doc"))
          val text = reader.readText(source)

          text should include ("Lorem ipsum")
          text should include ("Vestibulum neque massa, scelerisque sit amet ligula eu, congue molestie mi.")
          text should include ("In eleifend velit vitae libero sollicitudin euismod.")
        }
      }
    }
  }
}
