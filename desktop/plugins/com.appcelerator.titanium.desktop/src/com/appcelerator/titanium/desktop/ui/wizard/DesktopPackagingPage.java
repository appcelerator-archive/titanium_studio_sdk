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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.appcelerator.titanium.core.TitaniumCorePlugin;
import com.appcelerator.titanium.core.TitaniumProject;
import com.appcelerator.titanium.desktop.DesktopPlugin;

/**
 * @author cwilliams
 */
class DesktopPackagingPage extends WizardPage
{

	protected TitaniumProject tiProject;
	private Button macPlatformButton;
	private Button winPlatformButton;
	private Button linuxPlatformButton;
	private Button networkInstallerButton;
	private Button bundledInstallerType;
	private Button publicPublishButton;
	private Button privatePublishButton;
	private Button releaseButton;
	private Button dontReleaseButton;
	private Button showSplashButton;
	private Button dontShowSplashButton;

	public DesktopPackagingPage(IProject project)
	{
		super("Desktop Packaging", Messages.DesktopPackagingPage_Title, null); //$NON-NLS-1$
		this.tiProject = getTitaniumProject(project);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(DesktopPlugin.PLUGIN_ID,
				"icons/full/obj16/package.png")); //$NON-NLS-1$
	}

	public void createControl(Composite parent)
	{
		// TODO Change button text to 'Package Project'?
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());
		composite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		setControl(composite);

		initializeDialogUnits(parent);

		// Description/Title
		Label description = new Label(composite, SWT.CENTER | SWT.WRAP);
		description.setText(Messages.DesktopPackagingPage_Description);
		description.setLayoutData(GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false)
				.span(2, 1).create());

		// The options section (on the left)
		Composite options = createOptionsComposite(composite);
		options.setLayoutData(GridDataFactory.fillDefaults().grab(false, true).create());

		// The help text section (on the right)
		Composite help = createHelpComposite(composite);
		help.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		Dialog.applyDialogFont(composite);

		restoreWidgetValues();

		validate();
	}

	private Composite createOptionsComposite(Composite parent)
	{
		Composite options = new Composite(parent, SWT.NONE);
		options.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).create());

		// Platforms group
		Group group = new Group(options, SWT.NONE);
		group.setLayout(GridLayoutFactory.swtDefaults().create());
		group.setText(Messages.DesktopPackagingPage_PlatformsGroup);
		group.setLayoutData(GridDataFactory.fillDefaults().create());

		SelectionAdapter validatingSelectionListener = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				validate();
			}
		};

		// Platforms buttons
		macPlatformButton = new Button(group, SWT.CHECK);
		macPlatformButton.setText(Messages.DesktopPackagingPage_MacLabel);
		macPlatformButton.addSelectionListener(validatingSelectionListener);

		winPlatformButton = new Button(group, SWT.CHECK);
		winPlatformButton.setText(Messages.DesktopPackagingPage_WindowsLabel);
		winPlatformButton.addSelectionListener(validatingSelectionListener);

		linuxPlatformButton = new Button(group, SWT.CHECK);
		linuxPlatformButton.setText(Messages.DesktopPackagingPage_LinuxLabel);
		linuxPlatformButton.addSelectionListener(validatingSelectionListener);

		// Installer Type group
		group = new Group(options, SWT.NONE);
		group.setLayout(GridLayoutFactory.swtDefaults().create());
		group.setText(Messages.DesktopPackagingPage_InstallerTypeLabel);
		group.setLayoutData(GridDataFactory.fillDefaults().create());

		// Installer Type buttons
		networkInstallerButton = new Button(group, SWT.RADIO);
		networkInstallerButton.setText(Messages.DesktopPackagingPage_NetworkLabel);
		networkInstallerButton.setSelection(true);

		bundledInstallerType = new Button(group, SWT.RADIO);
		bundledInstallerType.setText(Messages.DesktopPackagingPage_InstallerLabel);

		// Public Privacy group
		group = new Group(options, SWT.NONE);
		group.setLayout(GridLayoutFactory.swtDefaults().create());
		group.setText(Messages.DesktopPackagingPage_PrivacyLabel);
		group.setLayoutData(GridDataFactory.fillDefaults().create());

		// Privacy buttons
		publicPublishButton = new Button(group, SWT.RADIO);
		publicPublishButton.setText(Messages.DesktopPackagingPage_PublicPrivacyLabel);

		privatePublishButton = new Button(group, SWT.RADIO);
		privatePublishButton.setText(Messages.DesktopPackagingPage_PrivatePrivacyLabel);
		privatePublishButton.setSelection(true);

		// TODO Use one checkbox?
		// Release group
		group = new Group(options, SWT.NONE);
		group.setLayout(GridLayoutFactory.swtDefaults().create());
		group.setText(Messages.DesktopPackagingPage_ReleaseToUsersLabel);
		GC gc = new GC(group);
		int widthHint = gc.stringExtent(group.getText()).x + 15;
		group.setLayoutData(GridDataFactory.fillDefaults().hint(widthHint, SWT.DEFAULT).create());
		gc.dispose();

		// Release to Users buttons
		releaseButton = new Button(group, SWT.RADIO);
		releaseButton.setText(Messages.DesktopPackagingPage_YesLabel);

		dontReleaseButton = new Button(group, SWT.RADIO);
		dontReleaseButton.setText(Messages.DesktopPackagingPage_NoLabel);
		dontReleaseButton.setSelection(true);

		// Show Splash Screen group
		group = new Group(options, SWT.NONE);
		group.setLayout(GridLayoutFactory.swtDefaults().create());
		group.setText(Messages.DesktopPackagingPage_ShowSplashLabel);
		gc = new GC(group);
		widthHint = gc.stringExtent(group.getText()).x + 15;
		group.setLayoutData(GridDataFactory.fillDefaults().hint(widthHint, SWT.DEFAULT).create());
		gc.dispose();

		// Show Titanium Installer splash screen on first run?
		showSplashButton = new Button(group, SWT.RADIO);
		showSplashButton.setText(Messages.DesktopPackagingPage_YesLabel);
		showSplashButton.setSelection(true);

		dontShowSplashButton = new Button(group, SWT.RADIO);
		dontShowSplashButton.setText(Messages.DesktopPackagingPage_NoLabel);

		return options;
	}

	private Composite createHelpComposite(Composite parent)
	{
		Composite help = new Composite(parent, SWT.NONE);
		help.setLayout(GridLayoutFactory.swtDefaults().spacing(0, 10).create());

		Composite text = createTextComposite(help, Messages.DesktopPackagingPage_PlatformsGroup + ":", //$NON-NLS-1$
				Messages.DesktopPackagingPage_PlatformsDescription);
		text.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		text = createTextComposite(help, Messages.DesktopPackagingPage_InstallerTypeLabel + ":", //$NON-NLS-1$
				Messages.DesktopPackagingPage_InstallerDescription);
		text.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		text = createTextComposite(help, Messages.DesktopPackagingPage_PrivacyLabel + ":", //$NON-NLS-1$
				Messages.DesktopPackagingPage_PublishDescription);
		text.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		text = createTextComposite(help, Messages.DesktopPackagingPage_ReleaseToUsersLabel + ":", //$NON-NLS-1$
				Messages.DesktopPackagingPage_ReleaseToUsersDescription);
		text.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		text = createTextComposite(help, Messages.DesktopPackagingPage_ShowSplashLabel + ":", //$NON-NLS-1$
				Messages.DesktopPackagingPage_ShowSplashDescription);
		text.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		return help;
	}

	private Composite createTextComposite(Composite parent, String header, String description)
	{
		Composite text = new Composite(parent, SWT.NONE);
		text.setLayout(GridLayoutFactory.fillDefaults().spacing(0, 0).create());

		Label headerLabel = new Label(text, SWT.NONE);
		headerLabel.setText(header);
		headerLabel.setLayoutData(GridDataFactory.swtDefaults().create());

		Label descriptionLabel = new Label(text, SWT.WRAP);
		descriptionLabel.setText(description);
		descriptionLabel.setLayoutData(GridDataFactory.swtDefaults().grab(true, false).create());

		return text;
	}

	protected void validate()
	{
		if (getPlatforms().isEmpty())
		{
			setErrorMessage(Messages.DesktopPackagingPage_SelectPlatformErrorMessage);
			setPageComplete(false);
			return;
		}
		// TODO Verify other pieces?

		setErrorMessage(null);
		setPageComplete(true);
	}

	Set<String> getPlatforms()
	{
		Set<String> platforms = new HashSet<String>();
		if (linuxPlatformButton.getSelection())
		{
			platforms.add(TiManifest.LINUX_PLATFORM);
		}
		if (macPlatformButton.getSelection())
		{
			platforms.add(TiManifest.MAC_PLATFORM);
		}
		if (winPlatformButton.getSelection())
		{
			platforms.add(TiManifest.WINDOWS_PLATFORM);
		}
		return platforms;
	}

	String getInstallerType()
	{
		if (networkInstallerButton.getSelection())
		{
			return Packager.NETWORK_RUNTIME;
		}
		return Packager.INCLUDE_RUNTIME;
	}

	String getVisibility()
	{
		if (isPublishPrivate())
		{
			return Packager.PRIVATE_VISIBILITY;
		}
		return Packager.PUBLIC_VISIBILITY;
	}

	private boolean isPublishPrivate()
	{
		return privatePublishButton.getSelection();
	}

	boolean releaseToUsers()
	{
		return releaseButton.getSelection();
	}

	boolean showSplash()
	{
		return showSplashButton.getSelection();
	}

	/**
	 * Store the selected options in an IDialodSettings.
	 */
	public void saveWidgetValues()
	{
		IDialogSettings settings = getDialogSettings();
		if (settings != null)
		{
			Set<String> platforms = getPlatforms();
			settings.put("platforms", platforms.toArray(new String[platforms.size()])); //$NON-NLS-1$
			settings.put("visibility", getVisibility()); //$NON-NLS-1$
			settings.put("runtime", getInstallerType()); //$NON-NLS-1$
			settings.put("release", releaseToUsers()); //$NON-NLS-1$
		}
	}

	/**
	 * Grab the stored values from IDialogSettings and set up the UI to reflect what was saved.
	 */
	private void restoreWidgetValues()
	{
		IDialogSettings dialogSettings = getDialogSettings();
		if (dialogSettings != null)
		{
			String[] array = dialogSettings.getArray("platforms"); //$NON-NLS-1$
			if (array != null)
			{
				Set<String> platforms = new HashSet<String>(Arrays.asList(array));
				if (platforms.contains(Packager.LINUX_PLATFORM))
				{
					linuxPlatformButton.setSelection(true);
				}
				if (platforms.contains(Packager.MAC_PLATFORM))
				{
					macPlatformButton.setSelection(true);
				}
				if (platforms.contains(Packager.WINDOWS_PLATFORM))
				{
					winPlatformButton.setSelection(true);
				}
			}

			String visibility = dialogSettings.get("visibility"); //$NON-NLS-1$
			if (visibility != null)
			{
				if (Packager.PRIVATE_VISIBILITY.equals(visibility))
				{
					privatePublishButton.setSelection(true);
					publicPublishButton.setSelection(false);
				}
				else
				{
					privatePublishButton.setSelection(false);
					publicPublishButton.setSelection(true);
				}
			}

			String runtime = dialogSettings.get("runtime"); //$NON-NLS-1$
			if (runtime != null)
			{
				if (Packager.NETWORK_RUNTIME.equals(runtime))
				{
					networkInstallerButton.setSelection(true);
					bundledInstallerType.setSelection(false);
				}
				else
				{
					networkInstallerButton.setSelection(false);
					bundledInstallerType.setSelection(true);
				}
			}

			boolean release = dialogSettings.getBoolean("release"); //$NON-NLS-1$
			releaseButton.setSelection(release);
			dontReleaseButton.setSelection(!release);
		}
	}

	private TitaniumProject getTitaniumProject(IProject project)
	{
		return TitaniumCorePlugin.getDefault().getTitaniumProjectFactory().create(project);
	}
}
