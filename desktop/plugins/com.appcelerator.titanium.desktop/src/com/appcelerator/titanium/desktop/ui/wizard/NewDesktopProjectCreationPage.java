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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.appcelerator.titanium.core.SDKEntity;
import com.appcelerator.titanium.core.TitaniumConstants;
import com.appcelerator.titanium.desktop.TitaniumDesktopSDKLocator;
import com.appcelerator.titanium.ui.wizard.BasicTitaniumProjectCreationPage;
import com.aptana.core.projects.templates.IProjectTemplate;

/**
 * Desktop project creation wizard main page.<br>
 * 
 * @author Shalom Gibly <sgibly@appcelerator.com>
 */
public class NewDesktopProjectCreationPage extends BasicTitaniumProjectCreationPage
{
	// Unique wizard-page buttons
	private Button rubyButton;
	private Button pythonButton;
	private Button phpButton;

	/**
	 * @param pageName
	 */
	public NewDesktopProjectCreationPage(IProjectTemplate projectTemplate)
	{
		super("mobileCreationMainPage", projectTemplate); //$NON-NLS-1$
		setTitle(Messages.NewDesktopProjectCreationPage_desktopProjectWizardTitle);
		setDescription(Messages.NewDesktopProjectCreationPage_desktopProjectWizardDescription);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardNewProjectCreationPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent)
	{
		super.createControl(parent);
		Composite group = createCommonControls(parent);
		// Loads the SDK versions
		sdkViewer.setInput(TitaniumDesktopSDKLocator.getAvailable());
		SDKEntity selection = null;
		// Load the saved setting for the Titanium Desktop SDK and set it as the selected one (in case we still have it
		// available in the combo)
		IDialogSettings dialogSettings = getDialogSettings();
		if (dialogSettings != null)
		{
			selection = TitaniumDesktopSDKLocator.findVersion(String.valueOf(dialogSettings.get(TITANIUM_SDK_SETTING)));
		}
		if (selection == null)
		{
			selection = TitaniumDesktopSDKLocator.getLatestVersion();
		}
		if (selection != null)
		{
			sdkViewer.setSelection(new StructuredSelection(selection));
		}

		// Add the platform selection buttons according to what is available
		Label deploymentLabel = new Label(group, SWT.NONE);
		deploymentLabel.setText(Messages.NewDesktopProjectCreationPage_languageModules);
		Composite platformsComposite = new Composite(group, SWT.NONE);
		platformsComposite.setLayout(new GridLayout(3, true));
		GridData gd = new GridData(GridData.FILL);
		platformsComposite.setLayoutData(gd);

		rubyButton = new Button(platformsComposite, SWT.CHECK);
		rubyButton.setText("Ruby"); //$NON-NLS-1$
		pythonButton = new Button(platformsComposite, SWT.CHECK);
		pythonButton.setText("Python"); //$NON-NLS-1$
		phpButton = new Button(platformsComposite, SWT.CHECK);
		phpButton.setText("PHP"); //$NON-NLS-1$

		Control control = getControl();
		Dialog.applyDialogFont(control);
		setControl(control);
	}

	protected boolean innerValidate()
	{
		if (TitaniumDesktopSDKLocator.getLatestVersion() == null)
		{
			setErrorMessage(Messages.NewDesktopProjectCreationPage_sdk_not_found);
			return false;
		}

		return super.innerValidate();
	}

	/**
	 * Returns the selected language-modules for this project.<br>
	 * The returned list contains string that matches the module constants defined at {@link TitaniumConstants}
	 */
	public List<String> getSelectedItems()
	{
		List<String> selected = new ArrayList<String>(3);
		if (rubyButton.isEnabled() && rubyButton.getSelection())
		{
			selected.add(TitaniumConstants.RUBY_MODULE);
		}
		if (pythonButton.isEnabled() && pythonButton.getSelection())
		{
			selected.add(TitaniumConstants.PYTHON_MODULE);
		}
		if (phpButton.isEnabled() && phpButton.getSelection())
		{
			selected.add(TitaniumConstants.PHP_MODULE);
		}
		return selected;
	}
}
