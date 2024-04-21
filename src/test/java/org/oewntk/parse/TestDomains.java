/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.parse;

import org.junit.Test;
import org.oewntk.pojos.Domain;
import org.oewntk.pojos.ParsePojoException;
import org.oewntk.utils.Tracing;

import java.io.PrintStream;

public class TestDomains
{
	private static final PrintStream ps = !System.getProperties().containsKey("SILENT") ? Tracing.psInfo : Tracing.psNull;

	@Test
	public void testDomains() throws ParsePojoException
	{
		for (Domain domain : Domain.getEntries())
		{
			String domainStr = domain.getDomain();
			ps.printf("%d %s %s %s %s%n", domain.id, domain.getName2(), domain.pos, domainStr, Domain.parseDomain(domainStr));
		}
	}

	@Test
	public void testDomainsIds() throws ParsePojoException
	{
		for (Domain domain : Domain.getEntries())
		{
			String domainId = Integer.toString(domain.id);
			Domain domain2 = Domain.parseDomainId(domainId);
			ps.printf("%s -> %s %s%n", domainId, domain2.getName2(), domain2.pos);
		}
	}
}
