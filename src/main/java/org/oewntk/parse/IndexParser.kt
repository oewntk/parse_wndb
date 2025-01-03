/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.parse

import org.oewntk.pojos.CoreIndex
import org.oewntk.pojos.CoreIndex.Companion.parseCoreIndex
import org.oewntk.pojos.Index
import org.oewntk.pojos.Index.Companion.parseIndex
import org.oewntk.pojos.ParsePojoException
import org.oewntk.utils.Tracing
import java.io.*
import java.util.function.Consumer

/**
 * Index parser index.{noun|verb|adj|adv}
 *
 * @author Bernard Bou
 */
object IndexParser {

    private const val THROW = false

    // Consumer
    private val consumer = Consumer<Index> { Tracing.psInfo.println(it) }

    /**
     * Parse all indexes
     *
     * @param dir      WNDB dir
     * @param consumer index consumer
     * @return index count
     * @throws IOException        io exception
     * @throws ParsePojoException parse pojo exception
     */
    @Throws(IOException::class, ParsePojoException::class)
    fun parseAllIndexes(dir: File, consumer: Consumer<Index>): Long {
        return sequenceOf("noun", "verb", "adj", "adv")
            .map { parseIndexes(dir, it, consumer) }
            .sum()
    }

    /**
     * Parse all core indexes
     *
     * @param dir      WNDB dir
     * @param consumer core index consumer
     * @return index count
     * @throws IOException        io exception
     * @throws ParsePojoException parse pojo exception
     */
    @Throws(IOException::class, ParsePojoException::class)
    fun parseAllCoreIndexes(dir: File, consumer: Consumer<CoreIndex>): Long {
        return sequenceOf("noun", "verb", "adj", "adv")
            .map { parseCoreIndexes(dir, it, consumer) }
            .sum()
    }

    /**
     * Parse indexes
     *
     * @param dir      WNDB dir
     * @param posName  pos
     * @param consumer index consumer
     * @return index count
     * @throws IOException        io exception
     * @throws ParsePojoException parse pojo exception
     */
    @Throws(IOException::class, ParsePojoException::class)
    fun parseIndexes(dir: File, posName: String, consumer: Consumer<Index>): Long {
        Tracing.psServ.println("* Indexes $posName")

        // iterate on lines
        val file = File(dir, "index.$posName")
        BufferedReader(InputStreamReader(FileInputStream(file), Flags.charSet)).use { reader ->
            var lineCount = 0
            var nonCommentCount = 0
            var indexCount: Long = 0
            var parseErrorCount = 0
            reader.useLines { lines ->
                lines.forEach { line ->
                    // Process each line here
                    lineCount++
                    if (line.isNotEmpty() && line[0] != ' ') {
                        nonCommentCount++
                        try {
                            val index = parseIndex(line)
                            indexCount++
                            consumer.accept(index)
                        } catch (e: ParsePojoException) {
                            parseErrorCount++
                            Tracing.psErr.print("\n${file.name}:$lineCount line=[$line] except=$e")
                            if (THROW) {
                                throw e
                            }
                        }
                    }
                }
            }
            val format = "%-50s"
            Tracing.psServ.println("${String.format(format, "lines")}$nonCommentCount")
            (if (parseErrorCount > 0) Tracing.psErr else Tracing.psServ).println("${String.format(format, "parse successes")}$indexCount")
            (if (parseErrorCount > 0) Tracing.psErr else Tracing.psServ).println("${String.format(format, "parse errors")}$parseErrorCount")
            return indexCount
        }
    }

    /**
     * Parse core indexes
     *
     * @param dir      WNDB dir
     * @param posName  pos
     * @param consumer core index consumer
     * @return index count
     * @throws IOException        io exception
     * @throws ParsePojoException parse pojo exception
     */
    @Throws(IOException::class, ParsePojoException::class)
    fun parseCoreIndexes(dir: File, posName: String, consumer: Consumer<CoreIndex>): Long {
        Tracing.psServ.println("* Indexes $posName")

        // iterate on lines
        val file = File(dir, "index.$posName")
        BufferedReader(InputStreamReader(FileInputStream(file), Flags.charSet)).use { reader ->
            var lineCount = 0
            var nonCommentCount = 0
            var indexCount: Long = 0
            var parseErrorCount = 0
            reader.useLines { lines ->
                lines.forEach { line ->
                    lineCount++
                    if (line.isNotEmpty() || line[0] != ' ') {
                        nonCommentCount++
                        try {
                            val index = parseCoreIndex(line)
                            indexCount++
                            consumer.accept(index)
                        } catch (e: ParsePojoException) {
                            parseErrorCount++
                            Tracing.psErr.print("\n${file.name}:$lineCount line=[$line] except=$e")
                            if (THROW) {
                                throw e
                            }
                        }
                    }
                }
            }
            val format = "%-50s"
            Tracing.psServ.println("${String.format(format, "lines")}$nonCommentCount")
            (if (parseErrorCount > 0) Tracing.psErr else Tracing.psServ).println("${String.format(format, "parse successes")}$indexCount")
            (if (parseErrorCount > 0) Tracing.psErr else Tracing.psServ).println("${String.format(format, "parse errors")}$parseErrorCount")
            return indexCount
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
        parseAllIndexes(dir, consumer)

        // Timing
        val endTime = System.currentTimeMillis()
        Tracing.psServ.println("Total execution time: " + (endTime - startTime) / 1000 + "s")
    }
}
