/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.parse

import org.oewntk.pojos.ParsePojoException
import org.oewntk.pojos.Synset
import org.oewntk.pojos.Synset.Companion.parseSynsetLine
import org.oewntk.utils.Tracing
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile

/**
 * Parse synset in [arg1=noun|verb|adj|adv] part-of-speech at offset [arg2=num]
 *
 * @author Bernard Bou
 */
object DataParser1 {

    /**
     * Read line at given offset
     *
     * @param dir        WNDB dir
     * @param posName    pos
     * @param fileOffset file offset
     * @return line
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun read(dir: String, posName: String, fileOffset: Long): String? {
        val file = File(dir, "data.$posName")
        RandomAccessFile(file, "r").use { raFile ->
            raFile.seek(fileOffset)
            val rawString = raFile.readLine()
            return if (rawString == null) null else String(rawString.toByteArray(Flags.charSet))
        }
    }

    /**
     * Parse synset from line
     *
     * @param line  line
     * @param isAdj whether adjs are being processed
     * @return synset
     * @throws ParsePojoException parse pojo exception
     */
    @Throws(ParsePojoException::class)
    fun parseSynset(line: String, isAdj: Boolean): Synset {
        return parseSynsetLine(line, isAdj)
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
        // Input
        val dir = args[0]
        val posName = args[1]
        val fileOffset = args[2].toLong()
        val isAdj = posName == "adj"

        // Process
        val line = read(dir, posName, fileOffset)
        Tracing.psInfo.println(line)
        if (line != null) {
            val synset = parseSynset(line, isAdj)
            Tracing.psInfo.println(synset.toPrettyString())
        }
    }
}
