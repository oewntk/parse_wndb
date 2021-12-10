package org.oewntk.parse;

import org.junit.Before;
import org.junit.Test;
import org.oewntk.pojos.ParsePojoException;

import java.io.File;
import java.io.IOException;

public class TestParseXX
{
	private final String wnHome = System.getProperty("SOURCEXX");

	private final boolean silent = System.getProperties().containsKey("SILENT");

	@Before
	public void init()
	{
		if (wnHome == null)
		{
			System.err.println("Define WNDB source dir with -DSOURCEXX=path%n");
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
