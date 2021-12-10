/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.parse;

import java.io.OutputStream;
import java.io.PrintStream;

public class Utils
{
	public static PrintStream nullPrintStream()
	{
		return new PrintStream(new OutputStream()
		{
			@Override
			public void write(final int i)
			{
				// do nothing
			}
		});
	}
}
