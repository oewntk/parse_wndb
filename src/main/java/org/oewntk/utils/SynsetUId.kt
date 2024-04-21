/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.utils

import org.oewntk.pojos.Pos
import org.oewntk.pojos.SynsetId

/**
 * Synset UId, unique Id across parts-of-speech
 *
 * @param uniqueId unique id
 * @property synsetId synset id
 *
 * @author Bernard Bou
 */
class SynsetUId
private constructor(uniqueId: Long) {
	private var synsetId: SynsetId?

	init {
		for (p in Pos.entries) {
			if (uniqueId >= getBaseUID(p.toChar()) && uniqueId < getCeilingUID(p.toChar())) {
				val relativeId = uniqueId - getBaseUID(p.toChar())
				this.synsetId = SynsetId(p, relativeId)
				break
			}
		}
		// never reached
		this.synsetId = null
	}

	/**
	 * Get unique id
	 *
	 * @return unique id
	 */
	private fun toUID(): Long {
		val base = getBaseUID(synsetId!!.pos.toChar())
		return base + synsetId!!.offset
	}

	/**
	 * Get unique id string
	 *
	 * @return unique id string
	 */
	fun toUIDString(): String {
		val uid = toUID()
		return uid.toString()
	}

	companion object {

		/**
		 * Make synset id fri unique id
		 *
		 * @param uniqueId unique id
		 * @return synset
		 */
		fun make(uniqueId: Long): SynsetUId {
			return SynsetUId(uniqueId)
		}

		/**
		 * Get base UID for pos
		 *
		 * @param pos part of speech
		 * @return base uid for given pos
		 */
		private fun getBaseUID(pos: Char): Long {
			when (pos) {
				'n' -> return 100000000
				'v' -> return 200000000
				'a', 's' -> return 300000000
				'r' -> return 400000000
				else -> {}
			}
			return 900000000 // invalid value
		}

		/**
		 * Get ceiling UID for pos
		 *
		 * @param pos part of speech
		 * @return ceiling uid for given pos
		 */
		private fun getCeilingUID(pos: Char): Long {
			return getBaseUID(pos) + 100000000 - 1
		}
	}
}
