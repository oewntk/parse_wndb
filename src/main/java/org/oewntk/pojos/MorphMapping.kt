/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.pojos

import org.oewntk.pojos.Lemma.Companion.make

/**
 * Morphology ({noun|verb|adj|adv|}.exc)
 *
 * @property morph  inflected form
 * @property lemmas base forms
 * @property pos    part of speech
 *
 * @author Bernard Bou
 */
class MorphMapping private constructor(
    val morph: NormalizedString,
    val lemmas: Collection<Lemma>,
    val pos: Pos,
) {

    override fun toString(): String {
        return "$morph $pos <- ${lemmas.joinToString(separator = " ")}"
    }

    companion object {

        /**
         * Parse morph mapping from line
         *
         * @param line line
         * @param pos  part of speech
         * @return morph mapping
         * @throws ParsePojoException parse exception
         * ```
         * an inflected form of a word or collocation, followed by one or more base forms
         * auspices auspex auspice (2 lemmas)
         * mice mouse
         * ```
         */
        @Throws(ParsePojoException::class)
        fun parseMorphMapping(line: String, pos: Pos): MorphMapping {
            try {
                val fields = line.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                // morph
                val morph = NormalizedString(fields[0])

                // lemmas
                val lemmas = fields
                    .asSequence()
                    .drop(1)
                    .map { field -> make(field) }
                    .toList()

                return MorphMapping(morph, lemmas, pos)
            } catch (e: Exception) {
                throw ParsePojoException(e)
            }
        }
    }
}