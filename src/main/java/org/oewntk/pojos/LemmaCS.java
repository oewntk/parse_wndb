/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

import java.util.Objects;

/**
 * Pair of lemma with cased (case-sensitive) form
 *
 * @author Bernard Bou
 */
public class LemmaCS
{
	public final Lemma lemma;

	public final NormalizedString cased;

	/**
	 * Constructor
	 *
	 * @param lemma lemma
	 * @param cased cased form
	 */
	public LemmaCS(final Lemma lemma, final NormalizedString cased)
	{
		this.lemma = lemma;
		this.cased = cased;
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
		LemmaCS that = (LemmaCS) o;
		return lemma.equals(that.lemma) && cased.equals(that.cased);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(lemma, cased);
	}

	@Override
	public String toString()
	{
		return this.cased.toString();
	}
}
