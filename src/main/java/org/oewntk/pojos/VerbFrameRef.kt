/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.pojos

/**
 * Verb Frame reference
 *
 * @property lemmas  lemmas
 * @property frameId frame id
 *
 * @author Bernard Bou
 */
class VerbFrameRef(
	@JvmField val lemmas: Array<Lemma>,
	val frameId: Int
) {

	override fun toString(): String {
		val sb = StringBuilder()
		sb.append("{")
		for ((i, lemma) in this.lemmas.withIndex()) {
			if (i != 0) {
				sb.append(",")
			}
			sb.append(lemma.toString())
		}
		sb.append("}:")
		sb.append(this.frameId)
		return sb.toString()
	}
}