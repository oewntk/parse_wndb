/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.pojos

import java.util.*

/**
 * Lemma Reference (nth word in synset)
 *
 * @property synsetId synsetId of the synset the lemma is member of
 * @property wordNum  1-based number of the word in the lemma list
 *
 * @author Bernard Bou
 */
class LemmaRef(
	val synsetId: SynsetId,
	val wordNum: Int
) {

	/**
	 * Dereference / Resolve
	 *
	 * @param f functions that when applied to synsetId yields synset
	 * @return lemma referred to by reference
	 */
	fun resolve(f: (SynsetId) -> CoreSynset): Lemma {
		val synset = f.invoke(this.synsetId)
		return synset.cSLemmas[wordNum - 1].lemma
	}

	/**
	 * Dereference / Resolve
	 *
	 * @param synset synset
	 * @return lemma referred to by reference
	 */
	fun resolve(synset: CoreSynset): Lemma {
		return synset.cSLemmas[wordNum - 1].lemma
	}

	// I D E N T I T Y

	override fun equals(other: Any?): Boolean {
		if (this === other) {
			return true
		}
		if (other == null || javaClass != other.javaClass) {
			return false
		}
		val lemmaRef = other as LemmaRef
		return wordNum == lemmaRef.wordNum && synsetId == lemmaRef.synsetId
	}

	override fun hashCode(): Int {
		return Objects.hash(synsetId, wordNum)
	}

	override fun toString(): String {
		return synsetId.toString() + "[" + this.wordNum + "]"
	}
}
