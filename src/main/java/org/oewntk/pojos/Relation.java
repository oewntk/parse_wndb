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
	public final RelationType type;

	public final SynsetId fromSynsetId;

	public final SynsetId toSynsetId;

	/**
	 * Constructor
	 *
	 * @param type         relation type
	 * @param fromSynsetId source synset id
	 * @param toSynsetId   target synset id
	 */
	public Relation(final RelationType type, final SynsetId fromSynsetId, final SynsetId toSynsetId)
	{
		this.type = type;
		this.fromSynsetId = fromSynsetId;
		this.toSynsetId = toSynsetId;
	}

	public RelationType getType()
	{
		return type;
	}

	public SynsetId getFromSynsetId()
	{
		return fromSynsetId;
	}

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
		return this.type.getName() + ":" + this.toSynsetId.toString();
	}
}
