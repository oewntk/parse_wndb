/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.pojos

/**
 * Adjective Position
 *
 * @param id          position tag
 * @param description position description
 *
 * @author Bernard Bou
 */
enum class AdjPosition(
    val id: String,
    private val description: String,
) {

    PREDICATIVE("p", "predicate"),
    ATTRIBUTIVE("a", "attributive"),
    POSTNOMINAL("ip", "immediately postnominal");

    override fun toString(): String {
        return "(" + this.id + "}"
    }

    companion object {

        /**
         * Find adj position from tag
         *
         * @param tag tag
         * @return adj position
         */
        fun find(tag: String): AdjPosition? {
            return entries.find { it.id == tag }
        }

        /**
         * Parse adj position from line
         *
         * @param suffix suffix = '(tag)'
         * @return adj position
         * @throws ParsePojoException parse exception
         */
        @Throws(ParsePojoException::class)
        fun parseAdjPosition(suffix: String): AdjPosition {
            // remove parentheses
            val name = suffix.substring(1, suffix.length - 1)
            // look up
            return entries.firstOrNull { it.id == name } ?: throw ParsePojoException("AdjPosition:$name")
        }
    }
}