/**
 * Appcelerator Titanium Studio
 * Copyright (c) 2012 by Appcelerator, Inc. All Rights Reserved.
 * Proprietary and Confidential - This source code is not for redistribution
 */
package com.appcelerator.titanium.publish.sample.ui;

import com.appcelerator.titanium.publish.sample.SamplePublishType;
import com.appcelerator.titanium.publish.ui.PublishContributionItem;

/**
 * Uses the default Publish menus
 */
public class SampleContributionItem extends PublishContributionItem
{
	private static final String ID = "com.appcelerator.titanium.publish.SampleContributionItem"; //$NON-NLS-1$

	public SampleContributionItem()
	{
		super(ID, SamplePublishType.ID);
	}

}
