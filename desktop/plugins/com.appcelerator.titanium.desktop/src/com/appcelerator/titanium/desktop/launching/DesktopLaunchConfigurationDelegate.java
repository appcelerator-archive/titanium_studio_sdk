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
package com.appcelerator.titanium.desktop.launching;

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IProcess;

import com.appcelerator.titanium.core.TitaniumCorePlugin;
import com.appcelerator.titanium.core.TitaniumProject;
import com.appcelerator.titanium.core.TitaniumSDKTools;
import com.appcelerator.titanium.core.launching.TitaniumLaunchConfigurationUtil;
import com.appcelerator.titanium.core.launching.TitaniumSingleProjectLaunchConfigurationDelegate;
import com.appcelerator.titanium.core.mobile.SDKEntity;
import com.appcelerator.titanium.desktop.DesktopPlugin;
import com.appcelerator.titanium.desktop.DesktopUsageUtil;
import com.appcelerator.titanium.desktop.TitaniumDesktopSDKTools;
import com.aptana.core.util.StringUtil;

/**
 * @author Max Stepanov
 */
public class DesktopLaunchConfigurationDelegate extends TitaniumSingleProjectLaunchConfigurationDelegate
{

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(org.eclipse.debug.core.ILaunchConfiguration,
	 * java.lang.String, org.eclipse.debug.core.ILaunch, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException
	{
		IProject project = TitaniumLaunchConfigurationUtil.getConfigurationProject(configuration);
		IPath destination = project.getLocation().append("dist").append(TitaniumDesktopSDKTools.getPlatform()); //$NON-NLS-1$
		if (!destination.toFile().exists())
		{
			if (!destination.toFile().mkdirs() && !destination.toFile().isDirectory())
			{
				throw new CoreException(new Status(IStatus.ERROR, DesktopPlugin.PLUGIN_ID,
						Messages.DesktopLaunchConfigurationDelegate_CreateDirectoryFiled_Error));
			}
		}
		SDKEntity sdkEntity = TitaniumDesktopSDKTools.getProjectSDK(project);
		boolean debug = ILaunchManager.DEBUG_MODE.equals(mode);
		if (debug && TitaniumCorePlugin.getDefault().hasDebuggerTurnedOff())
		{
			throw new CoreException(new Status(IStatus.ERROR, TitaniumCorePlugin.PLUGIN_ID,
					TitaniumLaunchConfigurationUtil.SUBSCRIPTION_ERROR_CODE, StringUtil.EMPTY, null));
		}
		DesktopUsageUtil.sendLaunchEvent(sdkEntity.getVersion(), getTitaniumProject(project));
		try
		{
			Process osProcess = TitaniumSDKTools.run(TitaniumDesktopSDKTools.getDesktopToolPath(project).toOSString(),
					"-d", //$NON-NLS-1$
					destination.toOSString(), "-a", //$NON-NLS-1$
					sdkEntity.getPath().toOSString(), "-n", //$NON-NLS-1$
					"-r", //$NON-NLS-1$
					"-v", //$NON-NLS-1$
					"-s", //$NON-NLS-1$
					sdkEntity.getPath().removeLastSegments(3).toOSString(), project.getLocation().toOSString());
			IProcess process = DebugPlugin.newProcess(launch, osProcess, project.getName());
			launch.addProcess(process);
		}
		catch (IOException e)
		{
			throw new CoreException(new Status(IStatus.ERROR, DesktopPlugin.PLUGIN_ID,
					Messages.DesktopLaunchConfigurationDelegate_LaunchProcessFailed_Error, e));
		}
	}

	protected TitaniumProject getTitaniumProject(IProject project)
	{
		return TitaniumCorePlugin.getDefault().getTitaniumProjectFactory().create(project);
	}
}
