/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.parse

import org.oewntk.pojos.CoreIndex
import org.oewntk.pojos.CoreIndex.Companion.parseCoreIndex
import org.oewntk.pojos.ParsePojoException
import org.oewntk.utils.Tracing
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

/**
 * Parse word index in [arg1=noun|verb|adj|adv] part-of-speech containing target [arg2=string]
 *
 * @author Bernard Bou
 */
object IndexParser1 {

	/**
	 * Read lines for target
	 *
	 * @param dir     WNDB dir
	 * @param posName pos
	 * @param target  target for line to include to be selected
	 * @throws IOException io exception
	 */
	@Throws(IOException::class)
	fun read(dir: String?, posName: String, target: String?) {
		val file = File(dir, "index.$posName")
		BufferedReader(FileReader(file)).use { reader ->
			var line: String
			while ((reader.readLine().also { line = it }) != null) {
				if (line.contains(target!!)) {
					Tracing.psInfo.println(line)
					var index: CoreIndex
					try {
						index = parseIndexLine(line)
						Tracing.psInfo.println(index)
					} catch (e: ParsePojoException) {
						Tracing.psErr.printf("%s cause:%s%n", e.message, e.cause)
						e.printStackTrace(Tracing.psErr)
					}
				}
			}
		}
	}

	/**
	 * Parse index line
	 *
	 * @param line line
	 * @return core index
	 * @throws ParsePojoException parse pojo exception
	 */
	@Throws(ParsePojoException::class)
	private fun parseIndexLine(line: String): CoreIndex {
		return parseCoreIndex(line)
	}

	/**
	 * Main
	 *
	 * @param args command-line arguments
	 * @throws IOException io exception
	 */
	@Throws(IOException::class)
	@JvmStatic
	fun main(args: Array<String>) {
		// Input
		val dir = args[0]
		val posName = args[1]
		val target = args[2]

		// Process
		read(dir, posName, target)
	}
}
