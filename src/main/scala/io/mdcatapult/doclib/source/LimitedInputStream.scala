package io.mdcatapult.doclib.source

import java.io.InputStream

import org.apache.commons.fileupload.util.{LimitedInputStream => ApacheLimitedInputStream}

object LimitedInputStream {

  /** Maximum number of bytes than we want to read into a single array. */
  val maxArraySize: Int = Int.MaxValue / 2

  /** Create an InputStream that will throw an exception when more bytes than we want to read into
    * a single array.
    */
  def readingIntoSingleArray(in: InputStream): LimitedInputStream =
    new LimitedInputStream(in, maxArraySize)
}

/** Implements Apache's LimitedInputStream in a simple way that throws a InputStreamTooLongException
  * if we try to read more bytes than the maximum allowed
  */
class LimitedInputStream(in: InputStream, pSizeMax: Long) extends ApacheLimitedInputStream(in, pSizeMax) {

  override def raiseError(pSizeMax: Long, pCount: Long): Unit =
    if (pCount >= pSizeMax) throw InputStreamTooLongException(pSizeMax)
}
