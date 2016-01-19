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

import org.eclipse.datatools.modelbase.sql.constraints.SQLConstraintsPackage;
import org.eclipse.datatools.modelbase.sql.constraints.UniqueConstraint;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.PersistentTable;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.IBaseLabelProvider;

import es.cv.gvcase.mdt.common.sections.AbstractCollectionPropertySection;
import es.cv.gvcase.mdt.db.properties.providers.SQLObjectLabelProvider;

public class MembersPropertySection extends AbstractCollectionPropertySection {

	/**
	 * @see org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractTextPropertySection#getLabelText()
	 */
	protected String getLabelText() {
		return "Members";
	}

	/**
	 * @see org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractTextPropertySection#getFeature()
	 */
	protected EStructuralFeature getFeature() {
		return SQLConstraintsPackage.eINSTANCE.getReferenceConstraint_Members();
	}
	
	@Override
	protected EList<EObject> getCandidateElements() {

		EList<EObject> result = new BasicEList<EObject>();
		EObject object = getEObject();
		if(object instanceof UniqueConstraint) {
			UniqueConstraint uk = (UniqueConstraint) object;
			Table container = (Table)uk.eContainer();
			if(container instanceof PersistentTable) {
				PersistentTable table = (PersistentTable) container;
				for(Column c: table.getColumns()) {
					result.add(c);
				}
			}
		}
		
		return result;
	}

	@Override
	protected String getMembersText() {
		return "Members";
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
