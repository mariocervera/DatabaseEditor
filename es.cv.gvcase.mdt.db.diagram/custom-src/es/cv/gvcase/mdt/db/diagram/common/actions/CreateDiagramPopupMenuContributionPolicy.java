/***************************************************************************
* Copyright (c) 2008 Conselleria de Infraestructuras y Transporte,
* Generalitat de la Comunitat Valenciana . All rights reserved. This program
* and the accompanying materials are made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Francisco Javier Cano Muñoz (Prodevelop) – initial API and implementation
* Mario Cervera Ubeda (Integranova)
*
******************************************************************************/

package es.cv.gvcase.mdt.db.diagram.common.actions;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.IPopupMenuContributionPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

// TODO: Auto-generated Javadoc
/**
 * Filter for "Create diagram" context menu action.
 * 
 * @author <a href="mailto:fjcano@prodeelop.es">Francisco Javier Cano Muñoz</a>
 */
public class CreateDiagramPopupMenuContributionPolicy implements
		IPopupMenuContributionPolicy {

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.IPopupMenuContributionPolicy#appliesTo(org.eclipse.jface.viewers.ISelection, org.eclipse.core.runtime.IConfigurationElement)
	 */
	public boolean appliesTo(ISelection selection,
			IConfigurationElement configuration) {

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ss = ((IStructuredSelection)selection);
			if (ss.size() <= 0 || ss.size() > 1) {
				return false;
			}
			Object first = ss.getFirstElement();
			if (first instanceof IGraphicalEditPart) {
				EObject element = ((IGraphicalEditPart)first).resolveSemanticElement();
				if (element instanceof Schema) {
					return true;
				}
			}
		}
		
		return false;
	}

}
