/*******************************************************************************
 * Copyright (c) 2007 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Javier Muñoz (Integranova) – Initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager.internal.ui.dialogs;

import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.LoadResourceAction.LoadResourceDialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

public class LoadProjectDialog extends LoadResourceDialog {

	private String nature = "";

	public LoadProjectDialog(Shell parent) {
		super(parent);
	}

	public LoadProjectDialog(Shell parent, String requiredNature) {
		super(parent);
		this.nature = requiredNature;
	}

	public LoadProjectDialog(Shell parent, EditingDomain domain) {
		super(parent, domain);
	}

	@Override
	protected void prepareBrowseWorkspaceButton(Button browseWorkspaceButton) {
		{
			browseWorkspaceButton.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent event) {

					IContainer container = null;

					ArrayList<ViewerFilter> filters = new ArrayList<ViewerFilter>();
					filters.add(((ViewerFilter) new ProjectViewFilter()));

					IContainer[] containers = WorkspaceResourceDialog.openFolderSelection(getShell(), null, null,
							false, null, filters);
					if (containers.length != 0) {
						container = containers[0];
					}

					if (container != null) {
						uriField.setText(URI.createPlatformResourceURI(container.getFullPath().toString(), true)
								.toString());
					}

				}
			});
		}
	}

	private class ProjectViewFilter extends ViewerFilter {

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (element instanceof IProject) {
				try {
					if (((IProject) element).getNature(nature) != null)
						return true;
				} catch (CoreException e) {
					return false;
				}
			}
			return false;
		}

	}

	@Override
	protected void prepareBrowseFileSystemButton(Button browseFileSystemButton) {
		browseFileSystemButton.setEnabled(false);
	}

}
