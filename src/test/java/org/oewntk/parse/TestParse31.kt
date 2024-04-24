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

class TestParse31 {

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

		private val wnHome: String? = System.getProperty("SOURCE31")

		private val wnDir = wnHome?.let { File(it) }

		@JvmStatic
		@BeforeClass
		fun init() {
			if (wnHome == null) {
				Tracing.psErr.println("Define WNDB source dir with -DSOURCE31=path%n")
				Tracing.psErr.println("When running Maven tests, define a WNHOME31 environment variable that points to WordNet 3.1 dict directory.")
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
