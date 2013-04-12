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

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.appcelerator.titanium.core.TitaniumConstants;
import com.appcelerator.titanium.core.TitaniumCorePlugin;
import com.appcelerator.titanium.core.TitaniumProject;
import com.appcelerator.titanium.core.tiapp.TiManifestModel.MODULE;
import com.appcelerator.titanium.desktop.TitaniumDesktopSDKTools;

/**
 * A model to wrap the "timanifest" file generated when packaging a desktop app. Nopt to be confused with
 * TimanifestModel which wraps the "manifest" file in a Titanium project.
 * 
 * @author cwilliams
 */
class TiManifest
{

	/**
	 * Name of the underlying file.
	 */
	static final String FILENAME = "timanifest"; //$NON-NLS-1$

	/**
	 * Constants for the target platforms
	 */
	static final String WINDOWS_PLATFORM = "win32"; //$NON-NLS-1$
	static final String LINUX_PLATFORM = "linux"; //$NON-NLS-1$
	static final String MAC_PLATFORM = "osx"; //$NON-NLS-1$

	// Keys used in JSON payload of file...
	/**
	 * Special JSON flag to tell if Titanium installer splash screen should be shown on app's first run.
	 */
	public static final String NO_INSTALL = "noinstall"; //$NON-NLS-1$
	static final String PLATFORMS = "platforms"; //$NON-NLS-1$
	static final String PACKAGE = "package"; //$NON-NLS-1$
	static final String RUNTIME = "runtime"; //$NON-NLS-1$
	static final String APPNAME = "appname"; //$NON-NLS-1$
	static final String APPID = "appid"; //$NON-NLS-1$
	static final String APPVERSION = "appversion"; //$NON-NLS-1$
	static final String MID = "mid"; //$NON-NLS-1$
	static final String PUBLISHER = "publisher"; //$NON-NLS-1$
	static final String URL = "url"; //$NON-NLS-1$
	static final String DESC = "desc"; //$NON-NLS-1$
	static final String RELEASE = "release"; //$NON-NLS-1$
	static final String GUID = "guid"; //$NON-NLS-1$
	static final String VISIBILITY = "visibility"; //$NON-NLS-1$

	private IProject project;

	TiManifest(IProject project)
	{
		this.project = project;
	}

	/**
	 * Writes the timanifest file based ont he passed in arguments and the existing metadata for the project. Does no
	 * validation!
	 * 
	 * @param networkRuntime
	 *            "network" || "include"
	 * @param release
	 *            Release to users?
	 * @param visibility
	 *            "public" || "private"
	 * @param showSplash
	 *            - Should the install splash screen be shown on first run of app? true || false
	 */
	@SuppressWarnings("unchecked")
	IStatus write(Set<String> platforms, String networkRuntime, boolean release, String visibility, boolean showSplash,
			IProgressMonitor monitor)
	{
		SubMonitor sub = SubMonitor.convert(monitor, 5);

		JSONObject timanifest = new JSONObject();
		TitaniumProject tiProj = getTitaniumProject();
		timanifest.put(APPNAME, tiProj.getAppName());
		timanifest.put(APPID, tiProj.getAppID());
		timanifest.put(APPVERSION, tiProj.getVersion());
		timanifest.put(MID, getMID());
		timanifest.put(PUBLISHER, tiProj.getPublisher());
		timanifest.put(URL, tiProj.getURL());
		timanifest.put(DESC, tiProj.getDescription());
		timanifest.put(RELEASE, release);
		timanifest.put(NO_INSTALL, !showSplash);
		timanifest.put(GUID, getGUID());
		timanifest.put(VISIBILITY, visibility);
		JSONObject runtime = new JSONObject();
		try
		{
			runtime.put("version", getProjectSDKVersion()); //$NON-NLS-1$
		}
		catch (CoreException e)
		{
			return e.getStatus();
		}
		runtime.put(PACKAGE, networkRuntime);
		timanifest.put(RUNTIME, runtime);

		if (tiProj.getImage() != null)
		{
			// FIXME What should we do here?
			// var image = TFS.getFile(project.image);
			// timanifest.image = image.name();
			timanifest.put("image", tiProj.getImage()); //$NON-NLS-1$
		}
		sub.worked(1);

		// OS options
		JSONArray array = new JSONArray();
		array.addAll(platforms);
		timanifest.put(PLATFORMS, array);

		if (TitaniumConstants.DESKTOP_TYPE.equals(tiProj.getType()))
		{
			JSONArray modules = new JSONArray();

			Map<MODULE, String> moduleMap = tiProj.getModules();
			for (Map.Entry<MODULE, String> entry : moduleMap.entrySet())
			{
				JSONObject m = new JSONObject();
				m.put("name", entry.getKey().getKey()); //$NON-NLS-1$
				m.put("version", entry.getValue()); //$NON-NLS-1$
				m.put(PACKAGE, networkRuntime);
				modules.add(m);
			}
			timanifest.put("modules", modules); //$NON-NLS-1$
		}
		else
		{
			timanifest.put("package_target", "test"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		sub.worked(1);

		IFile file = project.getFile(FILENAME);
		try
		{
			if (!file.exists())
			{
				file.create(new ByteArrayInputStream(timanifest.toJSONString().getBytes()), IResource.FORCE,
						sub.newChild(1));
			}
			else
			{
				file.setContents(new ByteArrayInputStream(timanifest.toJSONString().getBytes()), IResource.FORCE,
						sub.newChild(1));
			}
		}
		catch (CoreException e)
		{
			return e.getStatus();
		}

		sub.done();

		return Status.OK_STATUS;
	}

	protected String getMID()
	{
		return TitaniumCorePlugin.getMID();
	}

	protected String getProjectSDKVersion() throws CoreException
	{
		return TitaniumDesktopSDKTools.getProjectSDK(project).getVersion();
	}

	String getGUID()
	{
		return getTitaniumProject().getGUID();
	}

	protected TitaniumProject getTitaniumProject()
	{
		return TitaniumCorePlugin.getDefault().getTitaniumProjectFactory().create(project);
	}
}
