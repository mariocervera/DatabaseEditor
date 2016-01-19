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

import java.util.Collection;

import org.eclipse.datatools.modelbase.sql.constraints.PrimaryKey;
import org.eclipse.datatools.modelbase.sql.constraints.SQLConstraintsFactory;
import org.eclipse.datatools.modelbase.sql.constraints.SQLConstraintsPackage;
import org.eclipse.datatools.modelbase.sql.tables.PersistentTable;
import org.eclipse.datatools.modelbase.sql.tables.SQLTablesPackage;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import es.cv.gvcase.mdt.common.sections.AbstractCollectionPropertySection;
import es.cv.gvcase.mdt.db.properties.providers.SQLObjectLabelProvider;

public class PrimaryKeyPropertySection extends AbstractCollectionPropertySection {

	
	protected String getLabelText() {
		return "Primary key details";
	}

	protected EStructuralFeature getFeature() {
		return SQLConstraintsPackage.eINSTANCE.getReferenceConstraint_Members();
	}
	
	@Override
	protected Collection<?> getCandidateElements() {

		if (super.getEObject() instanceof PersistentTable){
			return ((PersistentTable) super.getEObject()).getColumns();
		} else {
			return null;
		}
	}
	
	
	@Override
	protected EObject getEObject() {

		return getPrimaryKey();
	}
	
	
	protected SelectionListener getAddButtonSelectionListener(){
		return new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {			
			}

			public void widgetSelected(SelectionEvent e) {
				handleAddButtonSelected();				
			}			
		};
	}
	
	protected SelectionListener getRemoveButtonSelectionListener(){
		return new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {			
			}

			public void widgetSelected(SelectionEvent e) {
				handleRemoveButtonSelected();				
			}			
		};
	}	
	
	private void handleAddButtonSelected() {
		
		if(!getMembersComposite().getCandidateElementsViewer().getSelection().isEmpty()) {
				PrimaryKey pk = getPrimaryKey();
				if(pk == null) {
					EObject eobject = super.getEObject();
					if(eobject instanceof PersistentTable) {
						PersistentTable pt = (PersistentTable) eobject;
						TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(pt);
						if(domain == null) return;
						String tableName = "";
						if(pt.getName() != null) tableName = pt.getName();
						PrimaryKey newPK = SQLConstraintsFactory.eINSTANCE.createPrimaryKey();
						CompoundCommand cc = new CompoundCommand("Create new primary key");
						Command command1 = SetCommand.create(domain, newPK,
								EcorePackage.eINSTANCE.getENamedElement_Name(), "PK_" + tableName);
						Command command2 = AddCommand.create(domain, pt,
								SQLTablesPackage.eINSTANCE.getBaseTable_Constraints(), newPK);
						cc.append(command1);
						cc.append(command2);
						domain.getCommandStack().execute(cc);
						getMembersComposite().setElement(newPK);
					}		
				}
				
				getMembersComposite().handleAddButtonSelected();
				
				this.refresh();
		}
		
	}
	
	protected void handleRemoveButtonSelected() {
		
		getMembersComposite().handleRemoveButtonSelected();
		
		this.refresh();
		
		PrimaryKey pk = getPrimaryKey();
		
		if(pk != null && pk.getMembers().size() == 0) {
			//The PK has run out of columns. Remove it
			TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(pk);
			if(domain == null) return;
			Command command = DeleteCommand.create(domain, pk);
			domain.getCommandStack().execute(command);
		}
	}
	
	
	private PrimaryKey getPrimaryKey() {
		
		EObject eobject = super.getEObject();
		if(eobject instanceof PersistentTable) {
			PersistentTable pt = (PersistentTable) eobject;
			return pt.getPrimaryKey();
		}
		
		return null;
	}
	
	@Override
	protected String getMembersText() {
		return "Participant columns";
	}
	
	@Override
	protected IBaseLabelProvider getLabelProvider() {
		return new SQLObjectLabelProvider();
	}
	
	@Override
	protected boolean enableOrdering() {
		return true;
	}
}
