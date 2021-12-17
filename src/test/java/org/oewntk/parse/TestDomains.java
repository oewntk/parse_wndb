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
		for (Domain domain : Domain.values())
		{
			String domainStr = domain.getDomain();
			ps.printf("%d %s %s %s %s%n", domain.getId(), domain.getName(), domain.getPos(), domainStr, Domain.parseDomain(domainStr));
		}
	}

	@Test
	public void testDomainsIds() throws ParsePojoException
	{
		for (Domain domain : Domain.values())
		{
			String domainId = Integer.toString(domain.getId());
			Domain domain2 = Domain.parseDomainId(domainId);
			ps.printf("%s -> %s %s%n", domainId, domain2.getName(), domain2.getPos());
		}
	}
}
