/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.parse

import org.oewntk.parse.DataParser.parseAllSynsets
import org.oewntk.parse.IndexParser.parseAllIndexes
import org.oewntk.parse.SenseParser.parseSenses
import org.oewntk.pojos.*
import org.oewntk.utils.Tracing
import java.io.File
import java.io.IOException
import java.util.*
import java.util.function.Consumer

/**
 * Counts
 *
 * @param dir WNDB source dir
 */
class Counter internal constructor(
    private val dir: File,
) {

    // Counts
    private var synsetCount: Long = 0
    private var senseCount: Long = 0
    private var indexCount: Long = 0
    private var relationCount: Long = 0
    private var synsetRelationCount: Long = 0
    private var senseRelationCount: Long = 0
    private val synsetRelationByTypeCount: MutableMap<String, Long> = TreeMap()
    private val senseRelationByTypeCount: MutableMap<String, Long> = TreeMap()
    private var verbFrameCount: Long = 0
    private var verbFrameMultiSensesCount: Long = 0
    private var verbFrameSingleSensesCount: Long = 0

    // Consumers
    private val synsetConsumer = Consumer<Synset> {
        synsetCount++
        // relations
        val relations = it.relations
        relationCount += relations!!.size.toLong()
        it.relations.forEach { r: Relation ->
            val type = r.type.toString()
            if (r is LexRelation) {
                senseRelationCount++
                var v = senseRelationByTypeCount.computeIfAbsent(type) { 0L }
                v += 1
                senseRelationByTypeCount[type] = v
            } else {
                synsetRelationCount++
                var v = synsetRelationByTypeCount.computeIfAbsent(type) { 0L }
                v += 1
                synsetRelationByTypeCount[type] = v
            }
        }

        // verb frames
        val verbFrames = it.verbFrames
        if (verbFrames != null) {
            verbFrameCount += verbFrames.size.toLong()
            for (verbFrameRef in verbFrames) {
                if (verbFrameRef.lemmas.size > 1) {
                    verbFrameMultiSensesCount += verbFrameRef.lemmas.size.toLong()
                } else {
                    verbFrameSingleSensesCount += verbFrameRef.lemmas.size.toLong()
                }
            }
        }
    }

    private val senseConsumer = Consumer<Sense> { senseCount++ }

    private val indexConsumer = Consumer<Index> { indexCount++ }

    /**
     * Parse all
     *
     * @return this
     * @throws IOException        io exception
     * @throws ParsePojoException parse pojo exception
     */
    @Throws(IOException::class, ParsePojoException::class)
    fun parseAll(): Counter {
        val rSynsetCount = parseAllSynsets(dir, synsetConsumer)
        val rIndexCount = parseAllIndexes(dir, indexConsumer)
        val rSenseCount = parseSenses(dir, senseConsumer)
        assert(rSynsetCount == synsetCount)
        assert(rSenseCount == senseCount)
        assert(rIndexCount == indexCount)
        return this
    }

    /**
     * Report
     *
     * @return this
     */
    fun reportCounts(): Counter {
        Tracing.psInfo.printf("%s synsets:%d senses:%d indexes:%d relations:%d synset_relations:%d sense_relations:%d%n", dir, synsetCount, senseCount, indexCount, relationCount, synsetRelationCount, senseRelationCount)
        return this
    }

    /**
     * Report
     *
     * @return this
     */
    fun reportVerbFrameCounts(): Counter {
        Tracing.psInfo.printf("verbframes: %d single_senses: %d multi_senses: %d senses: %d%n", verbFrameCount, verbFrameMultiSensesCount, verbFrameSingleSensesCount, verbFrameMultiSensesCount + verbFrameSingleSensesCount)
        return this
    }

    /**
     * Report relations
     *
     * @return this
     */
    fun reportRelationCounts(): Counter {
        val countSum = LongArray(2)

        Tracing.psInfo.printf("synset relations: %s%n", synsetRelationCount)
        synsetRelationByTypeCount.forEach { (r: String?, c: Long) ->
            Tracing.psInfo.printf("\t%s: %d%n", r, c)
            countSum[0] += c
        }
        assert(synsetRelationCount == countSum[0])

        Tracing.psInfo.printf("sense relations: %s%n", senseRelationCount)
        senseRelationByTypeCount.forEach { (r: String?, c: Long) ->
            Tracing.psInfo.printf("\t%s: %d%n", r, c)
            countSum[1] += c
        }
        assert(senseRelationCount == countSum[1])

        return this
    }

    companion object {

        // PrintStreams
        private val psi = if (!System.getProperties().containsKey("SILENT")) Tracing.psInfo else Tracing.psNull

        /**
         * Main
         *
         * @param args args
         * @throws IOException        io exception
         * @throws ParsePojoException parse pojo exception
         */
        @Throws(IOException::class, ParsePojoException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            // Timing
            val startTime = System.currentTimeMillis()

            // Input
            for (arg in args) {
                val dir = File(arg)
                Counter(dir).parseAll() //
                    .reportCounts() //
                    .reportRelationCounts() //
                    .reportVerbFrameCounts()
            }

            // Timing
            val endTime = System.currentTimeMillis()
            psi.println("Total execution time: " + (endTime - startTime) / 1000 + "s")
        }
    }
}
