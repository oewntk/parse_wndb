/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.pojos

import java.util.*

/**
 * Synset Id
 *
 * @param pos    part of speech
 * @param offset offset in file, the offset value is guaranteed to be unique only relative to the part of speech
 *
 * @author Bernard Bou
 */
class SynsetId(
	@JvmField val pos: Pos,
	@JvmField val offset: Long
) {

	// I D E N T I T Y

	override fun equals(other: Any?): Boolean {
		if (this === other) {
			return true
		}
		if (other == null || javaClass != other.javaClass) {
			return false
		}
		val synsetId = other as SynsetId
		return offset == synsetId.offset && pos == synsetId.pos
	}

	override fun hashCode(): Int {
		return Objects.hash(pos, offset)
	}

	override fun toString(): String {
		return "" + offset + '-' + pos.toChar()
	}
}
