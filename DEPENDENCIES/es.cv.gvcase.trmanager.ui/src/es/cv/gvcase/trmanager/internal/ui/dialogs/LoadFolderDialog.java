/*******************************************************************************
 * Copyright (c) 2007-2011 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Javier Mu�oz (Integranova) � Initial implementation
 *  Miguel Llacer San Fernando (Prodevelop)
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager.internal.ui.dialogs;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.LoadResourceAction.LoadResourceDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;

public class LoadFolderDialog extends LoadResourceDialog {

	private IResource resource;

	private boolean browseFileSystem;

	public LoadFolderDialog(Shell parent) {
		this(parent, null, true);
	}

	public LoadFolderDialog(Shell parent, IResource inputResource) {
		this(parent, inputResource, true);
	}

	public LoadFolderDialog(Shell parent, IResource inputResource,
			boolean browseFileSystem) {
		super(parent);
		this.resource = inputResource;
		this.browseFileSystem = browseFileSystem;
	}

	public LoadFolderDialog(Shell parent, EditingDomain domain) {
		super(parent, domain);
	}

	@Override
	protected void prepareBrowseWorkspaceButton(Button browseWorkspaceButton) {
		{
			browseWorkspaceButton.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent event) {

					Object[] initialSelection = null;

					if (resource instanceof IContainer) {
						initialSelection = new Object[] { resource };
					} else if (resource instanceof IFile) {
						IContainer parent = resource.getParent();
						while (parent != null) {
							if (parent instanceof IProject) {
								break;
							}
							parent = parent.getParent();
						}
						if (parent instanceof IProject) {
							initialSelection = new Object[] { parent };
						}
					}

					IContainer container = null;

					IContainer[] containers = WorkspaceResourceDialog
							.openFolderSelection(getShell(), null, null, false,
									initialSelection, null);
					if (containers.length != 0) {
						container = containers[0];
					}

					if (container != null) {
						uriField.setText(URI.createPlatformResourceURI(
								container.getFullPath().toString(), true)
								.toString());
					}

				}
			});
		}
	}

	@Override
	protected void prepareBrowseFileSystemButton(Button browseFileSystemButton) {
		if (!browseFileSystem) {
			browseFileSystemButton.setEnabled(false);
		} else {
			browseFileSystemButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					DirectoryDialog dirDialog = new DirectoryDialog(getShell(),
							style);

					String fileName = dirDialog.open();

					if (fileName != null) {
						uriField
								.setText(URI.createFileURI(fileName).toString());
					}
				}
			});
		}
	}
}
