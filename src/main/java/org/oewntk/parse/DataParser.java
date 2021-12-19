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
	private static final PrintStream psi = System.getProperties().containsKey("VERBOSE") ? Tracing.psInfo : Tracing.psNull;
	private static final PrintStream pse = !System.getProperties().containsKey("SILENT") ? Tracing.psErr : Tracing.psNull;

	// Consumer
	private static final Consumer<Synset> consumer = psi::println;

	public static void parseAllSynsets(final File dir, final Consumer<Synset> consumer) throws IOException, ParsePojoException
	{
		for (final String posName : new String[]{"noun", "verb", "adj", "adv"})
		{
			parseSynsets(dir, posName, consumer);
		}
	}

	public static void parseSynsets(final File dir, final String posName, final Consumer<Synset> consumer) throws ParsePojoException, IOException
	{
		psi.println("* Synsets " + posName);

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
					Synset synset = parseSynset(line, isAdj);
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
			psi.printf(format, "lines", nonCommentCount);
			psi.printf(format, "parse successes", synsetCount);
			(offsetErrorCount > 0 ? pse : psi).printf(format, "offset errors", offsetErrorCount);
			(parseErrorCount > 0 ? pse : psi).printf(format, "parse errors", parseErrorCount);
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
		File dir = new File(args[0]);

		// Process
		parseAllSynsets(dir, consumer);

		// Timing
		final long endTime = System.currentTimeMillis();
		psi.println("Total execution time: " + (endTime - startTime) / 1000 + "s");
	}
}
