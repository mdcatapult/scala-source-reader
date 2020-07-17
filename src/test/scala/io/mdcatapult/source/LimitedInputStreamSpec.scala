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
