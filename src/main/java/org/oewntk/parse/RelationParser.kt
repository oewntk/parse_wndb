/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.parse

import org.oewntk.pojos.*
import org.oewntk.utils.Tracing
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.util.function.Consumer

/**
 * Synset relation parser data.{noun|verb|adj|adv}
 *
 * @property dir WNDB source dir
 *
 * @author Bernard Bou
 */
class RelationParser(
    private val dir: File,
) {
    // Maps
    /**
     * Synset by id map
     */
    private val synsetsById: MutableMap<SynsetId, Synset> = HashMap()

    // Consumers
    /**
     * Synset consumer
     */
    private val synsetConsumer = Consumer { synset: Synset -> synsetsById[synset.id] = synset }

    /**
     * Semantic relation consumer
     */
    private val relationConsumer = Consumer { relation: Relation ->
        val type = relation.type.toString()
        require(!(relation.fromSynsetId.offset == 0L || relation.toSynsetId.offset == 0L)) { relation.toString() }
        if ((relation is LexRelation)) {
            val toWord = relation.toWord
            require(!(toWord.synsetId.offset == 0L || toWord.wordNum == 0)) { relation.toString() }
            require(RelationType.SENSE_RELATIONS.contains(type)) { relation.toString() }
            Tracing.psInfo.println("${String.format("%-6s", "sense")} $relation")
        } else {
            require(RelationType.SYNSET_RELATIONS.contains(type)) { relation.toString() }
            Tracing.psInfo.println("${String.format("%-6s", "synset")} $relation")
        }
    }

    /**
     * Lexical relation consumer
     */
    private val lexRelationConsumer = Consumer { relation: LexRelation ->
        val type = relation.type.toString()
        require(!(relation.fromSynsetId.offset == 0L || relation.toSynsetId.offset == 0L)) { relation.toString() }
        val toWord = relation.toWord
        require(!(toWord.synsetId.offset == 0L || toWord.wordNum == 0)) { relation.toString() }
        require(RelationType.SENSE_RELATIONS.contains(type)) { relation.toString() }
        // psi.println("${String.format("%-6s", "sense")}$relation")
        val resolvedToWord = resolveToWord(relation)
        Tracing.psInfo.println("${String.format("%-6s", "sense")}${relation.toString(resolvedToWord)}")
    }

    // Source

    /**
     * Parse all synsets
     *
     * @return this
     * @throws IOException        io exception
     * @throws ParsePojoException parse pojo exception
     */
    @Throws(IOException::class, ParsePojoException::class)
    fun parseAllSynsets(): RelationParser {
        // make map for resolution
        sequenceOf("noun", "verb", "adj", "adv")
            .forEach {
                parseSynsets(dir, it, synsetConsumer, null, null)
            }
        // consume relations
        sequenceOf("noun", "verb", "adj", "adv")
            .forEach {
                parseSynsets(dir, it, null, relationConsumer, lexRelationConsumer)
            }
        return this
    }

    /**
     * Resolve lexical relation to target word
     *
     * @param lr lexical relation
     * @return resolve target word
     */
    private fun resolveToWord(lr: LexRelation): String {
        val toSynsetId = lr.toSynsetId
        val toSynset = synsetsById[toSynsetId]
        val toWordRef = lr.toWord
        return toWordRef.resolve(toSynset!!).toString()
    }

    companion object {

        private const val THROW = false

        /**
         * Parse synsets
         *
         * @param dir                 WNDB dir
         * @param posName             pos
         * @param synsetConsumer      synset consumer
         * @param relationConsumer    relation consumer
         * @param lexRelationConsumer lex relation consumer
         * @return relation count
         * @throws ParsePojoException parse pojo exception
         * @throws IOException        io exception
         */
        @Throws(ParsePojoException::class, IOException::class)
        fun parseSynsets(dir: File, posName: String, synsetConsumer: Consumer<Synset>?, relationConsumer: Consumer<Relation>?, lexRelationConsumer: Consumer<LexRelation>?): Int {
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
                var relationCount = 0

                while (true) {
                    // record current offset
                    val fileOffset = raFile.filePointer

                    // read a line
                    val rawLine = raFile.readLine() ?: break
                    lineCount++

                    // discard comment
                    if (rawLine.isEmpty() || rawLine[0] == ' ') {
                        continue
                    }
                    nonCommentCount++

                    // decode
                    val line = String(rawLine.toByteArray(Flags.charSet))

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
                        synsetConsumer?.accept(synset)
                        if (relationConsumer == null && lexRelationConsumer == null) {
                            continue
                        }
                        relationCount = synset.relations
                            ?.onEach {
                                when (it) {
                                    is LexRelation -> lexRelationConsumer?.accept(it)
                                    else           -> relationConsumer?.accept(it)
                                }
                            }
                            ?.map { 1 }
                            ?.sum()
                            ?: 0
                    } catch (e: ParsePojoException) {
                        parseErrorCount++
                        Tracing.psErr.print("\n${file.name}:$lineCount offset=${String.format("%08d", fileOffset)} line=[line] except=${e.message}")
                        if (THROW) {
                            throw e
                        }
                    }
                }
                val format = "%-50s"
                Tracing.psServ.println("${String.format(format, "lines")}$nonCommentCount")
                Tracing.psServ.println("${String.format(format, "parse successes")}$relationCount")
                (if (offsetErrorCount > 0) Tracing.psErr else Tracing.psServ).println("${String.format(format, "offset errors")}$offsetErrorCount")
                (if (parseErrorCount > 0) Tracing.psErr else Tracing.psServ).println("${String.format(format, "parse errors")}$parseErrorCount")
                return relationCount
            }
        }

        /**
         * Parse synset line
         *
         * @param line  line
         * @param isAdj whether adjectives are being processed
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
            for (arg in args) {
                val dir = File(arg)

                // Process
                RelationParser(dir).parseAllSynsets()
            }

            // Timing
            val endTime = System.currentTimeMillis()
            Tracing.psServ.println("Total execution time: " + (endTime - startTime) / 1000 + "s")
        }
    }
}
