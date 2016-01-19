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

import org.eclipse.datatools.modelbase.sql.accesscontrol.AuthorizationIdentifier;
import org.eclipse.datatools.modelbase.sql.accesscontrol.Group;
import org.eclipse.datatools.modelbase.sql.accesscontrol.SQLAccessControlPackage;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.IBaseLabelProvider;

import es.cv.gvcase.mdt.common.sections.AbstractCollectionPropertySection;
import es.cv.gvcase.mdt.db.properties.providers.SQLObjectLabelProvider;

public class GroupsPropertySection extends AbstractCollectionPropertySection {

	@Override
	protected EStructuralFeature getFeature() {

		return SQLAccessControlPackage.eINSTANCE.getUser_Group();
	}

	@Override
	protected String getLabelText() {
		
		return "Groups";
	}
	
	@Override
	protected EList<EObject> getCandidateElements() {
		
		SQLObject selectedObject = (SQLObject)getEObject();
		
		if(selectedObject != null) {
			SQLObject rootElement = (SQLObject)selectedObject.eContainer();
			while(!(rootElement instanceof Database)) {
				rootElement = (SQLObject)rootElement.eContainer();
			}
			
			Database db = (Database) rootElement;
			
			EList<EObject> groups = new BasicEList<EObject>();
			for(AuthorizationIdentifier aid : db.getAuthorizationIds()) {
				if(aid instanceof Group) groups.add((Group)aid);
			}
			
			return groups;
		}
		
		return null;
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
