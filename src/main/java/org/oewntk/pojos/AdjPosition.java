/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

/**
 * Adjective Position
 *
 * @author Bernard Bou
 */
public enum AdjPosition
{
	PREDICATIVE("p", "predicate"), //
	ATTRIBUTIVE("a", "attributive"), //
	POSTNOMINAL("ip", "immediately postnominal");

	private final String id;

	private final String description;

	/**
	 * Constructor
	 *
	 * @param id          position tag
	 * @param description position description
	 */
	AdjPosition(final String id, final String description)
	{
		this.id = id;
		this.description = description;
	}

	/**
	 * Find adj position from tag
	 *
	 * @param tag tag
	 * @return adj position
	 */
	public static AdjPosition find(final String tag)
	{
		for (final AdjPosition position : AdjPosition.values())
		{
			if (position.id.equals(tag))
			{
				return position;
			}
		}
		return null;
	}

	/**
	 * Parse adj position from line
	 *
	 * @param suffix suffix = '(tag)'
	 * @return adj position
	 * @throws ParsePojoException parse exception
	 */
	public static AdjPosition parseAdjPosition(final String suffix) throws ParsePojoException
	{
		// remove parentheses
		final String name = suffix.substring(1, suffix.length() - 1);

		// look up
		for (final AdjPosition adjPosition : AdjPosition.values())
		{
			if (name.equals(adjPosition.id))
			{
				return adjPosition;
			}
		}
		throw new ParsePojoException("AdjPosition:" + name);
	}

	public String getId()
	{
		return this.id;
	}

	public String getDescription()
	{
		return this.description;
	}

	@Override
	public String toString()
	{
		return "(" + this.id + "}";
	}
}