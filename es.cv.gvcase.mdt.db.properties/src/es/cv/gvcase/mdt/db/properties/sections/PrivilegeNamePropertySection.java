/***************************************************************************
* Copyright (c) 2007 Conselleria de Infraestructuras y Transporte,
* Generalitat de la Comunitat Valenciana . All rights reserved. This program
* and the accompanying materials are made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors: Mario Cervera Ubeda (Prodevelop) � initial API and implementation
*
******************************************************************************/
package es.cv.gvcase.mdt.db.properties.sections;

import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractChooserPropertySection;
import org.eclipse.emf.edit.domain.EditingDomain;

public class PrivilegeNamePropertySection extends AbstractChooserPropertySection {

	@Override
	protected EditingDomain getEditingDomain() {
		try {
			return super.getEditingDomain();
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}
	
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
	
	/**
	 * @see org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractChooserPropertySection#getFeatureValue()
	 */
	protected Object getFeatureValue() {
		
		SQLObject object = (SQLObject)getEObject();
		
		return object.getName();
	}
	
	@Override
	protected Object[] getComboFeatureValues() {

		Object[] names = new Object[5];
		
		names[0] = "SELECT";
		names[1] = "INSERT";
		names[2] = "DELETE";
		names[3] = "UPDATE";
		names[4] = "ALL";
		
		return names;
	}

}
