/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.parse

import org.oewntk.pojos.MorphMapping
import org.oewntk.pojos.MorphMapping.Companion.parseMorphMapping
import org.oewntk.pojos.ParsePojoException
import org.oewntk.pojos.Pos.Companion.fromName
import org.oewntk.utils.Tracing
import java.io.*
import java.util.function.Consumer

/**
 * Morph Parser ({noun|verb|adj|adv|}.exc)
 *
 * @author Bernard Bou
 */
object MorphParser {

    private const val THROW = false

    // PrintStreams
    private val psi = if (System.getProperties().containsKey("VERBOSE")) Tracing.psInfo else Tracing.psNull
    private val pse = if (!System.getProperties().containsKey("SILENT")) Tracing.psErr else Tracing.psNull

    // Consumer
    private val consumer = Consumer<MorphMapping> { psi.println(it) }

    /**
     * Parse morph mappings
     *
     * @param dir      WNDB dir
     * @param consumer morphmapping consumer
     * @throws IOException        io exception
     * @throws ParsePojoException parse pojo exception
     */
    @Throws(IOException::class, ParsePojoException::class)
    fun parseAllMorphs(dir: File?, consumer: Consumer<MorphMapping>) {
        // Process for all pos
        for (posName in arrayOf("noun", "verb", "adj", "adv")) {
            parseMorphs(dir, posName, consumer)
        }
    }

    /**
     * Parse morph mappings
     *
     * @param dir      WNDB dir
     * @param posName  pos
     * @param consumer morphmapping consumer
     * @throws IOException        io exception
     * @throws ParsePojoException parse pojo exception
     */
    @Throws(IOException::class, ParsePojoException::class)
    fun parseMorphs(dir: File?, posName: String, consumer: Consumer<MorphMapping>) {
        psi.println("* Morphs")

        val pos = fromName(posName)

        // iterate on lines
        val file = File(dir, "$posName.exc")
        BufferedReader(InputStreamReader(FileInputStream(file), Flags.charSet)).use { reader ->
            var lineCount = 0
            var parseErrorCount = 0
            var morphMappingCount: Long = 0
            reader.useLines { lines ->
                lines.forEach { line ->
                    lineCount++
                    try {
                        val morphMapping = parseMorphMapping(line, pos)
                        morphMappingCount++
                        consumer.accept(morphMapping)
                    } catch (e: ParsePojoException) {
                        parseErrorCount++
                        pse.printf("%n%s:%d line=[%s] except=%s", file.name, lineCount, line, e)
                        if (THROW) {
                            throw e
                        }
                    }
                }
            }
            val format = "%-50s %d%n"
            psi.printf(format, "lines", lineCount)
            psi.printf(format, "parse successes", morphMappingCount)
            (if (parseErrorCount > 0) pse else psi).printf(format, "parse errors", parseErrorCount)
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
        parseAllMorphs(dir, consumer)

        // Timing
        val endTime = System.currentTimeMillis()
        psi.println("Total execution time: " + (endTime - startTime) / 1000 + "s")
    }
}
