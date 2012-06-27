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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.appcelerator.titanium.desktop.DesktopPlugin;
import com.aptana.core.logging.IdeLog;

class Release
{

	/**
	 * Preference keys used to store releases inside the project preferences.
	 */
	private static final String APP_PAGE_PREF_KEY = "app_page"; //$NON-NLS-1$
	private static final String PUBDATE_PREF_KEY = "pubdate"; //$NON-NLS-1$
	private static final String VERSION_PREF_KEY = "app_version"; //$NON-NLS-1$
	private static final String PLATFORM_PREF_KEY = "platform"; //$NON-NLS-1$
	private static final String LABEL_PREF_KEY = "label"; //$NON-NLS-1$
	private static final String URL_PREF_KEY = "url"; //$NON-NLS-1$

	/**
	 * Nodename for the parent of all the release packages stored in prefs for the project.
	 */
	private static final String PACKAGES_PREF_NODE = "packages"; //$NON-NLS-1$

	private String url;
	private String label;
	private String version;
	private String platform;
	private String pubDate;
	private String appPage;

	public Release(String url, String label, String platform, String version, String pubDate, String appPage)
	{
		this.url = url;
		this.label = label;
		this.version = version;
		this.platform = platform;
		this.pubDate = pubDate;
		this.appPage = appPage;
	}

	public String getURL()
	{
		return url;
	}

	public String getLabel()
	{
		return label;
	}

	public String getVersion()
	{
		return version;
	}

	public String getPlatform()
	{
		return platform;
	}

	public String getPubDate()
	{
		return pubDate;
	}

	public String getAppPage()
	{
		return appPage;
	}

	/**
	 * Given the JSON response from the Cloud service, store the result in the preferences (the releases, the pubDate,
	 * the public link).
	 * 
	 * @param project
	 * @param json
	 */
	public static void updateForProject(IProject project, JSONObject json)
	{
		final JSONArray releases = (JSONArray) json.get("releases"); //$NON-NLS-1$
		final String appPage = (String) json.get(APP_PAGE_PREF_KEY);
		final String pubDate = (String) json.get(PUBDATE_PREF_KEY);

		// delete current rows
		deletePackagesForProject(project);

		// insert new rows
		for (Object r : releases)
		{
			String label = (String) ((JSONObject) r).get(LABEL_PREF_KEY);
			String url = (String) ((JSONObject) r).get(URL_PREF_KEY);
			String platform = (String) ((JSONObject) r).get(PLATFORM_PREF_KEY);
			String version = (String) ((JSONObject) r).get(VERSION_PREF_KEY);
			addPackageToDatabase(project, url, label, platform, version, pubDate, appPage);
		}
	}

	private static void addPackageToDatabase(IProject project, String url, String label, String platform,
			String version, String pubDate, String appPage)
	{
		ProjectScope scope = new ProjectScope(project);
		try
		{
			Preferences p = scope.getNode(DesktopPlugin.PLUGIN_ID).node(PACKAGES_PREF_NODE);
			p = p.node(label);
			p.put(URL_PREF_KEY, url);
			p.put(LABEL_PREF_KEY, label);
			p.put(PLATFORM_PREF_KEY, platform);
			p.put(VERSION_PREF_KEY, version);
			p.put(PUBDATE_PREF_KEY, pubDate);
			p.put(APP_PAGE_PREF_KEY, appPage);
			p.flush();
		}
		catch (BackingStoreException e)
		{
			IdeLog.logError(DesktopPlugin.getDefault(), e);
		}
	}

	private static void deletePackagesForProject(IProject project)
	{
		ProjectScope scope = new ProjectScope(project);
		try
		{
			scope.getNode(DesktopPlugin.PLUGIN_ID).node(PACKAGES_PREF_NODE).removeNode();
		}
		catch (BackingStoreException e)
		{
			IdeLog.logError(DesktopPlugin.getDefault(), e);
		}
	}

	/**
	 * Load the saved/stored releases for the project (from prefs).
	 * 
	 * @param project
	 * @return
	 */
	public static List<Release> load(IProject project)
	{
		List<Release> releases = new ArrayList<Release>();
		ProjectScope scope = new ProjectScope(project);
		try
		{
			Preferences p = scope.getNode(DesktopPlugin.PLUGIN_ID);
			if (p == null)
			{
				return releases;
			}
			p = p.node(PACKAGES_PREF_NODE);
			if (p == null)
			{
				return releases;
			}
			for (String childName : p.childrenNames())
			{
				Preferences releasePref = p.node(childName);
				releases.add(new Release(releasePref.get(URL_PREF_KEY, null), releasePref.get(LABEL_PREF_KEY, null),
						releasePref.get(PLATFORM_PREF_KEY, null), releasePref.get(VERSION_PREF_KEY, null), releasePref
								.get(PUBDATE_PREF_KEY, null), releasePref.get(APP_PAGE_PREF_KEY, null)));
			}
		}
		catch (BackingStoreException e)
		{
			IdeLog.logError(DesktopPlugin.getDefault(), e);
		}
		return releases;
	}
}
