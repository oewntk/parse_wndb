package org.oewntk.parse;

import org.junit.Before;
import org.junit.Test;
import org.oewntk.pojos.ParsePojoException;

import java.io.File;
import java.io.IOException;

public class TestParse31
{
	private final String wnHome = System.getProperty("SOURCE31");

	@Before
	public void init()
	{
		if (wnHome == null)
		{
			System.err.println("Define WNDB source dir with -DSOURCE31=path%n");
			System.exit(1);
		}
		final File dir = new File(wnHome);
		System.out.printf("source=%s%n", dir.getAbsolutePath());
		if (!dir.exists())
		{
			System.err.println("Define WNDB source dir that exists");
			System.exit(2);
		}
	}

	@Test
	public void parse() throws IOException, ParsePojoException
	{
		Parser.main(new String[]{wnHome});
	}

	@Test
	public void indexParse() throws IOException, ParsePojoException
	{
		IndexParser.main(new String[]{wnHome});
	}

	@Test
	public void dataParse() throws IOException, ParsePojoException
	{
		DataParser.main(new String[]{wnHome});
	}

	@Test
	public void senseParse() throws IOException, ParsePojoException
	{
		SenseParser.main(new String[]{wnHome});
	}
}
