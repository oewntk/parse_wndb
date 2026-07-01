package org.oewntk.parse

import java.io.ByteArrayOutputStream
import java.io.RandomAccessFile

fun RandomAccessFile.lineSequence(): Sequence<Pair<Long, String>> = sequence {
    while (true) {
        // record current offset
        val fileOffset = filePointer
        // accumulate bytes in the buffer until line break is met
        val lineBytes = ByteArrayOutputStream()
        var hasRead = false
        while (true) {
            val b: Int = read()
            if (b == -1) {
                break
            }
            hasRead = true
            if (b == '\n'.code) break
            if (b != '\r'.code) lineBytes.write(b) // drop bare \r if you want CRLF handling
        }
        if (!hasRead) break

        val rawBytes = lineBytes.toByteArray()
        if (rawBytes.isEmpty() || rawBytes[0] == ' '.code.toByte()) continue
        val line = String(rawBytes, Flags.charSet)
        yield(fileOffset to line)
    }
}