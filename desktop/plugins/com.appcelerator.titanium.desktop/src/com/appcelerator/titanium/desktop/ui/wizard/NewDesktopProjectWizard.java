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

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import com.aptana.jetty.util.epl.ajax.JSON;

import com.appcelerator.titanium.core.ITiAppModel;
import com.appcelerator.titanium.core.TiElementDefinition;
import com.appcelerator.titanium.core.TitaniumConstants;
import com.appcelerator.titanium.core.TitaniumProjectBuilder;
import com.appcelerator.titanium.desktop.DesktopPlugin;
import com.appcelerator.titanium.desktop.DesktopProjectNature;
import com.appcelerator.titanium.ui.wizard.BasicNewTitaniumProjectWizard;
import com.appcelerator.titanium.ui.wizard.ImportResourcesOperation;
import com.aptana.core.build.UnifiedBuilder;
import com.aptana.core.projects.templates.TemplateType;
import com.aptana.core.util.StringUtil;
import com.aptana.projects.WebProjectNature;
import com.aptana.projects.wizards.IWizardProjectCreationPage;

/**
 * A desktop project creation wizard.
 * 
 * @author Shalom Gibly <sgibly@appcelerator.com>
 */
public class NewDesktopProjectWizard extends BasicNewTitaniumProjectWizard
{
	private static final String PROJECT_TEMPLATE_RESOURCE_FOLDER = "resources" + File.separator + "desktopTemplate"; //$NON-NLS-1$ //$NON-NLS-2$
	// Nature Ids
	private static final String RUBY_NATURE_ID = "com.aptana.ruby.core.rubynature"; //$NON-NLS-1$
	private static final String PHP_NATURE_ID = "com.aptana.editor.php.phpNature"; //$NON-NLS-1$
	private static final String PYTHON_NATURE_ID = "org.python.pydev.pythonNature"; //$NON-NLS-1$
	// Builder Ids
	private static final String PHP_BUILDER_ID = "com.aptana.editor.php.aptanaPhpBuilder"; //$NON-NLS-1$
	private static final String PYTHON_BUILDER_ID = "org.python.pydev.PyDevBuilder"; //$NON-NLS-1$

	private static final String IGNORED_TEMPLATE_FILE_NAME = ".ignore"; //$NON-NLS-1$

	@Override
	protected IWizardProjectCreationPage createMainPage()
	{
		return new NewDesktopProjectCreationPage(selectedTemplate);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection currentSelection)
	{
		super.init(workbench, currentSelection);
		setWindowTitle(Messages.NewDesktopProjectCreationPage_desktopProjectWizardTitle);
	}

	@Override
	protected String getProjectType()
	{
		return TitaniumConstants.DESKTOP_TYPE;
	}

	@Override
	protected void createProjectFiles(String projectType, IProgressMonitor monitor) throws Exception
	{
		// The super call also calls to the generateInitialManifest that we override here, so the manifest is created
		// with some dedicated fields that apply to Desktop projects.
		super.createProjectFiles(projectType, monitor);
		// Import the project template from the desktop plugin.
		ImportResourcesOperation importOperation = new ImportResourcesOperation(DesktopPlugin.getDefault().getBundle(),
				PROJECT_TEMPLATE_RESOURCE_FOLDER, newProject, true, getIgnoredResourceNames());
		importOperation.run(monitor);
		newProject.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
	}

	@Override
	protected void setExtendedTiAppModelValues(ITiAppModel tiAppModel, ProjectData projectData, String projectType)
	{
		super.setExtendedTiAppModelValues(tiAppModel, projectData, projectType);
		// We have to set those values manually to our Desktop template file.
		tiAppModel.setValue(TiElementDefinition.ID, projectData.appId);
		tiAppModel.setValue(TiElementDefinition.NAME, projectData.projectName);
		tiAppModel.setValue(TiElementDefinition.VERSION, "1.0"); //$NON-NLS-1$
		tiAppModel.setValue(TiElementDefinition.URL, projectData.appURL);
	}

	@Override
	protected boolean applySourcedProjectFilesAfterProjectCreated()
	{
		return false;
	}

	/**
	 * Override the default implementation for Desktop projects to use the workspace settings (e.g. do not set any
	 * project-specific UserAgents).
	 * 
	 * @see com.appcelerator.titanium.ui.wizard.BasicNewTitaniumProjectWizard#applyPreferenceValues(com.appcelerator.titanium.ui.wizard.BasicNewTitaniumProjectWizard.ProjectData,
	 *      java.lang.String)
	 */
	protected void applyPreferenceValues(ProjectData projectData, String projectType)
	{
		// no-op
	}

	/**
	 * Returns a Set with ignored resources names that will be ignored when importing project files with the
	 * ImportResourcesOperation
	 * 
	 * @return A set of file/folder names that will be ignored.
	 */
	private Set<String> getIgnoredResourceNames()
	{
		Set<String> ignored = new HashSet<String>(1);
		ignored.add(IGNORED_TEMPLATE_FILE_NAME);
		return ignored;
	}

	@Override
	protected StringBuilder generateInitialManifest(ProjectData projectData, String projectType)
	{
		StringBuilder content = super.generateInitialManifest(projectData, projectType);
		// Add the language modules into the manifest on creation
		String selectedSDK = projectData.sdkVersion;
		content.append("runtime:").append(selectedSDK).append('\n'); //$NON-NLS-1$
		// TODO Use the list of modules defined in TitaniumProject.MODULE?
		content.append("tiapp:").append(selectedSDK).append('\n'); //$NON-NLS-1$
		content.append("tifilesystem:").append(selectedSDK).append('\n'); //$NON-NLS-1$
		content.append("tiplatform:").append(selectedSDK).append('\n'); //$NON-NLS-1$
		content.append("tiui:").append(selectedSDK).append('\n'); //$NON-NLS-1$
		if (projectData.selectedItems.contains(TitaniumConstants.RUBY_MODULE))
		{
			content.append("ruby:").append(selectedSDK).append('\n'); //$NON-NLS-1$
		}
		if (projectData.selectedItems.contains(TitaniumConstants.PYTHON_MODULE))
		{
			content.append("python:").append(selectedSDK).append('\n'); //$NON-NLS-1$
		}
		if (projectData.selectedItems.contains(TitaniumConstants.PHP_MODULE))
		{
			content.append("php:").append(selectedSDK).append('\n'); //$NON-NLS-1$
		}
		content.append("ticodec:").append(selectedSDK).append('\n'); //$NON-NLS-1$
		content.append("tidatabase:").append(selectedSDK).append('\n'); //$NON-NLS-1$
		content.append("timedia:").append(selectedSDK).append('\n'); //$NON-NLS-1$
		content.append("timonkey:").append(selectedSDK).append('\n'); //$NON-NLS-1$
		content.append("tinetwork:").append(selectedSDK).append('\n'); //$NON-NLS-1$
		content.append("tiprocess:").append(selectedSDK).append('\n'); //$NON-NLS-1$
		content.append("tiworker:").append(selectedSDK).append('\n'); //$NON-NLS-1$
		return content;
	}

	@Override
	protected Map<String, String> generatePayload()
	{
		Map<String, String> payload = super.generatePayload();
		List<String> selectedLanguageModules = getSelectedItems();
		// Re-create the 'languages' part
		Map<String, String> languages = new LinkedHashMap<String, String>();
		// @formatter:off
		languages.put(
				"ruby", selectedLanguageModules.contains(TitaniumConstants.RUBY_MODULE) ? "on" : StringUtil.EMPTY); //$NON-NLS-1$//$NON-NLS-2$
		languages
				.put("python", selectedLanguageModules.contains(TitaniumConstants.PYTHON_MODULE) ? "on" : StringUtil.EMPTY); //$NON-NLS-1$//$NON-NLS-2$
		languages
				.put("php", selectedLanguageModules.contains(TitaniumConstants.PHP_MODULE) ? "on" : StringUtil.EMPTY); //$NON-NLS-1$//$NON-NLS-2$
		// @formatter:on
		payload.put("languageModules", JSON.toString(languages)); //$NON-NLS-1$
		return payload;
	}

	@Override
	protected String[] getProjectNatures()
	{
		List<String> selectedLanguageModules = getSelectedItems();
		List<String> natures = new ArrayList<String>(selectedLanguageModules.size());
		natures.add(DesktopProjectNature.ID);
		natures.add(WebProjectNature.ID);
		if (selectedLanguageModules.contains(TitaniumConstants.RUBY_MODULE))
		{
			natures.add(RUBY_NATURE_ID);
		}
		if (selectedLanguageModules.contains(TitaniumConstants.PYTHON_MODULE))
		{
			natures.add(PYTHON_NATURE_ID);
		}
		if (selectedLanguageModules.contains(TitaniumConstants.PHP_MODULE))
		{
			natures.add(PHP_NATURE_ID);
		}
		return natures.toArray(new String[natures.size()]);
	}

	@Override
	protected String[] getProjectBuilders()
	{
		List<String> selectedLanguageModules = getSelectedItems();
		List<String> builders = new ArrayList<String>(selectedLanguageModules.size());
		builders.add(TitaniumProjectBuilder.ID);
		builders.add(UnifiedBuilder.ID);
		// Ruby has the unified builder too, so no need to check for that.
		if (selectedLanguageModules.contains(TitaniumConstants.PHP_MODULE))
		{
			builders.add(PHP_BUILDER_ID);
		}
		if (selectedLanguageModules.contains(TitaniumConstants.PYTHON_MODULE))
		{
			builders.add(PYTHON_BUILDER_ID);
		}
		return builders.toArray(new String[builders.size()]);
	}

	@Override
	protected TemplateType[] getProjectTemplateTypes()
	{
		return new TemplateType[] { TemplateType.TITANIUM_DESKTOP };
	}

	@Override
	protected String getProjectCreateEventName()
	{
		return "project.create.desktop"; //$NON-NLS-1$
	}
}
