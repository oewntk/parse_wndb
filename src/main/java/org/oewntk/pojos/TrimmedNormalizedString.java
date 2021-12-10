/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

/**
 * Normalized string with possible suffix (adj position) stripped
 * - underscore converted to space
 * - double single quote converted to double quote
 * - adjective position (a,ip,p) trimmed
 *
 * @author Bernard Bou
 */
public class TrimmedNormalizedString extends NormalizedString
{
	private static final long serialVersionUID = 2771391035584386352L;

	/**
	 * Constructor
	 *
	 * @param normalized string with possible suffix
	 */
	public TrimmedNormalizedString(final NormalizedString normalized)
	{
		super(normalized);

		// remove possible trailing adj position between parentheses
		this.normalized = strip(this.normalized);
	}

	/**
	 * Constructor
	 *
	 * @param raw string with possible suffix
	 */
	public TrimmedNormalizedString(final String raw)
	{
		super(raw);

		// remove possible trailing adj position between parentheses
		this.normalized = strip(this.normalized);
	}

	public static String strip(String str)
	{
		return str.replaceAll("\\(.*\\)", "");
	}
}
