/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

import java.util.Objects;
import java.util.function.Function;

/**
 * Lexical Relation (a lexical relation is an extended semantical relation)
 *
 * @author Bernard Bou
 */
public class LexRelation extends Relation
{
	public final LemmaCS fromWord;

	private final LemmaRef toWord;

	public LexRelation(final RelationType type, final SynsetId fromSynsetId, final SynsetId toSynsetId, final LemmaCS fromWord, final LemmaRef toWord)
	{
		super(type, fromSynsetId, toSynsetId);
		this.fromWord = fromWord;
		this.toWord = toWord;
	}

	public LemmaCS getFromWord()
	{
		return fromWord;
	}

	public LemmaRef getToWord()
	{
		return toWord;
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
		if (!super.equals(o))
		{
			return false;
		}
		LexRelation that = (LexRelation) o;
		return fromWord.equals(that.fromWord) && toWord.equals(that.toWord);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), fromWord, toWord);
	}

	@Override
	public String toString()
	{
		return String.format("%s: %s[%s] -> %s[%d]", this.type.getName(), this.fromSynsetId.toString(), this.fromWord, this.toSynsetId.toString(), this.toWord.getWordNum());
	}

	public String toString(final String toWord)
	{
		return String.format("%s: %s[%s] -> %s[%s]", this.type.getName(), this.fromSynsetId.toString(), this.fromWord, this.toSynsetId.toString(), toWord);
	}

	public Lemma resolveToWord(final Function<SynsetId, CoreSynset> f)
	{
		return this.toWord.resolve(f);
	}
}
