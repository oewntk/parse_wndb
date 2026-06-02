/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.utils

import java.io.OutputStream
import java.io.PrintStream

object Tracing {

    val psNull: PrintStream = PrintStream(object : OutputStream(
    ) {
        override fun write(i: Int) {
            // do nothing
        }
    })

    val silent = if (System.getProperties().containsKey("VERBOSE")) false
    else if (System.getProperties().containsKey("SILENT")) true
    else true

    val psInfo: PrintStream = if (!silent) System.out else psNull

    val psServ: PrintStream = if (!silent) System.out else psNull

    val psErr: PrintStream = System.err
}
