package org.oewntk.parse;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.pojos.ParsePojoException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class TestParse31
{
	private static final PrintStream ps = !System.getProperties().containsKey("SILENT") ? System.out : new PrintStream(new OutputStream()
	{
		public void write(int b)
		{
			//DO NOTHING
		}
	});

	private static final String wnHome = System.getProperty("SOURCE31");

	private static final File wnDir = new File(wnHome);

	@BeforeClass
	public static void init()
	{
		if (wnHome == null)
		{
			System.err.println("Define WNDB source dir with -DSOURCE31=path%n");
			System.exit(1);
		}
		System.out.printf("source=%s%n", wnDir.getAbsolutePath());
		if (!wnDir.exists())
		{
			System.err.println("Define WNDB source dir that exists");
			System.exit(2);
		}
	}

	@Test
	public void parse() throws IOException, ParsePojoException
	{
		Parser.parseAll(wnDir, ps::println, ps::println, ps::println);
	}

	@Test
	public void indexParse() throws IOException, ParsePojoException
	{
		IndexParser.parseAllIndexes(wnDir, ps::println);
	}

	@Test
	public void dataParse() throws IOException, ParsePojoException
	{
		DataParser.parseAllSynsets(wnDir, ps::println);
	}

	@Test
	public void senseParse() throws IOException, ParsePojoException
	{
		SenseParser.parseSenses(wnDir, ps::println);
	}
}
