/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

/**
 * Verb template (sents.vrb)
 *
 * @author Bernard Bou
 */
public class VerbTemplate
{
	/**
	 * Template id
	 */
	public final int id;

	/**
	 * Template text
	 */
	public final String template;

	/**
	 * Constructor
	 *
	 * @param id       template id
	 * @param template template text
	 */
	private VerbTemplate(final int id, final String template)
	{
		this.id = id;
		this.template = template;
	}

	/**
	 * Parse from line
	 *
	 * @param line line
	 * @return verb template
	 * @throws ParsePojoException parse exception
	 */
	public static VerbTemplate parseVerbTemplate(final String line) throws ParsePojoException
	{
		try
		{
			final String[] fields = line.split("\\s+");
			final int id = Integer.parseInt(fields[0]);
			final String text = line.substring(fields[0].length() + 1);
			return new VerbTemplate(id, text);
		}
		catch (Exception e)
		{
			throw new ParsePojoException(e);
		}
	}

	@Override
	public String toString()
	{
		return "id=" + this.id + " template=" + this.template;
	}
}