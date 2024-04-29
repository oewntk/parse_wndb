/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.pojos

import java.util.*

/**
 * Core Index
 *
 * @property lemma  lemma
 * @property pos    pos
 * @property senses senses
 *
 * @author Bernard Bou
 */
open class CoreIndex protected constructor(
    val lemma: Lemma,
    val pos: Pos,
    val senses: Array<BaseSense>,
) {

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val coreIndex = other as CoreIndex
        return lemma == coreIndex.lemma && pos == coreIndex.pos && senses.contentEquals(coreIndex.senses)
    }

    override fun hashCode(): Int {
        var result = Objects.hash(lemma, pos)
        result = 31 * result + senses.contentHashCode()
        return result
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(lemma.toString())
        sb.append(" senses={")
        for (i in senses.indices) {
            if (i != 0) {
                sb.append(" ")
            }
            sb.append(senses[i].toString())
        }
        sb.append("}")
        return sb.toString()
    }

    companion object {

        /**
         * Parse core index from line
         *
         * @param line line
         * @return core index
         * @throws ParsePojoException parse exception
         */
        @Throws(ParsePojoException::class)
        fun parseCoreIndex(line: String): CoreIndex {
            try {
                // line from index.noun or index.verb or index.adj or index.adv:  lemma  pos  synset_cnt  p_cnt  [ptr_symbol...]  sense_cnt  tagsense_cnt   synset_offset  [synset_offset...]
                // lemma: Lower case ASCII text of word or collocation. Collocations are formed by joining individual words with an underscore (_ ) character.
                // pos: Syntactic category: n for noun files, v for verb files, a for adjective files, r for adverb files.
                // synset_cnt:   Number of synsets that lemma is in. This is the number of senses of the word in WordNet.
                // p_cnt: Number of different pointers that lemma has in all synsets containing it.
                // ptr_symbol:  A space separated list of p_cnt different types of pointers that lemma has in all synsets containing it. If all senses of lemma have no pointers, this field is omitted and p_cnt is 0 .
                // sense_cnt: Same as synset_cnt above. This is redundant.
                // tagsense_cnt: Number of senses of lemma that are ranked according to their frequency of occurrence in semantic concordance texts.
                // synset_offset:  Byte offset in data.pos file of a synset containing lemma . Each synset_offset in the list corresponds to a different sense of lemma in WordNet. synset_offset is an 8 digit, zero-filled decimal integer that can be used with fseek(link is external)(3)(link is external) to read a synset from the data file. When passed to read_synset(3WN) along with the syntactic category, a data structure containing the parsed synset is returned.

                // split into fields

                val fields = line.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                var fieldPointer = 0

                // lemma/word
                val lemmaString = fields[fieldPointer]
                val lemma = Lemma.make(lemmaString)
                fieldPointer++

                // part-of-speech
                val pos = Pos.parsePos(fields[fieldPointer][0])
                fieldPointer++

                // polysemy count
                val senseCount = fields[fieldPointer].toInt()
                fieldPointer++

                // relation count
                val relationCount = fields[fieldPointer].toInt(10)
                fieldPointer++

                // relations
                fieldPointer += relationCount

                // polysemy count 2
                fieldPointer++

                // tag count
                fieldPointer++

                // senses
                val senses = Array(senseCount) {
                    val sense = BaseSense.make(lemma, pos, fields[fieldPointer], it + 1)
                    fieldPointer++
                    sense
                }
                return CoreIndex(lemma, pos, senses)
            } catch (e: Exception) {
                throw ParsePojoException(e)
            }
        }
    }
}
