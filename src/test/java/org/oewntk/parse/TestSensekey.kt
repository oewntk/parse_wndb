package org.oewntk.parse

import org.junit.Assert
import org.junit.Test
import org.oewntk.pojos.ParsePojoException
import org.oewntk.pojos.Sensekey
import org.oewntk.pojos.Sensekey.Companion.decode
import org.oewntk.pojos.Sensekey.Companion.parseSensekey
import org.oewntk.pojos.Type
import org.oewntk.utils.Tracing
import java.util.*

class TestSensekey {

    @Test
    fun sk_escape() {
        val heads: List<String> = mutableListOf("", *lemmas)

        for (lemma in lemmas) {
            for (head in heads) {
                val sensekey = generate(lemma, POS, LEXFILE, LEXID, head, HEADID)
                ps.printf("%s%n", sensekey)

                val decoded = decode(sensekey)
                Assert.assertEquals(6, decoded.size.toLong())
                Assert.assertEquals(lemma, decoded[0])
                Assert.assertEquals(String.format("%01d", POS), decoded[1])
                Assert.assertEquals(String.format("%02d", LEXFILE), decoded[2])
                Assert.assertEquals(String.format("%02d", LEXID), decoded[3])
                Assert.assertEquals(head, decoded[4])
                if (head.isEmpty()) {
                    Assert.assertEquals("", decoded[5])
                } else {
                    Assert.assertEquals(String.format("%02d", HEADID), decoded[5])
                }
            }
        }
    }

    @Test
    @Throws(ParsePojoException::class)
    fun sk_parse() {
        val sensekeys = arrayOf(
            "go_to_the_dogs%2:30:00::",
            "half-size%5:00:00:small:00",
            "Yahoo!%1:10:00::",
            "Prince_William,_Duke_of_Cumberland%1:18:00::",
            "Capital:_Critique_of_Political_Economy%1:10:00::",
            "Hawai'i%1:15:00::",
            "Hawai'i_Volcanoes_National_Park%1:15:00::",
            "20/20%1:09:00::",
            "TCP/IP%1:10:00::"
        )

        val sk1 = parseSensekey(sensekeys[0])
        Assert.assertNotNull(sk1)
        Assert.assertEquals("go to the dogs", sk1!!.word.toString())
        Assert.assertEquals("go to the dogs", sk1.lemma.toString())
        Assert.assertEquals(Type.VERB, sk1.type)
        Assert.assertEquals("verb.change", sk1.domain.domain)
        Assert.assertNull(sk1.headWord)
        Assert.assertEquals(-1, sk1.headLexId.toLong())

        val sk2 = parseSensekey(sensekeys[1])
        Assert.assertNotNull(sk2)
        Assert.assertEquals("half-size", sk2!!.word.toString())
        Assert.assertEquals("half-size", sk2.lemma.toString())
        Assert.assertEquals(Type.ADJSAT, sk2.type)
        Assert.assertEquals("adj.all", sk2.domain.domain)
        checkNotNull(sk2.headWord)
        Assert.assertEquals("small", sk2.headWord.toString())
        Assert.assertEquals(0, sk2.headLexId.toLong())

        for (i in 2 until sensekeys.size) {
            val sk = parseSensekey(sensekeys[i])
            Assert.assertNotNull(sk)
            ps.println(sk!!.word)
        }
    }

    @Test
    @Throws(ParsePojoException::class)
    fun sk_parse2() {
        val sensekey = "100~%1:10:00::"

        val sk1 = parseSensekey(sensekey)
        Assert.assertNotNull(sk1)
        Assert.assertEquals("100%", sk1!!.word.toString())
        Assert.assertEquals("100%", sk1.lemma.toString())
        ps.println(sk1.word)
    }

    companion object {

        private val ps = if (!System.getProperties().containsKey("SILENT")) Tracing.psInfo else Tracing.psNull

        private const val POS = 1
        private const val LEXFILE = 22
        private const val LEXID = 33
        private const val HEADID = 44
        private val lemmas = arrayOf("one", "two%three", "two%%three", "two%%%%three", "two%%%%%three", "two%", "%three", "two%%", "%%three", "normal", "two:three", "two::three", "two::::three", "two:::::three", "two:", ":three", "two::", "::three")

        private fun generate(lemma: String, pos: Int, lexfile: Int, lexid: Int, head: String, headid: Int): String {
            val lexsense = String.format("%01d:%02d:%02d", pos, lexfile, lexid)
            val headidStr = if (head.isEmpty()) "" else String.format("%02d", headid)
            return lemma.replace("%", Sensekey.ESCAPED_PERCENT) + '%' + lexsense + ':' + head.replace("%", Sensekey.ESCAPED_PERCENT) + ':' + headidStr
        }
    }
}
