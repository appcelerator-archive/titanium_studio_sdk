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
package com.appcelerator.titanium.desktop.ui.wizard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.appcelerator.titanium.core.TitaniumCorePlugin;
import com.appcelerator.titanium.core.TitaniumProject;
import com.aptana.testing.utils.ProjectCreator;

@SuppressWarnings("deprecation")
public class TiManifestTest extends TestCase
{

	/**
	 * A TitaniumProject we can mock up for testing so we don't need an underlying tiapp.xml file.
	 * 
	 * @author cwilliams
	 */
	private static final class MockTitaniumProject extends TitaniumProject
	{
		private final String url;
		private final String appId;
		private final String appName;
		private final String guid;
		private final String version;
		private final String publisher;
		private final String description;

		private MockTitaniumProject(IProject project, String url, String appId, String appName, String guid,
				String version, String publisher, String description)
		{
			super(project);
			this.url = url;
			this.appId = appId;
			this.appName = appName;
			this.guid = guid;
			this.version = version;
			this.publisher = publisher;
			this.description = description;
		}

		@Override
		public synchronized String getAppID()
		{
			return appId;
		}

		@Override
		public synchronized String getAppName()
		{
			return appName;
		}

		@Override
		public String getVersion()
		{
			return version;
		}

		@Override
		public synchronized String getGUID()
		{
			return guid;
		}

		@Override
		public synchronized String getDescription()
		{
			return description;
		}

		@Override
		public synchronized String getPublisher()
		{
			return publisher;
		}

		@Override
		public synchronized String getURL()
		{
			return url;
		}
	}

	public void testWriteNewfile() throws Exception
	{
		final String appId = "com.appcelerator.test.app";
		final String appName = "testApp";
		final String guid = "1234567890";
		final String version = "1.0.3";
		final String description = "A fake app.";
		final String publisher = "Appcelerator, Inc.";
		final String url = "http://www.appcelerator.com";

		final IProject project = ProjectCreator.createAndOpen("testWriteNewTimanifest" + System.currentTimeMillis());

		Set<String> platforms = new HashSet<String>();
		platforms.add(Packager.WINDOWS_PLATFORM);
		platforms.add(Packager.MAC_PLATFORM);

		TiManifest timanifest = new TiManifest(project)
		{
			@Override
			protected synchronized TitaniumProject getTitaniumProject()
			{
				// Return our own special TitaniumProject object that returns expected values...
				return new MockTitaniumProject(project, url, appId, appName, guid, version, publisher, description);
			}

			@Override
			protected String getProjectSDKVersion() throws CoreException
			{
				return "1.7.0";
			}

			@Override
			protected String getMID()
			{
				// TODO Auto-generated method stub
				return super.getMID();
			}
		};
		IStatus result = timanifest.write(platforms, Packager.NETWORK_RUNTIME, false, Packager.PRIVATE_VISIBILITY,
				true, new NullProgressMonitor());
		assertTrue("result of writing timanifest was not OK: " + result.getMessage(), result.isOK());

		IFile tiManifest = project.getFile(TiManifest.FILENAME);
		File file = tiManifest.getLocation().toFile();
		assertTrue(MessageFormat.format("expected timanifest file ({0}) doesn''t exist", file.getAbsolutePath()),
				file.exists());
		// make sure it has the contents we expect
		assertTiManifestContents(file, appId, appName, guid, version, description, publisher, url, platforms,
				Packager.NETWORK_RUNTIME, false, Packager.PRIVATE_VISIBILITY, true);
	}

	// TODO Verify overwriting existing file
	// TODO Verify different values passed into write method produce proper results...

	protected void assertTiManifestContents(File file, final String appId, final String appName, final String guid,
			final String version, final String description, final String publisher, final String url,
			Set<String> platforms, String runtime, boolean release, String visibility, boolean showSplash)
			throws IOException, ParseException, FileNotFoundException
	{
		JSONParser parser = new JSONParser();
		JSONObject resultJSON = null;
		FileReader reader = null;
		try
		{
			reader = new FileReader(file);
			resultJSON = (JSONObject) parser.parse(reader);
		}
		finally
		{
			if (reader != null)
			{
				reader.close();
			}
		}
		assertNotNull(resultJSON);
		assertEquals("App ID didn't match", appId, resultJSON.get(TiManifest.APPID));
		assertEquals("App name didn't match", appName, resultJSON.get(TiManifest.APPNAME));
		assertEquals("App version didn't match", version, resultJSON.get(TiManifest.APPVERSION));
		assertEquals("App description didn't match", description, resultJSON.get(TiManifest.DESC));
		assertEquals("App GUID didn't match", guid, resultJSON.get(TiManifest.GUID));
		assertEquals("MID didn't match", TitaniumCorePlugin.getMID(), resultJSON.get(TiManifest.MID));
		assertEquals("'noinstall' value was incorrect", !showSplash, resultJSON.get(TiManifest.NO_INSTALL));
		JSONArray platformsArray = (JSONArray) resultJSON.get(TiManifest.PLATFORMS);
		assertEquals("'platforms' array size didn't match", platforms.size(), platformsArray.size());
		for (String platform : platforms)
		{
			assertTrue("'platforms' array didn't contain expected value: " + platform,
					platformsArray.contains(platform));
		}
		assertEquals("App publisher didn't match", publisher, resultJSON.get(TiManifest.PUBLISHER));
		assertEquals("'release' boolean didn't match", release, resultJSON.get(TiManifest.RELEASE));
		JSONObject runtimeObj = (JSONObject) resultJSON.get(TiManifest.RUNTIME);
		assertEquals("'runtime' didn't match", runtime, runtimeObj.get(TiManifest.PACKAGE));
		assertEquals("App URL didn't match", url, resultJSON.get(TiManifest.URL));
		assertEquals("Visibility is incorrect", visibility, resultJSON.get(TiManifest.VISIBILITY));
	}
}
