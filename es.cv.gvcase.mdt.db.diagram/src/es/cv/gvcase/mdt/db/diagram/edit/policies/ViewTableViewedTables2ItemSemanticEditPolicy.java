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
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyReferenceCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;

import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelElementTypes;

/**
 * @generated
 */
public class ViewTableViewedTables2ItemSemanticEditPolicy extends
		SqlmodelBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	public ViewTableViewedTables2ItemSemanticEditPolicy() {
		super(SqlmodelElementTypes.ViewTableViewedTables_3002);
	}

	/**
	 * Modified to remove all columns belonging to referenced table being
	 * deleted from viewedColumns
	 * 
	 * Modified to remove the referenced table from viewedTables
	 * 
	 * @author <a href="mailto:fjcano@prodeelop.es">Francisco Javier Cano Muñoz</a>, mcervera
	 * @generated
	 */
	protected Command getDestroyReferenceCommand(DestroyReferenceRequest req) {
		return getGEFWrapper(new DestroyReferenceCommand(req));
	}
}
