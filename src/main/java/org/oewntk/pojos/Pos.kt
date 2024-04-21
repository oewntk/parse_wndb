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
enum class Pos(id: Char, name2: String, description: String) {
	NOUN('n', "noun", "noun"),
	VERB('v', "verb", "verb"),
	ADJ('a', "adj", "adjective"),
	ADV('r', "adv", "adverb");

	private val id: Char

	/**
	 * Get name
	 *
	 * @return name
	 */
	val name2: String

	/**
	 * Get description
	 *
	 * @return description
	 */
	private val description: String

	init {
		assert(id == 'n' || id == 'v' || id == 'a' || id == 'r')
		this.id = id
		this.name2 = name
		this.description = description
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
		fun fromName(name: String): Pos? {
			for (pos in entries) {
				if (name == pos.name) {
					return pos
				}
			}
			return null
		}
	}
}
