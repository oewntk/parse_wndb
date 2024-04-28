/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.pojos

import java.util.*

/**
 * Lexical Relation (a lexical relation is an extended semantical relation)
 *
 * @param type         relation type
 * @param fromSynsetId source synset id
 * @param toSynsetId   target synset id
 * @param fromWord     source word
 * @param toWord       target word
 * @property fromWord  case-sensitive source word
 * @property toWord    unresolved word reference
 *
 * @author Bernard Bou
 */
class LexRelation(
	type: RelationType,
	fromSynsetId: SynsetId,
	toSynsetId: SynsetId,
	val fromWord: LemmaCS,
	@JvmField val toWord: LemmaRef
) : Relation(type, fromSynsetId, toSynsetId) {

	/**
	 * Resolve reference
	 *
	 * @param resolver resolver function
	 * @return resolved word
	 */
	fun resolveToWord(resolver: (SynsetId) -> CoreSynset): Lemma {
		return toWord.resolve(resolver)
	}

	// I D E N T I T Y

	override fun equals(other: Any?): Boolean {
		if (this === other) {
			return true
		}
		if (other == null || javaClass != other.javaClass) {
			return false
		}
		if (!super.equals(other)) {
			return false
		}
		val that = other as LexRelation
		return fromWord == that.fromWord && toWord == that.toWord
	}

	override fun hashCode(): Int {
		return Objects.hash(super.hashCode(), fromWord, toWord)
	}

	override fun toString(): String {
		return String.format("%s: %s[%s] -> %s[%d]", type.name2, fromSynsetId.toString(), this.fromWord, toSynsetId.toString(), toWord.wordNum)
	}

	fun toString(toWord: String?): String {
		return String.format("%s: %s[%s] -> %s[%s]", type.name2, fromSynsetId.toString(), this.fromWord, toSynsetId.toString(), toWord)
	}
}
