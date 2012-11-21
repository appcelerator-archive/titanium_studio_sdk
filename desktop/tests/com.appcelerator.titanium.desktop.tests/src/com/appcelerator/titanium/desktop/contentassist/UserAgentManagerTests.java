/**
 * Copyright 2011-2012 Appcelerator, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.appcelerator.titanium.desktop.contentassist;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import com.aptana.core.IUserAgent;
import com.aptana.core.util.ResourceUtil;
import com.aptana.editor.common.contentassist.UserAgentManager;

/**
 * UserAgentManagerTests
 */
public class UserAgentManagerTests extends TestCase
{
	private static final String UNKNOWN_NATURE_ID = "some.other.unknown.nature.id";
	private UserAgentManager manager;

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		manager = UserAgentManager.getInstance();

		restoreDefaultUserAgents();
	}

	private void restoreDefaultUserAgents()
	{
		// Set defaults for all Aptana natures
		for (String natureID : ResourceUtil.getAptanaNaturesMap().values())
		{
			String[] userAgentIDs = manager.getDefaultUserAgentIDs(natureID);

			manager.setActiveUserAgents(natureID, userAgentIDs);
		}

		// Add one unknown nature
		String[] userAgentIDs = manager.getDefaultUserAgentIDs(UNKNOWN_NATURE_ID);

		manager.setActiveUserAgents(UNKNOWN_NATURE_ID, userAgentIDs);
	}

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception
	{
		manager = null;

		super.tearDown();
	}

	public void assertIDs(String natureID, String... expectedUserAgentIDs)
	{
		IUserAgent[] userAgents = manager.getActiveUserAgents(natureID);

		assertNotNull(userAgents);

		// create expected set
		Set<String> expected = new HashSet<String>();
		for (String id : expectedUserAgentIDs)
		{
			expected.add(id);
		}

		// create actual set
		Set<String> actual = new HashSet<String>();
		for (IUserAgent userAgent : userAgents)
		{
			actual.add(userAgent.getID());
		}

		assertEquals(expected, actual);
	}

	public void testTitaniumDesktopDefaults()
	{
		// @formatter:off
		assertIDs(
			"com.appcelerator.titanium.desktop.nature",
			"Safari"
		);
		// @formatter:on
	}
}
