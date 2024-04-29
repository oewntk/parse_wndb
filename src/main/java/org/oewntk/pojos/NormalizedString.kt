/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.pojos

import java.io.Serializable
import java.util.*

/**
 * Normalized string
 * - underscore converted to space
 * - double single quote converted to double quote
 *
 * @property normalized normalized string
 *
 * @author Bernard Bou
 */
open class NormalizedString : Comparable<NormalizedString>, Serializable {
	var normalized: String

	/**
	 * Constructor
	 *
	 * @param rawStr raw string
	 */
	constructor(rawStr: String) {
		this.normalized = normalize(rawStr)
	}

	/**
	 * Copy constructor
	 *
	 * @param other other normalized string
	 */
	protected constructor(other: NormalizedString) {
		this.normalized = other.normalized
	}

	// I D E N T I T Y

	override fun equals(other: Any?): Boolean {
		if (this === other) {
			return true
		}
		if (other == null || javaClass != other.javaClass) {
			return false
		}
		val that = other as NormalizedString
		return normalized == that.normalized
	}

	override fun hashCode(): Int {
		return Objects.hash(normalized)
	}

	// O R D E R I N G

	override fun compareTo(other: NormalizedString): Int {
		return normalized.compareTo(other.normalized)
	}

	// T O S T R I N G

	override fun toString(): String {
		return this.normalized
	}

	companion object {

		/**
		 * Normalize
		 *
		 * @param rawStr raw string
		 * @return normalized string
		 */
		fun normalize(rawStr: String): String {
			// convert underscore to space
			var result = rawStr.replace('_', ' ')

			// double single quote to single quote
			result = result.replace("''", "'")
			return result
		}
	}
}
