/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

/**
 * Pos (part of speech)
 *
 * @author Bernard Bou
 */
public enum Pos
{
	NOUN('n', "noun", "noun"), //
	VERB('v', "verb", "verb"), //
	ADJ('a', "adj", "adjective"), //
	ADV('r', "adv", "adverb");

	private final char id;

	private final String name;

	private final String description;

	/**
	 * Constructor
	 *
	 * @param id          character id
	 * @param name        name
	 * @param description description
	 */
	Pos(final char id, final String name, final String description)
	{
		assert id == 'n' || id == 'v' || id == 'a' || id == 'r';
		this.id = id;
		this.name = name;
		this.description = description;
	}

	/**
	 * Get char id
	 *
	 * @return char id
	 */
	public char toChar()
	{
		return this.id;
	}

	/**
	 * Get name
	 *
	 * @return name
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Get description
	 *
	 * @return description
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * Parse pos from character id
	 *
	 * @param id character id
	 * @return pos
	 * @throws ParsePojoException parse exception
	 */
	public static Pos parsePos(final char id) throws ParsePojoException
	{
		for (final Pos pos : Pos.values())
		{
			if (id == pos.id)
			{
				return pos;
			}
		}
		throw new ParsePojoException("Pos:" + id);
	}

	/**
	 * Make pos from pos index
	 *
	 * @param index0 index
	 * @return pos
	 */
	public static Pos fromIndex(final int index0)
	{
		final int index = index0 - 1;
		if (index >= 0 && index < Pos.values().length)
		{
			return Pos.values()[index];
		}
		throw new IllegalArgumentException("Pos:" + index);
	}

	/**
	 * Make pos from name
	 *
	 * @param name name
	 * @return pos
	 */
	public static Pos fromName(final String name)
	{
		for (final Pos pos : Pos.values())
		{
			if (name.equals(pos.name))
			{
				return pos;
			}
		}
		return null;
	}

	@Override
	public String toString()
	{
		return Character.toString(this.id);
	}
}
