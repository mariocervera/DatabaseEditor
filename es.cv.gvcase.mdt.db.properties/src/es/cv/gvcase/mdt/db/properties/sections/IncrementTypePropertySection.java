/*******************************************************************************
 * Copyright (c) 2008 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Mario Cervera Ubeda (Prodevelop) - initial API and implementation
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.properties.sections;

import org.eclipse.datatools.modelbase.sql.constraints.IncrementType;
import org.eclipse.datatools.modelbase.sql.constraints.IndexMember;
import org.eclipse.datatools.modelbase.sql.constraints.SQLConstraintsPackage;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractChooserPropertySection;
import org.eclipse.emf.edit.domain.EditingDomain;

public class IncrementTypePropertySection extends AbstractChooserPropertySection {

	@Override
	protected EditingDomain getEditingDomain() {
		try {
			return super.getEditingDomain();
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}
	
	@Override
	protected EStructuralFeature getFeature() {
		return SQLConstraintsPackage.eINSTANCE.getIndexMember_IncrementType();
	}

	@Override
	protected String getLabelText() {
		return "Increment Type:";
	}

	@Override
	protected Object getFeatureValue() {

		SQLObject object = (SQLObject)getEObject();
		
		if(object instanceof IndexMember) {
			 return ((IndexMember)object).getIncrementType();
		}
		
		return null;
	}
	
	@Override
	protected Object[] getComboFeatureValues() {
		
		Object[] incrementTypes = new Object[2];
		
		incrementTypes[0] = IncrementType.ASC_LITERAL;
		incrementTypes[1] = IncrementType.DESC_LITERAL;
		
		return incrementTypes;
	}

}

