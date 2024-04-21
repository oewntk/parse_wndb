/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.parse;

import org.oewntk.pojos.ParsePojoException;
import org.oewntk.pojos.Synset;
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
public class DataParser
{
	private static final boolean THROW = false;

	// PrintStreams

	private static final PrintStream psl = Tracing.psNull;
	private static final PrintStream psi = System.getProperties().containsKey("VERBOSE") ? Tracing.psInfo : Tracing.psNull;
	private static final PrintStream pse = !System.getProperties().containsKey("SILENT") ? Tracing.psErr : Tracing.psNull;

	// Consumer

	private static final Consumer<Synset> consumer = psi::println;

	/**
	 * Parse all synsets
	 *
	 * @param dir      WNDB dir
	 * @param consumer synset consumer
	 * @return synset count
	 * @throws IOException        io exception
	 * @throws ParsePojoException parse pojo exception
	 */
	public static long parseAllSynsets(final File dir, final Consumer<Synset> consumer) throws IOException, ParsePojoException
	{
		long count = 0;
		for (final String posName : new String[]{"noun", "verb", "adj", "adv"})
		{
			count += parseSynsets(dir, posName, consumer);
		}
		return count;
	}

	/**
	 * Parse synsets
	 *
	 * @param dir      WNDB dir
	 * @param posName  pos
	 * @param consumer synset consumer
	 * @return synset count
	 * @throws ParsePojoException parse pojo exception
	 * @throws IOException        io exception
	 */
	public static long parseSynsets(final File dir, final String posName, final Consumer<Synset> consumer) throws ParsePojoException, IOException
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
			long synsetCount = 0;

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
					synsetCount++;
					consumer.accept(synset);
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
			psl.printf(format, "parse successes", synsetCount);
			(offsetErrorCount > 0 ? pse : psl).printf(format, "offset errors", offsetErrorCount);
			(parseErrorCount > 0 ? pse : psl).printf(format, "parse errors", parseErrorCount);
			return synsetCount;
		}
	}

	/**
	 * Parse line
	 *
	 * @param line  line
	 * @param isAdj whether the pos is adj
	 * @return synset
	 * @throws ParsePojoException parse pojo exception
	 */
	private static Synset parseSynsetLine(final String line, final boolean isAdj) throws ParsePojoException
	{
		return Synset.parseSynsetLine(line, isAdj);
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
		File dir = new File(args[0]);

		// Process
		parseAllSynsets(dir, consumer);

		// Timing
		final long endTime = System.currentTimeMillis();
		psl.println("Total execution time: " + (endTime - startTime) / 1000 + "s");
	}
}
