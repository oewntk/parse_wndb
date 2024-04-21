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

	/**
	 * Lex id
	 *
	 * @param id lex id
	 */
	private LexId(final int id)
	{
		this.id = id;
	}

	/**
	 * Factory
	 *
	 * @param sensekey sensekey
	 * @return lexid
	 */
	public static LexId make(final Sensekey sensekey)
	{
		final int id = sensekey.getLexId();
		return new LexId(id);
	}

	/**
	 * Get lexid
	 *
	 * @return lexid
	 */
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