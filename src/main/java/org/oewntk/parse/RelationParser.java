/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.parse;

import org.oewntk.pojos.*;
import org.oewntk.utils.Tracing;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Synset relation parser data.{noun|verb|adj|adv}
 *
 * @author Bernard Bou
 */
public class RelationParser
{
	private static final boolean THROW = false;

	// PrintStreams

	private static final PrintStream psl = Tracing.psInfo;
	private static final PrintStream psi = System.getProperties().containsKey("VERBOSE") ? Tracing.psInfo : Tracing.psNull;
	private static final PrintStream pse = !System.getProperties().containsKey("SILENT") ? Tracing.psErr : Tracing.psNull;

	// Maps

	/**
	 * Synset by id map
	 */
	private final Map<SynsetId, Synset> synsetsById = new HashMap<>();

	// Consumers

	/**
	 * Synset consumer
	 */
	private final Consumer<Synset> synsetConsumer = (synset) -> synsetsById.put(synset.getId(), synset);

	/**
	 * Semantic relation consumer
	 */
	private final Consumer<Relation> relationConsumer = (relation) -> {

		String type = relation.type.toString();
		if (relation.fromSynsetId == null || relation.fromSynsetId.getOffset() == 0L || relation.toSynsetId == null || relation.toSynsetId.getOffset() == 0L)
		{
			throw new IllegalArgumentException(relation.toString());
		}
		if ((relation instanceof LexRelation))
		{
			LexRelation lexRelation = (LexRelation) relation;
			LemmaRef toWord = lexRelation.getToWord();
			if (toWord == null || toWord.getSynsetId().getOffset() == 0 || toWord.getWordNum() == 0)
			{
				throw new IllegalArgumentException(lexRelation.toString());
			}
			if (!RelationType.SENSE_RELATIONS.contains(type))
			{
				throw new IllegalArgumentException(lexRelation.toString());
			}
			psi.printf("%-6s %s%n", "sense", relation);
		}
		else
		{
			if (!RelationType.SYNSET_RELATIONS.contains(type))
			{
				throw new IllegalArgumentException(relation.toString());
			}
			psi.printf("%-6s %s%n", "synset", relation);
		}
	};

	/**
	 * Lexical relation consumer
	 */
	private final Consumer<LexRelation> lexRelationConsumer = (relation) -> {

		String type = relation.type.toString();
		if (relation.fromSynsetId == null || relation.fromSynsetId.getOffset() == 0L || relation.toSynsetId == null || relation.toSynsetId.getOffset() == 0L)
		{
			throw new IllegalArgumentException(relation.toString());
		}
		LemmaRef toWord = relation.getToWord();
		if (toWord == null || toWord.getSynsetId().getOffset() == 0 || toWord.getWordNum() == 0)
		{
			throw new IllegalArgumentException(relation.toString());
		}
		if (!RelationType.SENSE_RELATIONS.contains(type))
		{
			throw new IllegalArgumentException(relation.toString());
		}
		// psi.printf("%-6s %s%n", "sense", relation);
		String resolvedToWord = resolveToWord(relation);
		psi.printf("%-6s %s%n", "sense", relation.toString(resolvedToWord));
	};

	// Source

	/**
	 * WNDB source dir
	 */
	private final File dir;

	/**
	 * Constructor
	 *
	 * @param dir WNDB dir
	 */
	public RelationParser(final File dir)
	{
		this.dir = dir;
	}

	/**
	 * Parse all synsets
	 *
	 * @return this
	 * @throws IOException        io exception
	 * @throws ParsePojoException parse pojo exception
	 */
	@SuppressWarnings("UnusedReturnValue")
	public RelationParser parseAllSynsets() throws IOException, ParsePojoException
	{
		// make map for resolution
		for (final String posName : new String[]{"noun", "verb", "adj", "adv"})
		{
			parseSynsets(dir, posName, synsetConsumer, null, null);
		}
		// consume relations
		for (final String posName : new String[]{"noun", "verb", "adj", "adv"})
		{
			parseSynsets(dir, posName, null, relationConsumer, lexRelationConsumer);
		}
		return this;
	}

	/**
	 * Parse synsets
	 *
	 * @param dir                 WNDB dir
	 * @param posName             pos
	 * @param synsetConsumer      synset consumer
	 * @param relationConsumer    relation consumer
	 * @param lexRelationConsumer lex relation consumer
	 * @return relation count
	 * @throws ParsePojoException parse pojo exception
	 * @throws IOException        io exception
	 */
	@SuppressWarnings("UnusedReturnValue")
	public static long parseSynsets(final File dir, final String posName, final Consumer<Synset> synsetConsumer, final Consumer<Relation> relationConsumer, final Consumer<LexRelation> lexRelationConsumer) throws ParsePojoException, IOException
	{
		psl.println("* Synsets " + posName);

		final boolean isAdj = posName.equals("adj");

		// iterate on lines
		final File file = new File(dir, "data." + posName);
		try (RandomAccessFile raFile = new RandomAccessFile(file, "r"))
		{
			raFile.seek(0);

			// iterate on lines
			int lineCount = 0;
			int nonCommentCount = 0;
			int offsetErrorCount = 0;
			int parseErrorCount = 0;
			long relationCount = 0;

			String rawLine;
			long fileOffset = raFile.getFilePointer();
			for (; (rawLine = raFile.readLine()) != null; fileOffset = raFile.getFilePointer())
			{
				lineCount++;
				if (rawLine.isEmpty() || rawLine.charAt(0) == ' ')
				{
					continue;
				}

				// decode
				String line = new String(rawLine.getBytes(Flags.charSet));
				nonCommentCount++;

				// split into fields
				final String[] lineFields = line.split("\\s+");

				// read offset
				long readOffset = Long.parseLong(lineFields[0]);
				if (fileOffset != readOffset)
				{
					pse.printf("Offset: data.%s:%d offset=%08d line=[%s]%n", posName, lineCount, fileOffset, line);
					offsetErrorCount++;
					continue;
				}

				// read
				try
				{
					Synset synset = parseSynsetLine(line, isAdj);
					if (synsetConsumer != null)
					{
						synsetConsumer.accept(synset);
					}
					if (relationConsumer == null && lexRelationConsumer == null)
					{
						continue;
					}
					Relation[] relations = synset.getRelations();
					for (Relation relation : relations)
					{
						if (relation instanceof LexRelation && lexRelationConsumer != null)
						{
							lexRelationConsumer.accept((LexRelation) relation);
						}
						else if (relationConsumer != null)
						{
							relationConsumer.accept(relation);
						}
						relationCount++;
					}
				}
				catch (final ParsePojoException e)
				{
					parseErrorCount++;
					pse.printf("%n%s:%d offset=%08d line=[%s] except=%s", file.getName(), lineCount, fileOffset, line, e.getMessage());
					if (THROW)
					{
						throw e;
					}
				}
			}
			String format = "%-50s %d%n";
			psl.printf(format, "lines", nonCommentCount);
			psl.printf(format, "parse successes", relationCount);
			(offsetErrorCount > 0 ? pse : psl).printf(format, "offset errors", offsetErrorCount);
			(parseErrorCount > 0 ? pse : psl).printf(format, "parse errors", parseErrorCount);
			return relationCount;
		}
	}

	/**
	 * Parse synset line
	 *
	 * @param line  line
	 * @param isAdj whether adjectives are being processed
	 * @return synset
	 * @throws ParsePojoException parse pojo exception
	 */
	private static Synset parseSynsetLine(final String line, final boolean isAdj) throws ParsePojoException
	{
		return Synset.parseSynsetLine(line, isAdj);
	}

	/**
	 * Resolve lexical relation to target word
	 *
	 * @param lr lexical relation
	 * @return resolve target word
	 */
	private String resolveToWord(final LexRelation lr)
	{
		SynsetId toSynsetId = lr.getToSynsetId();
		Synset toSynset = synsetsById.get(toSynsetId);
		LemmaRef toWordRef = lr.getToWord();
		return toWordRef.resolve(toSynset).toString();
	}

	/**
	 * Main
	 *
	 * @param args command-line arguments
	 * @throws ParsePojoException parse pojo exception
	 * @throws IOException        io exception
	 */
	public static void main(final String[] args) throws ParsePojoException, IOException
	{
		// Timing
		final long startTime = System.currentTimeMillis();

		// Input
		for (String arg : args)
		{
			File dir = new File(arg);

			// Process
			new RelationParser(dir).parseAllSynsets();
		}

		// Timing
		final long endTime = System.currentTimeMillis();
		psl.println("Total execution time: " + (endTime - startTime) / 1000 + "s");
	}
}
