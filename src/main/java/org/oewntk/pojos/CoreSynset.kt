/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.pojos

import org.oewntk.pojos.AdjLemma.Companion.makeAdj
import java.util.*

/**
 * Core Synset (without relations and frames)
 *
 * @property id synset id
 * @property cSLemmas  case-sensitive lemmas
 * @property lemmas    lower-case lemmas
 * @property type      type
 * @property domain    lex domain
 * @property gloss     gloss
 *
 * @author Bernard Bou
 */
open class CoreSynset protected constructor(
    val id: SynsetId,
    val cSLemmas: Array<LemmaCS>,
    val type: Type,
    val domain: Domain,
    val gloss: Gloss,
) {

    /**
     * Lemmas
     */
    val lemmas: Array<Lemma>
        get() {
            return Array(cSLemmas.size) {
                this.cSLemmas[it].lemma
            }
        }

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as CoreSynset
        return id == that.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("id=")
        sb.append(String.format("%08d", id.offset))
        sb.append(" words={")
        for ((i, lemma) in this.cSLemmas.withIndex()) {
            if (i > 0) {
                sb.append(",")
            }
            sb.append(lemma.toString())
            if (lemma.lemma is AdjLemma) {
                val adjLemma = lemma.lemma
                sb.append(adjLemma.toPositionSuffix())
            }
        }
        sb.append("} type=")
        sb.append(this.type)
        sb.append(" domain=")
        sb.append(this.domain)
        return sb.toString()
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
        fun parseCoreSynset(line: String, isAdj: Boolean): CoreSynset {
            try {
                // split into fields
                val fields = line.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                var fieldPointer = 0

                // offset
                val offset = fields[fieldPointer].toInt().toLong()
                fieldPointer++

                // lex domain
                val domain = Domain.parseDomainId(fields[fieldPointer])
                fieldPointer++

                // type
                val type = Type.parseType(fields[fieldPointer][0])

                // fieldPointer++;

                // id
                val synsetId = SynsetId(type.toPos(), offset)

                // lemma set
                val lemmas = parseLemmas(fields, isAdj)

                // fieldPointer += 1 + 2 * members.length;

                // glossary
                val gloss = Gloss(line.substring(line.indexOf('|') + 1))

                return CoreSynset(synsetId, lemmas, type, domain, gloss)
            } catch (e: Exception) {
                throw ParsePojoException(e)
            }
        }

        /**
         * Parse lemmas from fields
         *
         * @param fields fields
         * @param isAdj  whether an adjective is being processed
         * @return array of bare normalized strings
         * @throws ParsePojoException parse exception
         */
        @Throws(ParsePojoException::class)
        private fun parseLemmas(fields: Array<String>, isAdj: Boolean): Array<LemmaCS> {
            try {
                // data
                var fieldPointer = 3

                // count
                val count = fields[fieldPointer].toInt(16)
                fieldPointer++

                // lemmas
                val lemmas = Array(count) {
                    val field = fields[fieldPointer]
                    val normalized = NormalizedString(field)
                    val lemma = if (isAdj) makeAdj(normalized) else Lemma.make(normalized)

                    // this field
                    fieldPointer++

                    // lexid skipped
                    fieldPointer++

                    LemmaCS(lemma, if (isAdj) TrimmedNormalizedString(normalized) else normalized)
                }
                return lemmas
            } catch (e: Exception) {
                throw ParsePojoException(e)
            }
        }

        /**
         * Parse from line
         *
         * @param line line
         * @return members
         * @throws ParsePojoException parse exception
         */
        @Throws(ParsePojoException::class)
        fun parseMembers(line: String): Array<TrimmedNormalizedString?> {
            // split into fields
            val fields = line.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return parseMembers(fields)
        }

        /**
         * Parse members from fields
         *
         * @param fields fields
         * @return array of bare normalized strings
         * @throws ParsePojoException parse exception
         */
        @Throws(ParsePojoException::class)
        private fun parseMembers(fields: Array<String>): Array<TrimmedNormalizedString?> {
            try {
                // data
                var fieldPointer = 3

                // count
                val count = fields[fieldPointer].toInt(16)
                fieldPointer++

                // members
                val members = arrayOfNulls<TrimmedNormalizedString>(count)
                for (i in 0 until count) {
                    members[i] = TrimmedNormalizedString(fields[fieldPointer])
                    fieldPointer++

                    // lexid skipped
                    fieldPointer++
                }
                return members
            } catch (e: Exception) {
                throw ParsePojoException(e)
            }
        }
    }
}