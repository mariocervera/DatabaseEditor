/*******************************************************************************
 * Copyright (c) 2007-2012 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *  Miguel Llacer (mllacer@prodevelop.es - Prodevelop S.L.) - Initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager;

/**
 * The Class TransformationExtraOperations has to be extended for each specific
 * transformation that needs to execute previous and post operations before and
 * after the transformation. It has to be instantiated and used as argument in
 * the startInitializedTransformation method of the TransformationUIManager
 * class.
 */
public abstract class TransformationExtraOperations {

	/**
	 * Execute previous operations before launching transformation.
	 * 
	 * @param transformationInfo
	 *            the transformation info
	 */
	public void executePreOperations(
			TransformationInformation transformationInfo) {
	}

	/**
	 * Execute post operations after launching transformation.
	 * 
	 * @param transformationInfo
	 *            the transformation info
	 */
	public void executePostOperations(
			TransformationInformation transformationInfo) {
	}

}
