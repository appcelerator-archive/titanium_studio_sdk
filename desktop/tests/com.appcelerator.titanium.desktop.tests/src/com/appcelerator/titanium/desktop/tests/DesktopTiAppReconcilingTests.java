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
package com.appcelerator.titanium.desktop.tests;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

import com.appcelerator.titanium.core.ITiAppModel;
import com.appcelerator.titanium.core.SDKEntity;
import com.appcelerator.titanium.core.TiAppReconciler;
import com.appcelerator.titanium.core.TiModelFactory;
import com.appcelerator.titanium.core.tiapp.TiAppModelUtil;
import com.appcelerator.titanium.core.tiapp.TiManifestModel;
import com.appcelerator.titanium.core.tiapp.TiManifestModel.MODULE;
import com.appcelerator.titanium.desktop.DesktopSDKEntity;
import com.aptana.core.util.ResourceUtil;
import com.aptana.core.util.StringUtil;

/**
 * @author Shalom Gibly <sgibly@appcelerator.com>
 */
@SuppressWarnings("nls")
public class DesktopTiAppReconcilingTests extends TestCase
{
	protected static String TEST_BUNDLE_ID = "com.appcelerator.titanium.desktop.tests";
	protected static final String TEST_RESOURCES = ResourceUtil.resourcePathToString(Platform.getBundle(TEST_BUNDLE_ID)
			.getEntry("resources/tiapp/reconciler"));
	private static final IPath MANIFEST = Path.fromOSString(TEST_RESOURCES).append("manifest");
	private static final IPath TIAPP = Path.fromOSString(TEST_RESOURCES).append("tiapp.xml");

	private List<SDKEntity> mockupDesktopSDKs;
	private TiManifestModel manifestModel;
	private ITiAppModel tiAppModel;

	/**
	 * Constucts a new DesktopTiAppReconcilingTests
	 */
	public DesktopTiAppReconcilingTests()
	{
		mockupDesktopSDKs = createMockupDesktopSDKs();
	}

	@Override
	protected void setUp() throws Exception
	{
		manifestModel = TiModelFactory.createTiManifestModel(MANIFEST);
		tiAppModel = TiModelFactory.createTiAppModel(TIAPP);
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception
	{
		manifestModel = null;
		tiAppModel = null;
		super.tearDown();
	}

	/**
	 * Test a case where the tiapp contains a valid SDK and no update is needed.
	 * 
	 * @throws Exception
	 */
	public void testSDKAdjustNotNeeded() throws Exception
	{
		String preReconcilingVersion = TiAppModelUtil.getSDKVersion(tiAppModel);
		List<String> problems = TiAppReconciler.collectAndRepair(tiAppModel, manifestModel, mockupDesktopSDKs,
				mockupDesktopSDKs.get(0));
		assertFalse("Expected a 'non-dirty' TiApp model", tiAppModel.isDirty());
		assertEquals("Expected 0 problems", 0, problems.size());
		assertEquals("Wrong SDK version after reconciling", preReconcilingVersion,
				TiAppModelUtil.getSDKVersion(tiAppModel));
	}

	/**
	 * Test that a missing SDK in the tiapp is grabbed from the manifest during the reconciling.
	 * 
	 * @throws Exception
	 */
	public void testSDKAdjustWhenMissingSDK() throws Exception
	{
		String preReconcilingManifestSDK = manifestModel.getModuleValue(MODULE.RUNTIME);
		// First, set the SDK entry to an empty string (simulates a missing entry)
		TiAppModelUtil.setSDKVersion(tiAppModel, StringUtil.EMPTY);
		assertEquals("Expected an empty SDK version", StringUtil.EMPTY, TiAppModelUtil.getSDKVersion(tiAppModel));
		// Collect the problems
		List<String> problems = TiAppReconciler.collectAndRepair(tiAppModel, manifestModel, mockupDesktopSDKs,
				mockupDesktopSDKs.get(0));
		assertEquals("Expected 1 problems", 1, problems.size());
		assertEquals("Wrong SDK version after reconciling", preReconcilingManifestSDK,
				TiAppModelUtil.getSDKVersion(tiAppModel));
	}

	public void testSDKAdjustToDefault() throws Exception
	{
		// Set a non-existing SDK in the manifest
		manifestModel.setModule(MODULE.RUNTIME, "0.9.0");
		// First, set the SDK entry to an empty string (simulates a missing entry)
		TiAppModelUtil.setSDKVersion(tiAppModel, StringUtil.EMPTY);
		assertEquals("Expected an empty SDK version", StringUtil.EMPTY, TiAppModelUtil.getSDKVersion(tiAppModel));
		// Collect the problems
		List<String> problems = TiAppReconciler.collectAndRepair(tiAppModel, manifestModel, mockupDesktopSDKs,
				mockupDesktopSDKs.get(0));
		assertEquals("Expected 1 problems", 1, problems.size());
		assertEquals("Wrong SDK version after reconciling", mockupDesktopSDKs.get(0).getVersion(),
				TiAppModelUtil.getSDKVersion(tiAppModel));
	}

	public void testSDKAdjustToWrongVersion() throws Exception
	{
		// this code should throw an IllegalArgumentException since the given SDK version does not exist in the
		// available ones.
		try
		{
			TiAppReconciler.collectAndRepair(tiAppModel, manifestModel, mockupDesktopSDKs, new DesktopSDKEntity(
					"1.9.0", Path.fromOSString(StringUtil.EMPTY), getModules()));
		}
		catch (IllegalArgumentException e)
		{
			assertFalse("Expected a 'non-dirty' TiApp model", tiAppModel.isDirty());
			assertEquals("Wrong SDK version after reconciling", "1.0.0", TiAppModelUtil.getSDKVersion(tiAppModel));
			return;
		}
		assertTrue("Expected an IllegalArgumentException", false);
	}

	/**
	 * Create mockup desktop SDKs
	 */
	protected List<SDKEntity> createMockupDesktopSDKs()
	{
		List<SDKEntity> entities = new ArrayList<SDKEntity>();
		List<String> modules = getModules();
		entities.add(new DesktopSDKEntity("1.1.0", Path.fromOSString(StringUtil.EMPTY), modules));
		// Remove some of the module for the second entity
		modules.remove(0);
		modules.remove(0);
		modules.remove(0);
		entities.add(new DesktopSDKEntity("1.0.0", Path.fromOSString(StringUtil.EMPTY), modules));
		return entities;
	}

	/**
	 * Create and return a modules list for the tests
	 */
	protected List<String> getModules()
	{
		List<String> modules = new ArrayList<String>();
		for (MODULE m : EnumSet.allOf(MODULE.class))
		{
			modules.add(m.getKey());
		}
		return modules;
	}

}