/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.pojos

import java.util.*
import java.util.regex.Pattern

/**
 * Sensekey
 *
 * @property key key
 * @property word normalized word
 * @property type type
 * @property domain domain
 * @property lexId lex id
 * @property headWord head word or null if none
 * @property headLexId head lexid or -1 if none
 *
 * @author Bernard Bou
 */
class Sensekey {

    private val key: String

    val word: NormalizedString // may contain uppercase

    val type: Type

    val domain: Domain
    val lexId: Int

    val headWord: NormalizedString? // or null

    val headLexId: Int // -1 for none

    /**
     * Copy constructor
     *
     * @param other other sensekey
     */
    constructor(other: Sensekey) {
        this.key = other.key
        this.word = other.word
        this.type = other.type
        this.domain = other.domain
        this.lexId = other.lexId
        this.headWord = other.headWord
        this.headLexId = other.headLexId
    }

    /**
     * Constructor
     *
     * @param word      word (may contain uppercase)
     * @param type      type
     * @param domain    domain
     * @param lexId     lexid
     * @param headWord  head word
     * @param headLexId head lexid
     * @param key       sensekey
     */
    private constructor(word: NormalizedString, type: Type, domain: Domain, lexId: Int, headWord: NormalizedString?, headLexId: Int, key: String) {
        this.key = key.trim { it <= ' ' }
        this.word = word
        this.type = type
        this.domain = domain
        this.lexId = lexId
        this.headWord = headWord
        this.headLexId = headLexId
    }

    val lemma: Lemma
        /**
         * Get lemma
         *
         * @return lemma
         */
        get() = Lemma(this.word)

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val sensekey = other as Sensekey
        return key == sensekey.key
    }

    override fun hashCode(): Int {
        return Objects.hash(key)
    }

    override fun toString(): String {
        return this.key
    }

    /**
     * Get parsed sensekey string
     *
     * @return parsed sensekey string
     */
    fun toParsedString(): String {
        return "word=" + this.word + " lexid=" + this.lexId + " domain=" + this.domain + " type=" + this.type
    }

    companion object {

        private val patternBreak: Pattern = Pattern.compile("(?<!\\u005C)%")

        //public static final String ESCAPED_PERCENT = "\\%";
        const val ESCAPED_PERCENT: String = "~"

        /**
         * Parse sensekey from string
         *
         * @param str string
         * @return sensekey
         * @throws ParsePojoException parse exception
         */
        @Throws(ParsePojoException::class)
        fun parseSensekey(str: String): Sensekey {
            val fields = decode(str)
            if (fields.size < 4) {
                throw ParsePojoException("Sensekey:$str")
            }
            try {
                val word = NormalizedString(fields[0])
                val type = Type.fromIndex(fields[1].toInt())
                val domain = Domain.parseDomainId(fields[2])
                val lexid = fields[3].toInt()
                val headWord = if (fields[4].isEmpty()) null else NormalizedString(fields[4])
                val headLexid = if (fields[5].isEmpty()) -1 else fields[5].toInt()
                return Sensekey(word, type, domain, lexid, headWord, headLexid, str)
            } catch (e: Exception) {
                throw ParsePojoException("Sensekey:$str")
            }
        }

        /**
         * Decode sensekey string into fields
         *
         * @param skStr sensekey string
         * @return fields, fields[0] lemma (not space-normalized), fields[1] pos, fields[2] lexfile, fields[3] lexid, fields[4] head lemma (or ""), not space-normalized, fields[5] head lexid (or "")
         */
        fun decode(skStr: String): Array<String> {
            val fields1 = patternBreak.split(skStr)
            assert(fields1.size == 2)
            // lemma
            val field0 = fields1[0].replace(ESCAPED_PERCENT, "%")
            // lexsense
            val lexSense = fields1[1].replace(ESCAPED_PERCENT, "%")
            // String typeFileLexid = lexSense.substring(0, 8);
            val field1 = lexSense.substring(0, 1) // pos
            val field2 = lexSense.substring(2, 4) // lexfile
            val field3 = lexSense.substring(5, 7) // lexid
            val last = lexSense.lastIndexOf(':')
            val field4 = lexSense.substring(8, last) // head (or "")
            val field5 = lexSense.substring(last + 1) // head lexid (or "")
            return arrayOf(field0, field1, field2, field3, field4, field5)
        }
    }
}