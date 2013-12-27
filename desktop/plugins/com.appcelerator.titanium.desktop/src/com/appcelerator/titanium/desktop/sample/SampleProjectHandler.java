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
package com.appcelerator.titanium.desktop.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.appcelerator.titanium.core.TitaniumProjectBuilder;
import com.appcelerator.titanium.desktop.DesktopPlugin;
import com.aptana.core.build.UnifiedBuilder;
import com.aptana.core.logging.IdeLog;
import com.aptana.samples.handlers.ISampleProjectHandler;

public class SampleProjectHandler implements ISampleProjectHandler
{

	private static final String[] BUILDER_IDS = { UnifiedBuilder.ID, TitaniumProjectBuilder.ID };

	public void projectCreated(IProject project)
	{
		try
		{
			IProjectDescription description = project.getDescription();
			List<ICommand> buildSpecs = new ArrayList<ICommand>();
			buildSpecs.addAll(Arrays.asList(description.getBuildSpec()));

			List<ICommand> newBuilders = new ArrayList<ICommand>();
			for (String builderId : BUILDER_IDS)
			{
				boolean found = false;
				for (ICommand buildSpec : buildSpecs)
				{
					if (builderId.equals(buildSpec.getBuilderName()))
					{
						found = true;
						break;
					}
				}

				if (!found)
				{
					ICommand builder = description.newCommand();
					builder.setBuilderName(builderId);
					newBuilders.add(builder);
				}
			}
			buildSpecs.addAll(newBuilders);

			description.setBuildSpec(buildSpecs.toArray(new ICommand[buildSpecs.size()]));
			project.setDescription(description, null);

			markBuildFolderDerived(project);
		}
		catch (CoreException e)
		{
			IdeLog.logError(DesktopPlugin.getDefault(), e);
		}
	}

	/**
	 * Make the build subfolder "derived" so Eclipse doesn't search it.
	 * 
	 * @param project
	 * @throws CoreException
	 */
	protected void markBuildFolderDerived(final IProject project) throws CoreException
	{
		IFolder buildFolder = project.getFolder("build"); //$NON-NLS-1$
		if (buildFolder.exists())
		{
			// Mark build folder as derived if it isn't already.
			if (!buildFolder.isDerived())
			{
				buildFolder.setDerived(true, new NullProgressMonitor());
			}
		}
	}

	public void projectCreated(IProject project, Object data)
	{
	}
}
