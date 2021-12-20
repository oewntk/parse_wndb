/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

import java.io.Serializable;
import java.util.Objects;

/**
 * Lemma (normalized, lower-cased)
 *
 * @author Bernard Bou
 */
public class Lemma /* extends NormalizedString */ implements Comparable<Lemma>, Serializable
{
	private static final long serialVersionUID = 1L;

	private final String lowerCasedNormalized;

	/**
	 * Constructor from normalized string
	 *
	 * @param normString normalized string
	 */
	@SuppressWarnings("CommentedOutCode")
	protected Lemma(final NormalizedString normString)
	{
		// to lower case
		this.lowerCasedNormalized = normString.normalized.toLowerCase();
		//if (this.entry.matches(".*\\(.*\\)"))
		//	throw new RuntimeException(this.entry);
	}

	// factory

	/**
	 * Make from bare normalized string
	 *
	 * @param bareNormalized normalized bare normalized string
	 * @return lemma
	 */
	public static Lemma make(final TrimmedNormalizedString bareNormalized)
	{
		return new Lemma(bareNormalized);
	}

	/**
	 * Make from normalized string
	 *
	 * @param normalized normalized string
	 * @return lemma
	 */
	public static Lemma make(final NormalizedString normalized)
	{
		return new Lemma(normalized);
	}

	/**
	 * Make from rawString
	 *
	 * @param rawString raw string
	 * @return lemma
	 */
	public static Lemma make(final String rawString)
	{
		// normalize spaces then lowercase
		return new Lemma(new NormalizedString(rawString));
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
		Lemma lemma = (Lemma) o;
		return lowerCasedNormalized.equals(lemma.lowerCasedNormalized);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(lowerCasedNormalized);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final Lemma that)
	{
		return this.lowerCasedNormalized.compareTo(that.lowerCasedNormalized);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return this.lowerCasedNormalized;
	}
}
