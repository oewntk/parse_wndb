/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.parse;

import org.oewntk.pojos.CoreIndex;
import org.oewntk.pojos.Index;
import org.oewntk.pojos.ParsePojoException;
import org.oewntk.utils.Tracing;

import java.io.*;
import java.util.function.Consumer;

/**
 * Index parser index.{noun|verb|adj|adv}
 *
 * @author Bernard Bou
 */
public class IndexParser
{
	private static final boolean THROW = false;

	// PrintStreams
	private static final PrintStream psi = System.getProperties().containsKey("VERBOSE") ? Tracing.psInfo : Tracing.psNull;
	private static final PrintStream pse = !System.getProperties().containsKey("SILENT") ? Tracing.psErr : Tracing.psNull;

	// Consumer
	private static final Consumer<Index> consumer = psi::println;
	//private static final Consumer<CoreIndex> coreConsumer = psi::println;

	public static void parseAllIndexes(final File dir, final Consumer<Index> consumer) throws IOException, ParsePojoException
	{
		// Process for all pos
		for (final String posName : new String[]{"noun", "verb", "adj", "adv"})
		{
			parseIndexes(dir, posName, consumer);
		}
	}

	public static void parseAllCoreIndexes(final File dir, final Consumer<CoreIndex> consumer) throws IOException, ParsePojoException
	{
		// Process for all pos
		for (final String posName : new String[]{"noun", "verb", "adj", "adv"})
		{
			parseCoreIndexes(dir, posName, consumer);
		}
	}

	public static void parseIndexes(final File dir, final String posName, final Consumer<Index> consumer) throws IOException, ParsePojoException
	{
		psi.println("* Indexes " + posName);

		// iterate on lines
		final File file = new File(dir, "index." + posName);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Flags.charSet)))
		{
			int lineCount = 0;
			int nonCommentCount = 0;
			long indexCount = 0;
			int parseErrorCount = 0;
			String line;
			while ((line = reader.readLine()) != null)
			{
				lineCount++;
				if (line.isEmpty() || line.charAt(0) == ' ')
				{
					continue;
				}
				nonCommentCount++;

				try
				{
					Index index = Index.parseIndex(line);
					indexCount++;
					consumer.accept(index);
				}
				catch (final ParsePojoException e)
				{
					parseErrorCount++;
					pse.printf("%n%s:%d line=[%s] except=%s", file.getName(), lineCount, line, e);
					if (THROW)
					{
						throw e;
					}
				}
			}
			String format = "%-50s %d%n";
			psi.printf(format, "lines", nonCommentCount);
			(parseErrorCount > 0 ? pse : psi).printf(format, "parse successes", indexCount);
			(parseErrorCount > 0 ? pse : psi).printf(format, "parse errors", parseErrorCount);
		}
	}

	public static void parseCoreIndexes(final File dir, final String posName, final Consumer<CoreIndex> consumer) throws IOException, ParsePojoException
	{
		psi.println("* Indexes " + posName);

		// iterate on lines
		final File file = new File(dir, "index." + posName);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Flags.charSet)))
		{
			int lineCount = 0;
			int nonCommentCount = 0;
			long indexCount = 0;
			int parseErrorCount = 0;
			String line;
			while ((line = reader.readLine()) != null)
			{
				lineCount++;
				if (line.isEmpty() || line.charAt(0) == ' ')
				{
					continue;
				}
				nonCommentCount++;

				try
				{
					CoreIndex index = CoreIndex.parseCoreIndex(line);
					indexCount++;
					consumer.accept(index);
				}
				catch (final ParsePojoException e)
				{
					parseErrorCount++;
					pse.printf("%n%s:%d line=[%s] except=%s", file.getName(), lineCount, line, e);
					if (THROW)
					{
						throw e;
					}
				}
			}
			String format = "%-50s %d%n";
			psi.printf(format, "lines", nonCommentCount);
			(parseErrorCount > 0 ? pse : psi).printf(format, "parse successes", indexCount);
			(parseErrorCount > 0 ? pse : psi).printf(format, "parse errors", parseErrorCount);
		}
	}

	public static void main(final String[] args) throws IOException, ParsePojoException
	{
		// Timing
		final long startTime = System.currentTimeMillis();

		// Input
		File dir = new File(args[0]);

		// Process
		parseAllIndexes(dir, consumer);

		// Timing
		final long endTime = System.currentTimeMillis();
		psi.println("Total execution time: " + (endTime - startTime) / 1000 + "s");
	}
}
