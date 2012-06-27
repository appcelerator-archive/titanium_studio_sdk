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
package com.appcelerator.titanium.desktop;

/**
 * Debug scopes used for logging errors, warnings and info into the Studio's log file.
 * 
 * @author Shalom Gibly <sgibly@appcelerator.com>
 */
public interface IDebugScopes
{

	/**
	 * Generic debug scope
	 */
	String DEBUG = DesktopPlugin.PLUGIN_ID + "/debug"; //$NON-NLS-1$
}
