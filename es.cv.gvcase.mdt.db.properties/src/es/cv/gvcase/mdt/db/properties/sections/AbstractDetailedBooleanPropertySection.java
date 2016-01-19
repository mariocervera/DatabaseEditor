/***************************************************************************
* Copyright (c) 2008 Conselleria de Infraestructuras y Transporte,
* Generalitat de la Comunitat Valenciana . All rights reserved. This program
* and the accompanying materials are made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors: Mario Cervera Ubeda (Prodevelop) - initial API and implementation
*
******************************************************************************/
package es.cv.gvcase.mdt.db.properties.sections;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import es.cv.gvcase.mdt.common.sections.AbstractBooleanPropertySection;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabContents;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import es.cv.gvcase.mdt.common.composites.DetailComposite;

public abstract class AbstractDetailedBooleanPropertySection extends
		AbstractBooleanPropertySection {

	private Group groupDetails;
	
	private DetailComposite detailsComposite;
	
	private TabbedPropertySheetPage tabbedPropertySheetPage;
	
	private Composite parent;
	
	private boolean selected = false;
	
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		
		super.createControls(parent, tabbedPropertySheetPage);
		
		this.tabbedPropertySheetPage = tabbedPropertySheetPage;
	}
	
	@Override
	protected void createWidgets(Composite composite) {
		
		super.createWidgets(composite);
		
		this.parent = composite;
		
		groupDetails = getWidgetFactory().createGroup(composite, getDetailsLabelText());
		GridLayout gl = new GridLayout();
		groupDetails.setLayout(gl);
	}
	
	@Override
	protected void setSectionData(Composite composite) {
		
		super.setSectionData(composite);
		
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(getCheckButton(), ITabbedPropertyConstants.VSPACE);
		
		groupDetails.setLayoutData(data);
	}
	
	protected TabbedPropertySheetPage getTabbedPropertySheetPage() {
		return tabbedPropertySheetPage;
	}
	
	public void refresh() {
		
		super.refresh();
		
		if(!getCheckButton().isDisposed() && !groupDetails.isDisposed()) {
			
			disposeDetailsComposite();
			groupDetails.setVisible(false);
			
			if(getCheckButton().getSelection()) {
				createDetailsComposite();
				groupDetails.setVisible(true);
			}
			
			if(groupDetails != null && !groupDetails.isDisposed()) {
				
				if(detailsComposite != null && !detailsComposite.isDisposed()) {
					detailsComposite.loadData();
				}
				
				groupDetails.getParent().layout();
				groupDetails.layout();
				
				if(parent.getParent() != null && parent.getParent().getParent() != null
						&& selected != getCheckButton().getSelection()) {
					
					parent.getParent().getParent().layout(true, true);
					resizeScrolledComposite();
				}
			}
			
			selected = getCheckButton().getSelection();
		}
	}

	@Override
	public boolean shouldUseExtraSpace() {
		return true;
	}
	
	private void disposeDetailsComposite() {
		if(detailsComposite != null && !detailsComposite.isDisposed()) {
			detailsComposite.dispose();
		}
	}
	
	private void createDetailsComposite() {
		
		detailsComposite = getComposite(groupDetails);
		if(detailsComposite != null) {
			getWidgetFactory().adapt(detailsComposite);
			detailsComposite.createWidgets(groupDetails, getWidgetFactory());
			GridData gdata = new GridData(GridData.FILL_BOTH);
			detailsComposite.setLayoutData(gdata);
			detailsComposite.setSectionData(groupDetails);
			detailsComposite.hookListeners();
			detailsComposite.setEMFEditDomain(getEditingDomain());
		}

	}
	
	@Override
	protected boolean getFeatureValue() {
		Object is = getEObject().eGet(getFeature());
		if (is == null) {
			return false;
		}
		return true;

	}
	
	@Override
	protected void handleCheckButtonModified() {
		if(getCheckButton().getSelection()) {
			createCommand(null, newEObjectForFeature());
		}
		else {
			if(getEObject() != null && getFeature() != null) {
				Object objectToDelete = getEObject().eGet(getFeature());
				if(objectToDelete != null) {
					Command dc = DeleteCommand.create(getEditingDomain(), objectToDelete);
					getEditingDomain().getCommandStack().execute(dc);
				}
			}
		}
		
	}
	
	private EObject newEObjectForFeature() {
		EObject newEObject = null;
		EClass eclass = null;
		if(((EStructuralFeature)getFeature()).getEType() instanceof EClass) {
			eclass = (EClass)((EStructuralFeature)getFeature()).getEType();
		}
		if(eclass != null) {
			EObject container = eclass.eContainer();
			if(container instanceof EPackage) {
				EPackage epackage = (EPackage) container;
				EFactory factory = epackage.getEFactoryInstance();
				if(factory != null) {
					newEObject = factory.create(eclass);
				}
			}
		}
		
		return newEObject;
	}
	
	private void resizeScrolledComposite() {

		Point currentTabSize = new Point(0, 0);
		TabContents currentTab = this.getTabbedPropertySheetPage()
				.getCurrentTab();

		if (currentTab != null && parent != null && parent.getParent() != null
				&& parent.getParent().getParent() != null) {

			Composite sizeReference = parent.getParent().getParent();
			if (sizeReference != null) {
				currentTabSize = sizeReference.computeSize(SWT.DEFAULT,
						SWT.DEFAULT);
			}
		}

		// Get the ScrolledComposite and resize it

		if (this.getTabbedPropertySheetPage() != null
				&& this.getTabbedPropertySheetPage().getControl() instanceof Composite) {

			if (((Composite) this.getTabbedPropertySheetPage().getControl())
					.getChildren().length == 1) {

				
				Control layoutComposite = ((Composite) this
						.getTabbedPropertySheetPage().getControl()).getChildren()[0];
				
				for (Control con : ((Composite) layoutComposite).getChildren()) {
					if (con instanceof ScrolledComposite) {
						((ScrolledComposite) con).setMinSize(currentTabSize.x,
								currentTabSize.y);
					}
				}
			}
		}
	}
	
	protected abstract String getDetailsLabelText();
	
	protected abstract DetailComposite getComposite(Composite parent);
	
}
