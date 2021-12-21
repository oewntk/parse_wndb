/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.parse;

import org.oewntk.pojos.CoreIndex;
import org.oewntk.pojos.ParsePojoException;
import org.oewntk.utils.Tracing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Parse word index in [arg1=noun|verb|adj|adv] part-of-speech containing target [arg2=string]
 *
 * @author Bernard Bou
 */
public class IndexParser1
{
	public static void read(final String dir, final String posName, final String target) throws IOException
	{
		final File file = new File(dir, "index." + posName);
		try (final BufferedReader reader = new BufferedReader(new FileReader(file)))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				if (line.contains(target))
				{
					Tracing.psInfo.println(line);
					CoreIndex index;
					try
					{
						index = parseIndexLine(line);
						Tracing.psInfo.println(index);
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

	private static CoreIndex parseIndexLine(final String line) throws ParsePojoException
	{
		return CoreIndex.parseCoreIndex(line);
	}

	public static void main(final String[] args) throws IOException
	{
		// Input
		String dir = args[0];
		String posName = args[1];
		String target = args[2];

		// Process
		read(dir, posName, target);
	}
}
