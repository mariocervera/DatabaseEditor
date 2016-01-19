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

import org.eclipse.datatools.modelbase.sql.constraints.SQLConstraintsPackage;
import org.eclipse.datatools.modelbase.sql.tables.PersistentTable;
import org.eclipse.datatools.modelbase.sql.tables.SQLTablesPackage;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import es.cv.gvcase.mdt.common.composites.DetailComposite;
import es.cv.gvcase.mdt.common.composites.MembersComposite;
import es.cv.gvcase.mdt.common.composites.TableMembersComposite;
import es.cv.gvcase.mdt.common.sections.AbstractDetailedContainedObjectsPropertySection;
import es.cv.gvcase.mdt.db.properties.composites.NameComposite;
import es.cv.gvcase.mdt.db.properties.composites.SQLObjectTableComposite;
import es.cv.gvcase.mdt.db.properties.providers.SQLObjectLabelProvider;

public class UniqueKeysPropertySection extends AbstractDetailedContainedObjectsPropertySection {
	
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
		return SQLTablesPackage.eINSTANCE.getBaseTable_Constraints();
	}

	@Override
	protected String getLabelText() {
		return "Unique key details";
	}

	@Override
	protected EClass getTableElementsClass() {
		return SQLConstraintsPackage.eINSTANCE.getUniqueConstraint();
	}

	@Override
	protected EList<DetailComposite> createComposites(Composite parent) {
		
		EList<DetailComposite> composites = new BasicEList<DetailComposite>();
		
		if(this.getTable().getFirstSelected() != null) {
			NameComposite nc = new NameComposite(parent, parent.getStyle());
			MembersComposite mc = new TableMembersComposite(parent, parent.getStyle(), this.getWidgetFactory(), SQLConstraintsPackage.eINSTANCE.getReferenceConstraint_Members(),"Participant columns");
			mc.setLabelProvider(getLabelProvider());
			mc.setEnableOrdering(true);
			if(getCandidateElements() != null) {
				mc.setCandidateElements(getCandidateElements());
			}
			
			composites.add(nc);
			composites.add(mc);
		}
		
		return composites;
	}

	@Override
	protected String getTableName() {
		return "Unique keys";
	}

	@Override
	protected IBaseLabelProvider getLabelProvider() {
		
		return new SQLObjectLabelProvider();
	}
	
	private Collection<?> getCandidateElements() {

		if (getEObject() instanceof PersistentTable){
			return ((PersistentTable) getEObject()).getColumns();
		} else {
			return null;
		}
	}
	
}
