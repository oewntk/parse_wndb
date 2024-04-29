/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.pojos

/**
 * Tag
 *
 * @property tagCount tag count
 * @property senseNum sense number (index)
 *
 * @author Bernard Bou
 */
class TagCnt(
    val tagCount: Int,
    val senseNum: Int,
) {

    override fun toString(): String {
        return tagCount.toString()
    }

    companion object {

        /**
         * Parse tagcount from string
         *
         * @param str      string
         * @param senseNum senseNumber
         * @return tag count
         * @throws ParsePojoException parse exception
         */
        @JvmOverloads
        @Throws(ParsePojoException::class)
        fun parseTagCnt(str: String, senseNum: Int = 0): TagCnt {
            try {
                val tagCount = str.toInt()
                return TagCnt(tagCount, senseNum)
            } catch (e: Exception) {
                throw ParsePojoException(e)
            }
        }
    }
}