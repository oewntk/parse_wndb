/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.parse

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * Flags
 */
object Flags {
	/**
	 * Character set for output files.
	 */
	@JvmField
	val charSet: Charset = StandardCharsets.UTF_8
}
