/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

/**
 * Lex Domain
 *
 * @author Bernard Bou
 */
public enum Domain
{
	ADJ_ALL(0, "adj.all", Pos.ADJ), //
	ADJ_PERT(1, "adj.pert", Pos.ADJ), //
	ADV_ALL(2, "adv.all", Pos.ADV), //
	NOUN_TOPS(3, "noun.tops", Pos.NOUN), //
	NOUN_ACT(4, "noun.act", Pos.NOUN), //
	NOUN_ANIMAL(5, "noun.animal", Pos.NOUN), //
	NOUN_ARTIFACT(6, "noun.artifact", Pos.NOUN), //
	NOUN_ATTRIBUTE(7, "noun.attribute", Pos.NOUN), //
	NOUN_BODY(8, "noun.body", Pos.NOUN), //
	NOUN_COGNITION(9, "noun.cognition", Pos.NOUN), //
	NOUN_COMMUNICATION(10, "noun.communication", Pos.NOUN), //
	NOUN_EVENT(11, "noun.event", Pos.NOUN), //
	NOUN_FEELING(12, "noun.feeling", Pos.NOUN), //
	NOUN_FOOD(13, "noun.food", Pos.NOUN), //
	NOUN_GROUP(14, "noun.group", Pos.NOUN), //
	NOUN_LOCATION(15, "noun.location", Pos.NOUN), //
	NOUN_MOTIVE(16, "noun.motive", Pos.NOUN), //
	NOUN_OBJECT(17, "noun.object", Pos.NOUN), //
	NOUN_PERSON(18, "noun.person", Pos.NOUN), //
	NOUN_PHENOMENON(19, "noun.phenomenon", Pos.NOUN), //
	NOUN_PLANT(20, "noun.plant", Pos.NOUN), //
	NOUN_POSSESSION(21, "noun.possession", Pos.NOUN), //
	NOUN_PROCESS(22, "noun.process", Pos.NOUN), //
	NOUN_QUANTITY(23, "noun.quantity", Pos.NOUN), //
	NOUN_RELATION(24, "noun.relation", Pos.NOUN), //
	NOUN_SHAPE(25, "noun.shape", Pos.NOUN), //
	NOUN_STATE(26, "noun.state", Pos.NOUN), //
	NOUN_SUBSTANCE(27, "noun.substance", Pos.NOUN), //
	NOUN_TIME(28, "noun.time", Pos.NOUN), //
	VERB_BODY(29, "verb.body", Pos.VERB), //
	VERB_CHANGE(30, "verb.change", Pos.VERB), //
	VERB_COGNITION(31, "verb.cognition", Pos.VERB), //
	VERB_COMMUNICATION(32, "verb.communication", Pos.VERB), //
	VERB_COMPETITION(33, "verb.competition", Pos.VERB), //
	VERB_CONSUMPTION(34, "verb.consumption", Pos.VERB), //
	VERB_CONTACT(35, "verb.contact", Pos.VERB), //
	VERB_CREATION(36, "verb.creation", Pos.VERB), //
	VERB_EMOTION(37, "verb.emotion", Pos.VERB), //
	VERB_MOTION(38, "verb.motion", Pos.VERB), //
	VERB_PERCEPTION(39, "verb.perception", Pos.VERB), //
	VERB_POSSESSION(40, "verb.possession", Pos.VERB), //
	VERB_SOCIAL(41, "verb.social", Pos.VERB), //
	VERB_STATIVE(42, "verb.stative", Pos.VERB), //
	VERB_WEATHER(43, "verb.weather", Pos.VERB), //
	ADJ_PPL(44, "adj.ppl", Pos.ADJ); //

	private final int id;

	private final String name;

	private final String domain;

	private final Pos pos;

	Domain(final int id, final String name, final Pos pos)
	{
		final String[] fields = name.split("\\.");
		this.id = id;
		this.name = name;
		this.domain = fields[1];
		this.pos = pos;
	}

	/**
	 * Parse lex domain from string
	 *
	 * @param str string
	 * @return lex domain
	 * @throws ParsePojoException parse exception
	 */
	public static Domain parseDomain(final String str) throws ParsePojoException
	{
		final int id = Integer.parseInt(str);
		if (id >= 0 && id < Domain.values().length)
		{
			return Domain.values()[id];
		}

		throw new ParsePojoException("Domain:" + str);
	}

	public int getId()
	{
		return this.id;
	}

	public String getName()
	{
		return this.name;
	}

	public String getDomain()
	{
		return this.domain;
	}

	public Pos getPosId()
	{
		return this.pos;
	}

	@Override
	public String toString()
	{
		return Integer.toString(this.id);
	}
}
