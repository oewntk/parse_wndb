/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.parse;

import org.oewntk.pojos.Index;
import org.oewntk.pojos.ParsePojoException;
import org.oewntk.pojos.Sense;
import org.oewntk.pojos.Synset;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.function.Consumer;

public class Parser
{
	// Consumers
	private static final Consumer<Synset> synsetConsumer = System.out::println;
	private static final Consumer<Sense> senseConsumer = System.out::println;
	private static final Consumer<Index> indexConsumer = System.out::println;

	// PrintStreams
	private static final PrintStream psnull = Utils.nullPrintStream();
	private static final PrintStream psi = !System.getProperties().containsKey("SILENT") ? System.out : psnull;

	public static void main(String[] args) throws IOException, ParsePojoException
	{
		// Timing
		final long startTime = System.currentTimeMillis();

		// Input
		File dir = new File(args[0]);
		parseAll(dir, synsetConsumer, senseConsumer, indexConsumer);

		// Timing
		final long endTime = System.currentTimeMillis();
		psi.println("Total execution time: " + (endTime - startTime) / 1000 + "s");
	}

	public static void parseAll(final File dir, final Consumer<Synset> synsetConsumer, final Consumer<Sense> senseConsumer, final Consumer<Index> indexConsumer) throws IOException, ParsePojoException
	{
		DataParser.parseAllSynsets(dir, synsetConsumer);
		IndexParser.parseAllIndexes(dir, indexConsumer);
		SenseParser.parseSenses(dir, senseConsumer);
	}
}
