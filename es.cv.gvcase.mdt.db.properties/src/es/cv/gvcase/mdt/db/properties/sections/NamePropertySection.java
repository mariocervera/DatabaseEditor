/***************************************************************************
* Copyright (c) 2008 Conselleria de Infraestructuras y Transporte,
* Generalitat de la Comunitat Valenciana . All rights reserved. This program
* and the accompanying materials are made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors: Mario Cervera Ubeda (Prodevelop) - initial API and implementation
*
******************************************************************************/
package es.cv.gvcase.mdt.db.properties.sections;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import es.cv.gvcase.mdt.common.sections.AbstractStringPropertySection;

public class NamePropertySection extends AbstractStringPropertySection {
	
	/**
	 * @see org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractTextPropertySection#getLabelText()
	 */
	protected String getLabelText() {
		return "Name:";
	}

	/**
	 * @see org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractTextPropertySection#getFeature()
	 */
	protected EStructuralFeature getFeature() {
		return EcorePackage.eINSTANCE.getENamedElement_Name();
	}
	
	@Override
	protected Object getNewFeatureValue(String newText) {

		if(newText.equals("")) return null;
		
		return super.getNewFeatureValue(newText);
	}
}
