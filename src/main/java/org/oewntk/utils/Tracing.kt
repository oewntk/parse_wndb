/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.utils

import java.io.OutputStream
import java.io.PrintStream

object Tracing {

    val psNull: PrintStream = PrintStream(object : OutputStream() {
        override fun write(i: Int) {
            // do nothing
        }
    })

    val verbose = System.getProperties().containsKey("VERBOSE") || !System.getProperties().containsKey("SILENT")

    val psInfo: PrintStream = System.out

    var ps: PrintStream = if (verbose) System.out else psNull

    var psServ: PrintStream = if (verbose) System.out else psNull

    val psErr: PrintStream = System.err
}
