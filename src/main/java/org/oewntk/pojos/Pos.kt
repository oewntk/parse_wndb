/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.pojos

/**
 * Pos (part of speech)
 *
 * @param id          character id
 * @param name2       name2
 * @param description description
 *
 * @author Bernard Bou
 */
enum class Pos(
    val id: Char,
    val name2: String,
    val description: String,
) {

    NOUN('n', "noun", "noun"),
    VERB('v', "verb", "verb"),
    ADJ('a', "adj", "adjective"),
    ADV('r', "adv", "adverb");

    init {
        assert(id == 'n' || id == 'v' || id == 'a' || id == 'r')
    }

    /**
     * Get char id
     *
     * @return char id
     */
    fun toChar(): Char {
        return this.id
    }

    override fun toString(): String {
        return id.toString()
    }

    companion object {

        /**
         * Parse pos from character id
         *
         * @param id character id
         * @return pos
         * @throws ParsePojoException parse exception
         */
        @Throws(ParsePojoException::class)
        fun parsePos(id: Char): Pos {
            return entries
                .firstOrNull { id == it.id } ?: throw ParsePojoException("Pos:$id")
        }

        /**
         * Make pos from name
         *
         * @param name name
         * @return pos
         */
        fun fromName(name: String): Pos {
            return entries
                .firstOrNull { name == it.name2 } ?: throw IllegalArgumentException(name)
        }
    }
}
