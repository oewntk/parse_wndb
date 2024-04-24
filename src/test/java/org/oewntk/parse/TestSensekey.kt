package org.oewntk.parse;

import org.junit.Test;
import org.oewntk.pojos.ParsePojoException;
import org.oewntk.pojos.Sensekey;
import org.oewntk.pojos.Type;
import org.oewntk.utils.Tracing;

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
				assertEquals(6, decoded.length);
				assertEquals(lemma, decoded[0]);
				assertEquals(String.format("%01d", POS), decoded[1]);
				assertEquals(String.format("%02d", LEXFILE), decoded[2]);
				assertEquals(String.format("%02d", LEXID), decoded[3]);
				assertEquals(head, decoded[4]);
				if (head.isEmpty())
				{
					assertEquals("", decoded[5]);
				}
				else
				{
					assertEquals(String.format("%02d", HEADID), decoded[5]);
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
		assertEquals("go to the dogs", sk1.word.toString());
		assertEquals("go to the dogs", sk1.getLemma().toString());
		assertEquals(Type.VERB, sk1.type);
		assertEquals("verb.change", sk1.domain.getDomain());
		assertNull(sk1.headWord);
		assertEquals(-1, sk1.headLexId);

		Sensekey sk2 = Sensekey.parseSensekey(sensekeys[1]);
		assertNotNull(sk2);
		assertEquals("half-size", sk2.word.toString());
		assertEquals("half-size", sk2.getLemma().toString());
		assertEquals(Type.ADJSAT, sk2.type);
		assertEquals("adj.all", sk2.domain.getDomain());
		assert sk2.headWord != null;
		assertEquals("small", sk2.headWord.toString());
		assertEquals(0, sk2.headLexId);

		for (int i = 2; i < sensekeys.length; i++)
		{
			Sensekey sk = Sensekey.parseSensekey(sensekeys[i]);
			assertNotNull(sk);
			ps.println(sk.word);
		}
	}

	@Test
	public void sk_parse2() throws ParsePojoException
	{
		String sensekey = "100~%1:10:00::";

		Sensekey sk1 = Sensekey.parseSensekey(sensekey);
		assertNotNull(sk1);
		assertEquals("100%", sk1.word.toString());
		assertEquals("100%", sk1.getLemma().toString());
		ps.println(sk1.word);
	}

	private static String generate(String lemma, @SuppressWarnings("SameParameterValue") int pos, @SuppressWarnings("SameParameterValue") int lexfile, @SuppressWarnings("SameParameterValue") int lexid, String head, @SuppressWarnings("SameParameterValue") int headid)
	{
		final String lexsense = String.format("%01d:%02d:%02d", pos, lexfile, lexid);
		final String headidStr = head.isEmpty() ? "" : String.format("%02d", headid);
		return lemma.replace("%", Sensekey.ESCAPED_PERCENT) + '%' + lexsense + ':' + head.replace("%", Sensekey.ESCAPED_PERCENT) + ':' + headidStr;
	}
}
