package org.oewntk.parse

import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.parse.DataParser.parseAllSynsets
import org.oewntk.parse.IndexParser.parseAllIndexes
import org.oewntk.parse.Parser.parseAll
import org.oewntk.parse.SenseParser.parseSenses
import org.oewntk.pojos.Index
import org.oewntk.pojos.ParsePojoException
import org.oewntk.pojos.Sense
import org.oewntk.pojos.Synset
import org.oewntk.utils.Tracing
import java.io.File
import java.io.IOException

class TestParseXX {
	@Test
	@Throws(IOException::class, ParsePojoException::class)
	fun parse() {
		parseAll(wnDir, { x: Synset? -> ps.println(x) }, { x: Sense? -> ps.println(x) }, { x: Index? -> ps.println(x) })
	}

	@Test
	@Throws(IOException::class, ParsePojoException::class)
	fun indexParse() {
		parseAllIndexes(wnDir) { x: Index? -> ps.println(x) }
	}

	@Test
	@Throws(IOException::class, ParsePojoException::class)
	fun dataParse() {
		parseAllSynsets(wnDir) { x: Synset? -> ps.println(x) }
	}

	@Test
	@Throws(IOException::class, ParsePojoException::class)
	fun senseParse() {
		parseSenses(wnDir) { x: Sense? -> ps.println(x) }
	}

	companion object {
		private val ps = if (!System.getProperties().containsKey("SILENT")) Tracing.psInfo else Tracing.psNull

		private val wnHome: String? = System.getProperty("SOURCEXX")

		private val wnDir = wnHome?.let { File(it) }

		@JvmStatic
		@BeforeClass
		fun init() {
			if (wnHome == null) {
				Tracing.psErr.println("Define WNDB source dir with -DSOURCEXX=path%n")
				Tracing.psErr.println("When running Maven tests, define a WNHOMEXX_compat environment variable that points to WordNet 2021 compat dict directory.")
				Assert.fail()
			}
			Tracing.psInfo.printf("source=%s%n", wnDir?.absolutePath ?: "null")
			if (!wnDir!!.exists()) {
				Tracing.psErr.println("Define WNDB source dir that exists")
				Assert.fail()
			}
		}
	}
}
