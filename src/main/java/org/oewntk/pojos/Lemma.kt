/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.pojos

import java.io.Serializable
import java.util.*

/**
 * Lemma (normalized, lower-cased)
 *
 * @param normString normalized string
 * @property lowerCasedNormalized lower-cased normalized string
 *
 * @author Bernard Bou
 */
open class Lemma(
    normString: NormalizedString,
) : Comparable<Lemma>, Serializable {

    private val lowerCasedNormalized = normString.normalized.lowercase(Locale.getDefault())

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val lemma = other as Lemma
        return lowerCasedNormalized == lemma.lowerCasedNormalized
    }

    override fun hashCode(): Int {
        return Objects.hash(lowerCasedNormalized)
    }

    // O R D E R I N G

    override fun compareTo(other: Lemma): Int {
        return lowerCasedNormalized.compareTo(other.lowerCasedNormalized)
    }

    // T O S T R I N G

    override fun toString(): String {
        return this.lowerCasedNormalized
    }

    companion object {

        // factories

        /**
         * Make from bare normalized string
         *
         * @param bareNormalized normalized bare normalized string
         * @return lemma
         */
        fun make(bareNormalized: TrimmedNormalizedString): Lemma {
            return Lemma(bareNormalized)
        }

        /**
         * Make from normalized string
         *
         * @param normalized normalized string
         * @return lemma
         */
        fun make(normalized: NormalizedString): Lemma {
            return Lemma(normalized)
        }

        /**
         * Make from rawString
         *
         * @param rawString raw string
         * @return lemma
         */
        fun make(rawString: String?): Lemma {
            // normalize spaces then lowercase
            return Lemma(NormalizedString(rawString!!))
        }
    }
}
