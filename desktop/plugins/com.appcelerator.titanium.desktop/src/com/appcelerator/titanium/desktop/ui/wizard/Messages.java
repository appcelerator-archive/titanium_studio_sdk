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

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
	private static final String BUNDLE_NAME = "com.appcelerator.titanium.desktop.ui.wizard.messages"; //$NON-NLS-1$

	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}

	public static String DesktopPackagingPage_Description;
	public static String DesktopPackagingPage_InstallerDescription;
	public static String DesktopPackagingPage_InstallerLabel;
	public static String DesktopPackagingPage_InstallerTypeLabel;
	public static String DesktopPackagingPage_LinuxLabel;
	public static String DesktopPackagingPage_MacLabel;
	public static String DesktopPackagingPage_NetworkLabel;
	public static String DesktopPackagingPage_NoLabel;
	public static String DesktopPackagingPage_PlatformsDescription;
	public static String DesktopPackagingPage_PlatformsGroup;
	public static String DesktopPackagingPage_PrivacyLabel;
	public static String DesktopPackagingPage_PrivatePrivacyLabel;
	public static String DesktopPackagingPage_PublicPrivacyLabel;
	public static String DesktopPackagingPage_PublishDescription;
	public static String DesktopPackagingPage_ReleaseToUsersDescription;
	public static String DesktopPackagingPage_ReleaseToUsersLabel;
	public static String DesktopPackagingPage_ShowSplashLabel;
	public static String DesktopPackagingPage_ShowSplashDescription;
	public static String DesktopPackagingPage_SelectPlatformErrorMessage;
	public static String DesktopPackagingPage_Title;
	public static String DesktopPackagingPage_WindowsLabel;
	public static String DesktopPackagingPage_YesLabel;
	public static String DirectoryZipper_JobTitle;
	public static String DistributeDesktopWizard_ErrorDialogTitle;
	public static String NewDesktopProjectCreationPage_desktopProjectWizardDescription;
	public static String NewDesktopProjectCreationPage_desktopProjectWizardTitle;
	public static String NewDesktopProjectCreationPage_languageModules;
	public static String NewDesktopProjectCreationPage_sdk_not_found;
	public static String NewDesktopProjectWizard_internalError;
	public static String PackageHandler_LoginMessage;
	public static String PackageHandler_LoginTitle;
	public static String Packager_LastPackagedDist_Title;
	public static String Packager_LastPackagedDist_ToolTip;
	public static String Packager_LinksWebpageOpenError;
	public static String Packager_NoResourcesFolderValidationError;
	public static String Packager_NoTIAPPXMLValidationError;
	public static String Packager_PackagingFailedError;
	public static String Packager_PackagingFailedHTTPError;
	public static String Packager_PackagingTaskName;
	public static String Packager_PollingPackageStatusTaskName;
}
