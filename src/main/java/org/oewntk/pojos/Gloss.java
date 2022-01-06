/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

import org.oewntk.utils.Tracing;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Gloss
 *
 * @author Bernard Bou
 */
public final class Gloss
{
	private final String[] splitGloss;

	/**
	 * Constructor
	 *
	 * @param gloss raw gloss
	 */
	public Gloss(final String gloss)
	{
		this.splitGloss = split(gloss.trim());
	}

	/**
	 * Parse gloss into fields
	 *
	 * @param gloss gloss
	 * @return fields
	 */
	private String[] split(final String gloss)
	{
		final String REGEX = "\"[^\"]*\"";
		final Pattern pattern = Pattern.compile(REGEX);

		int quoteCount = 0;
		for (int p = 0; (p = gloss.indexOf('"', p + 1)) != -1; )
		{
			quoteCount++;
		}
		if (quoteCount % 2 != 0)
		{
			Tracing.psErr.println("Uneven quotes in :" + gloss);
		}

		final Matcher matcher = pattern.matcher(gloss); // get a matcher object
		int count = 0;
		int split = -1;
		while (matcher.find())
		{
			if (count == 0)
			{
				split = matcher.start();
			}
			count++;
		}

		final String[] result = new String[count + 1];

		// [0] definition
		String definition = split == -1 ? gloss : gloss.substring(0, split);
		definition = definition.replaceFirst("[;\\s]*$", "");
		result[0] = definition;

		// [1-n] samples
		matcher.reset();
		for (count = 1; matcher.find(); count++)
		{
			String sample = matcher.group();
			if (sample.startsWith("\"") && sample.endsWith("\""))
			{
				sample = sample.substring(1, sample.length() - 1);
			}
			result[count] = sample;
		}
		return result;
	}

	/**
	 * Get definition
	 *
	 * @return definition
	 */
	public String getDefinition()
	{
		return this.splitGloss[0];
	}

	/**
	 * Get samples
	 *
	 * @return samples
	 */
	public String[] getSamples()
	{
		return Arrays.copyOfRange(this.splitGloss, 1, this.splitGloss.length);
	}

	/**
	 * Get pretty string
	 *
	 * @return pretty string
	 */
	public String toPrettyString()
	{
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.splitGloss.length; i++)
		{
			if (i != 0)
			{
				sb.append(";");
				sb.append("\n\t");
			}
			sb.append(this.splitGloss[i]);
		}
		return sb.toString();
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.splitGloss.length; i++)
		{
			if (i != 0)
			{
				sb.append(";");
			}
			sb.append(this.splitGloss[i]);
		}
		return sb.toString();
	}
}
