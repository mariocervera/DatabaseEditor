/*******************************************************************************
 * Copyright (c) 2008 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Emilio Sánchez (Integranova) – Initial API and implementation.
 * [03/04/08] Francisco Javier Cano Muñoz (Prodevelop) - adaptation to Common Navigator Framework
 * Mario Cervera Ubeda (Integranova)
 *
 ******************************************************************************/

package es.cv.gvcase.mdt.db.navigator.actions.model;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.ui.actions.SelectionListenerAction;

import es.cv.gvcase.mdt.common.commands.OpenAsDiagramCommand;
import es.cv.gvcase.mdt.common.commands.wrappers.GMFtoEMFCommandWrapper;

// TODO: Auto-generated Javadoc
/**
 * The Class CreateUMLDiagramAction.
 */
public class CreateSqlDiagramAction extends SelectionListenerAction {

	/** The root e object. */
	private org.eclipse.emf.ecore.EObject rootEObject = null;

	/** The diagram kind. */
	private String diagramKind = null;

	/** The gmf resource. */
	private Resource gmfResource = null;

	/**
	 * Instantiates a new creates the uml diagram action.
	 * 
	 * @param text
	 *            the text
	 * @param rootEObject
	 *            the root e object
	 * @param diagramKind
	 *            the diagram kind
	 * @param resource
	 *            the resource
	 */
	public CreateSqlDiagramAction(String text,
			org.eclipse.emf.ecore.EObject rootEObject, String diagramKind,
			Resource resource) {
		super(text);
		this.rootEObject = rootEObject;
		this.diagramKind = diagramKind;
		this.gmfResource = resource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		Command command = new GMFtoEMFCommandWrapper(new OpenAsDiagramCommand(
				gmfResource, rootEObject, diagramKind));
		TransactionalEditingDomain domain = TransactionUtil
				.getEditingDomain(rootEObject);
		if (domain != null) {
			domain.getCommandStack().execute(command);
		} else {
			Log.warning(null, 0,
					"Cannot create diagram without a read-write transaction");
		}
	}

}
