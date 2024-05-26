/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.pojos

/**
 * Verb Frame reference
 *
 * @property lemmas  lemmas
 * @property frameId frame id
 *
 * @author Bernard Bou
 */
class VerbFrameRef(
    val lemmas: Array<Lemma>,
    val frameId: Int,
) {

    override fun toString(): String = lemmas.joinToString(separator = ",", prefix = "{", postfix = "}:$frameId")
}