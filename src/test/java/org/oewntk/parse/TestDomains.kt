/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.parse

import org.junit.Test
import org.oewntk.pojos.Domain
import org.oewntk.pojos.Domain.Companion.parseDomain
import org.oewntk.pojos.Domain.Companion.parseDomainId
import org.oewntk.pojos.ParsePojoException
import org.oewntk.utils.Tracing

class TestDomains {

    @Test
    @Throws(ParsePojoException::class)
    fun testDomains() {
        Domain.entries.forEach {
            ps.println("${it.id} ${it.name2} ${it.pos} ${it.domain} ${parseDomain(it.domain)}")
        }
    }

    @Test
    @Throws(ParsePojoException::class)
    fun testDomainsIds() {
        Domain.entries.forEach {
            val domain2 = parseDomainId(it.domain)
            ps.println("${it.id} -> ${domain2.name2} ${domain2.pos}")
        }
    }

    companion object {

        private val ps = if (!System.getProperties().containsKey("SILENT")) Tracing.psInfo else Tracing.psNull
    }
}
