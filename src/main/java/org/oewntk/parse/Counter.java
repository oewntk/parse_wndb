/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.parse;

import org.oewntk.pojos.*;
import org.oewntk.utils.Tracing;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.function.Consumer;

public class Counter
{

	// PrintStreams
	private static final PrintStream psi = !System.getProperties().containsKey("SILENT") ? Tracing.psInfo : Tracing.psNull;

	private long synsetCount;
	private long senseCount;
	private long indexCount;
	private long relationCount;
	private long synsetRelationCount;
	private long senseRelationCount;

	private final Consumer<Synset> synsetConsumer = synset -> {
		synsetCount++;
		Relation[] relations = synset.getRelations();
		relationCount += relations.length;
		Arrays.stream(relations).forEach(r -> {
			if (r instanceof LexRelation)
			{
				senseRelationCount++;
			}
			else
			{
				synsetRelationCount++;
			}
		});
	};
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
		relationCount = 0;
		synsetRelationCount = 0;
		senseRelationCount = 0;
	}

	public void parseAll() throws IOException, ParsePojoException
	{
		long rSynsetCount = DataParser.parseAllSynsets(dir, synsetConsumer);
		long rIndexCount = IndexParser.parseAllIndexes(dir, indexConsumer);
		long rSenseCount = SenseParser.parseSenses(dir, senseConsumer);
		assert rSynsetCount == synsetCount;
		assert rSenseCount == senseCount;
		assert rIndexCount == indexCount;
		Tracing.psInfo.printf("%s synsets:%d senses:%d indexes:%d relations:%d synset_relations:%d sense_relations:%d%n", dir, synsetCount, senseCount, indexCount, relationCount, synsetRelationCount, senseRelationCount);
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
