/***************************************************************************
 * Copyright (c) 2007 Conselleria de Infraestructuras y Transporte,
 * Generalitat de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Mario Cervera Ubeda (Integranova)
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.diagram.part;

import org.eclipse.emf.ecore.EObject;

import es.cv.gvcase.mdt.common.provider.INodeDescriptor;

/**
 * @generated
 */
public class SqlmodelNodeDescriptor implements INodeDescriptor {

	/**
	 * @generated
	 */
	private EObject myModelElement;

	/**
	 * @generated
	 */
	private int myVisualID;

	/**
	 * @generated
	 */
	private String myType;

	/**
	 * @generated
	 */
	public SqlmodelNodeDescriptor(EObject modelElement, int visualID) {
		myModelElement = modelElement;
		myVisualID = visualID;
	}

	/**
	 * @generated
	 */
	public EObject getModelElement() {
		return myModelElement;
	}

	/**
	 * @generated
	 */
	public int getVisualID() {
		return myVisualID;
	}

	/**
	 * @generated
	 */
	public String getType() {
		if (myType == null) {
			myType = SqlmodelVisualIDRegistry.getType(getVisualID());
		}
		return myType;
	}

}
