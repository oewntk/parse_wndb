/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

import java.util.Arrays;
import java.util.Objects;

/**
 * Core Index
 *
 * @author Bernard Bou
 */
public class CoreIndex
{
	public final Lemma lemma;

	public final Pos pos;

	public final BaseSense[] senses;

	protected CoreIndex(final Lemma lemma, final Pos pos, final BaseSense[] senses)
	{
		this.lemma = lemma;
		this.pos = pos;
		this.senses = senses;
	}

	/**
	 * Parse core index from line
	 *
	 * @param line line
	 * @return core index
	 * @throws ParsePojoException parse exception
	 */
	public static CoreIndex parseCoreIndex(final String line) throws ParsePojoException
	{
		try
		{
			// line from index.noun or index.verb or index.adj or index.adv:  lemma  pos  synset_cnt  p_cnt  [ptr_symbol...]  sense_cnt  tagsense_cnt   synset_offset  [synset_offset...]
			// lemma: Lower case ASCII text of word or collocation. Collocations are formed by joining individual words with an underscore (_ ) character.
			// pos: Syntactic category: n for noun files, v for verb files, a for adjective files, r for adverb files.
			// synset_cnt:   Number of synsets that lemma is in. This is the number of senses of the word in WordNet.
			// p_cnt: Number of different pointers that lemma has in all synsets containing it.
			// ptr_symbol:  A space separated list of p_cnt different types of pointers that lemma has in all synsets containing it. If all senses of lemma have no pointers, this field is omitted and p_cnt is 0 .
			// sense_cnt: Same as synset_cnt above. This is redundant.
			// tagsense_cnt: Number of senses of lemma that are ranked according to their frequency of occurrence in semantic concordance texts.
			// synset_offset:  Byte offset in data.pos file of a synset containing lemma . Each synset_offset in the list corresponds to a different sense of lemma in WordNet. synset_offset is an 8 digit, zero-filled decimal integer that can be used with fseek(link is external)(3)(link is external) to read a synset from the data file. When passed to read_synset(3WN) along with the syntactic category, a data structure containing the parsed synset is returned.

			// split into fields
			final String[] fields = line.split("\\s+");

			int fieldPointer = 0;

			// lemma/word
			final String lemmaString = fields[fieldPointer];
			final Lemma lemma = Lemma.make(lemmaString);
			fieldPointer++;

			// part-of-speech
			final Pos pos = Pos.parsePos(fields[fieldPointer].charAt(0));
			fieldPointer++;

			// polysemy count
			final int senseCount = Integer.parseInt(fields[fieldPointer]);
			fieldPointer++;

			// relation count
			final int relationCount = Integer.parseInt(fields[fieldPointer], 10);
			fieldPointer++;

			// relations
			fieldPointer += relationCount;

			// polysemy count 2
			fieldPointer++;

			// tag count
			fieldPointer++;

			// senses
			final BaseSense[] senses = new BaseSense[senseCount];
			for (int i = 0; i < senseCount; i++)
			{
				senses[i] = BaseSense.make(lemma, pos, fields[fieldPointer], i + 1);
				fieldPointer++;
			}
			return new CoreIndex(lemma, pos, senses);
		}
		catch (Exception e)
		{
			throw new ParsePojoException(e);
		}
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
		CoreIndex coreIndex = (CoreIndex) o;
		return lemma.equals(coreIndex.lemma) && pos == coreIndex.pos && Arrays.equals(senses, coreIndex.senses);
	}

	@Override
	public int hashCode()
	{
		int result = Objects.hash(lemma, pos);
		result = 31 * result + Arrays.hashCode(senses);
		return result;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(this.lemma.toString());
		sb.append(" senses={");
		for (int i = 0; i < this.senses.length; i++)
		{
			if (i != 0)
			{
				sb.append(" ");
			}
			sb.append(this.senses[i].toString());
		}
		sb.append("}");
		return sb.toString();
	}
}
