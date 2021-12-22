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

public class Counter
{

	// PrintStreams
	private static final PrintStream psi = !System.getProperties().containsKey("SILENT") ? Tracing.psInfo : Tracing.psNull;

	private long synsetCount;
	private long senseCount;
	private long indexCount;

	private final Consumer<Synset> synsetConsumer = synset -> synsetCount++;
	private final Consumer<Sense> senseConsumer = sense -> senseCount++;
	private final Consumer<Index> indexConsumer = index -> indexCount++;

	private final File dir;

	// Consumers
	Counter(final File dir)
	{
		this.dir = dir;
		synsetCount = 0;
		senseCount = 0;
		indexCount = 0;
	}

	public void parseAll() throws IOException, ParsePojoException
	{
		long rSynsetCount = DataParser.parseAllSynsets(dir, synsetConsumer);
		long rIndexCount = IndexParser.parseAllIndexes(dir, indexConsumer);
		long rSenseCount =  SenseParser.parseSenses(dir, senseConsumer);
		assert rSynsetCount == synsetCount;
		assert rSenseCount == senseCount;
		assert rIndexCount == indexCount;
		Tracing.psInfo.printf("-----%n%s synsets:%d senses:%d indexes:%d%n%n", dir, synsetCount, senseCount, indexCount);
	}

	public static void main(String[] args) throws IOException, ParsePojoException
	{
		// Timing
		final long startTime = System.currentTimeMillis();

		// Input
		for (String arg : args)
		{
			File dir = new File(arg);
			new Counter(dir).parseAll();
		}

		// Timing
		final long endTime = System.currentTimeMillis();
		psi.println("Total execution time: " + (endTime - startTime) / 1000 + "s");
	}
}
