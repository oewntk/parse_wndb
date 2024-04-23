/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.parse

import org.oewntk.pojos.ParsePojoException
import org.oewntk.pojos.Synset
import org.oewntk.utils.Tracing
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.util.function.Consumer

/**
 * Synset Parser data.{noun|verb|adj|adv}
 *
 * @author Bernard Bou
 */
object DataParser {

	private const val THROW = false

	// PrintStreams
	private val psl = Tracing.psNull
	private val psi = if (System.getProperties().containsKey("VERBOSE")) Tracing.psInfo else Tracing.psNull
	private val pse = if (!System.getProperties().containsKey("SILENT")) Tracing.psErr else Tracing.psNull

	// Consumer
	private val consumer = Consumer<Synset> { psi.println(it) }

	/**
	 * Parse all synsets
	 *
	 * @param dir      WNDB dir
	 * @param consumer synset consumer
	 * @return synset count
	 * @throws IOException        io exception
	 * @throws ParsePojoException parse pojo exception
	 */
	@JvmStatic
	@Throws(IOException::class, ParsePojoException::class)
	fun parseAllSynsets(dir: File?, consumer: Consumer<Synset>): Long {
		var count: Long = 0
		for (posName in arrayOf("noun", "verb", "adj", "adv")) {
			count += parseSynsets(dir, posName, consumer)
		}
		return count
	}

	/**
	 * Parse synsets
	 *
	 * @param dir      WNDB dir
	 * @param posName  pos
	 * @param consumer synset consumer
	 * @return synset count
	 * @throws ParsePojoException parse pojo exception
	 * @throws IOException        io exception
	 */
	@Throws(ParsePojoException::class, IOException::class)
	fun parseSynsets(dir: File?, posName: String, consumer: Consumer<Synset>): Long {
		psl.println("* Synsets $posName")

		val isAdj = posName == "adj"

		// iterate on lines
		val file = File(dir, "data.$posName")
		RandomAccessFile(file, "r").use { raFile ->
			raFile.seek(0)
			// iterate on lines
			var lineCount = 0
			var nonCommentCount = 0
			var offsetErrorCount = 0
			var parseErrorCount = 0
			var synsetCount: Long = 0

			var rawLine: String
			var fileOffset = raFile.filePointer
			while ((raFile.readLine().also { rawLine = it }) != null) {
				lineCount++
				if (rawLine.isEmpty() || rawLine[0] == ' ') {
					fileOffset = raFile.filePointer
					continue
				}

				// decode
				val line = String(rawLine.toByteArray(Flags.charSet))
				nonCommentCount++

				// split into fields
				val lineFields = line.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

				// read offset
				val readOffset = lineFields[0].toLong()
				if (fileOffset != readOffset) {
					pse.printf("Offset: data.%s:%d offset=%08d line=[%s]%n", posName, lineCount, fileOffset, line)
					offsetErrorCount++
					fileOffset = raFile.filePointer
					continue
				}

				// read
				try {
					val synset = parseSynsetLine(line, isAdj)
					synsetCount++
					consumer.accept(synset)
				} catch (e: ParsePojoException) {
					parseErrorCount++
					pse.printf("%n%s:%d offset=%08d line=[%s] except=%s", file.name, lineCount, fileOffset, line, e.message)
					if (THROW) {
						throw e
					}
				}
				fileOffset = raFile.filePointer
			}
			val format = "%-50s %d%n"
			psl.printf(format, "lines", nonCommentCount)
			psl.printf(format, "parse successes", synsetCount)
			(if (offsetErrorCount > 0) pse else psl).printf(format, "offset errors", offsetErrorCount)
			(if (parseErrorCount > 0) pse else psl).printf(format, "parse errors", parseErrorCount)
			return synsetCount
		}
	}

	/**
	 * Parse line
	 *
	 * @param line  line
	 * @param isAdj whether the pos is adj
	 * @return synset
	 * @throws ParsePojoException parse pojo exception
	 */
	@Throws(ParsePojoException::class)
	private fun parseSynsetLine(line: String, isAdj: Boolean): Synset {
		return Synset.parseSynsetLine(line, isAdj)
	}

	/**
	 * Main
	 *
	 * @param args command-line arguments
	 * @throws ParsePojoException parse pojo exception
	 * @throws IOException        io exception
	 */
	@Throws(ParsePojoException::class, IOException::class)
	@JvmStatic
	fun main(args: Array<String>) {
		// Timing
		val startTime = System.currentTimeMillis()

		// Input
		val dir = File(args[0])

		// Process
		parseAllSynsets(dir, consumer)

		// Timing
		val endTime = System.currentTimeMillis()
		psl.println("Total execution time: " + (endTime - startTime) / 1000 + "s")
	}
}
