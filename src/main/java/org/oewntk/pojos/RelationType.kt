/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.pojos

/**
 * Relation type qualifier
 *
 * @property symbol   pointer symbol
 * @property name2    relation name
 * @property recurses whether relation can recurse
 *
 * @author Bernard Bou
 */
enum class RelationType(
	private val symbol: String,
	val name2: String,
	private val recurses: Boolean
) {

	HYPERNYM("@", "hypernym", true),
	HYPONYM("~", "hyponym", true),
	INSTANCE_HYPERNYM("@i", "instance_hypernym", true),
	INSTANCE_HYPONYM("~i", "instance_hyponym", true),

	PART_HOLONYM("%p", "holo_part", true),
	PART_MERONYM("#p", "mero_part", true),
	MEMBER_HOLONYM("%m", "holo_member", true),
	MEMBER_MERONYM("#m", "mero_member", true),
	SUBSTANCE_HOLONYM("%s", "holo_substance", true),
	SUBSTANCE_MERONYM("#s", "mero_substance", true),

	ENTAIL("*", "entails", true),
	IS_ENTAILED("*^", "is_entailed_by", true),
	CAUSE(">", "causes", true),
	IS_CAUSED(">^", "is_caused_by", true),

	ANTONYM("!", "antonym", false),
	SIMILAR("&", "similar", false),

	ALSO("^", "also", false),
	ATTRIBUTE("=", "attribute", false),

	VERB_GROUP("$", "verb_group", false),
	PARTICIPLE("<", "participle", false),

	PERTAINYM("\\", "pertainym", false),
	DERIVATION("+", "derivation", false),

	DOMAIN_TOPIC(";c", "domain_topic", false),
	HAS_DOMAIN_TOPIC("-c", "has_domain_topic", false),
	DOMAIN_REGION(";r", "domain_region", false),
	HAS_DOMAIN_REGION("-r", "has_domain_region", false),
	DOMAIN_USAGE(";u", "exemplifies", false),
	HAS_DOMAIN_USAGE("-u", "is_exemplified_by", false),

	DOMAIN(";", "domain", false),
	MEMBER("-", "member", false);

	override fun toString(): String {
		return this.name
	}

	companion object {
		@JvmField
		val SYNSET_RELATIONS: Set<String> = setOf(
			"hypernym", "hyponym",
			"instance_hypernym", "instance_hyponym",
			"mero_part", "holo_part",
			"mero_member", "holo_member",
			"mero_substance", "holo_substance",
			"causes", "is_caused_by",
			"entails", "is_entailed_by",
			"exemplifies", "is_exemplified_by",
			"domain_topic", "has_domain_topic",
			"domain_region", "has_domain_region",
			"attribute",
			"similar",
			"verb_group",
			"also"
		)

		@JvmField
		val SENSE_RELATIONS: Set<String> = setOf(
			"antonym",
			"similar",
			"exemplifies", "is_exemplified_by",
			"derivation",
			"pertainym",
			"verb_group",
			"participle",
			"also",
			"domain_region", "has_domain_region",
			"domain_topic", "has_domain_topic",
			"other"
		)

		// @formatter:on
		private val MAP: MutableMap<String, RelationType> = HashMap()

		init {
			for (type in entries) {
				MAP[type.symbol] = type
			}
		}

		/**
		 * Parse relation type from string
		 *
		 * @param str string
		 * @return relation type
		 * @throws ParsePojoException parse exception
		 */
		@JvmStatic
		@Throws(ParsePojoException::class)
		fun parseRelationType(str: String): RelationType {
			return MAP[str] ?: throw ParsePojoException("Relation type: $str")
		}
	}
}
