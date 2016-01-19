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

import org.eclipse.datatools.modelbase.sql.schema.SQLSchemaPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import es.cv.gvcase.mdt.common.sections.AbstractStringPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;

public class DescriptionPropertySection extends AbstractStringPropertySection {

	@Override
	protected EStructuralFeature getFeature() {
		return SQLSchemaPackage.eINSTANCE.getSQLObject_Description();
	}

	@Override
	protected String getLabelText() {
		return "Description:";
	}
	
	/***** MULTILINE TEXT *****/
	
	@Override
	protected int getStyle() {
		return SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL;
	}
	
	@Override
	protected void setSectionData(Composite composite) {
		
		super.setSectionData(composite);
		
		if(getText().getLayoutData() instanceof FormData) {
			((FormData)getText().getLayoutData()).height = 40;
		}
	}
	
	/**************************/
}
