/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.pojos

/**
 * Core Sense with sensekey
 *
 * @param synsetId      synset id
 * @param lemma         lemma
 * @param sensePosIndex sense index in pos
 * @property sensekey      sensekey
 *
 * @author Bernard Bou
 */
open class CoreSense(
    synsetId: SynsetId,
    lemma: Lemma,
    sensePosIndex: Int,
    val sensekey: Sensekey,
) : BaseSense(synsetId, lemma, sensePosIndex) {

    override fun toString(): String {
        return super.toString() + " k=" + sensekey.toString()
    }
}
