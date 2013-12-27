/**
 * Copyright 2011-2013 Appcelerator, Inc.
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.help.IToc;
import org.eclipse.help.ITopic;
import org.eclipse.help.IUAElement;
import org.osgi.framework.Version;

import com.appcelerator.titanium.desktop.DesktopPlugin;
import com.appcelerator.titanium.desktop.DesktopSDKEntity;
import com.appcelerator.titanium.desktop.TitaniumDesktopSDKLocator;
import com.aptana.core.logging.IdeLog;
import com.aptana.core.util.StringUtil;
import com.aptana.core.util.VersionUtil;

/**
 * Class to automatically parse the currently installed Titanium Desktop SDKs and crate a link to the docs for each
 */
public class DesktopSDKToc implements IToc
{
	public DesktopSDKToc()
	{
	}

	public boolean isEnabled(IEvaluationContext context)
	{
		return true;
	}

	public IUAElement[] getChildren()
	{
		return getTopics();
	}

	public String getHref()
	{
		return null;
	}

	public String getLabel()
	{
		return Messages.SimpleToc_TitaniumDesktopSDKs;
	}

	public ITopic[] getTopics()
	{
		List<ITopic> al = new ArrayList<ITopic>();
		List<DesktopSDKEntity> sdks = TitaniumDesktopSDKLocator.getInstance().getAvailable();
		for (DesktopSDKEntity sdkEntity : sdks)
		{
			try
			{
				Version v = VersionUtil.parseVersion(sdkEntity.getVersion());
				if (StringUtil.EMPTY.equals(v.getQualifier()) || Integer.parseInt(v.getQualifier()) >= 0)
				{
					// version is really 1.1.0 (for example), but needs to be 1.1 for URLs
					al.add(new DesktopSDKTopic(MessageFormat.format(Messages.SimpleToc_TitaniumDesktopSDK,
							sdkEntity.getVersion()), MessageFormat.format(
							"http://studio.appcelerator.com/redirect.php?location=docs_desktop&version={0}.{1}", //$NON-NLS-1$
							v.getMajor(), v.getMinor())));
				}
			}
			catch (NumberFormatException ex)
			{
				// ignore, since we don't want to add items from the continuous build to the TOC ATM (they don't have
				// external doc links)
				IdeLog.logInfo(DesktopPlugin.getDefault(),
						MessageFormat.format("Skipping continuous-build-type version {0}", //$NON-NLS-1$
								sdkEntity.getVersion()));
			}
			catch (IllegalArgumentException ex)
			{
				// ignore, was a SDK like 1.8.0.1.2011 (non-OSGI-compliant)
				IdeLog.logInfo(DesktopPlugin.getDefault(),
						MessageFormat.format("Error parsing non-OSGI-compliant desktop SDK version {0}", //$NON-NLS-1$
								sdkEntity.getVersion()));
			}
		}
		return al.toArray(new ITopic[0]);
	}

	public ITopic getTopic(String href)
	{
		return null;
	}
}
