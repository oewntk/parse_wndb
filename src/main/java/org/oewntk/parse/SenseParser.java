/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.parse;

import org.oewntk.pojos.ParsePojoException;
import org.oewntk.pojos.Sense;

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

	// Consumer
	private static final Consumer<Sense> consumer = System.out::println;

	// PrintStreams
	private static final PrintStream psnull = Utils.nullPrintStream();
	private static final PrintStream pse = !System.getProperties().containsKey("SILENT") ? System.err : psnull;
	private static final PrintStream psi = !System.getProperties().containsKey("SILENT") ? System.out : psnull;

	public static void main(String[] args) throws IOException, ParsePojoException
	{
		// Timing
		final long startTime = System.currentTimeMillis();

		// Input
		File dir = new File(args[0]);

		// Process
		parseSenses(dir, consumer);

		// Timing
		final long endTime = System.currentTimeMillis();
		psi.println("Total execution time: " + (endTime - startTime) / 1000 + "s");
	}

	public static void parseSenses(final File dir, final Consumer<Sense> consumer) throws IOException, ParsePojoException
	{
		psi.println("* Senses");

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
			psi.printf(format, "lines", lineCount);
			psi.printf(format, "parse successes", senseCount);
			(parseErrorCount > 0 ? pse : psi).printf(format, "parse errors", parseErrorCount);
		}
	}
}
