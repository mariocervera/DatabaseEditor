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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.datatools.modelbase.sql.constraints.Index;
import org.eclipse.datatools.modelbase.sql.constraints.IndexMember;
import org.eclipse.datatools.modelbase.sql.constraints.SQLConstraintsPackage;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.tables.BaseTable;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractChooserPropertySection;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.viewers.ILabelProvider;

import es.cv.gvcase.mdt.db.properties.providers.ChooserLabelProvider;

public class TablePropertySection extends AbstractChooserPropertySection {

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
		return "Table:";
	}
	
	/**
	 * @see org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractTextPropertySection#getFeature()
	 */
	protected EStructuralFeature getFeature() {
		return SQLConstraintsPackage.eINSTANCE.getIndex_Table();
	}
	
	/**
	 * @see org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractChooserPropertySection#getFeatureValue()
	 */
	protected Object getFeatureValue() {
		
		SQLObject object = (SQLObject)getEObject();
		
		if(object instanceof Index) {
			return ((Index) object).getTable();
		}
		
		return null;
	}
	
	@Override
	protected Object[] getComboFeatureValues() {

		SQLObject selectedObject = (SQLObject)getEObject();
		
		SQLObject sqlobject = (SQLObject)selectedObject.eContainer();
		while(!(sqlobject instanceof Schema)) {
			sqlobject = (SQLObject)sqlobject.eContainer();
		}
		
		Schema schema = (Schema) sqlobject;
		
		List<BaseTable> tables = new ArrayList<BaseTable>();
		for(Table table : schema.getTables()) {
			if(table instanceof BaseTable) tables.add((BaseTable)table);
		}
		
		return tables.toArray();
	}
	
	@Override
	protected void handleComboModified() {
		super.handleComboModified();
		
		//The index members lose the column
		
		Index index = (Index)getEObject();
		EditingDomain domain = TransactionUtil.getEditingDomain(index);
		if(domain == null) return;
		 
		CompoundCommand cc = new CompoundCommand("Delete columns from index members");
		
		for(IndexMember im : index.getMembers()) {
			Command command = SetCommand.create(domain, im,
					SQLConstraintsPackage.eINSTANCE
							.getIndexMember_Column(),
							null);
			cc.append(command);
		}
		
		if(!cc.isEmpty()) {
			domain.getCommandStack().execute(cc);
		}
	}

}
