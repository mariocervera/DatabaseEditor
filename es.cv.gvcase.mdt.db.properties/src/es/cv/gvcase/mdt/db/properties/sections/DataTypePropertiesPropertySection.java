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

import org.eclipse.datatools.modelbase.sql.datatypes.ApproximateNumericDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.BinaryStringDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.CharacterStringDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.DataLinkDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.DataType;
import org.eclipse.datatools.modelbase.sql.datatypes.FixedPrecisionDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.IntegerDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.IntervalDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.TimeDataType;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractTabbedPropertySection;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import es.cv.gvcase.mdt.common.composites.DetailComposite;
import es.cv.gvcase.mdt.db.properties.composites.ApproximateNumericDetailsComposite;
import es.cv.gvcase.mdt.db.properties.composites.BinaryStringDetailsComposite;
import es.cv.gvcase.mdt.db.properties.composites.CharacterStringDetailsComposite;
import es.cv.gvcase.mdt.db.properties.composites.DataLinkDetailsComposite;
import es.cv.gvcase.mdt.db.properties.composites.FixedPrecisionDetailsComposite;
import es.cv.gvcase.mdt.db.properties.composites.IntegerDetailsComposite;
import es.cv.gvcase.mdt.db.properties.composites.IntervalDetailsComposite;
import es.cv.gvcase.mdt.db.properties.composites.TimeDetailsComposite;

public class DataTypePropertiesPropertySection extends AbstractTabbedPropertySection {
	
	private SQLObject selectedObject;
	
	/** Widgets */

	private Group groupDetails;
	
	private DetailComposite composite;
	
	@Override
	protected EditingDomain getEditingDomain() {
		try {
			return super.getEditingDomain();
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}
	
	@Override
	protected EStructuralFeature getFeature() {
		return null;
	}

	@Override
	protected String getLabelText() {
		return "Details";
	}
	
	@Override
	protected void createWidgets(Composite composite) {
		
		super.createWidgets(composite);
		
		groupDetails = getWidgetFactory().createGroup(composite, getLabelText());
		GridLayout gl = new GridLayout();
		groupDetails.setLayout(gl);
		
	}
	
	@Override
	protected void setSectionData(Composite composite) {
		
		super.setSectionData(composite);
		
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		
		groupDetails.setLayoutData(data);
	}
	
	public void refresh() {
	
		if(!groupDetails.isDisposed()) {
			
			super.refresh();
			
			if(selectedObject == null
					|| (getEObject() != null && !getEObject().equals(selectedObject))) {
				disposeDetailsComposite();
				createDetailsComposite();
			}
			
			SQLObject selected = (SQLObject)getEObject();
	
			if (selected != null) {
				groupDetails.setVisible(true);
				if(composite != null) {
					composite.setEMFEditDomain(this.getEditingDomain());
				}
			
			} else {
				groupDetails.setVisible(false);
			}
			
			groupDetails.redraw();
			groupDetails.update();
			
			if(composite != null) {
				composite.loadData();
			}
			
			groupDetails.getParent().layout();
			groupDetails.layout();
			
			selectedObject = (SQLObject)getEObject();
		}
	}

	@Override
	public boolean shouldUseExtraSpace() {
		return true;
	}
	
	private void disposeDetailsComposite() {
		if(composite != null && !composite.isDisposed()) {
			composite.dispose();
		}
	}
	
	private void createDetailsComposite() {
		
		composite = getComposite();
		if(composite != null) {
			getWidgetFactory().adapt(composite);
			composite.createWidgets(groupDetails, getWidgetFactory());
			GridData gdata = new GridData(GridData.FILL_BOTH);
			composite.setLayoutData(gdata);
			composite.setSectionData(groupDetails);
			composite.hookListeners();
		}

	}

	private DetailComposite getComposite() {
		
		DetailComposite composite = null;
		
		EObject objectSelected = getEObject();
		
		if(objectSelected instanceof DataType) {
			DataType dt = (DataType) objectSelected;
			
			if(dt instanceof IntegerDataType) {
				 composite = new IntegerDetailsComposite(groupDetails, groupDetails.getStyle());
			}
			else if(dt instanceof FixedPrecisionDataType) {
				composite = new FixedPrecisionDetailsComposite(groupDetails, groupDetails.getStyle());
			}
			else if(dt instanceof ApproximateNumericDataType) {
				 composite = new ApproximateNumericDetailsComposite(groupDetails, groupDetails.getStyle());
			}
			else if(dt instanceof BinaryStringDataType) {
				 composite = new BinaryStringDetailsComposite(groupDetails, groupDetails.getStyle());
			}
			else if(dt instanceof CharacterStringDataType) {
				 composite = new CharacterStringDetailsComposite(groupDetails, groupDetails.getStyle());
			}
			else if(dt instanceof TimeDataType) {
				 composite = new TimeDetailsComposite(groupDetails, groupDetails.getStyle());
			}
			else if(dt instanceof DataLinkDataType) {
				 composite = new DataLinkDetailsComposite(groupDetails, groupDetails.getStyle(), this);
			}
			else if(dt instanceof IntervalDataType) {
				 composite = new IntervalDetailsComposite(groupDetails, groupDetails.getStyle(), this);
			}
			
			if(composite != null) {
				composite.setElement(dt);
			}
		}
		
		return composite;
	}
	
}
