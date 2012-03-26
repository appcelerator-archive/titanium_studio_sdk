/**
 * Appcelerator Titanium Studio
 * Copyright (c) 2012 by Appcelerator, Inc. All Rights Reserved.
 * Proprietary and Confidential - This source code is not for redistribution
 */
package com.appcelerator.titanium.publish.sample;

import org.eclipse.osgi.util.NLS;

/**
 * @author Nam Le <nle@appcelerator.com>
 *
 */
public class Messages extends NLS
{
	private static final String BUNDLE_NAME = "com.appcelerator.titanium.publish.testprovider.messages"; //$NON-NLS-1$
	public static String TestPublishType_Test_publish_output;
	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}
}
