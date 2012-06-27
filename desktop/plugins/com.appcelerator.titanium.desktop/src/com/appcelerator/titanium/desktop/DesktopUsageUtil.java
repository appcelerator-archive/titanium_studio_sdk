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

import java.util.HashMap;
import java.util.Map;

import com.appcelerator.titanium.core.TitaniumProject;
import com.appcelerator.titanium.usage.TitaniumUsagePlugin;
import com.aptana.core.util.StringUtil;
import com.aptana.usage.FeatureEvent;
import com.aptana.usage.StudioAnalytics;

public class DesktopUsageUtil
{

	private static final String LAUNCH_EVENT = "desktop.launch"; //$NON-NLS-1$
	private static final String PACKAGE_EVENT = "desktop.package"; //$NON-NLS-1$

	public static void sendLaunchEvent(String sdk, TitaniumProject tiProj)
	{
		String appid = tiProj.getAppID();
		String appname = tiProj.getAppName();
		String guid = tiProj.getGUID();

		if (StringUtil.isEmpty(appid) || StringUtil.isEmpty(appname) || StringUtil.isEmpty(guid))
		{
			TitaniumUsagePlugin.logError(Messages.DesktopUsageUtil_desktop_launch_event_error);
			return;
		}

		Map<String, String> payload = new HashMap<String, String>();
		payload.put("sdk", sdk); //$NON-NLS-1$
		payload.put("appid", appid); //$NON-NLS-1$
		payload.put("name", appname); //$NON-NLS-1$
		payload.put("guid", guid); //$NON-NLS-1$

		StudioAnalytics.getInstance().sendEvent(new FeatureEvent(LAUNCH_EVENT, payload));
	}

	public static void sendPackageEvent(boolean win, boolean linux, boolean mac, String projectGUID)
	{
		if (StringUtil.isEmpty(projectGUID))
		{
			TitaniumUsagePlugin.logError(Messages.DesktopUsageUtil_desktop_package_event_error);
			return;
		}

		Map<String, String> payload = new HashMap<String, String>();
		payload.put("win", Boolean.toString(win)); //$NON-NLS-1$
		payload.put("linux", Boolean.toString(linux)); //$NON-NLS-1$
		payload.put("mac", Boolean.toString(mac)); //$NON-NLS-1$
		payload.put("guid", projectGUID); //$NON-NLS-1$

		StudioAnalytics.getInstance().sendEvent(new FeatureEvent(PACKAGE_EVENT, payload));
	}
}
