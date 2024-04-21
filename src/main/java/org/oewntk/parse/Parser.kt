/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.parse;

import org.oewntk.pojos.Index;
import org.oewntk.pojos.ParsePojoException;
import org.oewntk.pojos.Sense;
import org.oewntk.pojos.Synset;
import org.oewntk.utils.Tracing;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.function.Consumer;

/**
 * Parser
 */
public class Parser
{
	// PrintStreams

	private static final PrintStream psi = !System.getProperties().containsKey("SILENT") ? Tracing.psInfo : Tracing.psNull;

	// Consumers

	private static final Consumer<Synset> synsetConsumer = psi::println;
	private static final Consumer<Sense> senseConsumer = psi::println;
	private static final Consumer<Index> indexConsumer = psi::println;

	/**
	 * Parse all
	 *
	 * @param dir            WNDB dir
	 * @param synsetConsumer synset consumer
	 * @param senseConsumer  sense  consumer
	 * @param indexConsumer  index  consumer
	 * @throws IOException        io exception
	 * @throws ParsePojoException parse pojo exception
	 */
	public static void parseAll(final File dir, final Consumer<Synset> synsetConsumer, final Consumer<Sense> senseConsumer, final Consumer<Index> indexConsumer) throws IOException, ParsePojoException
	{
		DataParser.parseAllSynsets(dir, synsetConsumer);
		IndexParser.parseAllIndexes(dir, indexConsumer);
		SenseParser.parseSenses(dir, senseConsumer);
	}

	/**
	 * Main
	 *
	 * @param args command-line arguments
	 * @throws ParsePojoException parse pojo exception
	 * @throws IOException        io exception
	 */
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
}
