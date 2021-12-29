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
	public final int tagCount;

	public final int senseNum;

	public TagCnt(final int tagCount, final int senseNum)
	{
		this.tagCount = tagCount;
		this.senseNum = senseNum;
	}

	/**
	 * Parse tagcount from string
	 *
	 * @param str      string
	 * @param senseNum senseNumber
	 * @return tag count
	 * @throws ParsePojoException parse exception
	 */
	public static TagCnt parseTagCnt(final String str, int senseNum) throws ParsePojoException
	{
		try
		{
			final int tagCount = Integer.parseInt(str);
			return new TagCnt(tagCount, senseNum);
		}
		catch (Exception e)
		{
			throw new ParsePojoException(e);
		}
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
		return parseTagCnt(str, 0);
	}

	@Override
	public String toString()
	{
		return Integer.toString(this.tagCount);
	}
}