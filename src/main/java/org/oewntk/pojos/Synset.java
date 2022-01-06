/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.pojos;

/**
 * Synset, a core synset extended to include relations and possibly verb frames
 *
 * @author Bernard Bou
 */
public class Synset extends CoreSynset
{
	public final Relation[] relations;

	public final VerbFrameRef[] verbFrameRefs;

	/**
	 * Constructor
	 *
	 * @param synsetId   synset id
	 * @param lemmas     lemmas
	 * @param type       type
	 * @param domain     lex domain
	 * @param gloss      gloss
	 * @param relations  relations
	 * @param verbFrames verb frames
	 */
	private Synset(final SynsetId synsetId, final LemmaCS[] lemmas, final Type type, final Domain domain, final Gloss gloss, final Relation[] relations, final VerbFrameRef[] verbFrames)
	{
		super(synsetId, lemmas, type, domain, gloss);
		this.relations = relations;
		this.verbFrameRefs = verbFrames;
	}

	/**
	 * Parse from line
	 *
	 * @param line  line
	 * @param isAdj whether adj synsets are being parsed
	 * @return synset
	 * @throws ParsePojoException parse exception
	 */
	public static Synset parseSynsetLine(final String line, final boolean isAdj) throws ParsePojoException
	{
		try
		{
			// core subparse
			final CoreSynset protoSynset = CoreSynset.parseCoreSynset(line, isAdj);

			// copy data from proto
			final Type type = protoSynset.getType();
			final LemmaCS[] csLemmas = protoSynset.getCSLemmas();
			final SynsetId synsetId = protoSynset.getId();
			final Domain domain = protoSynset.getDomain();
			final Gloss gloss = protoSynset.getGloss();

			// split into fields
			final String[] fields = line.split("\\s+");
			int fieldPointer = 0;

			// data
			Relation[] relations;
			VerbFrameRef[] frames = null;

			// offset
			fieldPointer++;

			// domain
			fieldPointer++;

			// part-of-speech
			fieldPointer++;

			// lemma count
			fieldPointer++;

			// lemma set
			fieldPointer += 2 * csLemmas.length;

			// relation count
			final int relationCount = Integer.parseInt(fields[fieldPointer], 10);
			fieldPointer++;

			// relations
			relations = new Relation[relationCount];
			for (int i = 0; i < relationCount; i++)
			{
				// read data
				final String relationTypeField = fields[fieldPointer++];
				final String relationSynsetIdField = fields[fieldPointer++];
				final String relationPosField = fields[fieldPointer++];
				final String relationSourceTargetField = fields[fieldPointer++];
				final long relationSynsetId = Long.parseLong(relationSynsetIdField);
				final int relationSourceTarget = Integer.parseInt(relationSourceTargetField, 16);

				// compute
				final Type synsetType = Type.parseType(relationPosField.charAt(0));
				final RelationType relationType = RelationType.parseRelationType(relationTypeField);
				final SynsetId toId = new SynsetId(synsetType.toPos(), relationSynsetId);

				// create
				if (relationSourceTarget != 0)
				{
					final int fromWordIndex = relationSourceTarget >> 8;
					final int toWordIndex = relationSourceTarget & 0xff;
					final LemmaCS fromLemma = csLemmas[fromWordIndex - 1];
					final LemmaRef toLemma = new LemmaRef(toId, toWordIndex);
					relations[i] = new LexRelation(relationType, synsetId, toId, fromLemma, toLemma);
				}
				else
				{
					relations[i] = new Relation(relationType, synsetId, toId);
				}
			}

			// frames
			if (type.toChar() == 'v' && !fields[fieldPointer].equals("|"))
			{
				// frame count
				final int frameCount = Integer.parseInt(fields[fieldPointer], 10);
				fieldPointer++;

				// frames
				frames = new VerbFrameRef[frameCount];
				for (int i = 0; i < frameCount; i++)
				{
					// read data
					fieldPointer++; // '+'
					final String frameIdField = fields[fieldPointer++];
					final String wordIndexField = fields[fieldPointer++];

					// compute
					final int frameId = Integer.parseInt(frameIdField);
					final int wordIndex = Integer.parseInt(wordIndexField, 16);

					// create
					Lemma[] frameLemmas;
					if (wordIndex != 0)
					{
						frameLemmas = new Lemma[]{csLemmas[wordIndex - 1].lemma};
					}
					else // 0 means all
					{
						frameLemmas = protoSynset.getLemmas();
					}
					frames[i] = new VerbFrameRef(frameLemmas, frameId);
				}
			}
			return new Synset(synsetId, csLemmas, type, domain, gloss, relations, frames);
		}
		catch (Exception e)
		{
			throw new ParsePojoException(e);
		}
	}

	/**
	 * Get relations
	 *
	 * @return relations
	 */
	public Relation[] getRelations()
	{
		return this.relations;
	}

	/**
	 * Get verb frames
	 *
	 * @return verb frames
	 */
	public VerbFrameRef[] getVerbFrames()
	{
		return this.verbFrameRefs;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		if (this.relations != null)
		{
			sb.append(" relations={");
			for (int i = 0; i < this.relations.length; i++)
			{
				if (i != 0)
				{
					sb.append(",");
				}
				sb.append(this.relations[i].toString());
			}
			sb.append("}");
		}
		if (this.verbFrameRefs != null)
		{
			sb.append(" frames={");
			for (int i = 0; i < this.verbFrameRefs.length; i++)
			{
				if (i != 0)
				{
					sb.append(",");
				}
				sb.append(this.verbFrameRefs[i].toString());
			}
			sb.append("}");
		}
		return sb.toString();
	}

	/**
	 * Pretty string
	 *
	 * @return pretty string
	 */
	public String toPrettyString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append('\n');
		sb.append(this.gloss.toPrettyString());

		if (this.relations != null)
		{
			sb.append('\n');
			sb.append("relations={");
			sb.append('\n');
			for (int i = 0; i < this.relations.length; i++)
			{
				if (i != 0)
				{
					sb.append(",");
					sb.append('\n');
				}
				sb.append('\t');
				sb.append(this.relations[i].toString());
			}
			sb.append('\n');
			sb.append("}");
		}
		if (this.verbFrameRefs != null)
		{
			sb.append('\n');
			sb.append("frames={");
			for (int i = 0; i < this.verbFrameRefs.length; i++)
			{
				if (i != 0)
				{
					sb.append(",");
				}
				sb.append(this.verbFrameRefs[i].toString());
			}
			sb.append("}");
		}
		return sb.toString();
	}
}