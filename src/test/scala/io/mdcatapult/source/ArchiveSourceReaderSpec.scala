package io.mdcatapult.source

import java.io.File

import io.mdcatapult.util.io.stringToInputStream

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class ArchiveSourceReaderSpec extends AnyFreeSpec with Matchers {

  "An ArchiveSourceReader" - {
    "given the source is for a zip archive" - {
      "should give contents of both files in a list" in {
        val source = Source.fromFile(new File("sources/two-files.zip"))
        val reader = SourceReader()

        val content = reader.read(source)

        content should contain allElementsOf Seq(
          "Here is some text...".stripMargin,
          """Here is some more text...
            |And even more!""".stripMargin
        )
      }

      "should throw InputStreamTooLongException when maxBytes is smaller that full size" in {
        val source = Source.fromFile(new File("sources/two-files.zip"))
        val reader = SourceReader().withMaxBytes(maxBytes = 20)

        val e = the [InputStreamTooLongException] thrownBy reader.read(source)

        e.maxLength should be (20)
      }
    }

    "given the source is for a single file" - {

      "should give content of that file" in {
        val source = Source(stringToInputStream(" some sample text "), "name")
        val reader = SourceReader()

        reader.read(source) should contain only "some sample text"
      }

      "should throw InputStreamTooLongException when maxBytes is smaller that full size" in {
        val source = Source(stringToInputStream("some sample text"), "name")
        val reader = SourceReader().withMaxBytes(maxBytes = 10)

        val e = the [InputStreamTooLongException] thrownBy reader.read(source)

        e.maxLength should be (10)
      }
    }
  }
}
