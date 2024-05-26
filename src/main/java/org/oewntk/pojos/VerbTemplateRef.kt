/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.pojos

import org.oewntk.pojos.Sensekey.Companion.parseSensekey

/**
 * Sense reference to verb template
 *
 * @property sensekey sensekey
 * @property id       verb template id
 *
 * @author Bernard Bou
 */
class VerbTemplateRef private constructor(
    val sensekey: Sensekey,
    val id: Int,
) {

    override fun toString(): String {
        return "id=" + this.id + " sensekey=" + this.sensekey
    }

    companion object {

        /**
         * Parse from line
         *
         * @param line line
         * @return array of verb template references
         * @throws ParsePojoException parse exception
         */
        @Throws(ParsePojoException::class)
        fun parseVerbTemplateRef(line: String): Array<VerbTemplateRef>? {
            try {
                val fields = line.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (fields.size <= 1) {
                    return null
                }

                // parse sensekey
                val sensekey = parseSensekey(fields[0])

                val subFields = fields[1].split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val count = subFields.size

                // pair sensekey with id for each id
                val refs = Array(count) {
                    val id = subFields[it].toInt()
                    VerbTemplateRef(sensekey, id)
                }
                return refs
            } catch (e: Exception) {
                throw ParsePojoException(e)
            }
        }
    }
}