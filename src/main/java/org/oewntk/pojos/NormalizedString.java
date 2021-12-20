/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

import java.io.Serializable;
import java.util.Objects;

/**
 * Normalized string
 * - underscore converted to space
 * - double single quote converted to double quote
 *
 * @author Bernard Bou
 */
public class NormalizedString implements Comparable<NormalizedString>, Serializable
{
	private static final long serialVersionUID = 1L;

	protected String normalized;

	public NormalizedString(final String rawStr)
	{
		this.normalized = normalize(rawStr);
	}

	protected NormalizedString(final NormalizedString other)
	{
		this.normalized = other.normalized;
	}

	public static String normalize(final String rawStr)
	{
		// convert underscore to space
		String result = rawStr.replace('_', ' ');

		// double single quote to single quote
		result = result.replace("''", "'");
		return result;
	}

	public String getNormalized()
	{
		return this.normalized;
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
		NormalizedString that = (NormalizedString) o;
		return normalized.equals(that.normalized);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(normalized);
	}


	// O R D E R I N G

	@Override
	public int compareTo(final NormalizedString that)
	{
		return this.normalized.compareTo(that.normalized);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return this.normalized;
	}
}
