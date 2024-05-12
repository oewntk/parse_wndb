/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.pojos

import org.oewntk.utils.Tracing
import java.util.*

/**
 * Gloss
 *
 * @param gloss raw gloss
 * @property splitGloss split gloss
 *
 * @author Bernard Bou
 */
class Gloss(
    gloss: String,
) {

    private val splitGloss: Array<String>

    init {
        this.splitGloss = split(gloss.trim { it <= ' ' })
    }

    /**
     * Parse gloss into fields
     *
     * @param gloss gloss
     * @return fields
     */
    private fun split(gloss: String): Array<String> {
        val pattern = Regex("\"[^\"]*\"").toPattern()

        var quoteCount = 0
        var p = 0
        while ((gloss.indexOf('"', p + 1).also { p = it }) != -1) {
            quoteCount++
        }
        if (quoteCount % 2 != 0) {
            Tracing.psErr.println("Uneven quotes in :$gloss")
        }

        val matcher = pattern.matcher(gloss) // get a matcher object
        var count = 0
        var split = -1
        while (matcher.find()) {
            if (count == 0) {
                split = matcher.start()
            }
            count++
        }

        var definition = if (split == -1) gloss else gloss.substring(0, split)
        definition = definition.replaceFirst("[;\\s]*$".toRegex(), "")
        matcher.reset()

        val result = Array(count + 1) {
            if (it == 0)
            // [0] definition
                definition
            else {
                // [1-n] samples
                matcher.find()
                var sample = matcher.group()
                if (sample.startsWith("\"") && sample.endsWith("\"")) {
                    sample = sample.substring(1, sample.length - 1)
                }
                sample
            }
        }
        return result
    }

    /**
     * Get definition
     *
     * @return definition
     */
    val definition: String
        get() = splitGloss[0]

    /**
     * Get samples
     *
     * @return samples
     */
    val samples: Array<String>
        get() = Arrays.copyOfRange(this.splitGloss, 1, splitGloss.size)

    /**
     * Get pretty string
     *
     * @return pretty string
     */
    fun toPrettyString(): String = splitGloss.joinToString(separator = ";\n\t", postfix = ";\n\t")

    override fun toString(): String = splitGloss.joinToString(separator = ";")
}
