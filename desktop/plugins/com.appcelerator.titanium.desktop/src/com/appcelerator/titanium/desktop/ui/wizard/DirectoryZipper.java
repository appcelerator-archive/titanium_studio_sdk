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
package com.appcelerator.titanium.desktop.ui.wizard;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;

import com.aptana.core.util.IOUtil;

/**
 * Class to zip up a directory into a zipfile.
 * 
 * @author cwilliams
 */
class DirectoryZipper
{

	/**
	 * Zips a directory into a zip file
	 * 
	 * @param srcDir
	 *            The directory to zip up
	 * @param zipFile
	 *            The target file to create
	 * @param monitor
	 * @throws IOException
	 */
	IStatus createZip(File srcDir, File zipFile, IProgressMonitor monitor) throws IOException
	{
		SubMonitor sub = SubMonitor.convert(monitor,
				MessageFormat.format(Messages.DirectoryZipper_JobTitle, srcDir.getAbsolutePath()), 100);
		ZipOutputStream out = null;
		try
		{
			// Create the ZIP file
			out = new ZipOutputStream(new FileOutputStream(zipFile));
			IPath base = Path.fromOSString(srcDir.getAbsolutePath());
			addDirectory(base, srcDir, out, sub.newChild(100));
			out.flush();
		}
		finally
		{
			try
			{
				if (out != null)
				{
					out.close();
				}
			}
			catch (IOException e)
			{
				// ignore
			}
			sub.done();
		}
		return Status.OK_STATUS;
	}

	/**
	 * Recursively add files in a directory to a ZipOutputStream.
	 * 
	 * @param base
	 *            The base path to use, so filepaths added to zip are relative to this
	 * @param dir
	 *            The directory to zip up
	 * @param out
	 *            The stream to add the entries to
	 * @param monitor
	 * @throws IOException
	 */
	private void addDirectory(IPath base, File dir, ZipOutputStream out, IProgressMonitor monitor) throws IOException
	{
		if (dir == null)
		{
			return;
		}
		File children[] = dir.listFiles();
		if (children == null)
		{
			return;
		}
		SubMonitor sub = SubMonitor.convert(monitor, children.length);
		try
		{
			for (File child : children)
			{
				if (child.isDirectory())
				{
					// recurse!
					addDirectory(base, child, out, sub.newChild(1));
				}
				else
				{
					addFile(base, child, out);
					sub.worked(1);
				}
			}
		}
		finally
		{
			sub.done();
		}
	}

	/**
	 * Adds a file to a ZipOutputStream. ZipEntry name is filename relative to base.
	 * 
	 * @param file
	 *            The file to zip up
	 * @param base
	 *            The base path to use, so filepaths added to zip are relative to this
	 * @param out
	 *            The stream to add the entries to
	 * @throws IOException
	 */
	private void addFile(IPath base, File file, ZipOutputStream out) throws IOException
	{
		BufferedInputStream in = null;
		try
		{
			in = new BufferedInputStream(new FileInputStream(file));
			out.putNextEntry(new ZipEntry(Path.fromOSString(file.getAbsolutePath()).makeRelativeTo(base)
					.toPortableString()));
			IOUtil.pipe(in, out);
			out.closeEntry();
		}
		finally
		{
			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (Exception e)
				{
					// ignore
				}
			}
		}
	}
}
