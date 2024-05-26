/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.pojos

import java.util.*

/**
 * Pair of lemma with cased (case-sensitive) form
 *
 * @property lemma lemma
 * @property cased cased form
 *
 * @author Bernard Bou
 */
class LemmaCS(
    val lemma: Lemma,
    private val cased: NormalizedString,
) {

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as LemmaCS
        return lemma == that.lemma && cased == that.cased
    }

    override fun hashCode(): Int {
        return Objects.hash(lemma, cased)
    }

    override fun toString(): String {
        return cased.toString()
    }
}
