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
package com.appcelerator.titanium.desktop.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.appcelerator.titanium.desktop.contentassist.UserAgentManagerTests;
import com.appcelerator.titanium.desktop.ui.wizard.TiManifestTest;

public class AllTests
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for com.appcelerator.titanium.desktop.tests"); //$NON-NLS-1$
		// $JUnit-BEGIN$
		suite.addTestSuite(TitaniumDesktopPackagingTests.class);
		suite.addTestSuite(DesktopTiAppReconcilingTests.class);
		suite.addTestSuite(TiManifestTest.class);
		suite.addTestSuite(UserAgentManagerTests.class);
		// $JUnit-END$
		return suite;
	}
}
