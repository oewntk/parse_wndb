/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.pojos

import java.util.*

/**
 * Base Sense
 *
 * @property synsetId      synset id
 * @property lemma         lemma
 * @property sensePosIndex sense index in pos
 *
 * @author Bernard Bou
 */
open class BaseSense protected constructor(
	val synsetId: SynsetId,
	val lemma: Lemma,
	private val sensePosIndex: Int
) {

	// I D E N T I T Y

	override fun equals(other: Any?): Boolean {
		if (this === other) {
			return true
		}
		if (other == null || javaClass != other.javaClass) {
			return false
		}
		val baseSense = other as BaseSense
		return sensePosIndex == baseSense.sensePosIndex && lemma == baseSense.lemma && synsetId == baseSense.synsetId
	}

	override fun hashCode(): Int {
		return Objects.hash(lemma, synsetId, sensePosIndex)
	}

	override fun toString(): String {
		return "(w=" + lemma.toString() + " s=" + synsetId.toString() + " n=" + this.sensePosIndex + ")"
	}

	companion object {

		/**
		 * Make sense
		 *
		 * @param lemma          lemma
		 * @param pos            pos
		 * @param synsetIdString synset id as string
		 * @param sensePosIndex  sense index in pos
		 * @return base sense
		 */
		fun make(lemma: Lemma, pos: Pos, synsetIdString: String, sensePosIndex: Int): BaseSense {
			return BaseSense(SynsetId(pos, synsetIdString.toLong()), lemma, sensePosIndex)
		}
	}
}