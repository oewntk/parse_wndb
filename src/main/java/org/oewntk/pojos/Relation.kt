/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.pojos

import java.util.*

/**
 * (Semantical) Relation (a lexical relation is an extended semantical relation)
 *
 * @property type         relation type
 * @property fromSynsetId source synset id
 * @property toSynsetId   target synset id
 *
 * @author Bernard Bou
 */
open class Relation(
    val type: RelationType,
    val fromSynsetId: SynsetId,
    val toSynsetId: SynsetId,
) {

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val relation = other as Relation
        return type == relation.type && fromSynsetId == relation.fromSynsetId && toSynsetId == relation.toSynsetId
    }

    override fun hashCode(): Int {
        return Objects.hash(type, fromSynsetId, toSynsetId)
    }

    override fun toString(): String {
        return String.format("%s: %s -> %s", type.name2, fromSynsetId.toString(), toSynsetId.toString())
    }
}
