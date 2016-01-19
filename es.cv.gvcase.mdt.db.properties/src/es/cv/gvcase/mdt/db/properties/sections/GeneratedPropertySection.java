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
import es.cv.gvcase.mdt.common.sections.AbstractBooleanPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;

public class GeneratedPropertySection extends AbstractBooleanPropertySection {
	
	private CLabel label;

	@Override
	protected EStructuralFeature getFeature() {
		
		return EcorePackage.eINSTANCE.getEStringToStringMapEntry_Value();
	}
	
	@Override
	protected String getLabelText() {
		return "";
	}
	
	@Override
	protected void createWidgets(Composite composite) {
		
		super.createWidgets(composite);
		
		getCheckButton().setEnabled(false);
		
		label = getWidgetFactory().createCLabel(composite, "From UML:");
	}
	
	@Override
	protected void setSectionData(Composite composite) {
		FormData data = new FormData();
		data.left = new FormAttachment(0, getStandardLabelWidth(composite, new String[] { getLabelText() }));
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		data.bottom = new FormAttachment(100, 0);
		getCheckButton().setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(getCheckButton(), -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(getCheckButton(), 0, SWT.TOP);
		label.setLayoutData(data);
	}
	
	@Override
	protected EObject getEObject() {

		EObject eobject = super.getEObject();
		
		if(eobject instanceof SQLObject) {
			SQLObject sqlobject = (SQLObject)eobject;
			EAnnotation annotationGenerated = sqlobject.getEAnnotation("AdditionalAttributes");
			if (annotationGenerated != null) {
				Object obj = annotationGenerated.getDetails().get(annotationGenerated.getDetails().indexOfKey("Generated"));
				if(obj instanceof EObject) {
					return (EObject)obj;
				}
			}
		}
		
		return eobject;
	}
	
	@Override
	protected boolean getFeatureValue() {

		Object bool = getEObject().eGet(getFeature());
		if (bool == null) {
			return false;
		}
		
		if(bool.equals("true")) return true;
		else return false;
	}
	
	
//	@Override
//	protected void handleCheckButtonModified() {
//		createCommand(Boolean.toString(getFeatureValue()), Boolean.toString(this.getCheckButton().getSelection()));
//	}
	
}

