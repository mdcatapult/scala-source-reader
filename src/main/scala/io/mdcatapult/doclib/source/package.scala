package io.mdcatapult.doclib

import java.io.InputStream
import java.nio.charset.StandardCharsets.UTF_8

import org.apache.commons.io.IOUtils

package object source {

  def stringToInputStream(t: String): InputStream =
    IOUtils.toInputStream(t, UTF_8)
}
