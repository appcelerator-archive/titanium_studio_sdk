/**
 * Appcelerator Titanium Studio
 * Copyright (c) 2012 by Appcelerator, Inc. All Rights Reserved.
 * Proprietary and Confidential - This source code is not for redistribution
 */
package com.appcelerator.titanium.publish.sample.ui;

import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.appcelerator.titanium.publish.sample.SampleProviderPlugin;
import com.appcelerator.titanium.publish.sample.SamplePublishType;
import com.aptana.core.util.StatusCollector;
import com.aptana.core.util.StringUtil;

/**
 * Configuration control that collects the description and private level
 */
public class SampleConfigurationControl
{
	private final Map<String, Object> model;
	private final StatusCollector statusCollector;
	private Text descriptionText;
	private Button privateButton;

	/**
	 * Constructs a new Test configuration control.
	 * 
	 * @param model
	 * @param statusCollector
	 */
	public SampleConfigurationControl(Map<String, Object> model, StatusCollector statusCollector)
	{
		this.model = model;
		this.statusCollector = statusCollector;
	}

	/**
	 * Creates the Test configuration controls (the metadata fields)
	 * 
	 * @param parent
	 */
	public void create(Composite parent)
	{
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(GridLayoutFactory.swtDefaults().numColumns(3).create());
		composite.setLayoutData(GridDataFactory.swtDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).create());

		// Multiline
		Label label = new Label(composite, SWT.NONE);
		label.setText(StringUtil.makeFormLabel(Messages.TestSummaryComponent_Description_Label));
		label.setLayoutData(GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.BEGINNING).create());
		descriptionText = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		Point size = descriptionText.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		descriptionText.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).grab(true, false)
				.hint(SWT.DEFAULT, Math.max(size.y, 20) * 2).create());

		// Checkbox
		label = new Label(composite, SWT.NONE); // filler
		privateButton = new Button(composite, SWT.CHECK);
		privateButton.setText(Messages.TestSummaryComponent_Private_Label);
		privateButton.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());

		attachListeners();
		validateAndSave();
	}

	/**
	 * Attach listeners when needed.
	 */
	private void attachListeners()
	{
		ValidateAndSaveListener listener = new ValidateAndSaveListener();
		descriptionText.addModifyListener(listener);
		privateButton.addSelectionListener(listener);
	}

	/**
	 * Validate the contents, and save the values in the model {@link Map} when it's valid. Notify the problems through
	 * the {@link StatusCollector}.
	 */
	private void validateAndSave()
	{
		if (StringUtil.isEmpty(descriptionText.getText()))
		{
			statusCollector.setStatus(new Status(IStatus.ERROR, SampleProviderPlugin.PLUGIN_ID, Messages.TestConfigurationControl_Description_error),
					descriptionText);
		}
		else
		{
			statusCollector.clearStatus(descriptionText);
		}

		// Store the values in the model
		boolean isPrivate = privateButton.getSelection();
		model.put(SamplePublishType.PUBLISH_PROPERTY_PRIVATE, isPrivate);

		String descValue = descriptionText.getText().trim();
		model.put(SamplePublishType.PUBLISH_PROPERTY_DESCRIPTION, descValue);

	}

	/**
	 * A listener that calls validateAndSave on events.
	 */
	private class ValidateAndSaveListener implements ModifyListener, SelectionListener
	{

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetSelected(SelectionEvent e)
		{
			validateAndSave();
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetDefaultSelected(SelectionEvent e)
		{
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
		 */
		public void modifyText(ModifyEvent e)
		{
			validateAndSave();
		}
	}
}
