/**
 * Aptana Studio
 * Copyright (c) 2012 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.appcelerator.titanium.publish.sample.ui;

import org.eclipse.osgi.util.NLS;

/**
 * @author Nam Le <nle@appcelerator.com>
 *
 */
public class Messages extends NLS
{
	private static final String BUNDLE_NAME = "com.appcelerator.titanium.publish.testprovider.ui.messages"; //$NON-NLS-1$
	public static String RegisterTestTargetDialog_Password_error;
	public static String RegisterTestTargetDialog_Password_Label;
	public static String RegisterTestTargetDialog_URL_Label;
	public static String RegisterTestTargetDialog_Username_error;
	public static String RegisterTestTargetDialog_Username_Label;
	public static String TestConfigurationControl_Description_error;
	public static String TestSummaryComponent_Description_Label;
	public static String TestSummaryComponent_Private_Label;
	public static String TestSummaryComponent_URL_Label;
	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}
}
