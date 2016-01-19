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

import org.eclipse.datatools.modelbase.sql.constraints.IndexMember;
import org.eclipse.datatools.modelbase.sql.schema.SQLSchemaPackage;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import es.cv.gvcase.mdt.common.sections.AbstractStringPropertySection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class ColumnTypePropertySection extends AbstractStringPropertySection {
	
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		
		super.createControls(parent, tabbedPropertySheetPage);
		
		getText().setEditable(false);
	}

	
	protected String getLabelText() {
		return "Column Type:";
	}

	
	protected EStructuralFeature getFeature() {
		return SQLSchemaPackage.eINSTANCE.getTypedElement_ContainedType();
	}
	
	@Override
	protected String getFeatureAsString() {

		Column column = ((IndexMember)this.getEObject()).getColumn();
		
		if(column != null && column.getContainedType() != null) {
			String name = column.getContainedType().getClass().getSimpleName();
			if(name != null && name.endsWith("DataTypeImpl")) {
				name = name.substring(0, name.length() - 12);
			}
			return name;
		}
		
		return "";
	}
	
	@Override
	protected void handleModelChanged(Notification msg) {
		super.handleModelChanged(msg);
		
		if(!getFeatureAsString().equals(this.getText().getText())) {
			refresh();
		}
	}
	
}
