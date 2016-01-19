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

package es.cv.gvcase.trmanager.internal.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;

public class TransformationCommand extends org.eclipse.core.commands.AbstractHandler {

	public TransformationCommand() {
		super();
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {

		IResource selectedResource;

		ISelectionService selectionService = org.eclipse.ui.PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService();

		IStructuredSelection selection = (IStructuredSelection) selectionService.getSelection();

		selectedResource = (IResource) selection.getFirstElement();

		es.cv.gvcase.trmanager.ui.TransformationUIManager.startInitializedTransformation(event
				.getParameter("es.cv.gvcase.trmanager.transfId"), selectedResource);
		return null;
	}

	public boolean isEnabled() {
		return true;
	}
}
