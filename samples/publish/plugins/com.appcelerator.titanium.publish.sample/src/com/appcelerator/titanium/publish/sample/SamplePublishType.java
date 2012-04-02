/**
 * Appcelerator Titanium Studio
 * Copyright (c) 2012 by Appcelerator, Inc. All Rights Reserved.
 * Proprietary and Confidential - This source code is not for redistribution
 */
package com.appcelerator.titanium.publish.sample;

import java.io.File;
import java.text.MessageFormat;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Composite;

import com.appcelerator.titanium.publish.AbstractPublishType;
import com.appcelerator.titanium.publish.IPublishTarget;
import com.appcelerator.titanium.publish.PublishException;
import com.appcelerator.titanium.publish.sample.ui.SampleConfigurationControl;
import com.appcelerator.titanium.publish.sample.ui.SampleSummaryComponent;
import com.appcelerator.titanium.publish.ui.wizard.ISummaryWizardPageComponent;
import com.aptana.core.util.StatusCollector;

/**
 * Test Publish Type. Performs a build and displays the location and settings of the publish
 */
public class SamplePublishType extends AbstractPublishType
{
	public static final String ID = "com.appcelerator.titanium.publish.sample.provider"; //$NON-NLS-1$
	public static final String PUBLISH_PROPERTY_DESCRIPTION = "description"; //$NON-NLS-1$
	public static final String PUBLISH_PROPERTY_PRIVATE = "private"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * @see com.appcelerator.titanium.publish.IPublishType#createPublishTarget(java.lang.String, java.util.Map)
	 */
	public IPublishTarget createPublishTarget(String name, Map<String, String> properties) throws PublishException
	{
		return new SamplePublishTarget(name, properties);
	}

	/*
	 * (non-Javadoc)
	 * @see com.appcelerator.titanium.publish.IPublishType#getManagePublishTargetUrl(com.appcelerator.titanium.publish.
	 * IPublishTarget)
	 */
	public String getManagePublishTargetUrl(IPublishTarget publishTarget)
	{
		return publishTarget != null ? publishTarget.getName() : null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.appcelerator.titanium.publish.IPublishType#publish(com.appcelerator.titanium.publish.IPublishTarget,
	 * java.util.Map)
	 */
	public IStatus publish(IPublishTarget publishTarget, Map<String, String> parameters) throws PublishException
	{
		String platform = parameters.get(PUBLISH_PROPERTY_PLATFORM);
		String target = publishTarget.getName();
		String publishTypeValueId = publishTarget.getType();

		String output = parameters.get(PUBLISH_PROPERTY_OUTPUT);
		String description = parameters.get(PUBLISH_PROPERTY_DESCRIPTION);
		String isPrivate = parameters.get(PUBLISH_PROPERTY_PRIVATE);

		return new Status(IStatus.OK, SampleProviderPlugin.PLUGIN_ID, MessageFormat.format(
				Messages.TestPublishType_Test_publish_output, platform, target, publishTypeValueId, output, new File(
						output).canRead() ? "found" : "missing", description, isPrivate));
	}

	/*
	 * (non-Javadoc)
	 * @see com.appcelerator.titanium.publish.IPublishType#contributesConfigurationControl()
	 */
	public boolean contributesConfigurationControl()
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.appcelerator.titanium.publish.IPublishType#createConfigurationControl(org.eclipse.swt.widgets.Composite,
	 * java.util.Map, com.aptana.core.util.StatusCollector)
	 */
	public void createConfigurationControl(Composite parent, Map<String, Object> model, StatusCollector statusCollector)
	{
		new SampleConfigurationControl(model, statusCollector).create(parent);
	}

	/*
	 * (non-Javadoc)
	 * @see com.appcelerator.titanium.publish.IPublishType#contributesSummaryComponent()
	 */
	public boolean contributesSummaryComponent()
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.appcelerator.titanium.publish.IPublishType#createSummaryComponent(org.eclipse.swt.widgets.Composite,
	 * java.util.Map, com.aptana.core.util.StatusCollector)
	 */
	public ISummaryWizardPageComponent createSummaryComponent(Composite parent, Map<String, Object> model,
			StatusCollector listener)
	{
		return new SampleSummaryComponent();
	}
}
