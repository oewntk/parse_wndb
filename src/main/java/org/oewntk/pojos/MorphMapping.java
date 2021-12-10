/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Morphology ({noun|verb|adj|adv|}.exc)
 *
 * @author Bernard Bou
 */
public class MorphMapping
{
	// an inflected form of a word or collocation, followed by one or more base forms
	// auspices auspex auspice (2 lemmas)
	// mice mouse

	/**
	 * Inflected form
	 */
	public final NormalizedString morph;

	/**
	 * Base forms
	 */
	public final Collection<Lemma> lemmas;

	/**
	 * Part of speech
	 */
	public final Pos pos;

	/**
	 * Constructor
	 *
	 * @param morph  inflected form
	 * @param lemmas base forms
	 * @param pos    part of speech
	 */
	private MorphMapping(final NormalizedString morph, final Collection<Lemma> lemmas, final Pos pos)
	{
		this.morph = morph;
		this.lemmas = lemmas;
		this.pos = pos;
	}

	/**
	 * Parse morph mapping from line
	 *
	 * @param line line
	 * @param pos  part of speech
	 * @return morph mapping
	 * @throws ParsePojoException parse exception
	 */
	public static MorphMapping parseMorphMapping(final String line, final Pos pos) throws ParsePojoException
	{
		try
		{
			final String[] fields = line.split("\\s+");

			// morph
			final NormalizedString morph = new NormalizedString(fields[0]);

			// lemmas
			final Collection<Lemma> lemmas = new ArrayList<>();
			if (fields.length > 1)
			{
				for (int i = 1; i < fields.length; i++)
				{
					final Lemma lemma = Lemma.make(fields[i]);
					lemmas.add(lemma);
				}
			}
			return new MorphMapping(morph, lemmas, pos);
		}
		catch (Exception e)
		{
			throw new ParsePojoException(e);
		}
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(this.morph.toString());
		sb.append(' ');
		sb.append(this.pos.toChar());
		sb.append("<-");
		boolean first = true;
		for (final Lemma lemma : this.lemmas)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				sb.append(' ');
			}
			sb.append(lemma);
		}
		return sb.toString();
	}
}