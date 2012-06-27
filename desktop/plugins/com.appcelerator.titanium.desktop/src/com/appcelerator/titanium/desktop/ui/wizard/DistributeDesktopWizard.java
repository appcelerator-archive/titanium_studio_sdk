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

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import com.appcelerator.titanium.desktop.DesktopPlugin;
import com.aptana.core.logging.IdeLog;
import com.aptana.projects.ProjectsPlugin;

public class DistributeDesktopWizard extends Wizard implements IWorkbenchWizard
{

	private IProject project;
	private DesktopPackagingPage mainPage;

	public DistributeDesktopWizard()
	{
		setNeedsProgressMonitor(true);
		IDialogSettings workbenchSettings = ProjectsPlugin.getDefault().getDialogSettings();
		IDialogSettings section = workbenchSettings.getSection("DistributeDesktopWizard");//$NON-NLS-1$
		if (section == null)
		{
			section = workbenchSettings.addNewSection("DistributeDesktopWizard");//$NON-NLS-1$
		}
		setDialogSettings(section);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection)
	{
		Object element = selection.getFirstElement();
		if (element instanceof IAdaptable)
		{
			IResource resource = (IResource) ((IAdaptable) element).getAdapter(IResource.class);
			if (resource != null)
			{
				this.project = resource.getProject();
			}
		}
	}

	@Override
	public void addPages()
	{
		mainPage = new DesktopPackagingPage(this.project);
		addPage(mainPage);
	}

	@Override
	public boolean performFinish()
	{
		try
		{
			mainPage.saveWidgetValues();

			final Set<String> platforms = mainPage.getPlatforms();
			final String visibility = mainPage.getVisibility();
			final String runtime = mainPage.getInstallerType();
			final boolean release = mainPage.releaseToUsers();
			final boolean showSplash = mainPage.showSplash();

			getContainer().run(true, true, new IRunnableWithProgress()
			{

				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
					IStatus status = new Packager().distribute(DistributeDesktopWizard.this.project, platforms,
							runtime, release, visibility, showSplash, monitor);
					if (!status.isOK())
					{
						throw new InvocationTargetException(new CoreException(status));
					}
				}
			});
		}
		catch (Throwable e)
		{
			if (e instanceof InvocationTargetException)
			{
				Throwable t = ((InvocationTargetException) e).getTargetException();
				if (t != null)
				{
					e = t;
				}
			}
			IdeLog.logError(DesktopPlugin.getDefault(), e);
			MessageDialog.openError(getShell(), Messages.DistributeDesktopWizard_ErrorDialogTitle, e.getMessage());
		}

		return true;
	}
}
