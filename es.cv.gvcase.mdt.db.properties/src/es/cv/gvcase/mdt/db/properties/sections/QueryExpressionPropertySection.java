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

import org.eclipse.datatools.modelbase.sql.expressions.SQLExpressionsPackage;
import org.eclipse.datatools.modelbase.sql.tables.ViewTable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import es.cv.gvcase.mdt.common.sections.AbstractStringPropertySection;

public class QueryExpressionPropertySection extends AbstractStringPropertySection {

	/**
	 * @see org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractTextPropertySection#getLabelText()
	 */
	protected String getLabelText() {
		return "Query expression:";
	}

	/**
	 * @see org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractTextPropertySection#getFeature()
	 */
	protected EStructuralFeature getFeature() {
		return SQLExpressionsPackage.eINSTANCE.getQueryExpressionDefault_SQL();
	}
	
	@Override
	protected EObject getEObject() {
		
		EObject eobject = super.getEObject();
		
		if(eobject instanceof ViewTable) {
			ViewTable v = (ViewTable) eobject;
			return v.getQueryExpression();
		}
		
		return eobject;
	}

}
