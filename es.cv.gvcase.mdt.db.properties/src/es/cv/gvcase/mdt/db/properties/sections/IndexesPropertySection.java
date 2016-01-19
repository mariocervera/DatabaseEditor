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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.datatools.modelbase.sql.constraints.Index;
import org.eclipse.datatools.modelbase.sql.constraints.IndexMember;
import org.eclipse.datatools.modelbase.sql.constraints.SQLConstraintsFactory;
import org.eclipse.datatools.modelbase.sql.constraints.SQLConstraintsPackage;
import org.eclipse.datatools.modelbase.sql.schema.SQLSchemaPackage;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import es.cv.gvcase.mdt.common.composites.DetailComposite;
import es.cv.gvcase.mdt.common.composites.MembersComposite;
import es.cv.gvcase.mdt.common.composites.TableMembersComposite;
import es.cv.gvcase.mdt.common.sections.AbstractDetailedContainedObjectsPropertySection;
import es.cv.gvcase.mdt.db.properties.composites.ExpressionComposite;
import es.cv.gvcase.mdt.db.properties.composites.NameComposite;
import es.cv.gvcase.mdt.db.properties.composites.SQLObjectTableComposite;
import es.cv.gvcase.mdt.db.properties.providers.SQLObjectLabelProvider;

public class IndexesPropertySection extends AbstractDetailedContainedObjectsPropertySection {
	
	@Override
	protected void createWidgets(Composite composite) {
		
		this.parent = composite;
		
		setTable(new SQLObjectTableComposite(composite, SWT.NONE,
				getWidgetFactory(), this, getFeature(), getTableName()) {
			public void updateSelectedObject(EObject newObject) {
				for(Composite c: getComposites()) {
					if(c instanceof DetailComposite) {
						((DetailComposite)c).setElement(newObject);
					}
				}
				refresh();
			}
		});
		getTable().setFeatureClass(getTableElementsClass());
		getTable().setLabelProvider(getLabelProvider());
		
		setGroupDetails(getWidgetFactory().createGroup(composite, getLabelText()));
		GridLayout gl = new GridLayout();
		getGroupDetails().setLayout(gl);
		
	}

	@Override
	protected EStructuralFeature getFeature() {
		return SQLSchemaPackage.eINSTANCE.getSchema_Indices();
	}

	@Override
	protected String getLabelText() {
		return "Indexes details";
	}

	@Override
	protected EClass getTableElementsClass() {
		return SQLConstraintsPackage.eINSTANCE.getIndex();
	}

	@Override
	protected EList<DetailComposite> createComposites(Composite parent) {
		
		EList<DetailComposite> composites = new BasicEList<DetailComposite>();
		
		NameComposite nc = new NameComposite(parent, parent.getStyle());
		ExpressionComposite ec = new ExpressionComposite(parent, parent.getStyle());
		MembersComposite mc = new TableMembersComposite(parent, parent.getStyle(), this.getWidgetFactory(), SQLConstraintsPackage.eINSTANCE.getIndex_Members(),"Index members")
		{
			/* (non-Javadoc)
			 * @see es.cv.gvcase.mdt.common.composites.MembersComposite#getMemberElements()
			 */
			@Override
			protected EList<EObject> getMemberElements() {
				EList<EObject> members = new BasicEList<EObject>();
				EList<EObject> originalMembers =  super.getMemberElements();
				if (originalMembers == null){
					return ECollections.emptyEList();
				}
				for (EObject eo : originalMembers){
					if (eo instanceof IndexMember){
						members.add(((IndexMember)eo).getColumn());
					}
				}
				return members;
			}

			public void handleAddButtonSelected() {
				
				if(this.getCandidateElementsViewer().getSelection() instanceof StructuredSelection) {
					StructuredSelection selection = (StructuredSelection) this.getCandidateElementsViewer().getSelection();
					
					if(!selection.isEmpty()) {
						Iterator iterator = selection.iterator();
						while(iterator.hasNext()) {
							Object next = iterator.next();
							if(next instanceof Column && getElement() instanceof Index) {
								if(!containsColumn((Index)getElement(), (Column)next)) {
									executeAddCommand(next);
								}
							}
						}
						refresh();
					}					
				}				
			}
			
			private boolean containsColumn(Index index, Column column) {
				
				if(index == null || column == null) return false;

				for (IndexMember im : index.getMembers()) {
					if (im.getColumn() != null && im.getColumn().equals(column)) {
							return true;
					}
				}

				return false;
			}
			
			public void handleRemoveButtonSelected() {
				
				if(this.getMemberElementsViewer().getSelection() instanceof StructuredSelection) {
					StructuredSelection selection = (StructuredSelection) this.getMemberElementsViewer().getSelection();
					
					if(!selection.isEmpty()) {
						Iterator iterator = selection.iterator();
						while(iterator.hasNext()) {
							executeRemoveCommand(iterator.next());
						}
						refresh();
					}
					
				}
				
			}
			
			private void executeAddCommand(Object objectToAdd) {
				
				if(getEMFEditDomain() == null || !(objectToAdd instanceof Column)) return;
				
				IndexMember im = SQLConstraintsFactory.eINSTANCE.createIndexMember();
				Column col = (Column) objectToAdd;
				String colName = "";
				if(col.getName() != null) colName = col.getName();
				
				CompoundCommand cc = new CompoundCommand();
				
				cc.append(SetCommand.create(getEMFEditDomain(), im,
						EcorePackage.eINSTANCE.getENamedElement_Name(), colName));
				
				cc.append(SetCommand.create(getEMFEditDomain(), im,
						SQLConstraintsPackage.eINSTANCE.getIndexMember_Column(), col));
				
				cc.append(AddCommand.create(getEMFEditDomain(), getElement(), SQLConstraintsPackage.eINSTANCE.getIndex_Members(), im));
				
				getEMFEditDomain().getCommandStack().execute(cc);

			}
			
			private void executeRemoveCommand(Object objectToRemove) {
				
				if(getEMFEditDomain() == null || !(objectToRemove instanceof Column)
						|| !(getElement() instanceof Index)) return;
				
				Index index = (Index) getElement();
				Column column = (Column) objectToRemove;
				IndexMember imToRemove = getIndexMember(index, column);
				
				Command command = DeleteCommand.create(getEMFEditDomain(), imToRemove);
				
				getEMFEditDomain().getCommandStack().execute(command);
			}
			
			private IndexMember getIndexMember(Index index, Column column) {

				if (index == null || column == null)
					return null;

				for (IndexMember im : index.getMembers()) {
					if (im.getColumn() != null && im.getColumn().equals(column)) {
						return im;
					}
				}

				return null;
			}
			
			
		};
		mc.setLabelProvider(getLabelProvider());
		mc.setEnableOrdering(true);
		if(getCandidateElements() != null) {
			mc.setCandidateElements(getCandidateElements());
		}
		
		composites.add(nc);
		composites.add(ec);
		composites.add(mc);
		
		return composites;
	}

	private Collection<?> getCandidateElements() {
		EObject eo = this.getSelectedEObject();
		if (eo == null || !(eo instanceof Index))
			return Collections.emptyList();
		
		Index idx = (Index) eo;
		Collection<Object> result = new ArrayList<Object>();
		Table table = idx.getTable();
		if (table != null) {
			result.addAll(table.getColumns());
		}

		return result;
	}

	@Override
	protected String getTableName() {
		return "Indexes";
	}

	@Override
	protected IBaseLabelProvider getLabelProvider() {
		
		return new SQLObjectLabelProvider();
	}
	
	
}
