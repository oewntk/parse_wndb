/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.pojos

/**
 * Type (part-of-speech, with adj split between heads and satellites)
 *
 * @param id          char id
 * @param name2       name
 * @param description description
 * @param pos         pos
 *
 * @author Bernard Bou
 */
enum class Type(
    id: Char,
    name2: String,
    description: String,
    pos: Pos,
) {

    NOUN('n', "noun", "noun", Pos.NOUN),  
    VERB('v', "verb", "verb", Pos.VERB),  
    ADJHEAD('a', "adj_head", "adjective", Pos.ADJ),  
    ADV('r', "adv", "adverb", Pos.ADV),  
    ADJSAT('s', "adj_sat", "adjective satellite", Pos.ADJ);

    private val id: Char
    private val name2: String
    private val description: String
    private val pos: Pos

    init {
        assert(id == 'n' || id == 'v' || id == 'a' || id == 'r' || id == 's')
        this.id = id
        this.name2 = name2
        this.description = description
        this.pos = pos
    }

    /**
     * Get char id
     *
     * @return char id
     */
    fun toChar(): Char {
        return this.id
    }

    /**
     * Get pos
     *
     * @return pos
     */
    fun toPos(): Pos {
        return this.pos
    }

    val isAdj: Boolean
        /**
         * Whether this synset has adj type
         *
         * @return whether this synset has adj type
         */
        get() = this == ADJHEAD || (this == ADJSAT)

    override fun toString(): String {
        return id.toString()
    }

    companion object {

        /**
         * Parse type from character id
         *
         * @param id character id
         * @return type
         * @throws ParsePojoException parse exception
         */
        @Throws(ParsePojoException::class)
        fun parseType(id: Char): Type {
            for (type in entries) {
                if (id == type.id) {
                    return type
                }
            }
            throw ParsePojoException("Type:$id")
        }

        /**
         * Make type from type index
         *
         * @param index0 index
         * @return type
         */
        fun fromIndex(index0: Int): Type {
            val index = index0 - 1
            if (index >= 0 && index < entries.size) {
                return entries[index]
            }
            throw IllegalArgumentException("Type:$index")
        }

        /**
         * Make type from name
         *
         * @param name name
         * @return type
         */
        fun fromName(name: String): Type? {
            for (type in entries) {
                if (name == type.name) {
                    return type
                }
            }
            return null
        }
    }
}
