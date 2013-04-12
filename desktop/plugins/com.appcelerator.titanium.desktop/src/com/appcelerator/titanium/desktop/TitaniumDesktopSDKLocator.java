/**
 * Copyright 2011-2013 Appcelerator, Inc.
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
package com.appcelerator.titanium.desktop;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IScopeContext;

import com.appcelerator.titanium.core.TitaniumCorePlugin;
import com.appcelerator.titanium.core.mobile.SDKLocator;
import com.appcelerator.titanium.core.preferences.ITitaniumCorePreferencesConstants;
import com.appcelerator.titanium.core.tiapp.TiManifestModel.MODULE;
import com.aptana.core.util.CollectionsUtil;
import com.aptana.core.util.EclipseUtil;
import com.aptana.core.util.StringUtil;

/**
 * @author Max Stepanov
 */
public final class TitaniumDesktopSDKLocator extends SDKLocator
{

	private Map<String, List<String>> versionsToModules;
	private boolean watchLocation;

	public TitaniumDesktopSDKLocator()
	{
		this(getPreferencePath(), true);
	}

	public TitaniumDesktopSDKLocator(IPath sdkRoot, boolean watchLocation)
	{
		super(sdkRoot);
		this.watchLocation = watchLocation;
	}

	private static IPath getPreferencePath()
	{
		return Path.fromOSString(Platform.getPreferencesService().getString(TitaniumCorePlugin.PLUGIN_ID,
				ITitaniumCorePreferencesConstants.TITANIUM_SDK_PATH, StringUtil.EMPTY,
				new IScopeContext[] { EclipseUtil.instanceScope(), EclipseUtil.defaultScope() }));
	}

	/**
	 * @deprecated
	 * @return
	 */
	public static TitaniumDesktopSDKLocator getInstance()
	{
		return (TitaniumDesktopSDKLocator) TitaniumCorePlugin.getDefault().getSDKManager()
				.getInstance(TitaniumDesktopSDKLocator.class);
	}

	/**
	 * Initialize the SDK locator with a given SDK root path.
	 * 
	 * @param sdkRoot
	 */
	protected void initialize(IPath sdkRoot)
	{
		initializationStatus = Status.OK_STATUS;
		versionsToModules = null;
		if (sdkRoot.isEmpty())
		{
			initializationStatus = new Status(IStatus.WARNING, DesktopPlugin.PLUGIN_ID,
					SDKLocator.CONFIGURATION_WARNING, Messages.TitaniumDesktopSDKLocator_sdkRootNotSpecified, null);
			return;
		}
		if (!sdkRoot.toFile().isDirectory())
		{
			initializationStatus = new Status(IStatus.WARNING, DesktopPlugin.PLUGIN_ID, SDKLocator.CONFIGURATION_ERROR,
					MessageFormat.format(Messages.TitaniumDesktopSDKLocator_sdkRootPathNotDirectory,
							sdkRoot.toOSString()), null);
			return;
		}
		IPath desktopSdkRoot;
		IPath modulesRoot;
		IPath runtimeRoot;
		if (Platform.OS_MACOSX.equals(Platform.getOS()))
		{
			desktopSdkRoot = sdkRoot.append("sdk/osx"); //$NON-NLS-1$
			modulesRoot = sdkRoot.append("modules/osx"); //$NON-NLS-1$
			runtimeRoot = sdkRoot.append("runtime/osx"); //$NON-NLS-1$
		}
		else if (Platform.OS_WIN32.equals(Platform.getOS()))
		{
			desktopSdkRoot = sdkRoot.append("sdk\\win32"); //$NON-NLS-1$
			modulesRoot = sdkRoot.append("modules\\win32"); //$NON-NLS-1$
			runtimeRoot = sdkRoot.append("runtime\\win32"); //$NON-NLS-1$
		}
		else if (Platform.OS_LINUX.equals(Platform.getOS()))
		{
			desktopSdkRoot = sdkRoot.append("sdk/linux"); //$NON-NLS-1$
			modulesRoot = sdkRoot.append("modules/linux"); //$NON-NLS-1$
			runtimeRoot = sdkRoot.append("runtime/linux"); //$NON-NLS-1$
		}
		else
		{
			initializationStatus = new Status(IStatus.ERROR, DesktopPlugin.PLUGIN_ID, SDKLocator.COMPATIBILITY_ERROR,
					MessageFormat.format(Messages.TitaniumDesktopSDKLocator_unsupportedPlatform, Platform.getOS()),
					null);
			return;
		}
		if (!desktopSdkRoot.toFile().isDirectory())
		{
			initializationStatus = new Status(IStatus.WARNING, DesktopPlugin.PLUGIN_ID, SDKLocator.CONFIGURATION_ERROR,
					MessageFormat.format(Messages.TitaniumDesktopSDKLocator_mobileSDKRootNotDirectory,
							desktopSdkRoot.toOSString()), null);
			return;
		}

		// For each of the module directories under the root 'modules' directory, look inside to grab the versions and
		// create a map that maps from a version to the module name
		versionsToModules = new HashMap<String, List<String>>();

		FileFilter directoryFilter = new FileFilter()
		{
			public boolean accept(File pathname)
			{
				return pathname.isDirectory();
			}
		};
		FilenameFilter fileNameFilter = new FilenameFilter()
		{
			public boolean accept(File dir, String name)
			{
				return new File(dir, name).isDirectory();
			}
		};

		if (modulesRoot.toFile().isDirectory())
		{
			File[] moduleDirs = modulesRoot.toFile().listFiles(directoryFilter);
			for (File directory : moduleDirs)
			{
				File[] versions = directory.listFiles(directoryFilter);
				for (File version : versions)
				{
					updateVersionToModule(versionsToModules, version.getName(), directory.getName());
				}
			}
		}
		// Treat the 'runtime' as a module, even though it's in a different directory ("runtime" directory)
		if (runtimeRoot.toFile().isDirectory())
		{
			String[] runtimeVersions = runtimeRoot.toFile().list(fileNameFilter);
			for (String runtimeVersion : runtimeVersions)
			{
				updateVersionToModule(versionsToModules, runtimeVersion, MODULE.RUNTIME.getKey());
			}
		}

		String[] names = desktopSdkRoot.toFile().list(fileNameFilter);
		for (String name : names)
		{
			List<String> modules = versionsToModules.get(name);
			// Add only entities that have modules. Otherwise, they are invalid.
			if (!CollectionsUtil.isEmpty(modules))
			{
				addEntity(new DesktopSDKEntity(name, desktopSdkRoot.append(name), modules));
			}
		}
		if (watchLocation)
		{
			watchLocation(desktopSdkRoot, false);
			watchLocation(modulesRoot, false);
			watchLocation(runtimeRoot, false);
		}
	}

	private void updateVersionToModule(Map<String, List<String>> versionsToModules, String version, String moduleName)
	{
		List<String> modules = versionsToModules.get(version);
		if (modules == null)
		{
			modules = new ArrayList<String>();
			versionsToModules.put(version, modules);
		}
		modules.add(moduleName);
	}

	/**
	 * Returns <b>all</b> the Titanium Desktop Modules that were detected under the 'modules' directory of the SDK.<br>
	 * The returned map holds a Version-to-Modules list.
	 * 
	 * @return A read-only modules map.
	 */
	public Map<String, List<String>> getAvailableModules()
	{
		if (versionsToModules == null)
		{
			return Collections.emptyMap();
		}
		return Collections.unmodifiableMap(versionsToModules);
	}
}
