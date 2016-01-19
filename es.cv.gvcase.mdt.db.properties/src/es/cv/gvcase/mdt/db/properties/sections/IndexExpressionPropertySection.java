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

import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import es.cv.gvcase.mdt.common.sections.AbstractStringPropertySection;

public class IndexExpressionPropertySection extends AbstractStringPropertySection {

	@Override
	protected String getLabelText() {
		return "Expression:";
	}
	
	@Override
	protected EStructuralFeature getFeature() {
		return EcorePackage.eINSTANCE.getEStringToStringMapEntry_Value();
	}
	
	@Override
	protected EObject getEObject() {

		EObject eobject = super.getEObject();
		
		if(eobject instanceof SQLObject) {
			SQLObject sqlobject = (SQLObject)eobject;
			EAnnotation annotationExpression = sqlobject.getEAnnotation("AdditionalAttributes");
			if (annotationExpression != null) {
				Object obj = annotationExpression.getDetails().get(annotationExpression.getDetails().indexOfKey("Expression"));
				if(obj instanceof EObject) {
					return (EObject)obj;
				}
			}
		}
		
		return eobject;
	}

}
