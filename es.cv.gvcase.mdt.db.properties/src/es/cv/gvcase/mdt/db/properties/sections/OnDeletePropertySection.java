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

import org.eclipse.datatools.modelbase.sql.constraints.ForeignKey;
import org.eclipse.datatools.modelbase.sql.constraints.SQLConstraintsPackage;
import org.eclipse.datatools.modelbase.sql.schema.ReferentialActionType;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractChooserPropertySection;
import org.eclipse.emf.edit.domain.EditingDomain;

public class OnDeletePropertySection extends AbstractChooserPropertySection {

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
		return SQLConstraintsPackage.eINSTANCE.getForeignKey_OnDelete();
	}

	@Override
	protected String getLabelText() {
		return "On Delete:";
	}

	@Override
	protected Object getFeatureValue() {

		SQLObject object = (SQLObject)getEObject();
		
		if(object instanceof ForeignKey) {
			 return ((ForeignKey)object).getOnDelete();
		}
		
		return null;
	}
	
	@Override
	protected Object[] getComboFeatureValues() {
		
		Object[] actions = new Object[5];
		
		actions[0] = ReferentialActionType.CASCADE_LITERAL;
		actions[1] = ReferentialActionType.NO_ACTION_LITERAL;
		actions[2] = ReferentialActionType.RESTRICT_LITERAL;
		actions[3] = ReferentialActionType.SET_DEFAULT_LITERAL;
		actions[4] = ReferentialActionType.SET_NULL_LITERAL;
		
		return actions;
	}

}
