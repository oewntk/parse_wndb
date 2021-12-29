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
import java.util.function.Consumer;

/**
 * Synset Parser data.{noun|verb|adj|adv}
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

	// Consumer
	//private static final Consumer<Relation> consumer = psi::println;
	private final Consumer<Relation> consumer = relation -> {

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

	// Source

	private final File dir;

	RelationParser(final File dir)
	{
		this.dir = dir;
	}

	@SuppressWarnings("UnusedReturnValue")
	public RelationParser parseAllSynsets() throws IOException, ParsePojoException
	{
		long count = 0;
		for (final String posName : new String[]{"noun", "verb", "adj", "adv"})
		{
			count += parseSynsets(dir, posName, consumer);
		}
		return this;
	}

	public static long parseSynsets(final File dir, final String posName, final Consumer<Relation> consumer) throws ParsePojoException, IOException
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
					Synset synset = parseSynset(line, isAdj);
					Relation[] relations = synset.getRelations();
					for (Relation relation : relations)
					{
						consumer.accept(relation);
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

	private static Synset parseSynset(final String line, final boolean isAdj) throws ParsePojoException
	{
		return Synset.parseSynset(line, isAdj);
	}

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
