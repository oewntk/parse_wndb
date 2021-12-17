/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

import java.util.Locale;

/**
 * Lex Domain
 *
 * @author Bernard Bou
 */
public enum Domain
{
	ADJ_ALL(0, "all", Pos.ADJ), //
	ADJ_PERT(1, "pert", Pos.ADJ), //
	ADV_ALL(2, "all", Pos.ADV), //
	NOUN_TOPS(3, "Tops", Pos.NOUN), //
	NOUN_ACT(4, "act", Pos.NOUN), //
	NOUN_ANIMAL(5, "animal", Pos.NOUN), //
	NOUN_ARTIFACT(6, "artifact", Pos.NOUN), //
	NOUN_ATTRIBUTE(7, "attribute", Pos.NOUN), //
	NOUN_BODY(8, "body", Pos.NOUN), //
	NOUN_COGNITION(9, "cognition", Pos.NOUN), //
	NOUN_COMMUNICATION(10, "communication", Pos.NOUN), //
	NOUN_EVENT(11, "event", Pos.NOUN), //
	NOUN_FEELING(12, "feeling", Pos.NOUN), //
	NOUN_FOOD(13, "food", Pos.NOUN), //
	NOUN_GROUP(14, "group", Pos.NOUN), //
	NOUN_LOCATION(15, "location", Pos.NOUN), //
	NOUN_MOTIVE(16, "motive", Pos.NOUN), //
	NOUN_OBJECT(17, "object", Pos.NOUN), //
	NOUN_PERSON(18, "person", Pos.NOUN), //
	NOUN_PHENOMENON(19, "phenomenon", Pos.NOUN), //
	NOUN_PLANT(20, "plant", Pos.NOUN), //
	NOUN_POSSESSION(21, "possession", Pos.NOUN), //
	NOUN_PROCESS(22, "process", Pos.NOUN), //
	NOUN_QUANTITY(23, "quantity", Pos.NOUN), //
	NOUN_RELATION(24, "relation", Pos.NOUN), //
	NOUN_SHAPE(25, "shape", Pos.NOUN), //
	NOUN_STATE(26, "state", Pos.NOUN), //
	NOUN_SUBSTANCE(27, "substance", Pos.NOUN), //
	NOUN_TIME(28, "time", Pos.NOUN), //
	VERB_BODY(29, "body", Pos.VERB), //
	VERB_CHANGE(30, "change", Pos.VERB), //
	VERB_COGNITION(31, "cognition", Pos.VERB), //
	VERB_COMMUNICATION(32, "communication", Pos.VERB), //
	VERB_COMPETITION(33, "competition", Pos.VERB), //
	VERB_CONSUMPTION(34, "consumption", Pos.VERB), //
	VERB_CONTACT(35, "contact", Pos.VERB), //
	VERB_CREATION(36, "creation", Pos.VERB), //
	VERB_EMOTION(37, "emotion", Pos.VERB), //
	VERB_MOTION(38, "motion", Pos.VERB), //
	VERB_PERCEPTION(39, "perception", Pos.VERB), //
	VERB_POSSESSION(40, "possession", Pos.VERB), //
	VERB_SOCIAL(41, "social", Pos.VERB), //
	VERB_STATIVE(42, "stative", Pos.VERB), //
	VERB_WEATHER(43, "weather", Pos.VERB), //
	ADJ_PPL(44, "ppl", Pos.ADJ); //

	private final int id;

	private final String name;

	private final Pos pos;

	Domain(final int id, final String name, final Pos pos)
	{
		this.id = id;
		this.name = name;
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
		final String name = str.replace('.', '_').toUpperCase(Locale.ROOT);
		try
		{
			return Domain.valueOf(name);
		}
		catch (Exception e)
		{
			throw new ParsePojoException("Domain:" + str);
		}
	}

	/**
	 * Parse lex domain from string
	 *
	 * @param str string
	 * @return lex domain
	 * @throws ParsePojoException parse exception
	 */
	public static Domain parseDomainId(final String str) throws ParsePojoException
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

	public Pos getPos()
	{
		return this.pos;
	}

	public String getDomain()
	{
		return this.pos.getName() + '.' + this.name;
	}

	@Override
	public String toString()
	{
		return Integer.toString(this.id);
	}
}
