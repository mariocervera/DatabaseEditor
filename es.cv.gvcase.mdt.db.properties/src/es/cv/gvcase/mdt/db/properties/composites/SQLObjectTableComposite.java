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
package es.cv.gvcase.mdt.db.properties.composites;

import org.eclipse.datatools.modelbase.sql.constraints.CheckConstraint;
import org.eclipse.datatools.modelbase.sql.constraints.Index;
import org.eclipse.datatools.modelbase.sql.constraints.PrimaryKey;
import org.eclipse.datatools.modelbase.sql.constraints.SQLConstraintsPackage;
import org.eclipse.datatools.modelbase.sql.expressions.SQLExpressionsFactory;
import org.eclipse.datatools.modelbase.sql.expressions.SearchConditionDefault;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.ISection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

import es.cv.gvcase.mdt.common.actions.LabelHelper;
import es.cv.gvcase.mdt.common.composites.EObjectsTableComposite;

public class SQLObjectTableComposite extends EObjectsTableComposite {
	
	public SQLObjectTableComposite(Composite parent, int style,
			TabbedPropertySheetWidgetFactory widgetFactory, ISection section,
			EStructuralFeature feature, String tableName) {
		
		super(parent, style, widgetFactory, section, feature, tableName);
	}
	
	@Override
	protected void createContents(Composite parent) {
		
		super.createContents(parent);
		
		//getEObjectsTableViewer().setLabelProvider(new SQLObjectLabelProvider());
		
		getAddButton().removeSelectionListener(getAddButtonSelectionAdapter());
		getAddButton().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if(getEObject() == null) return;
				Object newChild = getNewChild();
				addSubElements(newChild);
				EObject parent = null;
				if(newChild instanceof Index) {
					parent = getEObject().eContainer();
				}
				else {
					parent = getEObject();
				}
				String name = LabelHelper.INSTANCE.findName(parent, (ENamedElement)newChild);
				SetCommand setCommand = (SetCommand) SetCommand.create(
						getEditDomain(), newChild, EcorePackage.eINSTANCE.getENamedElement_Name(),
						name);
				
				if(newChild instanceof Index) {
					Command setCommand2 = SetCommand.create(
							getEditDomain(), newChild, SQLConstraintsPackage.eINSTANCE.getIndex_Table(),
							getEObject());
					getEditDomain().getCommandStack().execute(setCommand2);
				}
				
				AddCommand addCommand = null;
				
				if(newChild instanceof Index) {
					addCommand = (AddCommand) AddCommand.create(
							getEditDomain(), getEObject().eContainer(), getFeature(), newChild);
				}else {
					addCommand = (AddCommand) AddCommand.create(
							getEditDomain(), getEObject(), getFeature(), newChild);
				}
				getEditDomain().getCommandStack().execute(setCommand);
				getEditDomain().getCommandStack().execute(addCommand);
				
				refresh();
				getEObjectsTableViewer().setSelection(new StructuredSelection(newChild));
				getSection().refresh();

			}
		});
		getRemoveButton().removeSelectionListener(getRemoveButtonSelectionAdapter());
		getRemoveButton().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if(getEObject() == null) return;
				if(getEObjectsTable().getSelection().length > 0) {
					Object object = getEObjectsTable().getSelection()[0].getData();
					
					getEObjectsTableViewer().setSelection(null);
					
					DeleteCommand command = null;
					if(object instanceof Index) {
						command = (DeleteCommand)DeleteCommand.create(
								getEditDomain(), object);
					}
					else {
						command = (DeleteCommand)DeleteCommand.create(
								getEditDomain(), object);
					}
					getEditDomain().getCommandStack().execute(command);

					refresh();				
					getSection().refresh();
				}
			}
		});
	}
	
	
	private void addSubElements(Object object) {
		if(object instanceof CheckConstraint) {
			SearchConditionDefault scd = SQLExpressionsFactory.eINSTANCE.createSearchConditionDefault();
			SetCommand setCommand = (SetCommand) SetCommand.create(
					getEditDomain(), scd, EcorePackage.eINSTANCE.getENamedElement_Name(),
					"searchCondition");
			SetCommand setCommand2 = (SetCommand) SetCommand.create(
					getEditDomain(), object,
					SQLConstraintsPackage.eINSTANCE.getCheckConstraint_SearchCondition(), scd);
			getEditDomain().getCommandStack().execute(setCommand);
			getEditDomain().getCommandStack().execute(setCommand2);
		}
	}
	
	@Override
	protected boolean passFilter(EClass featureClass, EObject eo) {
		if(featureClass.isInstance(eo)) {
			if(eo instanceof Index) {
				Index index = (Index) eo;
				if(index.getTable() != null && index.getTable().equals(getEObject())) {
					return true;
				}
			}
			else if(!(eo instanceof PrimaryKey)) {
				return true;
			}
		}
		
		return false;
	}
	
	
}