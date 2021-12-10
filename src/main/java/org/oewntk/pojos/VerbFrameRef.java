/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

/**
 * Verb Frame reference
 *
 * @author Bernard Bou
 */
public class VerbFrameRef
{
	public final int frameId;

	public final Lemma[] lemmas;

	public VerbFrameRef(final Lemma[] lemmas, final int frameId)
	{
		this.lemmas = lemmas;
		this.frameId = frameId;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("{");
		int i = 0;
		for (final Lemma lemma : this.lemmas)
		{
			if (i != 0)
			{
				sb.append(",");
			}
			sb.append(lemma.toString());
			i++;
		}
		sb.append("}:");
		sb.append(this.frameId);
		return sb.toString();
	}
}