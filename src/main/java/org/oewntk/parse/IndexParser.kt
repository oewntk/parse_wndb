/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.parse

import org.oewntk.pojos.CoreIndex
import org.oewntk.pojos.CoreIndex.Companion.parseCoreIndex
import org.oewntk.pojos.Index
import org.oewntk.pojos.Index.Companion.parseIndex
import org.oewntk.pojos.ParsePojoException
import org.oewntk.utils.Tracing
import java.io.*
import java.util.function.Consumer

/**
 * Index parser index.{noun|verb|adj|adv}
 *
 * @author Bernard Bou
 */
object IndexParser {
	private const val THROW = false

	// PrintStreams
	private val psl = Tracing.psNull
	private val psi = if (System.getProperties().containsKey("VERBOSE")) Tracing.psInfo else Tracing.psNull
	private val pse = if (!System.getProperties().containsKey("SILENT")) Tracing.psErr else Tracing.psNull

	// Consumer
	private val consumer = Consumer<Index> { psi.println(it) }

	/**
	 * Parse all indexes
	 *
	 * @param dir      WNDB dir
	 * @param consumer index consumer
	 * @return index count
	 * @throws IOException        io exception
	 * @throws ParsePojoException parse pojo exception
	 */
	@JvmStatic
	@Throws(IOException::class, ParsePojoException::class)
	fun parseAllIndexes(dir: File?, consumer: Consumer<Index>): Long {
		var count: Long = 0
		for (posName in arrayOf("noun", "verb", "adj", "adv")) {
			count += parseIndexes(dir, posName, consumer)
		}
		return count
	}

	/**
	 * Parse all core indexes
	 *
	 * @param dir      WNDB dir
	 * @param consumer core index consumer
	 * @return index count
	 * @throws IOException        io exception
	 * @throws ParsePojoException parse pojo exception
	 */
	@Throws(IOException::class, ParsePojoException::class)
	fun parseAllCoreIndexes(dir: File?, consumer: Consumer<CoreIndex?>): Long {
		var count: Long = 0
		for (posName in arrayOf("noun", "verb", "adj", "adv")) {
			count += parseCoreIndexes(dir, posName, consumer)
		}
		return count
	}

	/**
	 * Parse indexes
	 *
	 * @param dir      WNDB dir
	 * @param posName  pos
	 * @param consumer index consumer
	 * @return index count
	 * @throws IOException        io exception
	 * @throws ParsePojoException parse pojo exception
	 */
	@Throws(IOException::class, ParsePojoException::class)
	fun parseIndexes(dir: File?, posName: String, consumer: Consumer<Index>): Long {
		psl.println("* Indexes $posName")

		// iterate on lines
		val file = File(dir, "index.$posName")
		BufferedReader(InputStreamReader(FileInputStream(file), Flags.charSet)).use { reader ->
			var lineCount = 0
			var nonCommentCount = 0
			var indexCount: Long = 0
			var parseErrorCount = 0
			var line: String
			while ((reader.readLine().also { line = it }) != null) {
				lineCount++
				if (line.isEmpty() || line[0] == ' ') {
					continue
				}
				nonCommentCount++

				try {
					val index = parseIndex(line)
					indexCount++
					consumer.accept(index)
				} catch (e: ParsePojoException) {
					parseErrorCount++
					pse.printf("%n%s:%d line=[%s] except=%s", file.name, lineCount, line, e)
					if (THROW) {
						throw e
					}
				}
			}
			val format = "%-50s %d%n"
			psl.printf(format, "lines", nonCommentCount)
			(if (parseErrorCount > 0) pse else psl).printf(format, "parse successes", indexCount)
			(if (parseErrorCount > 0) pse else psl).printf(format, "parse errors", parseErrorCount)
			return indexCount
		}
	}

	/**
	 * Parse core indexes
	 *
	 * @param dir      WNDB dir
	 * @param posName  pos
	 * @param consumer core index consumer
	 * @return index count
	 * @throws IOException        io exception
	 * @throws ParsePojoException parse pojo exception
	 */
	@Throws(IOException::class, ParsePojoException::class)
	fun parseCoreIndexes(dir: File?, posName: String, consumer: Consumer<CoreIndex?>): Long {
		psl.println("* Indexes $posName")

		// iterate on lines
		val file = File(dir, "index.$posName")
		BufferedReader(InputStreamReader(FileInputStream(file), Flags.charSet)).use { reader ->
			var lineCount = 0
			var nonCommentCount = 0
			var indexCount: Long = 0
			var parseErrorCount = 0
			var line: String
			while ((reader.readLine().also { line = it }) != null) {
				lineCount++
				if (line.isEmpty() || line[0] == ' ') {
					continue
				}
				nonCommentCount++

				try {
					val index = parseCoreIndex(line)
					indexCount++
					consumer.accept(index)
				} catch (e: ParsePojoException) {
					parseErrorCount++
					pse.printf("%n%s:%d line=[%s] except=%s", file.name, lineCount, line, e)
					if (THROW) {
						throw e
					}
				}
			}
			val format = "%-50s %d%n"
			psl.printf(format, "lines", nonCommentCount)
			(if (parseErrorCount > 0) pse else psl).printf(format, "parse successes", indexCount)
			(if (parseErrorCount > 0) pse else psl).printf(format, "parse errors", parseErrorCount)
			return indexCount
		}
	}

	/**
	 * Main
	 *
	 * @param args command-line arguments
	 * @throws ParsePojoException parse pojo exception
	 * @throws IOException        io exception
	 */
	@Throws(IOException::class, ParsePojoException::class)
	@JvmStatic
	fun main(args: Array<String>) {
		// Timing
		val startTime = System.currentTimeMillis()

		// Input
		val dir = File(args[0])

		// Process
		parseAllIndexes(dir, consumer)

		// Timing
		val endTime = System.currentTimeMillis()
		psl.println("Total execution time: " + (endTime - startTime) / 1000 + "s")
	}
}
