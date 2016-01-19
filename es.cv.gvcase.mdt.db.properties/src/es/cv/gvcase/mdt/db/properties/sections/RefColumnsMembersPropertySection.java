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
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.IBaseLabelProvider;

import es.cv.gvcase.mdt.common.sections.AbstractCollectionPropertySection;
import es.cv.gvcase.mdt.db.properties.providers.SQLObjectLabelProvider;

public class RefColumnsMembersPropertySection extends AbstractCollectionPropertySection {
	
	@Override
	protected IBaseLabelProvider getLabelProvider() {
		return new SQLObjectLabelProvider();
	}
	
	@Override
	protected String getLabelText() {
		return "Referenced columns";
	}
	
	@Override
	protected String getMembersText() {
		return "";
	}
	
	@Override
	protected EStructuralFeature getFeature() {
		return SQLConstraintsPackage.eINSTANCE.getForeignKey_ReferencedMembers();
	}

	@Override
	protected EList<EObject> getCandidateElements() {

		EObject eobject = getEObject();
		
		EList <EObject> elements = new BasicEList<EObject>();
		
		if(eobject instanceof ForeignKey) {
			ForeignKey fk = (ForeignKey) eobject;
			if(fk.getReferencedTable() != null) {
				elements.addAll(fk.getReferencedTable().getColumns());
			}
		}
		
		return elements;
	}
	
	@Override
	public void refresh() {
		
		super.refresh();
		
		setButtonsEnablement();
	}
	
	private void buttonsEnablement(boolean enablement) {
		
		if(getMembersComposite() != null) {
			getMembersComposite().setAddButtonEnablement(enablement);
			getMembersComposite().setRemoveButtonEnablement(enablement);
		}
	}
	
	private void setButtonsEnablement() {
		
		EObject eobject = getEObject();
		
		if(eobject instanceof ForeignKey) {
			ForeignKey fk = (ForeignKey) eobject;
			if(fk.getUniqueIndex() != null || fk.getUniqueConstraint() != null) {
				buttonsEnablement(false);
			}
			else {
				buttonsEnablement(true);
			}
		}
	}
	
	@Override
	protected void handleModelChanged(Notification msg) {
		
		super.handleModelChanged(msg);
		
		setButtonsEnablement();
	}

}
