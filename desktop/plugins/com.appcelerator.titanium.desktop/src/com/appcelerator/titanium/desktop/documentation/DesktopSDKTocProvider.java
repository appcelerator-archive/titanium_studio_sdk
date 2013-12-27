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
package com.appcelerator.titanium.desktop.documentation;

import org.eclipse.help.AbstractTocProvider;
import org.eclipse.help.IToc;
import org.eclipse.help.ITocContribution;

import com.appcelerator.titanium.core.documentation.SDKListenerProxy;
import com.appcelerator.titanium.core.mobile.SDKLocator;
import com.appcelerator.titanium.core.mobile.SDKManager;
import com.appcelerator.titanium.desktop.DesktopPlugin;
import com.appcelerator.titanium.desktop.TitaniumDesktopSDKLocator;

/**
 * Creates a TOC entry for the currently installed Titanium Desktop SDKs
 */
public class DesktopSDKTocProvider extends AbstractTocProvider implements SDKManager.SDKChangeListener
{
	public static SDKListenerProxy proxy;

	public DesktopSDKTocProvider()
	{
		if (proxy == null)
		{
			proxy = new SDKListenerProxy(this, TitaniumDesktopSDKLocator.class);
		}
	}

	public ITocContribution[] getTocContributions(final String locale)
	{
		ITocContribution contribution = new ITocContribution()
		{
			public String getContributorId()
			{
				return DesktopPlugin.PLUGIN_ID;
			}

			public String getId()
			{
				return DesktopPlugin.PLUGIN_ID + "DesktopSDKTocProvider"; //$NON-NLS-1$
			}

			public String getCategoryId()
			{
				return null;
			}

			public boolean isPrimary()
			{
				return true;
			}

			public IToc getToc()
			{
				return new DesktopSDKToc();
			}

			public String getLocale()
			{
				return locale;
			}

			public String[] getExtraDocuments()
			{
				return new String[0];
			}

			public String getLinkTo()
			{
				return "../com.appcelerator.titanium.ui/tocreference.xml#titanium_desktop_sdk_reference"; //$NON-NLS-1$
			}
		};

		return new ITocContribution[] { contribution };
	}

	/**
	 * Update TOC when content changes
	 */
	public void sdkChanged(Class<? extends SDKLocator> clazz)
	{
		super.contentChanged();
	}

}
