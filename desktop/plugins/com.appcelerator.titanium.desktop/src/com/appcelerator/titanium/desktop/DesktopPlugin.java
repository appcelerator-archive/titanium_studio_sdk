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

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import com.appcelerator.titanium.core.TitaniumCorePlugin;
import com.appcelerator.titanium.core.preferences.ITitaniumCorePreferencesConstants;

public class DesktopPlugin extends Plugin
{

	// The plug-in ID
	public static final String PLUGIN_ID = "com.appcelerator.titanium.desktop"; //$NON-NLS-1$

	// The shared instance
	private static DesktopPlugin plugin;

	/**
	 * The constructor
	 */
	public DesktopPlugin()
	{
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
		plugin = this;

		TitaniumCorePlugin
				.getDefault()
				.getSDKManager()
				.registerPrefKey(TitaniumDesktopSDKLocator.class, TitaniumCorePlugin.PLUGIN_ID,
						ITitaniumCorePreferencesConstants.TITANIUM_SDK_PATH);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static DesktopPlugin getDefault()
	{
		return plugin;
	}
}
