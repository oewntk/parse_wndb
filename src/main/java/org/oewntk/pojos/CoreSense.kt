/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.pojos;

/**
 * Core Sense with sensekey
 *
 * @author Bernard Bou
 */
public class CoreSense extends BaseSense
{
	public final Sensekey sensekey;

	/**
	 * Core sense
	 *
	 * @param synsetId      synset id
	 * @param lemma         lemma
	 * @param sensePosIndex sense index in pos
	 * @param sensekey      sensekey
	 */
	public CoreSense(final SynsetId synsetId, final Lemma lemma, final int sensePosIndex, final Sensekey sensekey)
	{
		super(synsetId, lemma, sensePosIndex);
		this.sensekey = sensekey;
	}

	@Override
	public String toString()
	{
		return super.toString() + " k=" + this.sensekey.toString();
	}
}
