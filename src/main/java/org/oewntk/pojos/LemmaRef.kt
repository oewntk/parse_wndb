/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

import java.util.Objects;
import java.util.function.Function;

/**
 * Lemma Reference (nth word in synset)
 *
 * @author Bernard Bou
 */
public class LemmaRef
{
	/**
	 * SynsetId of the synset the lemma is member of
	 */
	private final SynsetId synsetId;

	/**
	 * 1-based number of the word in the lemma list
	 */
	private final int wordNum;

	/**
	 * Constructor
	 *
	 * @param synsetId synsetId of the synset the lemma is member of
	 * @param wordNum  1-based number of the word in the lemma list
	 */
	protected LemmaRef(final SynsetId synsetId, final int wordNum)
	{
		this.synsetId = synsetId;
		this.wordNum = wordNum;
	}

	/**
	 * Get synset id
	 *
	 * @return synset id
	 */
	public SynsetId getSynsetId()
	{
		return synsetId;
	}

	/**
	 * Get word num
	 *
	 * @return word num
	 */
	public int getWordNum()
	{
		return wordNum;
	}

	/**
	 * Dereference / Resolve
	 *
	 * @param f functions that when applied to synsetId yields synset
	 * @return lemma referred to by reference
	 */
	public Lemma resolve(final Function<SynsetId, CoreSynset> f)
	{
		final CoreSynset synset = f.apply(this.synsetId);
		return synset.getCSLemmas()[this.wordNum - 1].lemma;
	}

	/**
	 * Dereference / Resolve
	 *
	 * @param synset synset
	 * @return lemma referred to by reference
	 */
	public Lemma resolve(final CoreSynset synset)
	{
		return synset.getCSLemmas()[this.wordNum - 1].lemma;
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
		LemmaRef lemmaRef = (LemmaRef) o;
		return wordNum == lemmaRef.wordNum && synsetId.equals(lemmaRef.synsetId);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(synsetId, wordNum);
	}

	@Override
	public String toString()
	{
		return this.synsetId + "[" + this.wordNum + "]";
	}
}
