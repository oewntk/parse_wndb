/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Relation type
 *
 * @author Bernard Bou
 */
public enum RelationType
{
	// @formatter:off
	HYPERNYM("@", "hypernym", true), //
	HYPONYM("~", "hyponym", true), //
	INSTANCE_HYPERNYM("@i", "instance_hypernym", true),  //
	INSTANCE_HYPONYM("~i", "instance_hyponym", true), //

	PART_HOLONYM("%p", "holo_part", true),  //
	PART_MERONYM("#p", "mero_part", true),  //
	MEMBER_HOLONYM("%m", "holo_member", true),  //
	MEMBER_MERONYM("#m", "mero_member", true),  //
	SUBSTANCE_HOLONYM("%s", "holo_substance", true),  //
	SUBSTANCE_MERONYM("#s","mero_substance", true), //

	ENTAIL("*", "entails", true),  //
	IS_ENTAILED("*^", "is_entailed_by", true),  //
	CAUSE(">", "causes", true),  //
	IS_CAUSED(">^", "is_caused_by", true), //

	ANTONYM("!", "antonym", false),  //
	SIMILAR("&", "similar", false), //

	ALSO("^", "also", false),  //
	ATTRIBUTE("=", "attribute", false), //

	VERB_GROUP("$", "verb_group", false),
	PARTICIPLE("<", "participle", false),

	PERTAINYM("\\", "pertainym", false),
	DERIVATION("+", "derivation", false),

	DOMAIN_TOPIC(";c", "domain_topic", false),
	HAS_DOMAIN_TOPIC("-c", "has_domain_topic", false),
	DOMAIN_REGION(";r", "domain_region", false),
	HAS_DOMAIN_REGION("-r", "has_domain_region", false),
	DOMAIN_USAGE(";u", "exemplifies",false),
	HAS_DOMAIN_USAGE("-u", "is_exemplified_by", false),

	DOMAIN(";", "domain", false),
	MEMBER("-", "member", false);

	public static final Set<String> SYNSET_RELATIONS = Set.of( //
			"hypernym", "hyponym", //
			"instance_hypernym", "instance_hyponym", //
			"mero_part", "holo_part", //
			"mero_member", "holo_member", //
			"mero_substance", "holo_substance", //
			"causes", "is_caused_by", //
			"entails", "is_entailed_by", //
			"exemplifies", "is_exemplified_by", //
			"domain_topic", "has_domain_topic", //
			"domain_region", "has_domain_region", //
			"attribute", //
			"similar", //
			"verb_group", //
			"also");

	public static final Set<String> SENSE_RELATIONS = Set.of( //
			"antonym", //
			"similar",  //
			"exemplifies", "is_exemplified_by", //
			"derivation",  //
			"pertainym",  //
			"verb_group", //
			"participle",  //
			"also",  //
			"domain_region", "has_domain_region", //
			"domain_topic", "has_domain_topic", //
			"other");

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

	/**
	 * Constructor
	 *
	 * @param symbol   pointer symbol
	 * @param name     name
	 * @param recurses whether relation can recurse
	 */
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

	/**
	 * Get recursion capability
	 *
	 * @return recursion capability
	 */
	public boolean getRecurses()
	{
		return this.recurses;
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

	@Override
	public String toString()
	{
		return this.name;
	}
}
