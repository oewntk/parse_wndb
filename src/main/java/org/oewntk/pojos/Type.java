/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

/**
 * Type (part-of-speech, with adj split between heads and satellites)
 *
 * @author Bernard Bou
 */
public enum Type
{
	NOUN('n', "noun", "noun", Pos.NOUN), //
	VERB('v', "verb", "verb", Pos.VERB), //
	ADJHEAD('a', "adj_head", "adjective", Pos.ADJ), //
	ADV('r', "adv", "adverb", Pos.ADV), //
	ADJSAT('s', "adj_sat", "adjective satellite", Pos.ADJ);

	private final char id;

	private final String name;

	private final String description;

	private final Pos pos;

	Type(final char id, final String name, final String description, final Pos pos)
	{
		assert id == 'n' || id == 'v' || id == 'a' || id == 'r' || id == 's';
		this.id = id;
		this.name = name;
		this.description = description;
		this.pos = pos;
	}

	public char toChar()
	{
		return this.id;
	}

	public String getName()
	{
		return this.name;
	}

	public String getDescription()
	{
		return this.description;
	}

	public Pos toPos()
	{
		return this.pos;
	}

	/**
	 * Parse type from character id
	 *
	 * @param id character id
	 * @return type
	 * @throws ParsePojoException parse exception
	 */
	public static Type parseType(final char id) throws ParsePojoException
	{
		for (final Type type : Type.values())
		{
			if (id == type.id)
			{
				return type;
			}
		}
		throw new ParsePojoException("Type:" + id);
	}

	/**
	 * Make type from type index
	 *
	 * @param index0 index
	 * @return type
	 */
	public static Type fromIndex(final int index0)
	{
		final int index = index0 - 1;
		if (index >= 0 && index < Type.values().length)
		{
			return Type.values()[index];
		}
		throw new IllegalArgumentException("Type:" + index);
	}

	/**
	 * Make type from name
	 *
	 * @param name name
	 * @return type
	 */
	public static Type fromName(final String name)
	{
		for (final Type type : Type.values())
		{
			if (name.equals(type.name))
			{
				return type;
			}
		}
		return null;
	}

	public boolean isAdj()
	{
		return this.equals(Type.ADJHEAD) || this.equals(Type.ADJSAT);
	}

	@Override
	public String toString()
	{
		return Character.toString(this.id);
	}
}
