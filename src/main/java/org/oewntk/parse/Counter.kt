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
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 * Counts
 */
public class Counter
{
	// PrintStreams

	private static final PrintStream psi = !System.getProperties().containsKey("SILENT") ? Tracing.psInfo : Tracing.psNull;

	// Counts

	private long synsetCount;
	private long senseCount;
	private long indexCount;
	private long relationCount;
	private long synsetRelationCount;
	private long senseRelationCount;
	private final Map<String, Long> synsetRelationByTypeCount = new TreeMap<>();
	private final Map<String, Long> senseRelationByTypeCount = new TreeMap<>();
	private long verbFrameCount;
	private long verbFrameMultiSensesCount;
	private long verbFrameSingleSensesCount;

	// Consumers

	private final Consumer<Synset> synsetConsumer = synset -> {
		synsetCount++;

		// relations
		Relation[] relations = synset.relations;
		relationCount += relations.length;
		Arrays.stream(relations).forEach(r -> {
			var type = r.type.toString();
			if (r instanceof LexRelation)
			{
				senseRelationCount++;
				var val = senseRelationByTypeCount.computeIfAbsent(type, k -> 0L);
				senseRelationByTypeCount.put(type, ++val);
			}
			else
			{
				synsetRelationCount++;
				var val = synsetRelationByTypeCount.computeIfAbsent(type, k -> 0L);
				synsetRelationByTypeCount.put(type, ++val);
			}
		});

		// verb frames
		VerbFrameRef[] verbFrames = synset.getVerbFrames();
		if (verbFrames != null)
		{
			verbFrameCount += verbFrames.length;
			for (VerbFrameRef verbFrameRef : verbFrames)
			{
				if (verbFrameRef.lemmas.length > 1)
				{
					verbFrameMultiSensesCount += verbFrameRef.lemmas.length;
				}
				else
				{
					verbFrameSingleSensesCount += verbFrameRef.lemmas.length;
				}
			}
		}
	};

	private final Consumer<Sense> senseConsumer = sense -> senseCount++;

	private final Consumer<Index> indexConsumer = index -> indexCount++;

	// Source

	private final File dir;

	/**
	 * Constructor
	 *
	 * @param dir WNDB dir
	 */
	Counter(final File dir)
	{
		this.dir = dir;
		synsetCount = 0;
		senseCount = 0;
		indexCount = 0;
		relationCount = 0;
		synsetRelationCount = 0;
		senseRelationCount = 0;
		verbFrameCount = 0;
		verbFrameMultiSensesCount = 0;
		verbFrameSingleSensesCount = 0;
	}

	/**
	 * Parse all
	 *
	 * @return this
	 * @throws IOException        io exception
	 * @throws ParsePojoException parse pojo exception
	 */
	public Counter parseAll() throws IOException, ParsePojoException
	{
		long rSynsetCount = DataParser.parseAllSynsets(dir, synsetConsumer);
		long rIndexCount = IndexParser.parseAllIndexes(dir, indexConsumer);
		long rSenseCount = SenseParser.parseSenses(dir, senseConsumer);
		assert rSynsetCount == synsetCount;
		assert rSenseCount == senseCount;
		assert rIndexCount == indexCount;
		return this;
	}

	/**
	 * Report
	 *
	 * @return this
	 */
	public Counter reportCounts()
	{
		Tracing.psInfo.printf("%s synsets:%d senses:%d indexes:%d relations:%d synset_relations:%d sense_relations:%d%n", dir, synsetCount, senseCount, indexCount, relationCount, synsetRelationCount, senseRelationCount);
		return this;
	}

	/**
	 * Report
	 *
	 * @return this
	 */
	public Counter reportVerbFrameCounts()
	{
		Tracing.psInfo.printf("verbframes: %d single_senses: %d multi_senses: %d senses: %d%n", verbFrameCount, verbFrameMultiSensesCount, verbFrameSingleSensesCount, verbFrameMultiSensesCount + verbFrameSingleSensesCount);
		return this;
	}

	/**
	 * Report relations
	 *
	 * @return this
	 */
	@SuppressWarnings("UnusedReturnValue")
	public Counter reportRelationCounts()
	{
		long[] countSum = new long[2];

		Tracing.psInfo.printf("synset relations: %s%n", synsetRelationCount);
		synsetRelationByTypeCount.forEach((r, c) -> {
			Tracing.psInfo.printf("\t%s: %d%n", r, c);
			countSum[0] += c;
		});
		assert synsetRelationCount == countSum[0];

		Tracing.psInfo.printf("sense relations: %s%n", senseRelationCount);
		senseRelationByTypeCount.forEach((r, c) -> {
			Tracing.psInfo.printf("\t%s: %d%n", r, c);
			countSum[1] += c;
		});
		assert senseRelationCount == countSum[1];

		return this;
	}

	/**
	 * Main
	 *
	 * @param args args
	 * @throws IOException        io exception
	 * @throws ParsePojoException parse pojo exception
	 */
	public static void main(String[] args) throws IOException, ParsePojoException
	{
		// Timing
		final long startTime = System.currentTimeMillis();

		// Input
		for (String arg : args)
		{
			File dir = new File(arg);
			new Counter(dir).parseAll() //
					.reportCounts() //
					.reportRelationCounts() //
					.reportVerbFrameCounts();
		}

		// Timing
		final long endTime = System.currentTimeMillis();
		psi.println("Total execution time: " + (endTime - startTime) / 1000 + "s");
	}
}
