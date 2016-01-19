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
package es.cv.gvcase.mdt.db.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;

import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelElementTypes;

/**
 * @generated
 */
public class ForeignKeyItemSemanticEditPolicy extends
		SqlmodelBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	public ForeignKeyItemSemanticEditPolicy() {
		super(SqlmodelElementTypes.ForeignKey_3001);
	}

	/**
	 * @generated
	 */
	protected Command getDestroyElementCommand(DestroyElementRequest req) {
		return getGEFWrapper(new es.cv.gvcase.mdt.common.commands.DestroyElementCommand(
				req));
	}

}
