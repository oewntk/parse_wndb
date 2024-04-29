/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.pojos

import java.util.regex.Pattern

/**
 * Adjective Lemma annotation (suffix-stripped)
 *
 * @author Bernard Bou
 */
class AdjLemma private constructor(normString: TrimmedNormalizedString, adjPosition: AdjPosition) : Lemma(normString) {

    /**
     * Get position
     *
     * @return position
     */
    val position: AdjPosition = adjPosition

    /**
     * Get position tag (to append to lemma)
     *
     * @return position string
     */
    fun toPositionSuffix(): String {
        return "(" + position.id + ")"
    }

    // I D E N T I T Y

    //override fun equals(other: Any?): Boolean {
    //	// ignore position
    //	return super.equals(other)
    //}

    //override fun hashCode(): Int {
    //	// ignore position
    //	return super.hashCode()
    //}

    companion object {

        private const val REGEX = "\\((a|p|ip)\\)"

        private val PATTERN: Pattern = Pattern.compile(REGEX)

        /**
         * Constructor
         *
         * @param normalizedString normalized string
         * @return adj lemma if input matches pattern or standard lemma
         */
        fun makeAdj(normalizedString: NormalizedString): Lemma {
            // trailing adjective position

            val matcher = PATTERN.matcher(normalizedString.toString())
            if (matcher.find()) {
                try {
                    // parse position
                    val adjPosition = AdjPosition.parseAdjPosition(matcher.group())

                    // strip position
                    return AdjLemma(TrimmedNormalizedString(normalizedString), adjPosition)
                } catch (e: ParsePojoException) {
                    //
                }
            }
            return make(normalizedString)
        }
    }
}
