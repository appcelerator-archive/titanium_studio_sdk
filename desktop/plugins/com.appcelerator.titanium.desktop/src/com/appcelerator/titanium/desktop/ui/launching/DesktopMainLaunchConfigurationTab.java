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
package com.appcelerator.titanium.desktop.ui.launching;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.appcelerator.titanium.ui.launching.BaseTitaniumLaunchConfigurationTab;

/**
 * @author Max Stepanov
 */
public class DesktopMainLaunchConfigurationTab extends BaseTitaniumLaunchConfigurationTab
{

	private static final String TYPE_ID = "com.appcelerator.titanium.desktop.launchConfigurationType"; //$NON-NLS-1$

	@Override
	protected Group createPlatformControl(Composite composite)
	{
		Group platformGroup = super.createPlatformControl(composite);
		createLogLevelControl(platformGroup);
		return platformGroup;
	}

	public String getName()
	{
		return Messages.DesktopMainLaunchConfigurationTab_TabName;
	}

	@Override
	protected boolean allowCleanBuild()
	{
		return false;
	}

	@Override
	protected String getLaunchConfigurationTypeId()
	{
		return TYPE_ID;
	}
}
