/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.pojos

/**
 * Verb Frame (in verb.Framestext)
 *
 * @property id    frame id
 * @property frame frame text
 *
 * @author Bernard Bou
 */
class VerbFrame private constructor(
	val id: Int,
	private val frame: String
) {

	override fun toString(): String {
		return "id=" + this.id + " frame=" + this.frame
	}

	companion object {

		/**
		 * Parse from line
		 *
		 * @param line line
		 * @return verb frame
		 * @throws ParsePojoException parse exception
		 */
		@Throws(ParsePojoException::class)
		fun parseVerbFrame(line: String): VerbFrame {
			try {
				val id = line.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].toInt()
				val text = line.substring(3)
				return VerbFrame(id, text)
			} catch (e: Exception) {
				throw ParsePojoException(e)
			}
		}
	}
}
