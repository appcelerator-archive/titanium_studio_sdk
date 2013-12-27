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

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

import com.appcelerator.titanium.core.TitaniumCorePlugin;
import com.appcelerator.titanium.core.user.ITitaniumUser;
import com.appcelerator.titanium.desktop.ui.wizard.Packager;
import com.aptana.core.util.ResourceUtil;

@SuppressWarnings("nls")
public class TitaniumDesktopPackagingTests extends TestCase
{
	private static final String USERNAME = "unit_tests@aptana.com";
	private static final String USERNAME2 = "allen@aptana.com";
	private static final String PASSWORD = "unittest";
	private static final String BUNDLE_ID = "com.appcelerator.titanium.desktop.tests";
	private static final String DESKTOP_PROJECT_DIR = "resources/desktopPackageTest";

	@Override
	protected void tearDown() throws Exception
	{
		try
		{

			TitaniumCorePlugin.getDefault().getUserManager().signOut();
		}
		finally
		{
			super.tearDown();
		}
	}

	public void testDistributeDesktopProject() throws Exception
	{
		ITitaniumUser user = signIn(USERNAME, PASSWORD);
		if (!user.isOnline())
		{
			user.setOnline(true);
		}

		assertEquals("Failed to package project", Status.OK_STATUS, packageProject());
	}

	public void testDistributeWithInactiveUser() throws Exception
	{
		// App was created with unit_tests@aptana.com, but we are signing in with allen@aptana.com (This should fail)
		ITitaniumUser user = signIn(USERNAME2, PASSWORD);
		if (!user.isOnline())
		{
			user.setOnline(true);
		}
		// FIXME Shouldn't server return a 403 as a better status?
		assertEquals("Expected error code 400", 400, packageProject().getCode());

	}

	protected ITitaniumUser signIn(String username, String password) throws Exception
	{
		TitaniumCorePlugin.getDefault().getUserManager().signIn(username, password);
		ITitaniumUser user = TitaniumCorePlugin.getDefault().getUserManager().getSignedInUser();
		assertNotNull("Current user is null, cannot package desktop project without a valid user.", user);
		return user;
	}

	public void testDistributeWhileOffline() throws CoreException
	{
		assertEquals("Expected error message \"{'offline':true}\"", "{'offline':true}", packageProject().getMessage());
	}

	private IStatus packageProject() throws CoreException
	{
		// Open a titanium desktop project, and setup distribute fields
		String projectDir = ResourceUtil.resourcePathToString(Platform.getBundle(BUNDLE_ID).getEntry(
				DESKTOP_PROJECT_DIR));
		IPath projectDirPath = Path.fromOSString(projectDir);
		IProgressMonitor monitor = new NullProgressMonitor();

		IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription(
				projectDirPath.append(IProjectDescription.DESCRIPTION_FILE_NAME));
		description.setLocation(projectDirPath);

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
		project.create(description, monitor);

		Set<String> platforms = new HashSet<String>();
		platforms.add(Packager.WINDOWS_PLATFORM);
		platforms.add(Packager.MAC_PLATFORM);

		// create a new packager and call distribute
		IStatus status = new Packager().distribute(project, platforms, Packager.NETWORK_RUNTIME, false,
				Packager.PRIVATE_VISIBILITY, false, monitor);

		project.close(monitor);
		project.delete(false, false, monitor);

		return status;
	}
}
