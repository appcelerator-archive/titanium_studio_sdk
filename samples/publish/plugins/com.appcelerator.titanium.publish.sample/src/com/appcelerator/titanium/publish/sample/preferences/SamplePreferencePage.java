/**
 * Appcelerator Titanium Studio
 * Copyright (c) 2012 by Appcelerator, Inc. All Rights Reserved.
 * Proprietary and Confidential - This source code is not for redistribution
 */
package com.appcelerator.titanium.publish.sample.preferences;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.appcelerator.titanium.publish.preferences.AbstractPublishTargetPreferencePage;
import com.appcelerator.titanium.publish.sample.SamplePublishTarget;
import com.appcelerator.titanium.publish.sample.SamplePublishType;
import com.aptana.core.util.StringUtil;

/**
 * Preference page that displays a target's URL, username and password
 */
public class SamplePreferencePage extends AbstractPublishTargetPreferencePage
{

	private static final String[] COLUMN_HEADERS = { Messages.TestPreferencePage_URL_Columns, Messages.TestPreferencePage_Username_column, Messages.TestPreferencePage_Password_column };
	private static final int[] COLUMN_CHAR_WIDTHS = { 40, 20, 10 };

	/*
	 * (non-Javadoc)
	 * @see
	 * com.appcelerator.titanium.publish.preferences.AbstractPublishTargetPreferencePage#createPublishTargetTableColumns
	 * (org.eclipse.swt.widgets.Table)
	 */
	@Override
	protected void createPublishTargetTableColumns(Table table)
	{
		for (int i = 0; i < COLUMN_HEADERS.length; ++i)
		{
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setText(COLUMN_HEADERS[i]);
			column.setResizable(true);
			column.setWidth(convertWidthInCharsToPixels(COLUMN_CHAR_WIDTHS[i]));

			column.addSelectionListener(new SelectionListener()
			{

				public void widgetDefaultSelected(SelectionEvent e)
				{
					columnSelected((TableColumn) e.widget);
				}

				public void widgetSelected(SelectionEvent e)
				{
					columnSelected((TableColumn) e.widget);
				}
			});
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.appcelerator.titanium.publish.preferences.AbstractPublishTargetPreferencePage#getPublishTypeId()
	 */
	@Override
	protected String getPublishTypeId()
	{
		return SamplePublishType.ID;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.appcelerator.titanium.publish.preferences.AbstractPublishTargetPreferencePage#createPublishTargetLabelProvider
	 * ()
	 */
	@Override
	protected DefaultPublishTargetLabelProvider createPublishTargetLabelProvider()
	{
		return new TestTargetLabelProvider();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.appcelerator.titanium.publish.preferences.AbstractPublishTargetPreferencePage#createPublishTargetComparator()
	 */
	@Override
	protected DefaultPublishTargetComparator createPublishTargetComparator()
	{
		return new TestTargetComparator();
	}

	private static class TestTargetLabelProvider extends DefaultPublishTargetLabelProvider
	{

		public String getColumnText(Object element, int columnIndex)
		{
			if (element instanceof SamplePublishTarget)
			{
				SamplePublishTarget target = (SamplePublishTarget) element;
				switch (columnIndex)
				{
					case 0:
						return target.getName();
					case 1:
						return target.getUsername();
					case 2:
						return StringUtil.pad(StringUtil.EMPTY, 4, '*');
				}
			}
			return super.getColumnText(element, columnIndex);
		}
	}

	private static class TestTargetComparator extends DefaultPublishTargetComparator
	{

		@Override
		public int compare(Viewer viewer, Object e1, Object e2)
		{
			if (!(e1 instanceof SamplePublishTarget) || !(e2 instanceof SamplePublishTarget))
			{
				return super.compare(viewer, e1, e2);
			}
			SamplePublishTarget a1 = (SamplePublishTarget) e1;
			SamplePublishTarget a2 = (SamplePublishTarget) e2;
			String key1, key2;
			switch (getSortedColumn())
			{
				case 1:
					key1 = a1.getUsername();
					key2 = a2.getUsername();
					break;
				case 2:
					key1 = StringUtil.EMPTY;
					key2 = key1;
					break;
				default:
					key1 = a1.getName();
					key2 = a2.getName();
			}
			return key1.compareToIgnoreCase(key2) * (isAscending() ? 1 : -1);
		}
	}
}
