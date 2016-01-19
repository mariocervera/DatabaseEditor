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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.datatools.modelbase.sql.accesscontrol.Privilege;
import org.eclipse.datatools.modelbase.sql.accesscontrol.SQLAccessControlPackage;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractChooserPropertySection;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ILabelProvider;

import es.cv.gvcase.mdt.db.properties.providers.ChooserLabelProvider;

public class ObjectChooserPropertySection extends AbstractChooserPropertySection {

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
		return "Object:";
	}
	
	/**
	 * @see org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractTextPropertySection#getFeature()
	 */
	protected EStructuralFeature getFeature() {
		return SQLAccessControlPackage.eINSTANCE.getPrivilege_Object();
	}
	
	/**
	 * @see org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractChooserPropertySection#getFeatureValue()
	 */
	protected Object getFeatureValue() {
		
		SQLObject object = (SQLObject)getEObject();
		
		if(object instanceof Privilege) {
			return ((Privilege) object).getObject();
		}
		
		return null;
	}
	
	@Override
	protected Object[] getComboFeatureValues() {

		SQLObject selectedObject = (SQLObject)getEObject();
		
		SQLObject rootElement = (SQLObject)selectedObject.eContainer();
		while(!(rootElement instanceof Database)) {
			rootElement = (SQLObject)rootElement.eContainer();
		}
		
		List<Schema> schemas = ((Database)rootElement).getSchemas();
		
		List<SQLObject> objects = new ArrayList<SQLObject>();
		
		for(Schema schema: schemas) {
			for(Table table: schema.getTables()) {
				objects.add(table);
			}
		}
		
		return objects.toArray();
	}

}
