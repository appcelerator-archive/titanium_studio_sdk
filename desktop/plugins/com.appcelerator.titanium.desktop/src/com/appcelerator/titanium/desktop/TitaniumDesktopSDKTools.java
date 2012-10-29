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
package com.appcelerator.titanium.desktop;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;

import com.appcelerator.titanium.core.SDKEntity;
import com.appcelerator.titanium.core.TitaniumProject;
import com.appcelerator.titanium.desktop.preferences.IDesktopPreferenceConstants;
import com.aptana.core.logging.IdeLog;
import com.aptana.core.util.EclipseUtil;
import com.aptana.core.util.StringUtil;

/**
 * @author Max Stepanov
 */
public final class TitaniumDesktopSDKTools
{

	/**
	 * 
	 */
	private TitaniumDesktopSDKTools()
	{
	}

	/**
	 * Returns SDK tool path for the given project
	 * 
	 * @param project
	 * @return
	 * @throws CoreException
	 */
	public static IPath getDesktopToolPath(IProject project) throws CoreException
	{
		return getProjectSDK(project).getPath().append("tibuild.py"); //$NON-NLS-1$
	}

	public static String getPlatform()
	{
		if (Platform.OS_MACOSX.equals(Platform.getOS()))
		{
			return "osx"; //$NON-NLS-1$
		}
		return Platform.getOS();
	}

	/**
	 * Returns the SDK version assigned to the given project.
	 * 
	 * @param project
	 *            A project
	 * @return The SDK version stored in the preferences. In case the project is holding a non-existing SDK version, the
	 *         default SDK version is returned.
	 * @throws CoreException
	 *             In case there are no SDKs in the system
	 */
	@SuppressWarnings("deprecation")
	public static SDKEntity getProjectSDK(IProject project) throws CoreException
	{
		// We start by checking if we can grab the SDK version from the tiapp.xml.
		// If we can't, we fall back to the preferences (backward compatability)
		String sdk = (project != null) ? (new TitaniumProject(project)).getSDKVersion() : null;
		if (StringUtil.isEmpty(sdk))
		{
			IScopeContext[] lookupContexts;
			if (project != null)
			{
				lookupContexts = new IScopeContext[] { new ProjectScope(project), EclipseUtil.instanceScope(),
						EclipseUtil.defaultScope() };
			}
			else
			{
				lookupContexts = new IScopeContext[] { EclipseUtil.instanceScope(), EclipseUtil.defaultScope() };
			}
			sdk = Platform.getPreferencesService().getString(DesktopPlugin.PLUGIN_ID,
					IDesktopPreferenceConstants.DESKTOP_PROJECT_SDK_VERSION, null, lookupContexts);
		}
		SDKEntity sdkEntity = null;
		if (sdk != null)
		{
			sdkEntity = TitaniumDesktopSDKLocator.getInstance().findVersion(sdk);
		}
		if (sdkEntity == null)
		{
			sdkEntity = TitaniumDesktopSDKLocator.getInstance().getLatestVersion();
		}
		if (sdkEntity == null)
		{
			throw new CoreException(new Status(IStatus.ERROR, DesktopPlugin.PLUGIN_ID,
					Messages.TitaniumSDKTools_NoTitaniumSDKError));
		}
		return sdkEntity;
	}

	/**
	 * Sets the SDK version into the project's preferences.<br>
	 * In case the project or the version is null, nothing happens.
	 * 
	 * @param project
	 *            A project
	 * @param version
	 *            The SDK version
	 * @return True, in case the SDK version was successfully set.
	 * @deprecated We save the SDK version in the tiapp.xml, so this method is here now for backward compatibility
	 */
	public static boolean setProjectSDK(IProject project, SDKEntity version)
	{
		if (project != null && version != null)
		{
			IEclipsePreferences node = new ProjectScope(project).getNode(DesktopPlugin.PLUGIN_ID);
			node.put(IDesktopPreferenceConstants.DESKTOP_PROJECT_SDK_VERSION, version.getVersion());
			try
			{
				node.flush();
				return true;
			}
			catch (Exception e)
			{
				IdeLog.logError(DesktopPlugin.getDefault(), "Error saving the project's SDK version", e); //$NON-NLS-1$
			}
		}
		return false;
	}
}
