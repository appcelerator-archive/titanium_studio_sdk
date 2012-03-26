/**
 * Aptana Studio
 * Copyright (c) 2012 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.appcelerator.titanium.publish.sample.preferences;

import org.eclipse.osgi.util.NLS;

/**
 * @author Nam Le <nle@appcelerator.com>
 *
 */
public class Messages extends NLS
{
	private static final String BUNDLE_NAME = "com.appcelerator.titanium.publish.testprovider.preferences.messages"; //$NON-NLS-1$
	public static String TestPreferencePage_Password_column;
	public static String TestPreferencePage_URL_Columns;
	public static String TestPreferencePage_Username_column;
	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}
}
