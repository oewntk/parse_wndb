/*
 * Copyright (c) 2021. Bernard Bou.
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
        for (domain in Domain.entries) {
            val domainStr = domain.domain
            ps.printf("%d %s %s %s %s%n", domain.id, domain.name2, domain.pos, domainStr, parseDomain(domainStr))
        }
    }

    @Test
    @Throws(ParsePojoException::class)
    fun testDomainsIds() {
        for (domain in Domain.entries) {
            val domainId = domain.id.toString()
            val domain2 = parseDomainId(domainId)
            ps.printf("%s -> %s %s%n", domainId, domain2.name2, domain2.pos)
        }
    }

    companion object {

        private val ps = if (!System.getProperties().containsKey("SILENT")) Tracing.psInfo else Tracing.psNull
    }
}
