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

	/**
	 * Lex relation
	 *
	 * @param type         relation type
	 * @param fromSynsetId source synset id
	 * @param toSynsetId   target synset id
	 * @param fromWord     source word
	 * @param toWord       target word
	 */
	public LexRelation(final RelationQualifier type, final SynsetId fromSynsetId, final SynsetId toSynsetId, final LemmaCS fromWord, final LemmaRef toWord)
	{
		super(type, fromSynsetId, toSynsetId);
		this.fromWord = fromWord;
		this.toWord = toWord;
	}

	/**
	 * Get (case-sensitive) source word
	 *
	 * @return (case - sensitive) source word
	 */
	public LemmaCS getFromWord()
	{
		return fromWord;
	}

	/**
	 * Get target word reference
	 *
	 * @return target word (unresolved) reference
	 */
	public LemmaRef getToWord()
	{
		return toWord;
	}

	/**
	 * Resolve reference
	 *
	 * @param resolver resolver function
	 * @return resolved word
	 */

	public Lemma resolveToWord(final Function<SynsetId, CoreSynset> resolver)
	{
		return this.toWord.resolve(resolver);
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
		return String.format("%s: %s[%s] -> %s[%d]", this.type.getType(), this.fromSynsetId.toString(), this.fromWord, this.toSynsetId.toString(), this.toWord.getWordNum());
	}

	public String toString(final String toWord)
	{
		return String.format("%s: %s[%s] -> %s[%s]", this.type.getType(), this.fromSynsetId.toString(), this.fromWord, this.toSynsetId.toString(), toWord);
	}
}
