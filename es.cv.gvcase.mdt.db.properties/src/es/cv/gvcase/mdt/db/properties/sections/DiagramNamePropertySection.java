/*******************************************************************************
 * Copyright (c) 2005 AIRBUS FRANCE. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jacques Lescot (Anyware Technologies) - initial API and
 * implementation
 *               Francisco Javier Cano Muñoz (Prodevelop) - Adaptation Diagram name property
 ******************************************************************************/
package es.cv.gvcase.mdt.db.properties.sections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import es.cv.gvcase.mdt.common.sections.AbstractStringPropertySection;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Property section to edit the name of a <Diagram>
 * 
 * @author <a href="mailto:fjcano@prodeelop.es">Francisco Javier Cano Muñoz</a>
 * 
 */
public class DiagramNamePropertySection extends AbstractStringPropertySection {

	/**
	 * <Diagram> name is the feature to modify
	 */
	@Override
	protected EStructuralFeature getFeature() {
		return NotationPackage.eINSTANCE.getDiagram_Name();
	}

	/**
	 * 
	 */
	@Override
	protected String getLabelText() {
		return "Diagram name:";
	}

	private Diagram diagram = null;

	/**
	 * Store the selected <Diagram>
	 */
	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		if (selection instanceof StructuredSelection) {
			StructuredSelection ss = (StructuredSelection) selection;
			Object selected = ss.getFirstElement();
			if (selected instanceof EditPart) {
				Object model = ((EditPart) selected).getModel();
				if (model instanceof Diagram) {
					diagram = (Diagram) model;
				}
			} else if (selected instanceof Diagram) {
				diagram = (Diagram) selected;
			} else {
				diagram = null;
			}
		}
	}

	/**
	 * The <EObject> we want for this section is a <Diagram>
	 */
	@Override
	protected EObject getEObject() {
		return diagram;
	}

}
