/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.pojos

/**
 * Sense
 *
 * @param synsetId      synset id
 * @param lemma         lemma
 * @param sensePosIndex sense index in pos
 * @param sensekey      sensekey
 * @property tagCnt     tag count
 */
class Sense(
    synsetId: SynsetId,
    lemma: Lemma,
    sensePosIndex: Int,
    sensekey: Sensekey,
    val tagCnt: TagCnt,
) : CoreSense(synsetId, lemma, sensePosIndex, sensekey) {

    override fun toString(): String {
        return super.toString() + " l" + " t" + this.tagCnt
    }

    companion object {

        /**
         * Parse sense from line
         *
         * @param line line
         * @return sense
         * @throws ParsePojoException parse exception
         */
        @Throws(ParsePojoException::class)
        fun parseSense(line: String): Sense {
            try {
                // line from index.sense: sense_key synset_offset  sense_number  tag_cnt
                // [0] sensekey, an encoding of the word sense
                // [1] synset offset, the byte offset that the synset containing the sense is found at in the database "data" file corresponding to the part of speech encoded in the sense_key . synset_offset is an 8 digit, zero-filled decimal integer, and can be used with fseek(3) to read a synset from the data file. When passed to the WordNet library function read_synset() along with the syntactic category, a data structure containing the parsed synset is returned.
                // [2] sense number, a decimal integer indicating the sense number of the word, within the part of speech encoded in sense_key , in the WordNet database.
                // [3] tag count, the decimal number of times the sense is tagged in various semantic concordance texts. A tag_cnt of 0 indicates that the sense has not been semantically tagged.

                // read line into fields

                val fields = line.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                // core fields
                val sensekey = Sensekey.parseSensekey(fields[0])
                val type = sensekey.type
                val lemma = sensekey.lemma
                val sensenum = fields[2].toInt()
                val synsetId = SynsetId(type.toPos(), fields[1].toLong())

                // parse tag/lexid
                val tagCnt = TagCnt.parseTagCnt(fields[3], sensenum)

                return Sense(synsetId, lemma, sensenum, sensekey, tagCnt)
            } catch (e: Exception) {
                throw ParsePojoException(e)
            }
        }
    }
}
