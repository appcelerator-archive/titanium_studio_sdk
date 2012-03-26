/**
 * Appcelerator Titanium Studio
 * Copyright (c) 2012 by Appcelerator, Inc. All Rights Reserved.
 * Proprietary and Confidential - This source code is not for redistribution
 */
package com.appcelerator.titanium.publish.sample;

import java.util.Map;

import com.appcelerator.titanium.publish.PublishTarget;

/**
 * Test Publish Target. Cotnains a username and password
 */
public class SamplePublishTarget extends PublishTarget
{
	public static final String USERNAME = "user_name"; //$NON-NLS-1$
	public static final String PASSWORD = "password"; //$NON-NLS-1$

	/**
	 * @param name
	 * @param properties
	 */
	public SamplePublishTarget(String name, Map<String, String> properties)
	{
		super(name, properties, SamplePublishType.ID);
	}

	public String getUsername()
	{
		return getProperties().get(USERNAME);
	}

	public String getPassword()
	{
		return getProperties().get(PASSWORD);
	}
}
