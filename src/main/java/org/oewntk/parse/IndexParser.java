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

	private static final PrintStream psl = Tracing.psNull;
	private static final PrintStream psi = System.getProperties().containsKey("VERBOSE") ? Tracing.psInfo : Tracing.psNull;
	private static final PrintStream pse = !System.getProperties().containsKey("SILENT") ? Tracing.psErr : Tracing.psNull;

	// Consumer

	private static final Consumer<Index> consumer = psi::println;

	/**
	 * Parse all indexes
	 *
	 * @param dir      WNDB dir
	 * @param consumer index consumer
	 * @return index count
	 * @throws IOException        io exception
	 * @throws ParsePojoException parse pojo exception
	 */
	public static long parseAllIndexes(final File dir, final Consumer<Index> consumer) throws IOException, ParsePojoException
	{
		long count = 0;
		for (final String posName : new String[]{"noun", "verb", "adj", "adv"})
		{
			count += parseIndexes(dir, posName, consumer);
		}
		return count;
	}

	/**
	 * Parse all core indexes
	 *
	 * @param dir      WNDB dir
	 * @param consumer core index consumer
	 * @return index count
	 * @throws IOException        io exception
	 * @throws ParsePojoException parse pojo exception
	 */
	public static long parseAllCoreIndexes(final File dir, final Consumer<CoreIndex> consumer) throws IOException, ParsePojoException
	{
		long count = 0;
		for (final String posName : new String[]{"noun", "verb", "adj", "adv"})
		{
			count += parseCoreIndexes(dir, posName, consumer);
		}
		return count;
	}

	/**
	 * Parse indexes
	 *
	 * @param dir      WNDB dir
	 * @param posName  pos
	 * @param consumer index consumer
	 * @return index count
	 * @throws IOException        io exception
	 * @throws ParsePojoException parse pojo exception
	 */
	public static long parseIndexes(final File dir, final String posName, final Consumer<Index> consumer) throws IOException, ParsePojoException
	{
		psl.println("* Indexes " + posName);

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
			psl.printf(format, "lines", nonCommentCount);
			(parseErrorCount > 0 ? pse : psl).printf(format, "parse successes", indexCount);
			(parseErrorCount > 0 ? pse : psl).printf(format, "parse errors", parseErrorCount);
			return indexCount;
		}
	}

	/**
	 * Parse core indexes
	 *
	 * @param dir      WNDB dir
	 * @param posName  pos
	 * @param consumer core index consumer
	 * @return index count
	 * @throws IOException        io exception
	 * @throws ParsePojoException parse pojo exception
	 */
	public static long parseCoreIndexes(final File dir, final String posName, final Consumer<CoreIndex> consumer) throws IOException, ParsePojoException
	{
		psl.println("* Indexes " + posName);

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
			psl.printf(format, "lines", nonCommentCount);
			(parseErrorCount > 0 ? pse : psl).printf(format, "parse successes", indexCount);
			(parseErrorCount > 0 ? pse : psl).printf(format, "parse errors", parseErrorCount);
			return indexCount;
		}
	}

	/**
	 * Main
	 *
	 * @param args cmd-line args
	 * @throws ParsePojoException parse pojo exception
	 * @throws IOException        io exception
	 */
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
		psl.println("Total execution time: " + (endTime - startTime) / 1000 + "s");
	}
}
