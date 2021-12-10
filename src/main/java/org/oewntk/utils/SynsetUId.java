/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.utils;

import org.oewntk.pojos.Pos;
import org.oewntk.pojos.SynsetId;

/**
 * Synset UId, unique Id across parts-of-speech
 *
 * @author Bernard Bou
 */
public class SynsetUId
{
	private final SynsetId synsetId;

	/**
	 * Constructor from unique id
	 *
	 * @param uniqueId unique id
	 */
	private SynsetUId(final long uniqueId)
	{
		for (final Pos p : Pos.values())
		{
			if (uniqueId >= SynsetUId.getBaseUID(p.toChar()) && uniqueId < SynsetUId.getCeilingUID(p.toChar()))
			{
				final long relativeId = uniqueId - SynsetUId.getBaseUID(p.toChar());
				this.synsetId = new SynsetId(p, relativeId);
				return;
			}
		}
		// never reached
		this.synsetId = null;
	}

	/**
	 * Make synset id fri unique id
	 *
	 * @param uniqueId unique id
	 * @return synset
	 */
	public static SynsetUId make(final long uniqueId)
	{
		return new SynsetUId(uniqueId);
	}

	/**
	 * Get unique id
	 *
	 * @return unique id
	 */
	public long toUID()
	{
		final long base = SynsetUId.getBaseUID(this.synsetId.getPos().toChar());
		return base + this.synsetId.getOffset();
	}

	/**
	 * Get unique id string
	 *
	 * @return unique id string
	 */
	public String toUIDString()
	{
		final long uid = toUID();
		return Long.toString(uid);
	}

	/**
	 * Get base UID for pos
	 *
	 * @param pos part of speech
	 * @return base uid for given pos
	 */
	private static long getBaseUID(final char pos)
	{
		switch (pos)
		{
			case 'n':
				return 100000000;
			case 'v':
				return 200000000;
			case 'a':
			case 's':
				return 300000000;
			case 'r':
				return 400000000;
			default:
				break;
		}
		return 900000000; // invalid value
	}

	/**
	 * Get ceiling UID for pos
	 *
	 * @param pos part of speech
	 * @return ceiling uid for given pos
	 */
	private static long getCeilingUID(final char pos)
	{
		return SynsetUId.getBaseUID(pos) + 100000000 - 1;
	}
}
