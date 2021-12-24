package org.oewntk.parse;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.pojos.ParsePojoException;
import org.oewntk.utils.Tracing;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class TestParse31
{
	private static final PrintStream ps = !System.getProperties().containsKey("SILENT") ? Tracing.psInfo : Tracing.psNull;

	private static final String wnHome = System.getProperty("SOURCE31");

	private static final File wnDir = new File(wnHome);

	@BeforeClass
	public static void init()
	{
		if (wnHome == null)
		{
			Tracing.psErr.println("Define WNDB source dir with -DSOURCE31=path%n");
			Tracing.psErr.println("When running Maven tests, define a WNHOME31 environment variable that points to WordNet 3.1 dict directory.");
			Assert.fail();
		}
		Tracing.psInfo.printf("source=%s%n", wnDir.getAbsolutePath());
		if (!wnDir.exists())
		{
			Tracing.psErr.println("Define WNDB source dir that exists");
			Assert.fail();
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
