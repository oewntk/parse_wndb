/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

import java.util.Objects;

/**
 * Synset Id
 *
 * @author Bernard Bou
 */
public class SynsetId
{
	/**
	 * Part of speech
	 */
	private final Pos pos;

	/**
	 * The offset value is guaranteed to be unique only relative to the part of speech
	 */
	private final long offset;

	/**
	 * Constructor
	 *
	 * @param pos    part of speech
	 * @param offset offset in file
	 */
	public SynsetId(final Pos pos, final long offset)
	{
		this.pos = pos;
		this.offset = offset;
	}

	/**
	 * Get file offset
	 *
	 * @return file offset
	 */
	public long getOffset()
	{
		return this.offset;
	}

	/**
	 * Get part of speech
	 *
	 * @return part of speech
	 */
	public Pos getPos()
	{
		return this.pos;
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
		SynsetId synsetId = (SynsetId) o;
		return offset == synsetId.offset && pos == synsetId.pos;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(pos, offset);
	}

	@Override
	public String toString()
	{
		return "" + getOffset() + '-' + getPos().toChar();
	}
}
