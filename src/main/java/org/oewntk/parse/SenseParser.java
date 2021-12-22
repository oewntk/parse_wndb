/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.parse;

import org.oewntk.pojos.ParsePojoException;
import org.oewntk.pojos.Sense;
import org.oewntk.utils.Tracing;

import java.io.*;
import java.util.function.Consumer;

/**
 * Sense Parser (index.sense)
 *
 * @author Bernard Bou
 */
public class SenseParser
{
	private static final boolean THROW = false;

	// PrintStreams
	private static final PrintStream psl = Tracing.psNull;
	private static final PrintStream psi = System.getProperties().containsKey("VERBOSE") ? Tracing.psInfo : Tracing.psNull;
	private static final PrintStream pse = !System.getProperties().containsKey("SILENT") ? Tracing.psErr : Tracing.psNull;

	// Consumer
	private static final Consumer<Sense> consumer = psi::println;

	public static long parseSenses(final File dir, final Consumer<Sense> consumer) throws IOException, ParsePojoException
	{
		psl.println("* Senses");

		// iterate on lines
		final File file = new File(dir, "index.sense");
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Flags.charSet)))
		{
			int lineCount = 0;
			int parseErrorCount = 0;
			long senseCount = 0;

			String line;
			while ((line = reader.readLine()) != null)
			{
				lineCount++;

				try
				{
					Sense sense = Sense.parseSense(line);
					senseCount++;
					consumer.accept(sense);
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
			psl.printf(format, "lines", lineCount);
			psl.printf(format, "parse successes", senseCount);
			(parseErrorCount > 0 ? pse : psl).printf(format, "parse errors", parseErrorCount);
			return senseCount;
		}
	}

	public static void main(final String[] args) throws IOException, ParsePojoException
	{
		// Timing
		final long startTime = System.currentTimeMillis();

		// Input
		File dir = new File(args[0]);

		// Process
		parseSenses(dir, consumer);

		// Timing
		final long endTime = System.currentTimeMillis();
		psl.println("Total execution time: " + (endTime - startTime) / 1000 + "s");
	}
}
