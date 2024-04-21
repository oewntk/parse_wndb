/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

import java.util.Objects;

/**
 * (Semantical) Relation (a lexical relation is an extended semantical relation)
 *
 * @author Bernard Bou
 */
public class Relation
{
	public final RelationQualifier type;

	public final SynsetId fromSynsetId;

	public final SynsetId toSynsetId;

	/**
	 * Constructor
	 *
	 * @param type         relation type
	 * @param fromSynsetId source synset id
	 * @param toSynsetId   target synset id
	 */
	public Relation(final RelationQualifier type, final SynsetId fromSynsetId, final SynsetId toSynsetId)
	{
		this.type = type;
		this.fromSynsetId = fromSynsetId;
		this.toSynsetId = toSynsetId;
	}

	/**
	 * Get relation type
	 *
	 * @return relation type
	 */
	public RelationQualifier getType()
	{
		return type;
	}

	/**
	 * Get source synset id
	 *
	 * @return source synset id
	 */
	public SynsetId getFromSynsetId()
	{
		return fromSynsetId;
	}

	/**
	 * Get target synset id
	 *
	 * @return target synset id
	 */
	public SynsetId getToSynsetId()
	{
		return toSynsetId;
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		Relation relation = (Relation) o;
		return type == relation.type && fromSynsetId.equals(relation.fromSynsetId) && toSynsetId.equals(relation.toSynsetId);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(type, fromSynsetId, toSynsetId);
	}

	@Override
	public String toString()
	{
		return String.format("%s: %s -> %s", this.type.getType(), this.fromSynsetId.toString(), this.toSynsetId.toString());
	}
}
