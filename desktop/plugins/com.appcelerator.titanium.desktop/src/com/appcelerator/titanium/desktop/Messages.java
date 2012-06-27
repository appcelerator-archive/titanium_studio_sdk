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

import org.eclipse.osgi.util.NLS;

/**
 * @author Shalom Gibly <sgibly@appcelerator.com>
 */
/* package */class Messages extends NLS
{
	private static final String BUNDLE_NAME = "com.appcelerator.titanium.desktop.messages"; //$NON-NLS-1$

	public static String DesktopDiagnosticLog_sdk_location_label;
	public static String DesktopDiagnosticLog_sdk_version_label;

	public static String DesktopUsageUtil_desktop_launch_event_error;
	public static String DesktopUsageUtil_desktop_package_event_error;

	public static String TitaniumSDKTools_NoTitaniumSDKError;

	public static String TitaniumDesktopSDKLocator_sdkRootNotSpecified;
	public static String TitaniumDesktopSDKLocator_sdkRootPathNotDirectory;
	public static String TitaniumDesktopSDKLocator_unsupportedPlatform;
	public static String TitaniumDesktopSDKLocator_mobileSDKRootNotDirectory;

	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}
}
