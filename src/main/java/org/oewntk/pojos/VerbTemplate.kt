/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.pojos

/**
 * Verb template (sents.vrb)
 *
 * @property id       template id
 * @property template template text
 *
 * @author Bernard Bou
 */
class VerbTemplate private constructor(
    val id: Int,
    private val template: String,
) {

    override fun toString(): String {
        return "id=" + this.id + " template=" + this.template
    }

    companion object {

        /**
         * Parse from line
         *
         * @param line line
         * @return verb template
         * @throws ParsePojoException parse exception
         */
        @Throws(ParsePojoException::class)
        fun parseVerbTemplate(line: String): VerbTemplate {
            try {
                val fields = line.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val id = fields[0].toInt()
                val text = line.substring(fields[0].length + 1)
                return VerbTemplate(id, text)
            } catch (e: Exception) {
                throw ParsePojoException(e)
            }
        }
    }
}