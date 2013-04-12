/**
 * Copyright 2012-2013 Appcelerator, Inc.
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

import com.appcelerator.titanium.core.TitaniumProject;
import com.appcelerator.titanium.core.mobile.SDKEntity;
import com.appcelerator.titanium.core.mobile.SDKLocator;
import com.appcelerator.titanium.core.platform.IPlatformTypeConfigurator;
import com.appcelerator.titanium.core.tiapp.ITiAppModel;
import com.appcelerator.titanium.core.tiapp.TiAppReconciler;
import com.appcelerator.titanium.core.tiapp.TiManifestModel;
import com.appcelerator.titanium.core.tiapp.TiManifestModel.MODULE;
import com.aptana.core.util.StringUtil;

public class DesktopPlatformConfigurator implements IPlatformTypeConfigurator
{

	public String getProjectNatureId()
	{
		return DesktopProjectNature.ID;
	}

	public List<SDKEntity> getAvailableSDKs()
	{
		return getSDKLocator().getAvailable();
	}

	public SDKLocator getSDKLocator()
	{
		return TitaniumDesktopSDKLocator.getInstance();
	}

	public SDKEntity getSDK(String version)
	{
		return getSDKLocator().findVersion(version);
	}

	public SDKEntity getProjectSDK(IProject project) throws CoreException
	{
		return TitaniumDesktopSDKTools.getProjectSDK(project);
	}

	public void postProjectImport(IProject project, TitaniumProject tiProject, IProgressMonitor monitor)
			throws CoreException
	{
		// In case it's a desktop project, just call the TiAppReconciler to verify that we have the required elements.
		ITiAppModel reconciledModel = new TiAppReconciler(project, getSDKLocator().getAvailable(), getSDKLocator()
				.getLatestVersion()).reconcile(monitor);
		// We might have to update the manifest here after the reconcile, as it may change due to SDK compatibilities.
		// We do it here, and not in the TiAppReconciler, for dependency reasons (Desktop-specific code that cannot be
		// accessed from the core plugin).
		String sdkVersion = reconciledModel.getSDKVersion();
		if (!StringUtil.isEmpty(sdkVersion))
		{
			DesktopSDKEntity sdkEntity = (DesktopSDKEntity) getSDKLocator().findVersion(sdkVersion);
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
