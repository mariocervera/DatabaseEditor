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

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

import es.cv.gvcase.mdt.common.composites.TableMembersComposite;

/*
 * This class's been created to be used in the class AbstractCollectionContainedPropertySection
 * as MembersComposite is used in AbstractCollectionPropertySection
 */
public class MembersContainedComposite extends TableMembersComposite {
	
	private EList<EObject> membersFeatureAsList; 

	public MembersContainedComposite(Composite parent, int style,
			TabbedPropertySheetWidgetFactory widgetFactory, EStructuralFeature structuralFeature, String labelText) {
		super(parent, style, widgetFactory, structuralFeature, labelText);
		
		membersFeatureAsList = new BasicEList<EObject>();
	}
	
	public void setMembersFeatureAsList(EList<EObject> members) {
		membersFeatureAsList = members;
	}

	@Override
	protected EList<EObject> getMemberElements() {
		return membersFeatureAsList;
	}
}
