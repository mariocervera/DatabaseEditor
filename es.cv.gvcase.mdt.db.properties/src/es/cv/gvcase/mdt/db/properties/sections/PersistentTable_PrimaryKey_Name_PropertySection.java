/***************************************************************************
 * Copyright (c) 2011 Conselleria de Infraestructuras y Transporte,
 * Generalitat de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Marc Gil Sendra (Prodevelop) - initial API and implementation
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.properties.sections;

import org.eclipse.datatools.modelbase.sql.tables.BaseTable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import es.cv.gvcase.mdt.common.sections.AbstractStringPropertySection;

public class PersistentTable_PrimaryKey_Name_PropertySection extends
		AbstractStringPropertySection {

	protected String getLabelText() {
		return "PK Name:";
	}

	protected EStructuralFeature getFeature() {
		return EcorePackage.eINSTANCE.getENamedElement_Name();
	}

	@Override
	protected EObject getEObject() {
		EObject eo = super.getEObject();
		if (eo != null) {
			BaseTable bt = (BaseTable) eo;
			eo = bt.getPrimaryKey();
		} else {
			eo = null;
		}

		return eo;
	}

	@Override
	protected boolean isReadOnly() {
		if (getEObject() == null) {
			return true;
		} else {
			return super.isReadOnly();
		}
	}

	@Override
	protected void createCommand(Object oldValue, Object newValue) {
		if (getEObject() != null) {
			super.createCommand(oldValue, newValue);
		}
	}

	@Override
	protected Object getNewFeatureValue(String newText) {
		if (newText.equals("")) {
			return null;
		}

		return super.getNewFeatureValue(newText);
	}
}
