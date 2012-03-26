/**
 * Appcelerator Titanium Studio
 * Copyright (c) 2012 by Appcelerator, Inc. All Rights Reserved.
 * Proprietary and Confidential - This source code is not for redistribution
 */
package com.appcelerator.titanium.publish.sample.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import com.appcelerator.titanium.publish.IPublishType;
import com.appcelerator.titanium.publish.TitaniumPublishPlugin;
import com.appcelerator.titanium.publish.sample.SamplePublishType;
import com.appcelerator.titanium.publish.ui.wizard.ISummaryWizardPageComponent;
import com.appcelerator.titanium.publish.ui.wizard.PublishWizardConstants;
import com.aptana.core.util.StatusCollector;
import com.aptana.core.util.StringUtil;

/**
 * Summary component that displays the target URL, description and privacy setting
 */
public class SampleSummaryComponent implements ISummaryWizardPageComponent
{
	private List<Control> alignedControls = new ArrayList<Control>();

	/*
	 * (non-Javadoc)
	 * @see
	 * com.appcelerator.titanium.publish.ui.wizard.ISummaryWizardPageComponent#createContents(org.eclipse.swt.widgets
	 * .Composite, java.util.Map, com.aptana.core.util.StatusCollector)
	 */
	public void createContents(Composite parent, Map<String, Object> model, StatusCollector listener)
	{
		IPublishType publishType = TitaniumPublishPlugin.getDefault().getPublishManager()
				.getPublishType(SamplePublishType.ID);
		Group testInfo = new Group(parent, SWT.NONE);
		testInfo.setText(publishType.getName());
		testInfo.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		testInfo.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).create());

		Label targetLabel = new Label(testInfo, SWT.NONE);
		targetLabel.setText(StringUtil.makeFormLabel(Messages.TestSummaryComponent_URL_Label));
		addAlignedControl(targetLabel);

		Label targetValue = new Label(testInfo, SWT.NONE);
		String alias = (String) model.get(PublishWizardConstants.SELECTED_TARGET_ALIAS);
		targetValue.setText(StringUtil.isEmpty(alias) ? StringUtil.EMPTY : alias);

		CLabel descLabel = new CLabel(testInfo, SWT.NONE);
		descLabel.setText(StringUtil.makeFormLabel(Messages.TestSummaryComponent_Description_Label));
		addAlignedControl(descLabel);

		CLabel descValue = new CLabel(testInfo, SWT.LEFT);
		descValue.setLayoutData(GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false)
				.create());
		String notes = (String) model.get(SamplePublishType.PUBLISH_PROPERTY_DESCRIPTION);
		notes = StringUtil.isEmpty(notes) ? StringUtil.EMPTY : notes;
		descValue.setText(notes);
		descValue.setToolTipText(notes);

		Label privateLabel = new Label(testInfo, SWT.NONE);
		privateLabel.setText(StringUtil.makeFormLabel(Messages.TestSummaryComponent_Private_Label));
		addAlignedControl(privateLabel);

		Label privateValue = new Label(testInfo, SWT.NONE);
		Boolean notify = (Boolean) model.get(SamplePublishType.PUBLISH_PROPERTY_PRIVATE);
		privateValue.setText(notify == null || !notify.booleanValue() ? IDialogConstants.NO_LABEL
				: IDialogConstants.YES_LABEL);
	}

	private void addAlignedControl(Control control)
	{
		control.setLayoutData(GridDataFactory.fillDefaults().create());
		alignedControls.add(control);
	}

	/*
	 * (non-Javadoc)
	 * @see com.appcelerator.titanium.publish.ui.wizard.ISummaryWizardPageComponent#getLeftAlignedControls()
	 */
	public List<Control> getLeftAlignedControls()
	{
		return alignedControls;
	}

}
