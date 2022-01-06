/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.parse;

import org.oewntk.pojos.ParsePojoException;
import org.oewntk.pojos.Sense;
import org.oewntk.utils.Tracing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Parse sense index for target [arg1=string]
 *
 * @author Bernard Bou
 */
public class SenseParser1
{
	/**
	 * Read lines for target
	 *
	 * @param dir    WNDB dir
	 * @param target target for lines to contain to be selected
	 * @throws IOException io exception
	 */
	static void read(final String dir, final String target) throws IOException
	{
		final File file = new File(dir, "index.sense");
		try (final BufferedReader reader = new BufferedReader(new FileReader(file)))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				if (line.contains(target))
				{
					Tracing.psInfo.println(line);
					Sense sense;
					try
					{
						sense = parseSenseLine(line);
						Tracing.psInfo.println(sense);
					}
					catch (ParsePojoException e)
					{
						Tracing.psErr.printf("%s cause:%s%n", e.getMessage(), e.getCause());
						e.printStackTrace(Tracing.psErr);
					}
				}
			}
		}
	}

	/**
	 * Parse sense line
	 *
	 * @param line line
	 * @return sense
	 * @throws ParsePojoException parse pojo exception
	 */
	private static Sense parseSenseLine(final String line) throws ParsePojoException
	{
		return Sense.parseSense(line);
	}

	/**
	 * Main
	 *
	 * @param args cmd-line args
	 * @throws IOException io exception
	 */
	public static void main(final String[] args) throws IOException
	{
		// Input
		String dir = args[0];
		String target = args[1];

		// Process
		read(dir, target);
	}
}
