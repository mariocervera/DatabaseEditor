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

import org.eclipse.datatools.modelbase.sql.constraints.Constraint;
import org.eclipse.datatools.modelbase.sql.constraints.ForeignKey;
import org.eclipse.datatools.modelbase.sql.constraints.Index;
import org.eclipse.datatools.modelbase.sql.constraints.IndexMember;
import org.eclipse.datatools.modelbase.sql.constraints.SQLConstraintsPackage;
import org.eclipse.datatools.modelbase.sql.constraints.UniqueConstraint;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.tables.BaseTable;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractChooserPropertySection;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ILabelProvider;

import es.cv.gvcase.mdt.db.properties.providers.ChooserLabelProvider;

public class RefColumnsConstraintPropertySection extends AbstractChooserPropertySection {
	
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
	protected String getLabelText() {
		
		return "Constraint:";
	}
	
	@Override
	protected EStructuralFeature getFeature() {
		
		if(getCSingleObjectChooser().getSelection() instanceof Index) {
			return SQLConstraintsPackage.eINSTANCE.getForeignKey_UniqueIndex();
		}
		else {
			return SQLConstraintsPackage.eINSTANCE.getForeignKey_UniqueConstraint();
		}
	}

	@Override
	protected Object getFeatureValue() {

		SQLObject object = (SQLObject)getEObject();
		
		if(object instanceof ForeignKey) {
			 if(((ForeignKey)object).getUniqueConstraint() != null) {
				 return ((ForeignKey)object).getUniqueConstraint();
			 }
			 else {
				 return ((ForeignKey)object).getUniqueIndex();
			 }
		}
		
		return null;
	}
	
	@Override
	protected Object[] getComboFeatureValues() {

		SQLObject selectedObject = (SQLObject)getEObject();
		
		if(selectedObject instanceof ForeignKey) {
		
			List<UniqueConstraint> constraints = new ArrayList<UniqueConstraint>();
			List<Index> indexes = new ArrayList<Index>();
			
			BaseTable refTable = ((ForeignKey)selectedObject).getReferencedTable();
			if(refTable != null) {
				Schema schema = (Schema)refTable.eContainer();
				
				for(Constraint c: refTable.getConstraints()) {
					if(c instanceof UniqueConstraint) {
						constraints.add((UniqueConstraint)c);
					}
				}
				for(Index i: schema.getIndices()) {
					if(i.getTable() != null && i.getTable().equals(refTable)) {
						indexes.add(i);
					}
				}
				
				List<SQLObject> elements = new ArrayList<SQLObject>();
				elements.add(null);
				elements.addAll(constraints);
				elements.addAll(indexes);
				
				return elements.toArray();
			}
		}
		
		return null;
	}
	
	@Override
	protected void handleComboModified() {
		
		//The selected constraint has been changed so either the feature "Unique Constraint"
		//or "Unique Index" must be set to null
		
		EObject eobject = this.getEObject();
		
		if(eobject != null && eobject instanceof ForeignKey) {
			
			Command command = null;
			
			if(getCSingleObjectChooser().getSelection() instanceof Index) {
				command = SetCommand.create(getEditingDomain(), 
						eobject, SQLConstraintsPackage.eINSTANCE.getForeignKey_UniqueConstraint(),
						null);
			}
			else {
				command = SetCommand.create(getEditingDomain(), 
						eobject, SQLConstraintsPackage.eINSTANCE.getForeignKey_UniqueIndex(),
						null);
			}
			
			if(command != null) {
				getEditingDomain().getCommandStack().execute(command);
			}
			
		}
		
		super.handleComboModified();
		
		updateReferencedMembers();
	}
	
	
	private void updateReferencedMembers() {
		
		EObject eobject = this.getEObject();
		
		if(eobject != null && eobject instanceof ForeignKey) {
		
			ForeignKey fk = (ForeignKey) eobject;
			SQLObject featureValue = (SQLObject) getFeatureValue();
			
			List<Column> members = new ArrayList<Column>();
			
			if(featureValue instanceof UniqueConstraint) {
				UniqueConstraint uk = (UniqueConstraint) featureValue;
				members.addAll(uk.getMembers());
			}
			else if(featureValue instanceof Index) {
				Index index = (Index) featureValue;
				for(IndexMember im: index.getMembers()) {
					if(im.getColumn() != null) {
						members.add(im.getColumn());
					}
				}
			}
			
			Command command = SetCommand.create(getEditingDomain(), fk,
					getMembersFeature(), members);
			getEditingDomain().getCommandStack().execute(command);
		}
	}
	
	private EStructuralFeature getMembersFeature() {
		return SQLConstraintsPackage.eINSTANCE.getForeignKey_ReferencedMembers();
	}

}
