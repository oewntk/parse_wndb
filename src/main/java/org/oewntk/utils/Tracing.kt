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

    val psInfo: PrintStream = if (!System.getProperties().containsKey("SILENT")) System.out else psNull

    val psServ: PrintStream = if (!System.getProperties().containsKey("SILENT")) System.out else psNull

    val psErr: PrintStream = System.err
}
