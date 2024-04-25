/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.pojos

/**
 * Pos (part of speech)
 *
 * @param id          character id
 * @param name2       name2
 * @param description description
 *
 * @author Bernard Bou
 */
enum class Pos(
	val id: Char,
	val name2: String,
	val description: String
) {
	NOUN('n', "noun", "noun"),
	VERB('v', "verb", "verb"),
	ADJ('a', "adj", "adjective"),
	ADV('r', "adv", "adverb");

	init {
		assert(id == 'n' || id == 'v' || id == 'a' || id == 'r')
	}

	/**
	 * Get char id
	 *
	 * @return char id
	 */
	fun toChar(): Char {
		return this.id
	}

	override fun toString(): String {
		return id.toString()
	}

	companion object {

		/**
		 * Parse pos from character id
		 *
		 * @param id character id
		 * @return pos
		 * @throws ParsePojoException parse exception
		 */
		@JvmStatic
		@Throws(ParsePojoException::class)
		fun parsePos(id: Char): Pos {
			for (pos in entries) {
				if (id == pos.id) {
					return pos
				}
			}
			throw ParsePojoException("Pos:$id")
		}

		/**
		 * Make pos from pos index
		 *
		 * @param index0 index
		 * @return pos
		 */
		fun fromIndex(index0: Int): Pos {
			val index = index0 - 1
			if (index >= 0 && index < entries.size) {
				return entries[index]
			}
			throw IllegalArgumentException("Pos:$index")
		}

		/**
		 * Make pos from name
		 *
		 * @param name name
		 * @return pos
		 */
		@JvmStatic
		fun fromName(name: String): Pos {
			for (pos in entries) {
				if (name == pos.name2) {
					return pos
				}
			}
			throw IllegalArgumentException(name)
		}
	}
}
