/**
 * Copyright 2012 Appcelerator, Inc.
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

import java.util.EnumSet;
import java.util.Set;

import com.appcelerator.titanium.core.tiapp.TiManifestModel;
import com.appcelerator.titanium.core.tiapp.TiManifestModel.MODULE;
import com.aptana.core.logging.IdeLog;
import com.aptana.core.util.StringUtil;

/**
 * Desktop manifest utils.
 * 
 * @author Shalom Gibly <sgibly@appcelerator.com>
 */
public class TitaniumDesktopManifestUtil
{
	/**
	 * Supported language modules (e.g. Ruby, PHP and Python)
	 */
	public static final Set<MODULE> SUPPORTED_LANGUAGE_MODULES = EnumSet.of(MODULE.RUBY, MODULE.PYTHON, MODULE.PHP);

	/**
	 * Update the SDK version directive in the manifest file.
	 * 
	 * @param manifestModel
	 * @param version
	 * @param sdkEntity
	 */
	public static void updateSDKVersion(TiManifestModel manifestModel, String version, DesktopSDKEntity sdkEntity)
	{
		if (manifestModel == null || version == null || sdkEntity == null)
		{
			IdeLog.logWarning(DesktopPlugin.getDefault(),
					"Fail update the SDK version in the manifest. Null value passed.", IDebugScopes.DEBUG); //$NON-NLS-1$
			return;
		}
		// Collect the language modules we have in the manifest before we update it. This will allow us maintaining them
		// during the update.
		Set<MODULE> preselectedLanguageModules = EnumSet.noneOf(MODULE.class);
		if (!StringUtil.isEmpty(manifestModel.getModuleValue(MODULE.RUBY)))
		{
			preselectedLanguageModules.add(MODULE.RUBY);
		}
		if (!StringUtil.isEmpty(manifestModel.getModuleValue(MODULE.PYTHON)))
		{
			preselectedLanguageModules.add(MODULE.PYTHON);
		}
		if (!StringUtil.isEmpty(manifestModel.getModuleValue(MODULE.PHP)))
		{
			preselectedLanguageModules.add(MODULE.PHP);
		}
		updateSDKVersion(manifestModel, version, sdkEntity, preselectedLanguageModules);
	}

	/**
	 * Update the SDK version directive in the manifest file.
	 * 
	 * @param manifestModel
	 * @param version
	 * @param sdkEntity
	 * @param selectedLanguageModules
	 *            The language modules to maintain during the update.
	 */
	public static void updateSDKVersion(TiManifestModel manifestModel, String version, DesktopSDKEntity sdkEntity,
			Set<MODULE> selectedLanguageModules)
	{
		if (manifestModel == null || version == null || sdkEntity == null || selectedLanguageModules == null)
		{
			IdeLog.logWarning(DesktopPlugin.getDefault(),
					"Fail update the SDK version in the manifest. Null value passed.", IDebugScopes.DEBUG); //$NON-NLS-1$
			return;
		}
		Set<MODULE> modules = sdkEntity.getModules();
		manifestModel.setModules(modules.toArray(new MODULE[modules.size()]), version, true, true);
		// The setModules operation will add missing modules, so we need to make sure that we don't add any
		// new language-modules into the manifest.
		Set<MODULE> unsupportedLanguageModules = EnumSet.copyOf(SUPPORTED_LANGUAGE_MODULES);
		unsupportedLanguageModules.removeAll(selectedLanguageModules);
		for (MODULE toRemove : unsupportedLanguageModules)
		{
			manifestModel.setModule(toRemove, null);
		}
		if (manifestModel.isDirty())
		{
			manifestModel.save();
		}
	}
}
