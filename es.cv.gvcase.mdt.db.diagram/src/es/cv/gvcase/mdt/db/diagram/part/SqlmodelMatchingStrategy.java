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

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;

import es.cv.gvcase.mdt.common.util.MDTUtil;

/**
 * @generated
 */
public class SqlmodelMatchingStrategy implements IEditorMatchingStrategy {

	/**
	 * @generated
	 */
	public boolean matches(IEditorReference editorRef, IEditorInput newInput) {
		return MDTUtil.checkSameEditorInput(editorRef, newInput);

	}

}
