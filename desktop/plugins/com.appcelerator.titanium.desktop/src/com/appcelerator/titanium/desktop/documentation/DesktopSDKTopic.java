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
package com.appcelerator.titanium.desktop.documentation;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.help.ITopic;
import org.eclipse.help.IUAElement;

/**
 * Link for a currently installed Titanium Mobile SDK
 */
public class DesktopSDKTopic implements ITopic
{
	private String label;
	private String href;

	public DesktopSDKTopic(String label, String href)
	{
		this.label = label;
		this.href = href;
	}

	public boolean isEnabled(IEvaluationContext context)
	{
		return true;
	}

	public IUAElement[] getChildren()
	{
		return getSubtopics();
	}

	public ITopic[] getSubtopics()
	{
		return new ITopic[0];
	}

	public String getLabel()
	{
		return label;
	}

	public String getHref()
	{
		return href;
	}
}
