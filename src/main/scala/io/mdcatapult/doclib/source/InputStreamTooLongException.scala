package io.mdcatapult.doclib.source

import java.io.IOException

/** InputStreamTooLongException is thrown when more than a specified number of bytes are
  * tried to be read from an InputStream.
  */
case class InputStreamTooLongException(maxLength: Long) extends IOException
