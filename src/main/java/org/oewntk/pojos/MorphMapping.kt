/*
 * Copyright (c) 2021. Bernard Bou.
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
	val pos: Pos
) {

	override fun toString(): String {
		val sb = StringBuilder()
		sb.append(morph.toString())
		sb.append(' ')
		sb.append(pos.toChar())
		sb.append("<-")
		var first = true
		for (lemma in this.lemmas) {
			if (first) {
				first = false
			} else {
				sb.append(' ')
			}
			sb.append(lemma)
		}
		return sb.toString()
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
				val lemmas: MutableCollection<Lemma> = ArrayList()
				if (fields.size > 1) {
					for (i in 1 until fields.size) {
						val lemma = make(fields[i])
						lemmas.add(lemma)
					}
				}
				return MorphMapping(morph, lemmas, pos)
			} catch (e: Exception) {
				throw ParsePojoException(e)
			}
		}
	}
}