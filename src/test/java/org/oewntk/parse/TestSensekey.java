package org.oewntk.parse;

import org.junit.Test;
import org.oewntk.pojos.ParsePojoException;
import org.oewntk.pojos.Sensekey;
import org.oewntk.pojos.Type;
import org.oewntk.utils.Tracing;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestSensekey
{
	private static final PrintStream ps = !System.getProperties().containsKey("SILENT") ? Tracing.psInfo : Tracing.psNull;

	private static final int POS = 1;
	private static final int LEXFILE = 22;
	private static final int LEXID = 33;
	private static final int HEADID = 44;
	private static final String[] lemmas = {"one", "two%three", "two%%three", "two%%%%three", "two%%%%%three", "two%", "%three", "two%%", "%%three", "normal", "two:three", "two::three", "two::::three", "two:::::three", "two:", ":three", "two::", "::three"};

	@Test
	public void sk_escape()
	{
		List<String> heads = new ArrayList<>(Arrays.asList(lemmas));
		heads.add(0, "");

		for (String lemma : lemmas)
		{
			for (String head : heads)
			{
				String sensekey = generate(lemma, POS, LEXFILE, LEXID, head, HEADID);
				ps.printf("%s%n", sensekey);

				String[] decoded = Sensekey.decode(sensekey);
				assertEquals(decoded.length, 6);
				assertEquals(decoded[0], lemma);
				assertEquals(decoded[1], String.format("%01d", POS));
				assertEquals(decoded[2], String.format("%02d", LEXFILE));
				assertEquals(decoded[3], String.format("%02d", LEXID));
				assertEquals(decoded[4], head);
				if (head.isEmpty())
				{
					assertEquals(decoded[5], "");
				}
				else
				{
					assertEquals(decoded[5], String.format("%02d", HEADID));
				}
			}
		}
	}

	@Test
	public void sk_parse() throws ParsePojoException
	{
		String[] sensekeys = {"go_to_the_dogs%2:30:00::", "half-size%5:00:00:small:00", "Yahoo!%1:10:00::", "Prince_William,_Duke_of_Cumberland%1:18:00::", "Capital:_Critique_of_Political_Economy%1:10:00::", "Hawai'i%1:15:00::", "Hawai'i_Volcanoes_National_Park%1:15:00::", "20/20%1:09:00::", "TCP/IP%1:10:00::"};

		Sensekey sk1 = Sensekey.parseSensekey(sensekeys[0]);
		assertNotNull(sk1);
		assertEquals(sk1.getWord().toString(), "go to the dogs");
		assertEquals(sk1.getLemma().toString(), "go to the dogs");
		assertEquals(sk1.getType(), Type.VERB);
		assertEquals(sk1.getDomain().getDomain(), "change");
		assertNull(sk1.getHeadWord());
		assertEquals(sk1.getHeadLexId(), -1);

		Sensekey sk2 = Sensekey.parseSensekey(sensekeys[1]);
		assertNotNull(sk2);
		assertEquals(sk2.getWord().toString(), "half-size");
		assertEquals(sk2.getLemma().toString(), "half-size");
		assertEquals(sk2.getType(), Type.ADJSAT);
		assertEquals(sk2.getDomain().getDomain(), "all");
		assertEquals(sk2.getHeadWord().toString(), "small");
		assertEquals(sk2.getHeadLexId(), 0);

		for (int i = 2; i < sensekeys.length; i++)
		{
			Sensekey sk = Sensekey.parseSensekey(sensekeys[i]);
			assertNotNull(sk);
			ps.println(sk.getWord().toString());
		}
	}

	@Test
	public void sk_parse2() throws ParsePojoException
	{
		String sensekey = "100~%1:10:00::";

		Sensekey sk1 = Sensekey.parseSensekey(sensekey);
		assertNotNull(sk1);
		assertEquals(sk1.getWord().toString(), "100%");
		assertEquals(sk1.getLemma().toString(), "100%");
		ps.println(sk1.getWord().toString());
	}

	private static String generate(String lemma, @SuppressWarnings("SameParameterValue") int pos, @SuppressWarnings("SameParameterValue") int lexfile, @SuppressWarnings("SameParameterValue") int lexid, String head, @SuppressWarnings("SameParameterValue") int headid)
	{
		final String lexsense = String.format("%01d:%02d:%02d", pos, lexfile, lexid);
		final String headidStr = head.isEmpty() ? "" : String.format("%02d", headid);
		return lemma.replace("%", Sensekey.ESCAPED_PERCENT) + '%' + lexsense + ':' + head.replace("%", Sensekey.ESCAPED_PERCENT) + ':' + headidStr;
	}
}
