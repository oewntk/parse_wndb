/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

public class Sense extends CoreSense
{
	public final TagCnt tagCnt;

	public Sense(final SynsetId synsetId, final Lemma lemma, final int sensePosIndex, final Sensekey sensekey, final TagCnt tagCnt)
	{
		super(synsetId, lemma, sensePosIndex, sensekey);
		this.tagCnt = tagCnt;
	}

	/**
	 * Parse sense from line
	 *
	 * @param line line
	 * @return sense
	 * @throws ParsePojoException parse exception
	 */
	public static Sense parseSense(final String line) throws ParsePojoException
	{
		try
		{
			// line from index.sense: sense_key synset_offset  sense_number  tag_cnt
			// [0] sensekey, an encoding of the word sense
			// [1] synset offset, the byte offset that the synset containing the sense is found at in the database "data" file corresponding to the part of speech encoded in the sense_key . synset_offset is an 8 digit, zero-filled decimal integer, and can be used with fseek(3) to read a synset from the data file. When passed to the WordNet library function read_synset() along with the syntactic category, a data structure containing the parsed synset is returned.
			// [2] sense number, a decimal integer indicating the sense number of the word, within the part of speech encoded in sense_key , in the WordNet database.
			// [3] tag count, the decimal number of times the sense is tagged in various semantic concordance texts. A tag_cnt of 0 indicates that the sense has not been semantically tagged.

			// read line into fields
			final String[] fields = line.split("\\s+");

			// core fields
			final Sensekey sensekey = Sensekey.parseSensekey(fields[0]);
			final Type type = sensekey.getType();
			final Lemma lemma = sensekey.getLemma();
			final int sensenum = Integer.parseInt(fields[2]);
			final SynsetId synsetId = new SynsetId(type.toPos(), Long.parseLong(fields[1]));

			// parse tag/lexid
			final TagCnt tagCnt = TagCnt.parseTagCnt(fields[3], sensenum);

			return new Sense(synsetId, lemma, sensenum, sensekey, tagCnt);
		}
		catch (Exception e)
		{
			throw new ParsePojoException(e);
		}
	}

	@Override
	public String toString()
	{
		return super.toString() + " l" + " t" + this.tagCnt;
	}
}
