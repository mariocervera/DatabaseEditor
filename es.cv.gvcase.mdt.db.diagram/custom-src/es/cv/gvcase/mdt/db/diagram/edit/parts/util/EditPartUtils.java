/*******************************************************************************
 * Copyright (c) 2007 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Francisco Javier Cano Mu�oz (Prodevelop) - Initial API and implementation.
 * Mario Cervera Ubeda (Integranova)
 *
 ******************************************************************************/

package es.cv.gvcase.mdt.db.diagram.edit.parts.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;

/**
 * Has several utility methods for EditPart.
 * 
 * @author <a href="mailto:fjcano@prodeelop.es">Francisco Javier Cano Muñoz</a>
 */
public class EditPartUtils {

	/**
	 * Refresh edit part and children.
	 * 
	 * @param editPart the edit part
	 */
	public static void refreshEditPartAndChildren(EditPart editPart){
		
		if (editPart == null) {
			return;
		}
		
		List children = editPart.getChildren();
		for (Object o : children) {
			if (o instanceof EditPart) {
				refreshEditPartAndChildren((EditPart) o);
			}
		}
		
		editPart.refresh();
	}
	
	
	/**
	 * Gets a list of all GraphicalEditParts in the diagram of the given EditPart.
	 * @param editPart Any <code>EditPart</code> in the diagram. The TopGraphicalNode will be found from this.
	 * @return a list containing all <code>GraphicalEditPart</code> in the diagram.
	 */
	public static List<IGraphicalEditPart> getAllEditParts(EditPart editPart) {
		
		if (editPart == null) {
			return null;
		}
			
		EditPart topEditPart = getTopMostEditPart(editPart);
		
		List<IGraphicalEditPart> editParts = new ArrayList<IGraphicalEditPart>();
		
		if (editPart instanceof IGraphicalEditPart) {
			editParts.add((IGraphicalEditPart) editPart);
		}
		addEditPartGraphicalChildren(topEditPart, editParts);
		
		return editParts;
	}
	
	/**
	 * Returns the upper most EditPart in the hierarchy of the given EditPart
	 * @param editPart A non-null EditPart
	 * @return The upper most EditPart in the hierarchy; may be the same EditPart
	 */
	public static EditPart getTopMostEditPart(EditPart editPart) {
		
		if (editPart == null) {
			return null;
		}
		
		EditPart actual, parent;
		
		actual = editPart;
		
		while ((parent = actual.getParent()) != null) {
			actual = parent;
		}
		
		return actual;
	}
	
	private static void addEditPartGraphicalChildren(EditPart editPart,
			List<IGraphicalEditPart> list) {

		if (editPart == null) {
			return;
		}

		List<EditPart> children = editPart.getChildren();

		for (EditPart ep : children) {
			if (ep instanceof IGraphicalEditPart) {
				list.add((IGraphicalEditPart) ep);
			}
			addEditPartGraphicalChildren(ep, list);
		}
	}
}
