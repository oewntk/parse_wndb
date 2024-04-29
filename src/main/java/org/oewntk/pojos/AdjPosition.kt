/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.pojos

import org.oewntk.pojos.ParsePojoException

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

    PREDICATIVE("p", "predicate"),  //
    ATTRIBUTIVE("a", "attributive"),  //
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
            for (position in entries) {
                if (position.id == tag) {
                    return position
                }
            }
            return null
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
            for (adjPosition in entries) {
                if (name == adjPosition.id) {
                    return adjPosition
                }
            }
            throw ParsePojoException("AdjPosition:$name")
        }
    }
}