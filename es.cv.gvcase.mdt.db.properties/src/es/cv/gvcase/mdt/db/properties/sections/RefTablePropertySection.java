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

import org.eclipse.datatools.modelbase.sql.constraints.ForeignKey;
import org.eclipse.datatools.modelbase.sql.constraints.SQLConstraintsPackage;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.tables.BaseTable;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractChooserPropertySection;
import org.eclipse.jface.viewers.ILabelProvider;

import es.cv.gvcase.mdt.db.properties.providers.ChooserLabelProvider;

public class RefTablePropertySection extends AbstractChooserPropertySection {
	
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

	@Override
	protected EStructuralFeature getFeature() {
		return SQLConstraintsPackage.eINSTANCE.getForeignKey_ReferencedTable();
	}

	@Override
	protected String getLabelText() {
		return "Referenced Table:";
	}

	@Override
	protected Object getFeatureValue() {

		SQLObject object = (SQLObject)getEObject();
		
		if(object instanceof ForeignKey) {
			 return ((ForeignKey)object).getReferencedTable();
		}
		
		return null;
	}
	
	@Override
	protected Object[] getComboFeatureValues() {
		
		SQLObject selectedObject = (SQLObject)getEObject();
		
		if(selectedObject instanceof ForeignKey) {
		
			SQLObject object = (SQLObject)selectedObject.eContainer();
			while(!(object instanceof Schema)) {
				object = (SQLObject)object.eContainer();
			}
			Schema schema = (Schema) object;
			BaseTable parentTable = (BaseTable)selectedObject.eContainer();
			
			List<BaseTable> tables = new ArrayList<BaseTable>();
			tables.add(null);
			for(Table table : schema.getTables()) {
				if(table instanceof BaseTable && !table.equals(parentTable)) {
					tables.add((BaseTable)table);
				}
			}
			
			return tables.toArray();
		}
		
		return null;
	}
	
	@Override
	protected void handleComboModified() {
		
		super.handleComboModified();
		
		//The referenced table has been changed so the features "Unique Constraint", "Unique Index"
		//and "Referenced Members" must be set to null
		
		EObject eobject = this.getEObject();
		
		if(eobject != null && eobject instanceof ForeignKey) {
			
			CompoundCommand cc = new CompoundCommand("Clean referenced members");
			
			Command command1 = SetCommand.create(getEditingDomain(), 
					eobject, SQLConstraintsPackage.eINSTANCE.getForeignKey_UniqueConstraint(),
					null);
			cc.append(command1);
			
			Command command2 = SetCommand.create(getEditingDomain(),
					eobject, SQLConstraintsPackage.eINSTANCE.getForeignKey_UniqueIndex(),
					null);
			cc.append(command2);
			
			for(Column column: ((ForeignKey)eobject).getReferencedMembers()) {
				Command command3 = RemoveCommand.create(getEditingDomain(),
						eobject, SQLConstraintsPackage.eINSTANCE.getForeignKey_ReferencedMembers(),
						column);
				cc.append(command3);
			}
			
			if(!cc.isEmpty()) {
				getEditingDomain().getCommandStack().execute(cc);
			}
			
		}
		
	}	

}
