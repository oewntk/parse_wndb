/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Sensekey
 *
 * @author Bernard Bou
 */
public class Sensekey
{
	private static final Pattern patternBreak = Pattern.compile("(?<!\\u005C)%");

	//public static final String ESCAPED_PERCENT = "\\%";
	public static final String ESCAPED_PERCENT = "~";

	private final String key;

	private final NormalizedString word; // may contain uppercase

	private final Type type;

	private final Domain domain;

	private final int lexId;

	private final NormalizedString headWord; // or null

	private final int headLexId; // -1 for none

	/**
	 * Copy constructor
	 *
	 * @param other other sensekey
	 */
	public Sensekey(final Sensekey other)
	{
		this.key = other.key;
		this.word = other.word;
		this.type = other.type;
		this.domain = other.domain;
		this.lexId = other.lexId;
		this.headWord = other.headWord;
		this.headLexId = other.headLexId;
	}

	/**
	 * Constructor
	 *
	 * @param word      word (may contain uppercase)
	 * @param type      type
	 * @param domain    domain
	 * @param lexId     lexid
	 * @param headWord  head word
	 * @param headLexId head lexid
	 * @param key       sensekey
	 */
	private Sensekey(final NormalizedString word, final Type type, final Domain domain, final int lexId, final NormalizedString headWord, final int headLexId, final String key)
	{
		this.key = key.trim();
		this.word = word;
		this.type = type;
		this.domain = domain;
		this.lexId = lexId;
		this.headWord = headWord;
		this.headLexId = headLexId;
	}

	/**
	 * Parse sensekey from string
	 *
	 * @param str string
	 * @return sensekey
	 * @throws ParsePojoException parse exception
	 */
	public static Sensekey parseSensekey(final String str) throws ParsePojoException
	{
		if (str == null)
		{
			return null;
		}
		final String[] fields = decode(str);
		if (fields.length < 4)
		{
			throw new ParsePojoException("Sensekey:" + str);
		}
		try
		{
			final NormalizedString word = new NormalizedString(fields[0]);
			final Type type = Type.fromIndex(Integer.parseInt(fields[1]));
			final Domain domain = Domain.parseDomainId(fields[2]);
			final int lexid = Integer.parseInt(fields[3]);
			final NormalizedString headWord = fields[4].isEmpty() ? null : new NormalizedString(fields[4]);
			final int headLexid = fields[5].isEmpty() ? -1 : Integer.parseInt(fields[5]);
			return new Sensekey(word, type, domain, lexid, headWord, headLexid, str);
		}
		catch (Exception e)
		{
			throw new ParsePojoException("Sensekey:" + str);
		}
	}

	/**
	 * Decode sensekey string into fields
	 *
	 * @param skStr sensekey string
	 * @return fields, fields[0] lemma (not space-normalized), fields[1] pos, fields[2] lexfile, fields[3] lexid, fields[4] head lemma (or ""), not space-normalized, fields[5] head lexid (or "")
	 */
	public static String[] decode(String skStr)
	{
		String[] fields = new String[6];
		String[] fields1 = patternBreak.split(skStr);
		assert fields1.length == 2;
		// lemma
		fields[0] = fields1[0].replace(ESCAPED_PERCENT, "%");
		// lexsense
		String lexSense = fields1[1].replace(ESCAPED_PERCENT, "%");
		// String typeFileLexid = lexSense.substring(0, 8);
		fields[1] = lexSense.substring(0, 1); // pos
		fields[2] = lexSense.substring(2, 4); // lexfile
		fields[3] = lexSense.substring(5, 7); // lexid
		int last = lexSense.lastIndexOf(':');
		fields[4] = lexSense.substring(8, last); // head (or "")
		fields[5] = lexSense.substring(last + 1); // head lexid (or "")
		return fields;
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
	 * Get normalized word
	 *
	 * @return normalized word
	 */
	public NormalizedString getWord()
	{
		return this.word;
	}

	/**
	 * Get lemma
	 *
	 * @return lemma
	 */
	public Lemma getLemma()
	{
		return new Lemma(this.word);
	}

	/**
	 * Get lex id
	 *
	 * @return lexid
	 */
	public int getLexId()
	{
		return this.lexId;
	}

	/**
	 * Get type
	 *
	 * @return type
	 */
	public Type getType()
	{
		return this.type;
	}

	/**
	 * Get key
	 *
	 * @return key
	 */
	public String getKey()
	{
		return this.key;
	}

	/**
	 * Get head word
	 *
	 * @return head word
	 */
	public NormalizedString getHeadWord()
	{
		return this.headWord;
	}

	/**
	 * Get head lexid
	 *
	 * @return head lexid
	 */
	public int getHeadLexId()
	{
		return this.headLexId;
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
		Sensekey sensekey = (Sensekey) o;
		return key.equals(sensekey.key);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(key);
	}

	@Override
	public String toString()
	{
		return this.key;
	}

	/**
	 * Get parsed sensekey string
	 *
	 * @return parsed sensekey string
	 */
	public String toParsedString()
	{
		return "word=" + this.word + " lexid=" + this.lexId + " domain=" + this.domain + " type=" + this.type;
	}
}