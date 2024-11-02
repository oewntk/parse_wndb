/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.parse

import org.oewntk.parse.DataParser.parseAllSynsets
import org.oewntk.parse.IndexParser.parseAllIndexes
import org.oewntk.parse.SenseParser.parseSenses
import org.oewntk.pojos.Index
import org.oewntk.pojos.ParsePojoException
import org.oewntk.pojos.Sense
import org.oewntk.pojos.Synset
import org.oewntk.utils.Tracing
import java.io.File
import java.io.IOException
import java.util.function.Consumer

/**
 * Parser
 */
object Parser {

    // Consumers
    private val synsetConsumer = Consumer<Synset> { Tracing.psInfo.println(it) }
    private val senseConsumer = Consumer<Sense> { Tracing.psInfo.println(it) }
    private val indexConsumer = Consumer<Index> { Tracing.psInfo.println(it) }

    /**
     * Parse all
     *
     * @param dir            WNDB dir
     * @param synsetConsumer synset consumer
     * @param senseConsumer  sense  consumer
     * @param indexConsumer  index  consumer
     * @throws IOException        io exception
     * @throws ParsePojoException parse pojo exception
     */
    @Throws(IOException::class, ParsePojoException::class)
    fun parseAll(dir: File, synsetConsumer: Consumer<Synset>, senseConsumer: Consumer<Sense>, indexConsumer: Consumer<Index>) {
        parseAllSynsets(dir, synsetConsumer)
        parseAllIndexes(dir, indexConsumer)
        parseSenses(dir, senseConsumer)
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
        parseAll(dir, synsetConsumer, senseConsumer, indexConsumer)

        // Timing
        val endTime = System.currentTimeMillis()
        Tracing.psServ.println("Total execution time: " + (endTime - startTime) / 1000 + "s")
    }
}
