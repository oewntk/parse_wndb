/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

import java.util.HashMap;
import java.util.Map;

/**
 * Relation type
 *
 * @author Bernard Bou
 */
public enum RelationType
{
	// @formatter:off
	HYPERNYM("@", "hypernym", true), HYPONYM("~", "hyponym", true), INSTANCE_HYPERNYM("@i", "instance hypernym", true), INSTANCE_HYPONYM("~i", "instance hyponym", true),

	PART_HOLONYM("%p", "part holonym", true), PART_MERONYM("#p", "part meronym", true), MEMBER_HOLONYM("%m", "member holonym", true), MEMBER_MERONYM("#m", "member meronym", true), SUBSTANCE_HOLONYM("%s", "substance holonym", true), SUBSTANCE_MERONYM("#s",
			"substance meronym", true),

	ENTAIL("*", "entail", true), IS_ENTAILED("*^", "is entailed by", true), CAUSE(">", "cause", true), IS_CAUSED(">^", "is caused by", true),

	ANTONYM("!", "antonym", false), SIMILAR("&", "similar", false),

	ALSO("^", "also", false), ATTRIBUTE("=", "attribute", false),

	VERB_GROUP("$", "verb group", false), PARTICIPLE("<", "participle", false),

	PERTAINYM("\\", "pertainym", false), DERIVATION("+", "derivation", false),

	DOMAIN_CATEGORY(";c", "domain category", false), MEMBER_CATEGORY("-c", "domain member category", false), DOMAIN_REGION(";r", "domain region", false), MEMBER_REGION("-r", "domain member region", false), DOMAIN_USAGE(";u", "domain usage",
			false), MEMBER_USAGE("-u", "domain member usage", false),

	DOMAIN(";", "domain", false), MEMBER("-", "member", false);
	// @formatter:on

	private static final Map<String, RelationType> MAP = new HashMap<>();

	static
	{
		for (final RelationType type : RelationType.values())
		{
			RelationType.MAP.put(type.symbol, type);
		}
	}

	private final String symbol;

	private final String name;

	private final boolean recurses;

	RelationType(final String symbol, final String name, final boolean recurses)
	{
		this.symbol = symbol;
		this.name = name;
		this.recurses = recurses;
	}

	/**
	 * Parse relation type from string
	 *
	 * @param str string
	 * @return relation type
	 * @throws ParsePojoException parse exception
	 */
	public static RelationType parseRelationType(final String str) throws ParsePojoException
	{
		final RelationType value = RelationType.MAP.get(str);
		if (value == null)
		{
			throw new ParsePojoException("Relation type: " + str);
		}
		return value;
	}

	public boolean getRecurses()
	{
		return this.recurses;
	}

	public String getName()
	{
		return this.name;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}
