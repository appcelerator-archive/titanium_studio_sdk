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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

import com.appcelerator.titanium.core.TitaniumProjectBuilder;
import com.aptana.core.util.ResourceUtil;

public class DesktopProjectNature implements IProjectNature
{

	public static final String ID = DesktopPlugin.PLUGIN_ID + ".nature"; //$NON-NLS-1$

	private IProject project;

	public void configure() throws CoreException
	{
		ResourceUtil.addBuilder(getProject(), TitaniumProjectBuilder.ID);
	}

	public void deconfigure() throws CoreException
	{
		ResourceUtil.removeBuilder(getProject(), TitaniumProjectBuilder.ID);
	}

	public IProject getProject()
	{
		return project;
	}

	public void setProject(IProject project)
	{
		this.project = project;
	}
}
