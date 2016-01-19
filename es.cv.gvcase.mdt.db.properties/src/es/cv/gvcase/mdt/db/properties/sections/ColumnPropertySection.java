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

import org.eclipse.datatools.modelbase.sql.constraints.Index;
import org.eclipse.datatools.modelbase.sql.constraints.IndexMember;
import org.eclipse.datatools.modelbase.sql.constraints.SQLConstraintsPackage;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractChooserPropertySection;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ILabelProvider;

import es.cv.gvcase.mdt.db.properties.providers.ChooserLabelProvider;

public class ColumnPropertySection extends AbstractChooserPropertySection {
	
	@Override
	protected EditingDomain getEditingDomain() {
		try {
			return super.getEditingDomain();
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}
	
	@Override
	protected ILabelProvider getLabelProvider() {
		
		return new ChooserLabelProvider(new EcoreItemProviderAdapterFactory());
	}


	/**
	 * @see org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractTextPropertySection#getLabelText()
	 */
	protected String getLabelText() {
		return "Column:";
	}
	
	/**
	 * @see org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractTextPropertySection#getFeature()
	 */
	protected EStructuralFeature getFeature() {
		return SQLConstraintsPackage.eINSTANCE.getIndexMember_Column();
	}
	
	/**
	 * @see org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractChooserPropertySection#getFeatureValue()
	 */
	protected Object getFeatureValue() {
		
		SQLObject object = (SQLObject)getEObject();
		
		if(object instanceof IndexMember) {
			return ((IndexMember) object).getColumn();
		}
		
		return null;
	}
	
	@Override
	protected Object[] getComboFeatureValues() {

		SQLObject selectedObject = (SQLObject)getEObject();
		
		if(selectedObject instanceof IndexMember) {
			IndexMember im = (IndexMember) selectedObject;
			Table table = ((Index)im.eContainer()).getTable();
			if(table != null) {
				return table.getColumns().toArray();
			}
		}
		
		return (new BasicEList<Column>().toArray());
	}

}
