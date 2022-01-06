/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.parse;

import org.oewntk.pojos.MorphMapping;
import org.oewntk.pojos.ParsePojoException;
import org.oewntk.pojos.Pos;
import org.oewntk.utils.Tracing;

import java.io.*;
import java.util.function.Consumer;

/**
 * Morph Parser ({noun|verb|adj|adv|}.exc)
 *
 * @author Bernard Bou
 */
public class MorphParser
{
	private static final boolean THROW = false;

	// PrintStreams

	private static final PrintStream psi = System.getProperties().containsKey("VERBOSE") ? Tracing.psInfo : Tracing.psNull;
	private static final PrintStream pse = !System.getProperties().containsKey("SILENT") ? Tracing.psErr : Tracing.psNull;

	// Consumer

	private static final Consumer<MorphMapping> consumer = psi::println;

	/**
	 * Parse morph mappings
	 *
	 * @param dir      WNDB dir
	 * @param consumer morphmapping consumer
	 * @throws IOException        io exception
	 * @throws ParsePojoException parse pojo exception
	 */
	public static void parseAllMorphs(final File dir, final Consumer<MorphMapping> consumer) throws IOException, ParsePojoException
	{
		// Process for all pos
		for (final String posName : new String[]{"noun", "verb", "adj", "adv"})
		{
			parseMorphs(dir, posName, consumer);
		}
	}

	/**
	 * Parse morph mappings
	 *
	 * @param dir      WNDB dir
	 * @param posName  pos
	 * @param consumer morphmapping consumer
	 * @throws IOException        io exception
	 * @throws ParsePojoException parse pojo exception
	 */
	public static void parseMorphs(final File dir, String posName, final Consumer<MorphMapping> consumer) throws IOException, ParsePojoException
	{
		psi.println("* Morphs");

		Pos pos = Pos.fromName(posName);

		// iterate on lines
		final File file = new File(dir, posName + ".exc");
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Flags.charSet)))
		{
			int lineCount = 0;
			int parseErrorCount = 0;
			long morphMappingCount = 0;

			String line;
			while ((line = reader.readLine()) != null)
			{
				lineCount++;

				try
				{
					MorphMapping morphMapping = MorphMapping.parseMorphMapping(line, pos);
					morphMappingCount++;
					consumer.accept(morphMapping);
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
			psi.printf(format, "parse successes", morphMappingCount);
			(parseErrorCount > 0 ? pse : psi).printf(format, "parse errors", parseErrorCount);
		}
	}

	/**
	 * Main
	 *
	 * @param args cmd-line args
	 * @throws ParsePojoException parse pojo exception
	 * @throws IOException        io exception
	 */
	public static void main(String[] args) throws IOException, ParsePojoException
	{
		// Timing
		final long startTime = System.currentTimeMillis();

		// Input
		File dir = new File(args[0]);

		// Process
		parseAllMorphs(dir, consumer);

		// Timing
		final long endTime = System.currentTimeMillis();
		psi.println("Total execution time: " + (endTime - startTime) / 1000 + "s");
	}
}
