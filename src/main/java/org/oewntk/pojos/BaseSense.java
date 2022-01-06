/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

import java.util.Objects;

/**
 * Base Sense
 *
 * @author Bernard Bou
 */
public class BaseSense
{
	public final Lemma lemma;

	public final SynsetId synsetId;

	public final int sensePosIndex;

	/**
	 * Constructor
	 *
	 * @param synsetId      synset id
	 * @param word          word
	 * @param sensePosIndex sense index in pos
	 */
	protected BaseSense(final SynsetId synsetId, final Lemma word, final int sensePosIndex)
	{
		this.lemma = word;
		this.synsetId = synsetId;
		this.sensePosIndex = sensePosIndex;
	}

	/**
	 * Make sense
	 *
	 * @param lemma          lemma
	 * @param pos            pos
	 * @param synsetIdString synset id as string
	 * @param sensePosIndex  sense index in pos
	 * @return base sense
	 */
	public static BaseSense make(final Lemma lemma, final Pos pos, final String synsetIdString, final int sensePosIndex)
	{
		return new BaseSense(new SynsetId(pos, Long.parseLong(synsetIdString)), lemma, sensePosIndex);
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
		BaseSense baseSense = (BaseSense) o;
		return sensePosIndex == baseSense.sensePosIndex && lemma.equals(baseSense.lemma) && synsetId.equals(baseSense.synsetId);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(lemma, synsetId, sensePosIndex);
	}

	@Override
	public String toString()
	{
		return "(w=" + this.lemma.toString() + " s=" + this.synsetId.toString() + " n=" + this.sensePosIndex + ")";
	}
}