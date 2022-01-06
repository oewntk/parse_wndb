/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

import java.util.Objects;

/**
 * Core Synset (without relations and frames)
 *
 * @author Bernard Bou
 */
public class CoreSynset
{
	public final SynsetId synsetId;

	public final LemmaCS[] lemmas;

	public final Type type;

	public final Domain domain;

	public final Gloss gloss;

	/**
	 * Constructor
	 *
	 * @param synsetId synset id
	 * @param lemmas   case-sensitive lemmas
	 * @param type     type
	 * @param domain   lex domain
	 * @param gloss    gloss
	 */
	protected CoreSynset(final SynsetId synsetId, final LemmaCS[] lemmas, final Type type, final Domain domain, final Gloss gloss)
	{
		this.synsetId = synsetId;
		this.lemmas = lemmas;
		this.type = type;
		this.domain = domain;
		this.gloss = gloss;
	}

	/**
	 * Parse from line
	 *
	 * @param line  line
	 * @param isAdj whether adj synsets are being parsed
	 * @return synset
	 * @throws ParsePojoException parse exception
	 */
	public static CoreSynset parseCoreSynset(final String line, final boolean isAdj) throws ParsePojoException
	{
		try
		{
			// split into fields
			final String[] fields = line.split("\\s+");
			int fieldPointer = 0;

			// offset
			final long offset = Integer.parseInt(fields[fieldPointer]);
			fieldPointer++;

			// lex domain
			final Domain domain = Domain.parseDomainId(fields[fieldPointer]);
			fieldPointer++;

			// type
			final Type type = Type.parseType(fields[fieldPointer].charAt(0));
			// fieldPointer++;

			// id
			final SynsetId synsetId = new SynsetId(type.toPos(), offset);

			// lemma set
			final LemmaCS[] lemmas = CoreSynset.parseLemmas(fields, isAdj);
			// fieldPointer += 1 + 2 * members.length;

			// glossary
			final Gloss gloss = new Gloss(line.substring(line.indexOf('|') + 1));

			return new CoreSynset(synsetId, lemmas, type, domain, gloss);
		}
		catch (Exception e)
		{
			throw new ParsePojoException(e);
		}
	}

	/**
	 * Parse lemmas from fields
	 *
	 * @param fields fields
	 * @param isAdj  whether an adjective is being processed
	 * @return array of bare normalized strings
	 * @throws ParsePojoException parse exception
	 */
	private static LemmaCS[] parseLemmas(final String[] fields, final boolean isAdj) throws ParsePojoException
	{
		try
		{
			// data
			int fieldPointer = 3;

			// count
			final int count = Integer.parseInt(fields[fieldPointer], 16);
			fieldPointer++;

			// lemmas
			final LemmaCS[] lemmas2 = new LemmaCS[count];
			for (int i = 0; i < count; i++)
			{
				final String field = fields[fieldPointer];
				final NormalizedString normalized = new NormalizedString(field);
				Lemma lemma = isAdj ? AdjLemma.makeAdj(normalized) : Lemma.make(normalized);
				lemmas2[i] = new LemmaCS(lemma, isAdj ? new TrimmedNormalizedString(normalized) : normalized);
				fieldPointer++;

				// lexid skipped
				fieldPointer++;
			}
			return lemmas2;
		}
		catch (Exception e)
		{
			throw new ParsePojoException(e);
		}
	}

	/**
	 * Parse from line
	 *
	 * @param line line
	 * @return members
	 * @throws ParsePojoException parse exception
	 */
	public static TrimmedNormalizedString[] parseMembers(final String line) throws ParsePojoException
	{
		// split into fields
		final String[] fields = line.split("\\s+");
		return CoreSynset.parseMembers(fields);
	}

	/**
	 * Parse members from fields
	 *
	 * @param fields fields
	 * @return array of bare normalized strings
	 * @throws ParsePojoException parse exception
	 */
	private static TrimmedNormalizedString[] parseMembers(final String[] fields) throws ParsePojoException
	{
		try
		{
			// data
			int fieldPointer = 3;

			// count
			final int count = Integer.parseInt(fields[fieldPointer], 16);
			fieldPointer++;

			// members
			final TrimmedNormalizedString[] members = new TrimmedNormalizedString[count];
			for (int i = 0; i < count; i++)
			{
				members[i] = new TrimmedNormalizedString(fields[fieldPointer]);
				fieldPointer++;

				// lexid skipped
				fieldPointer++;
			}
			return members;
		}
		catch (Exception e)
		{
			throw new ParsePojoException(e);
		}
	}

	/**
	 * Get synset id
	 *
	 * @return synset id
	 */
	public SynsetId getId()
	{
		return this.synsetId;
	}

	/**
	 * Get case-sensitive lemma
	 *
	 * @return case-sensitive lemma
	 */
	public LemmaCS[] getCSLemmas()
	{
		return this.lemmas;
	}

	/**
	 * Get lemmas
	 *
	 * @return lemmas
	 */
	public Lemma[] getLemmas()
	{
		Lemma[] lemmas = new Lemma[this.lemmas.length];
		int i = 0;
		for (LemmaCS lemma : this.lemmas)
		{
			lemmas[i++] = lemma.lemma;
		}
		return lemmas;
	}

	/**
	 * Get synset type
	 *
	 * @return synset type
	 */
	public Type getType()
	{
		return this.type;
	}

	/**
	 * Get domain
	 *
	 * @return domain
	 */
	public Domain getDomain()
	{
		return this.domain;
	}

	/**
	 * get gloss
	 *
	 * @return gloss
	 */
	public Gloss getGloss()
	{
		return this.gloss;
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		CoreSynset that = (CoreSynset) o;
		return synsetId.equals(that.synsetId);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(synsetId);
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("id=");
		sb.append(String.format("%08d", this.synsetId.getOffset()));
		sb.append(" words={");
		int i = 0;
		for (final LemmaCS lemma : this.lemmas)
		{
			if (i != 0)
			{
				sb.append(",");
			}
			sb.append(lemma.toString());
			if (lemma.lemma instanceof AdjLemma)
			{
				final AdjLemma adjLemma = (AdjLemma) lemma.lemma;
				sb.append(adjLemma.toPositionSuffix());
			}
			i++;
		}
		sb.append("} type=");
		sb.append(this.type);
		sb.append(" domain=");
		sb.append(this.domain);
		return sb.toString();
	}
}