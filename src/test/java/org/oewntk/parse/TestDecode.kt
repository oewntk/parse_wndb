package org.oewntk.parse

import org.oewntk.utils.Tracing
import kotlin.test.Test

class TestDecode {

    @Test
    fun TextDecode() {
        val rawLine = "usually followed by âtoâ) having the necessary means"
        val byteArray = rawLine.toByteArray(Charsets.ISO_8859_1)
        val line = String(byteArray, Flags.charSet)
        Tracing.psInfo.println(rawLine)
        Tracing.psInfo.println(line)
    }
}