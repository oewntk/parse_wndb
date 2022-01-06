/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.parse;

import org.oewntk.pojos.ParsePojoException;
import org.oewntk.pojos.Synset;
import org.oewntk.utils.Tracing;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Parse synset in [arg1=noun|verb|adj|adv] part-of-speech at offset [arg2=num]
 *
 * @author Bernard Bou
 */
public class DataParser1
{
	/**
	 * Read line at given offset
	 *
	 * @param dir        WNDB dir
	 * @param posName    pos
	 * @param fileOffset file offset
	 * @return line
	 * @throws IOException io exception
	 */
	public static String read(final String dir, final String posName, final long fileOffset) throws IOException
	{
		final File file = new File(dir, "data." + posName);
		try (final RandomAccessFile raFile = new RandomAccessFile(file, "r"))
		{
			raFile.seek(fileOffset);
			String rawString = raFile.readLine();
			return new String(rawString.getBytes(Flags.charSet));
		}
	}

	/**
	 * Parse synset from line
	 *
	 * @param line  line
	 * @param isAdj whether adjs are being processed
	 * @return synset
	 * @throws ParsePojoException parse pojo exception
	 */
	public static Synset parseSynset(String line, boolean isAdj) throws ParsePojoException
	{
		return Synset.parseSynsetLine(line, isAdj);
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
		// Input
		String dir = args[0];
		String posName = args[1];
		long fileOffset = Long.parseLong(args[2]);
		final boolean isAdj = posName.equals("adj");

		// Process
		String line = read(dir, posName, fileOffset);
		Tracing.psInfo.println(line);
		Synset synset = parseSynset(line, isAdj);
		Tracing.psInfo.println(synset.toPrettyString());
	}
}
