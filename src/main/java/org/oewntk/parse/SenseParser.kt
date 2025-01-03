/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.parse

import org.oewntk.pojos.ParsePojoException
import org.oewntk.pojos.Sense
import org.oewntk.pojos.Sense.Companion.parseSense
import org.oewntk.utils.Tracing
import java.io.*
import java.util.function.Consumer

/**
 * Sense Parser (index.sense)
 *
 * @author Bernard Bou
 */
object SenseParser {

    private const val THROW = false

    // Consumer
    private val consumer = Consumer<Sense> { Tracing.psInfo.println(it) }

    /**
     * Parse senses
     * @param dir WNDB dir
     * @param consumer sense consumer
     * @return sense count
     * @throws IOException io exception
     * @throws ParsePojoException parse pojo exception
     */
    @Throws(IOException::class, ParsePojoException::class)
    fun parseSenses(dir: File, consumer: Consumer<Sense>): Long {
        Tracing.psServ.println("* Senses")

        // iterate on lines
        val file = File(dir, "index.sense")
        BufferedReader(InputStreamReader(FileInputStream(file), Flags.charSet)).use { reader ->
            var lineCount = 0
            var parseErrorCount = 0
            var senseCount: Long = 0
            reader.useLines { lines ->
                lines.forEach { line ->
                    lineCount++
                    try {
                        val sense = parseSense(line)
                        senseCount++
                        consumer.accept(sense)
                    } catch (e: ParsePojoException) {
                        parseErrorCount++
                        Tracing.psErr.print("\n${file.name}:$lineCount line=[$line] except=$e")
                        if (THROW) {
                            throw e
                        }
                    }
                }
            }
            val format = "%-50s"
            Tracing.psServ.println("${String.format(format, "lines")}$lineCount")
            Tracing.psServ.println("${String.format(format, "parse successes")}$senseCount")
            (if (parseErrorCount > 0) Tracing.psErr else Tracing.psServ).println("${String.format(format, "parse errors")}$parseErrorCount")
            return senseCount
        }
    }

    /**
     * Main
     *
     * @param args command-line arguments
     * @throws ParsePojoException parse pojo exception
     * @throws IOException        io exception
     */
    @Throws(IOException::class, ParsePojoException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        // Timing
        val startTime = System.currentTimeMillis()

        // Input
        val dir = File(args[0])

        // Process
        parseSenses(dir, consumer)

        // Timing
        val endTime = System.currentTimeMillis()
        Tracing.psServ.println("Total execution time: " + (endTime - startTime) / 1000 + "s")
    }
}
