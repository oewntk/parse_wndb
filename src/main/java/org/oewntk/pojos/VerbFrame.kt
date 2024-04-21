/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

/**
 * Verb Frame (in verb.Framestext)
 *
 * @author Bernard Bou
 */
public class VerbFrame
{
	/**
	 * Frame id
	 */
	public final int id;

	/**
	 * Frame text
	 */
	public final String frame;

	/**
	 * Constructor
	 *
	 * @param id    frame id
	 * @param frame frame text
	 */
	private VerbFrame(final int id, final String frame)
	{
		super();
		this.id = id;
		this.frame = frame;
	}

	/**
	 * Parse from line
	 *
	 * @param line line
	 * @return verb frame
	 * @throws ParsePojoException parse exception
	 */
	public static VerbFrame parseVerbFrame(final String line) throws ParsePojoException
	{
		try
		{
			final int id = Integer.parseInt(line.split("\\s+")[0]);
			final String text = line.substring(3);
			return new VerbFrame(id, text);
		}
		catch (Exception e)
		{
			throw new ParsePojoException(e);
		}
	}

	@Override
	public String toString()
	{
		return "id=" + this.id + " frame=" + this.frame;
	}
}
