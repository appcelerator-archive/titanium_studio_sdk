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

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import com.appcelerator.titanium.ui.launching.AbstractTitaniumLaunchShortcut;

/**
 * @author Max Stepanov
 */
public class DesktopLaunchShortcut extends AbstractTitaniumLaunchShortcut
{

	/*
	 * (non-Javadoc)
	 * @see com.appcelerator.titanium.ui.launching.AbstractTitaniumLaunchShortcut#getLaunchConfigurationTypeId()
	 */
	@Override
	protected String getLaunchConfigurationTypeId()
	{
		return IDesktopLaunchConfigurationConstants.ID_DESKTOP_LAUNCH_CONFIGURATION_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.appcelerator.titanium.ui.launching.AbstractTitaniumLaunchShortcut#setLaunchConfigurationDefaults(org.eclipse
	 * .debug.core.ILaunchConfigurationWorkingCopy)
	 */
	@Override
	protected void setLaunchConfigurationDefaults(ILaunchConfigurationWorkingCopy configuration)
	{
	}
}
