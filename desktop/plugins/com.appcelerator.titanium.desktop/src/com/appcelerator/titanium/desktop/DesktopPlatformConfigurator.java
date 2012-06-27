/**
 * Copyright 2012 Appcelerator, Inc.
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

import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.appcelerator.titanium.core.ITiAppModel;
import com.appcelerator.titanium.core.SDKEntity;
import com.appcelerator.titanium.core.SDKLocator;
import com.appcelerator.titanium.core.TiAppReconciler;
import com.appcelerator.titanium.core.TitaniumProject;
import com.appcelerator.titanium.core.platform.IPlatformTypeConfigurator;
import com.appcelerator.titanium.core.tiapp.TiAppModelUtil;
import com.appcelerator.titanium.core.tiapp.TiManifestModel;
import com.appcelerator.titanium.core.tiapp.TiManifestModel.MODULE;
import com.aptana.core.util.StringUtil;

public class DesktopPlatformConfigurator implements IPlatformTypeConfigurator
{

	private TitaniumDesktopSDKLocator sdkLocator;

	public String getProjectNatureId()
	{
		return DesktopProjectNature.ID;
	}

	public List<SDKEntity> getAvailableSDKs()
	{
		return TitaniumDesktopSDKLocator.getAvailable();
	}

	public SDKLocator getSDKLocator()
	{
		if (sdkLocator == null)
		{
			sdkLocator = new TitaniumDesktopSDKLocator();
		}
		return sdkLocator;
	}

	public SDKEntity getSDK(String version)
	{
		return TitaniumDesktopSDKLocator.findVersion(version);
	}

	public SDKEntity getProjectSDK(IProject project) throws CoreException
	{
		return TitaniumDesktopSDKTools.getProjectSDK(project);
	}

	public void postProjectImport(IProject project, TitaniumProject tiProject, IProgressMonitor monitor)
			throws CoreException
	{
		// In case it's a desktop project, just call the TiAppReconciler to verify that we have the required elements.
		ITiAppModel reconciledModel = new TiAppReconciler(project, TitaniumDesktopSDKLocator.getAvailable(),
				TitaniumDesktopSDKLocator.getLatestVersion()).reconcile(monitor);
		// We might have to update the manifest here after the reconcile, as it may change due to SDK compatibilities.
		// We do it here, and not in the TiAppReconciler, for dependency reasons (Desktop-specific code that cannot be
		// accessed from the core plugin).
		String sdkVersion = TiAppModelUtil.getSDKVersion(reconciledModel);
		if (!StringUtil.isEmpty(sdkVersion))
		{
			DesktopSDKEntity sdkEntity = (DesktopSDKEntity) TitaniumDesktopSDKLocator.findVersion(sdkVersion);
			if (sdkEntity != null)
			{
				TitaniumDesktopManifestUtil.updateSDKVersion(tiProject.getTiManifest(), sdkVersion, sdkEntity);
			}
		}
	}

	public void onTiAppSDKSave(String sdkVersion, TiManifestModel manifestModel, Set<MODULE> mdoules)
	{
		if (manifestModel != null)
		{
			DesktopSDKEntity sdkEntity = (DesktopSDKEntity) getSDK(sdkVersion);
			TitaniumDesktopManifestUtil.updateSDKVersion(manifestModel, sdkVersion, sdkEntity, mdoules);
		}
	}
}
