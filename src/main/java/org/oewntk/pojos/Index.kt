/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.pojos

import org.oewntk.pojos.BaseSense.Companion.make
import org.oewntk.pojos.Pos.Companion.parsePos
import org.oewntk.pojos.RelationType.Companion.parseRelationType

/**
 * Index
 *
 * @param lemma         lemma
 * @param pos           pos
 * @param senses        senses
 * @property relationTypes relation types
 * @property tagCnt        tag count
 */
class Index(
    lemma: Lemma,
    pos: Pos,
    senses: Array<BaseSense>,
    private val relationTypes: Array<RelationType>,
    private val tagCnt: TagCnt,
) : CoreIndex(lemma, pos, senses) {

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(super.toString())
        sb.append(" relations={")
        for (i in relationTypes.indices) {
            if (i != 0) {
                sb.append(" ")
            }
            sb.append(relationTypes[i].toString())
        }
        sb.append("}")
        sb.append(" tagcnt=")
        sb.append(this.tagCnt)
        return sb.toString()
    }

    companion object {

        /**
         * Parse index from line
         *
         * @param line line
         * @return index
         * @throws ParsePojoException parse exception
         */
        @Throws(ParsePojoException::class)
        fun parseIndex(line: String): Index {
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

                // lemma
                val lemmaString = fields[fieldPointer]
                val lemma = Lemma.make(lemmaString)
                fieldPointer++

                // part-of-speech
                val pos = parsePos(fields[fieldPointer][0])
                fieldPointer++

                // polysemy count
                val senseCount = fields[fieldPointer].toInt()
                fieldPointer++

                // relation types count
                val relationTypesCount = fields[fieldPointer].toInt(10)
                fieldPointer++

                // relation types
                val relationTypes = Array(relationTypesCount) {
                    parseRelationType(fields[fieldPointer + it])
                }
                fieldPointer += relationTypesCount

                // polysemy count 2
                val senseCount2 = fields[fieldPointer].toInt()
                assert(senseCount == senseCount2)
                fieldPointer++

                // tag count
                val tagCnt = TagCnt.parseTagCnt(fields[fieldPointer])
                fieldPointer++

                // senses
                val senses = Array(senseCount) {
                    val sense = make(lemma, pos, fields[fieldPointer], it + 1)
                    fieldPointer++
                    sense
                }
                return Index(lemma, pos, senses, relationTypes, tagCnt)
            } catch (e: Exception) {
                throw ParsePojoException(e)
            }
        }
    }
}
