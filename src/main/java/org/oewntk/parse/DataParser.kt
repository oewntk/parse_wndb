/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.parse

import org.oewntk.pojos.ParsePojoException
import org.oewntk.pojos.Synset
import org.oewntk.utils.Tracing
import java.io.*
import java.util.function.Consumer

/**
 * Synset Parser data.{noun|verb|adj|adv}
 *
 * @author Bernard Bou
 */
object DataParser {

    private const val THROW = false

    // Consumer
    private val consumer = Consumer<Synset> { Tracing.psInfo.println(it) }

    /**
     * Parse all synsets
     *
     * @param dir      WNDB dir
     * @param consumer synset consumer
     * @return synset count
     * @throws IOException        io exception
     * @throws ParsePojoException parse pojo exception
     */
    @Throws(IOException::class, ParsePojoException::class)
    fun parseAllSynsets(dir: File, consumer: Consumer<Synset>): Long {
        return sequenceOf("noun", "verb", "adj", "adv")
            .map { parseSynsets(dir, it, consumer) }
            .sum()
    }

    /**
     * Parse synsets
     *
     * @param dir      WNDB dir
     * @param posName  pos
     * @param consumer synset consumer
     * @return synset count
     * @throws ParsePojoException parse pojo exception
     * @throws IOException        io exception
     */
    @Throws(ParsePojoException::class, IOException::class)
    fun parseSynsets(dir: File, posName: String, consumer: Consumer<Synset>): Long {
        Tracing.psServ.println("* Synsets $posName")

        val isAdj = posName == "adj"

        // iterate on lines
        val file = File(dir, "data.$posName")
        RandomAccessFile(file, "r").use { raFile ->
            raFile.seek(0)

            // iterate on lines
            var lineCount = 0
            var nonCommentCount = 0
            var offsetErrorCount = 0
            var parseErrorCount = 0
            var synsetCount: Long = 0

            while (true) {
                val fileOffset = raFile.filePointer
                val rawLine = raFile.readLine() ?: break

                lineCount++
                if (rawLine.isEmpty() || rawLine[0] == ' ') {
                    continue
                }

                // decode
                val line = String(rawLine.toByteArray(Flags.charSet))
                nonCommentCount++

                // split into fields
                val lineFields = line.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                // read offset
                val readOffset = lineFields[0].toLong()
                if (fileOffset != readOffset) {
                    Tracing.psErr.println("Offset: data.$posName:$lineCount offset=${String.format("%08d", fileOffset)} line=[$line]")
                    offsetErrorCount++
                    continue
                }

                // read
                try {
                    val synset = parseSynsetLine(line, isAdj)
                    synsetCount++
                    consumer.accept(synset)
                } catch (e: ParsePojoException) {
                    parseErrorCount++
                    Tracing.psErr.println("\n${file.name}:$lineCount offset=${String.format("%08d", fileOffset)} line=[$line] except=${e.message}")
                    if (THROW) {
                        throw e
                    }
                }
            }
            val format = "%-50s"
            Tracing.psServ.println("${String.format(format, "lines")}$nonCommentCount")
            Tracing.psServ.println("${String.format(format, "parse successes")}$synsetCount")
            (if (offsetErrorCount > 0) Tracing.psErr else Tracing.psServ).println("${String.format(format, "offset errors")}$offsetErrorCount")
            (if (parseErrorCount > 0) Tracing.psErr else Tracing.psServ).println("${String.format(format, "parse errors")}$parseErrorCount")
            return synsetCount
        }
    }

    /**
     * Parse line
     *
     * @param line  line
     * @param isAdj whether the pos is adj
     * @return synset
     * @throws ParsePojoException parse pojo exception
     */
    @Throws(ParsePojoException::class)
    private fun parseSynsetLine(line: String, isAdj: Boolean): Synset {
        return Synset.parseSynsetLine(line, isAdj)
    }

    /**
     * Main
     *
     * @param args command-line arguments
     * @throws ParsePojoException parse pojo exception
     * @throws IOException        io exception
     */
    @Throws(ParsePojoException::class, IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        // Timing
        val startTime = System.currentTimeMillis()

        // Input
        val dir = File(args[0])

        // Process
        parseAllSynsets(dir, consumer)

        // Timing
        val endTime = System.currentTimeMillis()
        Tracing.psServ.println("Total execution time: " + (endTime - startTime) / 1000 + "s")
    }
}
