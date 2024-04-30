/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.parse

import org.oewntk.pojos.ParsePojoException
import org.oewntk.pojos.Sense
import org.oewntk.pojos.Sense.Companion.parseSense
import org.oewntk.utils.Tracing
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

/**
 * Parse sense index for target [arg1=string]
 *
 * @author Bernard Bou
 */
object SenseParser1 {

    /**
     * Read lines for target
     *
     * @param dir    WNDB dir
     * @param target target for lines to contain to be selected
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun read(dir: String?, target: String?) {
        val file = File(dir, "index.sense")
        BufferedReader(FileReader(file)).use { reader ->
            reader.useLines { lines ->
                lines.forEach { line ->
                    if (line.contains(target!!)) {
                        Tracing.psInfo.println(line)
                        val sense: Sense
                        try {
                            sense = parseSenseLine(line)
                            Tracing.psInfo.println(sense)
                        } catch (e: ParsePojoException) {
                            Tracing.psErr.printf("%s cause:%s%n", e.message, e.cause)
                            e.printStackTrace(Tracing.psErr)
                        }
                    }
                }
            }
        }
    }

    /**
     * Parse sense line
     *
     * @param line line
     * @return sense
     * @throws ParsePojoException parse pojo exception
     */
    @Throws(ParsePojoException::class)
    private fun parseSenseLine(line: String): Sense {
        return parseSense(line)
    }

    /**
     * Main
     *
     * @param args command-line arguments
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        // Input
        val dir = args[0]
        val target = args[1]

        // Process
        read(dir, target)
    }
}
