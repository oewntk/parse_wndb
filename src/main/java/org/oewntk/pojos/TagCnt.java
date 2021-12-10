/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

/**
 * Tag
 *
 * @author Bernard Bou
 */
public class TagCnt
{
	private final int tagCount;

	public TagCnt(final int tagCount)
	{
		this.tagCount = tagCount;
	}

	/**
	 * Parse tagcount from string
	 *
	 * @param str string
	 * @return tag count
	 * @throws ParsePojoException parse exception
	 */
	public static TagCnt parseTagCnt(final String str) throws ParsePojoException
	{
		try
		{
			final int tagCount = Integer.parseInt(str);
			return new TagCnt(tagCount);
		}
		catch (Exception e)
		{
			throw new ParsePojoException(e);
		}
	}

	public int getTagCount()
	{
		return this.tagCount;
	}

	@Override
	public String toString()
	{
		return Integer.toString(this.tagCount);
	}
}