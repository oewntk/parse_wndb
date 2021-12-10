/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

/**
 * Lexid
 *
 * @author Bernard Bou
 */
public class LexId
{
	private final int id;

	private LexId(final int id)
	{
		this.id = id;
	}

	public static LexId make(final Sensekey sensekey)
	{
		final int id = sensekey.getLexId();
		return new LexId(id);
	}

	public int getId()
	{
		return this.id;
	}

	@Override
	public String toString()
	{
		return Integer.toString(this.id);
	}
}