/**
 * Appcelerator Titanium Studio
 * Copyright (c) 2012 by Appcelerator, Inc. All Rights Reserved.
 * Proprietary and Confidential - This source code is not for redistribution
 */
package com.appcelerator.titanium.publish.sample.ui;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.appcelerator.titanium.publish.IPublishType;
import com.appcelerator.titanium.publish.PublishException;
import com.appcelerator.titanium.publish.sample.SampleProviderPlugin;
import com.appcelerator.titanium.publish.sample.SamplePublishTarget;
import com.appcelerator.titanium.publish.sample.SamplePublishType;
import com.appcelerator.titanium.publish.ui.RegisterPublishTargetDialog;
import com.aptana.core.logging.IdeLog;
import com.aptana.core.util.StringUtil;

/**
 * Target dialog that collects URL, username and password
 */
public class SampleRegisterTargetDialog extends RegisterPublishTargetDialog
{
	private Text username;
	private Text password;

	public SampleRegisterTargetDialog()
	{
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.appcelerator.titanium.publish.ui.RegisterPublishTargetDialog#createProviderContents(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	protected void createProviderContents(Composite contents)
	{
		publishTargetName.setText(StringUtil.makeFormLabel(Messages.RegisterTestTargetDialog_URL_Label));

		Label apiTokenLabel = new Label(contents, SWT.NONE);
		apiTokenLabel.setText(StringUtil.makeFormLabel(Messages.RegisterTestTargetDialog_Username_Label));

		username = new Text(contents, SWT.SINGLE | SWT.BORDER);
		if (publishTarget instanceof SamplePublishTarget)
		{
			SamplePublishTarget testTarget = (SamplePublishTarget) publishTarget;
			String text = testTarget.getUsername();
			if (!StringUtil.isEmpty(text))
			{
				username.setText(text);
			}
		}
		username.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		username.addModifyListener(textChangeAdapter);

		Label passwordLabel = new Label(contents, SWT.NONE);
		passwordLabel.setText(StringUtil.makeFormLabel(Messages.RegisterTestTargetDialog_Password_Label));

		password = new Text(contents, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		if (publishTarget instanceof SamplePublishTarget)
		{
			SamplePublishTarget testTarget = (SamplePublishTarget) publishTarget;
			String text = testTarget.getPassword();
			if (!StringUtil.isEmpty(text))
			{
				password.setText(text);
			}
		}
		password.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		password.addModifyListener(textChangeAdapter);

		// Set the tab-list to traverse the text fields first.
		contents.setTabList(new Control[] { publishTargetText, username, password, apiTokenLabel, passwordLabel });
	}

	@Override
	protected String getPublishTypeId()
	{
		return SamplePublishType.ID;
	}

	/**
	 * Ensure you can perform a HEAD on the url. Since the check can take some time, we perform it here
	 */
	@Override
	protected boolean validatePublishTarget()
	{
		try
		{
			HttpURLConnection connection = (HttpURLConnection) new URL(publishTargetText.getText().trim())
					.openConnection();
			connection.setRequestMethod("HEAD"); //$NON-NLS-1$
			int responseCode = connection.getResponseCode();
			if (responseCode != 200)
			{
				return false;
			}
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}

	@Override
	protected void savePublishTarget()
	{
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(SamplePublishTarget.USERNAME, username.getText().trim());
		properties.put(SamplePublishTarget.PASSWORD, password.getText().trim());

		IPublishType publishType = publishManager.getPublishType(getPublishTypeId());
		if (publishType != null)
		{
			try
			{
				publishTarget = publishType.createPublishTarget(publishTargetText.getText().trim(), properties);
			}
			catch (PublishException e)
			{
				IdeLog.logError(SampleProviderPlugin.getDefault(), "Error creating Test publish target", e); //$NON-NLS-1$
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.appcelerator.titanium.publish.ui.RegisterPublishTargetDialog#calculateErrorMessage()
	 */
	@Override
	protected String calculateErrorMessage()
	{
		// Ensures non-empty values for password and username
		String errorMessage = super.calculateErrorMessage();

		if (StringUtil.isEmpty(errorMessage))
		{
			if (StringUtil.isEmpty(username.getText()))
			{
				errorMessage = Messages.RegisterTestTargetDialog_Username_error;
			}
			else if (StringUtil.isEmpty(password.getText()))
			{
				errorMessage = Messages.RegisterTestTargetDialog_Password_error;
			}
		}

		return errorMessage;
	}
}
