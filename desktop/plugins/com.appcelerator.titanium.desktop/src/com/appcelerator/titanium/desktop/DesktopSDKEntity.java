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
package com.appcelerator.titanium.desktop;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IPath;

import com.appcelerator.titanium.core.mobile.SDKEntity;
import com.appcelerator.titanium.core.tiapp.TiManifestModel.MODULE;

/**
 * Desktop SDK entity.
 * 
 * @author Shalom Gibly <sgibly@appcelerator.com>
 */
public class DesktopSDKEntity extends SDKEntity
{
	private Set<MODULE> modules;

	/**
	 * Constructs a new DesktopSDKEntity
	 * 
	 * @param version
	 *            SDK version
	 * @param path
	 *            SDK path
	 * @param modules
	 *            A list of module names
	 */
	public DesktopSDKEntity(String version, IPath path, List<String> modules)
	{
		super(version, path);
		setModules(modules);
	}

	/**
	 * Sets the modules for this entity.<br>
	 * Note that only the known modules will be eventually added to this entity. See {@link MODULE} for the list of
	 * known items.
	 * 
	 * @param moduleNames
	 *            - A list of module names.
	 * @see MODULE
	 */
	protected void setModules(List<String> moduleNames)
	{
		modules = EnumSet.noneOf(MODULE.class);
		for (String name : moduleNames)
		{
			if (MODULE.isValid(name))
			{
				modules.add(MODULE.getModule(name));
			}
		}
	}

	/**
	 * Returns a copy of a {@link Set} for the supported desktop-SDK modules.
	 * 
	 * @return An {@link Set} of modules.
	 */
	public Set<MODULE> getModules()
	{
		return EnumSet.copyOf(modules);
	}
}
