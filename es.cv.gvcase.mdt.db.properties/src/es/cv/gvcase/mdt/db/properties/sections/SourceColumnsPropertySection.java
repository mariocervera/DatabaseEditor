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

import org.eclipse.datatools.modelbase.sql.constraints.ForeignKey;
import org.eclipse.datatools.modelbase.sql.constraints.SQLConstraintsPackage;
import org.eclipse.datatools.modelbase.sql.tables.BaseTable;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.IBaseLabelProvider;

import es.cv.gvcase.mdt.common.sections.AbstractCollectionPropertySection;
import es.cv.gvcase.mdt.db.properties.providers.SQLObjectLabelProvider;

public class SourceColumnsPropertySection extends AbstractCollectionPropertySection {

	@Override
	protected EStructuralFeature getFeature() {

		return SQLConstraintsPackage.eINSTANCE.getReferenceConstraint_Members();
	}

	@Override
	protected String getLabelText() {
		
		return "Source Columns";
	}
	
	@Override
	protected EList<EObject> getCandidateElements() {
		
		EObject eobj = getEObject();
		EList<EObject> columns = new BasicEList<EObject>();
		
		if(eobj instanceof ForeignKey) {
			ForeignKey fk = (ForeignKey) eobj;
			BaseTable parent = (BaseTable)fk.eContainer();
			for(Column column: parent.getColumns()) {
				columns.add(column);
			}
		}
		
		return columns;
	}

	@Override
	protected String getMembersText() {
		return "";
	}
	
	@Override
	protected IBaseLabelProvider getLabelProvider() {
		return new SQLObjectLabelProvider();
	}

}
