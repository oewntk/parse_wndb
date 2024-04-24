package org.oewntk.parse;

import org.junit.Test;
import org.oewntk.pojos.ParsePojoException;
import org.oewntk.pojos.Synset;
import org.oewntk.utils.Tracing;

import java.io.PrintStream;

import static org.junit.Assert.assertNotNull;

public class TestLine
{
	private static final PrintStream ps = !System.getProperties().containsKey("SILENT") ? Tracing.psInfo : Tracing.psNull;

	private static final String[] lines = { //
			"04643324 07 n 04 humor 1 humour 1 sense_of_humor 0 sense_of_humour 0 005 @ 04642395 n 0000 + 01248854 a 0101 + 10189454 n 0101 + 01248854 a 0202 + 10189454 n 0202 | the trait of appreciating (and being able to express) the humorous; \"she didn't appreciate my humor\" \"you can't survive in the army without a sense of humor\"", //
			"00965755 32 v 03 go_around 2 spread 2 circulate 2 005 $ 00964109 v 0000 @ 01828809 v 0000 >^ 00964109 v 0000 + 05081187 n 0302 + 07453315 n 0301 01 + 01 00 | become widely known and passed on; \"the rumor spread\" \"the story went around in the office\"", //
			"01373683 00 a 02 small 0 little 1 030 = 05092133 n 0000 & 01374416 s 0000 & 01374503 s 0000 & 01374590 s 0000 & 01374968 s 0000 & 01375233 s 0000 & 01375406 s 0000 & 01375530 s 0000 & 01375697 s 0000 & 01375781 s 0000 & 01375865 s 0000 & 01376055 s 0000 & 01376193 s 0000 & 01376345 s 0000 & 01376442 s 0000 & 01376545 s 0000 & 01376683 s 0000 & 01376765 s 0000 & 01376921 s 0000 & 01377105 s 0000 & 01377281 s 0000 & 01377385 s 0000 & 01377450 s 0000 & 01377581 s 0000 & 03120881 s 0000 + 05099811 n 0101 ! 01364543 a 0101 + 05103746 n 0201 + 05099811 n 0202 ! 01364543 a 0202 | limited or below average in number or quantity or magnitude or extent; \"a little dining room\" \"a little house\" \"a small car\" \"a little (or small) group\"", //
			"00389424 00 a 02 black-and-white 0 black_and_white(p) 0 002 ;c 00897860 n 0000 ! 00389232 a 0101 | not having or not capable of producing colors; \"black-and-white film\" \"a black-and-white TV\" \"the movie was in black and white\"", //
			"00003291 02 r 04 hardly 2 scarcely 2 barely 3 scarce 2 001 \\ 00016524 a 0401 | almost not; \"he hardly ever goes fishing\" \"he was scarce sixteen years old\" \"they scarcely ever used the emergency generator\" \"I can hardly hear what she is saying\" \"she barely seemed to notice him\" \"we were so far back in the theater, we could barely read the subtitles\"", //
	};

	@Test
	public void line() throws ParsePojoException
	{
		for (String line : lines)
		{
			ps.println(line);
			assertNotNull(line);
			Synset synset = Synset.parseSynsetLine(line, false);
			assertNotNull(synset);
		}
	}
}
