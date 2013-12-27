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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

import com.appcelerator.titanium.core.TitaniumCorePlugin;
import com.appcelerator.titanium.core.user.ITitaniumUser;
import com.aptana.ui.util.UIUtils;

public class PackageHandler extends AbstractHandler
{

	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		ITitaniumUser user = TitaniumCorePlugin.getDefault().getUserManager().getSignedInUser();
		if (user == null)
		{
			// Force the login!
			// TODO Make it easy for user to sign in from here!
			MessageDialog.openError(UIUtils.getActiveShell(), Messages.PackageHandler_LoginTitle,
					Messages.PackageHandler_LoginMessage);
			return null;
		}

		DistributeDesktopWizard wizard = new DistributeDesktopWizard();
		WizardDialog dialog = new WizardDialog(UIUtils.getActiveShell(), wizard);
		dialog.setPageSize(400, 300);

		IResource selectedResource = UIUtils.getSelectedResource((IEvaluationContext) event.getApplicationContext());
		if (selectedResource != null)
		{
			wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(selectedResource));
		}
		dialog.open();
		return null;
	}

}
