/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.pojos

/**
 * Lexid
 *
 * @property id lex id
 *
 * @author Bernard Bou
 */
class LexId private constructor(
	val id: Int
) {

	override fun toString(): String {
		return id.toString()
	}

	companion object {

		/**
		 * Factory
		 *
		 * @param sensekey sensekey
		 * @return lexid
		 */
		fun make(sensekey: Sensekey): LexId {
			return LexId(sensekey.lexId)
		}
	}
}