/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.pojos

import org.oewntk.pojos.RelationType.Companion.parseRelationType

/**
 * Synset, a core synset extended to include relations and possibly verb frames
 *
 * @param synsetId   synset id
 * @param lemmas     lemmas
 * @param type       type
 * @param domain     lex domain
 * @param gloss      gloss
 * @param relations  relations
 * @param verbFrames verb frames
 * @property relations  relations
 * @property verbFrames verb frames
 *
 * @author Bernard Bou
 */
class Synset private constructor(
    synsetId: SynsetId,
    lemmas: Array<LemmaCS>,
    type: Type,
    domain: Domain,
    gloss: Gloss,
    var relations: Array<Relation>?,
    val verbFrames: Array<VerbFrameRef>?,
) : CoreSynset(synsetId, lemmas, type, domain, gloss) {

    override fun toString(): String {
        val r = relations?.joinToString(separator = ",", prefix = " relations={", postfix = "}")
        val f = verbFrames?.joinToString(separator = ",", prefix = " relations={", postfix = "}")
        return super.toString() + (r ?: "") + (f ?: "")
    }

    /**
     * Pretty string
     *
     * @return pretty string
     */
    fun toPrettyString(): String {
        val r = relations?.joinToString(separator = ",\n\t", prefix = "\nrelations={\n\t", postfix = "\n}")
        val f = verbFrames?.joinToString(separator = ",\n\t", prefix = "\nframes={\n\t", postfix = "\n}")
        return super.toString() + '\n' + gloss.toPrettyString() + (r ?: "") + (f ?: "")
    }

    companion object {

        /**
         * Parse from line
         *
         * @param line  line
         * @param isAdj whether adj synsets are being parsed
         * @return synset
         * @throws ParsePojoException parse exception
         */
        @Throws(ParsePojoException::class)
        fun parseSynsetLine(line: String, isAdj: Boolean): Synset {
            try {
                // core subparse
                val protoSynset = parseCoreSynset(line, isAdj)

                // copy data from proto
                val type = protoSynset.type
                val csLemmas = protoSynset.cSLemmas
                val synsetId = protoSynset.id
                val domain = protoSynset.domain
                val gloss = protoSynset.gloss

                // split into fields
                val fields = line.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                var fieldPointer = 0

                // offset
                fieldPointer++

                // domain
                fieldPointer++

                // part-of-speech
                fieldPointer++

                // lemma count
                fieldPointer++

                // lemma set
                fieldPointer += 2 * csLemmas.size

                // relation count
                val relationCount = fields[fieldPointer].toInt(10)
                fieldPointer++

                // relations
                val relations = if (relationCount == 0) null else {
                    Array(relationCount) {

                        // read data
                        val relationTypeField = fields[fieldPointer++]
                        val relationSynsetIdField = fields[fieldPointer++]
                        val relationPosField = fields[fieldPointer++]
                        val relationSourceTargetField = fields[fieldPointer++]
                        val relationSynsetId = relationSynsetIdField.toLong()
                        val relationSourceTarget = relationSourceTargetField.toInt(16)

                        // compute
                        val synsetType = Type.parseType(relationPosField[0])
                        val relationType = parseRelationType(relationTypeField)
                        val toId = SynsetId(synsetType.toPos(), relationSynsetId)

                        // create
                        if (relationSourceTarget != 0) {
                            val fromWordIndex = relationSourceTarget shr 8
                            val toWordIndex = relationSourceTarget and 0xff
                            val fromLemma = csLemmas[fromWordIndex - 1]
                            val toLemma = LemmaRef(toId, toWordIndex)
                            LexRelation(relationType, synsetId, toId, fromLemma, toLemma)
                        } else {
                            Relation(relationType, synsetId, toId)
                        }
                    }
                }

                // frames
                var frames: Array<VerbFrameRef>? = null
                if (type.toChar() == 'v' && fields[fieldPointer] != "|") {

                    // frame count
                    val frameCount = fields[fieldPointer].toInt(10)
                    fieldPointer++

                    // frames
                    frames = Array(frameCount) {
                        // read data
                        fieldPointer++ // '+'
                        val frameIdField = fields[fieldPointer++]
                        val wordIndexField = fields[fieldPointer++]

                        // compute
                        val frameId = frameIdField.toInt()
                        val wordIndex = wordIndexField.toInt(16)

                        // create
                        val frameLemmas = if (wordIndex != 0) {
                            arrayOf(csLemmas[wordIndex - 1].lemma)
                        } else  // 0 means all
                        {
                            protoSynset.lemmas
                        }
                        VerbFrameRef(frameLemmas, frameId)
                    }
                }
                return Synset(synsetId, csLemmas, type, domain, gloss, relations, frames)
            } catch (e: Exception) {
                throw ParsePojoException(e)
            }
        }
    }
}