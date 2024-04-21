/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

public class Index extends CoreIndex
{
	final RelationQualifier[] relationTypes;

	final TagCnt tagCnt;

	/**
	 * Constructor
	 *
	 * @param lemma         lemma
	 * @param pos           pos
	 * @param senses        senses
	 * @param relationTypes relation types
	 * @param tagCnt        tag count
	 */
	public Index(final Lemma lemma, final Pos pos, final BaseSense[] senses, final RelationQualifier[] relationTypes, final TagCnt tagCnt)
	{
		super(lemma, pos, senses);
		this.relationTypes = relationTypes;
		this.tagCnt = tagCnt;
	}

	/**
	 * Parse index from line
	 *
	 * @param line line
	 * @return index
	 * @throws ParsePojoException parse exception
	 */
	public static Index parseIndex(final String line) throws ParsePojoException
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

			// lemma
			final String lemmaString = fields[fieldPointer];
			final Lemma lemma = Lemma.make(lemmaString);
			fieldPointer++;

			// part-of-speech
			final Pos pos = Pos.parsePos(fields[fieldPointer].charAt(0));
			fieldPointer++;

			// polysemy count
			final int senseCount = Integer.parseInt(fields[fieldPointer]);
			fieldPointer++;

			// relation types count
			final int relationTypesCount = Integer.parseInt(fields[fieldPointer], 10);
			fieldPointer++;

			// relation types
			final RelationQualifier[] relationTypes = new RelationQualifier[relationTypesCount];
			for (int i = 0; i < relationTypesCount; i++)
			{
				relationTypes[i] = RelationQualifier.parseRelationType(fields[fieldPointer + i]);
			}
			fieldPointer += relationTypesCount;

			// polysemy count 2
			final int senseCount2 = Integer.parseInt(fields[fieldPointer]);
			assert senseCount == senseCount2;
			fieldPointer++;

			// tag count
			final TagCnt tagCnt = TagCnt.parseTagCnt(fields[fieldPointer]);
			fieldPointer++;

			// senses
			final BaseSense[] senses = new BaseSense[senseCount];
			for (int i = 0; i < senseCount; i++)
			{
				senses[i] = BaseSense.make(lemma, pos, fields[fieldPointer], i + 1);
				fieldPointer++;
			}
			return new Index(lemma, pos, senses, relationTypes, tagCnt);
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
		sb.append(super.toString());
		sb.append(" relations={");
		for (int i = 0; i < this.relationTypes.length; i++)
		{
			if (i != 0)
			{
				sb.append(" ");
			}
			sb.append(this.relationTypes[i].toString());
		}
		sb.append("}");
		sb.append(" tagcnt=");
		sb.append(this.tagCnt);
		return sb.toString();
	}
}
