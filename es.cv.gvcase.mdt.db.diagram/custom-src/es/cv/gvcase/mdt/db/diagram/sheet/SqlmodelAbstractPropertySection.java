/*******************************************************************************
 * Copyright (c) 2008 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Francisco Javier Cano Mu�oz (Prodevelop) - Initial API and implementation.
 * Mario Cervera Ubeda (Integranova) - Additional functionality
 *
 ******************************************************************************/

package es.cv.gvcase.mdt.db.diagram.sheet;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractModelerPropertySection;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * Superclass for sqlmodel diagram editor property sections.
 * 
 * @author <a href="mailto:fjcano@prodeelop.es">Francisco Javier Cano Muñoz</a>
 */
public abstract class SqlmodelAbstractPropertySection extends
	AbstractModelerPropertySection implements Adapter{
	
	//The name of the selected SQLObject
	static String sqlObjectName = "";

	/** RootComposite. */
	private Composite rootComposite = null;
	
	/** Composite to add widgets and controls to. Has a GridLayout with 2 columns. */
	protected Composite canvasComposite = null;
	
	
	protected EditPart selectedEditPart = null;
	
	@Override
	protected TransactionalEditingDomain getEditingDomain() {
		try {
			return super.getEditingDomain();
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}
	
	/**
	 * Creates the controls.
	 * 
	 * @param parent the parent
	 * @param tabbedPropertySheetPage the tabbed property sheet page
	 * 
	 * @generated NOT
	 */
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
				.createFlatFormComposite(parent);
		rootComposite = composite;
		createCanvasComposite();
	}
	
	/**
	 * Creates the controls.
	 * 
	 * @param parent the parent
	 * @param tabbedPropertySheetPage the tabbed property sheet page
	 * @param numberOfColumns the number of columns
	 * 
	 * @generated NOT
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage, int numberOfColumns) {
		super.createControls(parent, tabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
				.createFlatFormComposite(parent);
		rootComposite = composite;
		gridColumns = numberOfColumns > 0 ? numberOfColumns : 1; 
		createCanvasComposite();
	}
	
	/** The grid columns. */
	private int gridColumns = 2;
	
	/**
	 * Creates the canvas composite.
	 */
	private void createCanvasComposite() {
		FormData data;
		canvasComposite = getWidgetFactory()
				.createFlatFormComposite(rootComposite);
		GridLayout layoutEditParam = new GridLayout();
		layoutEditParam.numColumns = gridColumns;
		canvasComposite.setLayout(layoutEditParam);
		data = new FormData();
		data.top = new FormAttachment(0, 2);
		data.left = new FormAttachment(0, 2);
		data.right = new FormAttachment(100, -2);
		data.bottom = new FormAttachment(100, -2);
		canvasComposite.setLayoutData(data);
	}
	
	/**
	 * Sets the input.
	 * 
	 * @param part the part
	 * @param selection the selection
	 * 
	 * @generated NOT
	 */
	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		IEditingDomainProvider provider = (IEditingDomainProvider) part
	        .getAdapter(IEditingDomainProvider.class);
	    if (provider != null) {
	        EditingDomain theEditingDomain = provider.getEditingDomain();
	        if (theEditingDomain instanceof TransactionalEditingDomain) {
	            setEditingDomain((TransactionalEditingDomain) theEditingDomain);
	        }
	    }
	    
	    // Set the eObject for the section, too. The workbench part may not
		// adapt to IEditingDomainProvider, in which case the selected EObject
		// will be used to derive the editing domain.
		if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
	        Object firstElement = ((IStructuredSelection) selection)
	            .getFirstElement();
	        
	        if (firstElement != null) {
	        	if (firstElement instanceof EditPart) {
	        		selectedEditPart = (EditPart) firstElement;
	        	}
	        	
	        	EObject adapted = unwrap(firstElement);
	        	
	            if (adapted != null) {
	                setEObject(adapted);
	            }
	        }
	    }
		
		if (selection.isEmpty()
				|| false == selection instanceof StructuredSelection) {
			super.setInput(part, selection);
			return;
		}
		final StructuredSelection structuredSelection = ((StructuredSelection) selection);
		ArrayList transformedSelection = new ArrayList(structuredSelection
				.size());
		for (Iterator it = structuredSelection.iterator(); it.hasNext();) {
			Object r = transformSelection(it.next());
			if (r != null) {
				transformedSelection.add(r);
			}
		}
		super.setInput(part, new StructuredSelection(transformedSelection));
	}
	
	
	/**
	 * Modify/unwrap selection.
	 * 
	 * @param selected the selected
	 * 
	 * @return the object
	 * 
	 * @generated NOT
	 */
	protected Object transformSelection(Object selected) {
		if (selected instanceof EditPart) {
			Object model = ((EditPart) selected).getModel();
			return model instanceof View ? ((View) model).getElement() : null;
		}
		if (selected instanceof View) {
			return ((View) selected).getElement();
		}
		if (selected instanceof IAdaptable) {
			View view = (View) ((IAdaptable) selected).getAdapter(View.class);
			if (view != null) {
				return view.getElement();
			}
		}
		return selected;
	}
	
	/**
	 * Clear canvas composite.
	 */
	protected void clearCanvasComposite() {
		canvasComposite.dispose();
		createCanvasComposite();
	}
	
	protected EditPart getSelectedEditPart() {
		return selectedEditPart;
	}
	
	protected GraphicalEditPart getSelectedGraphicalEditPart() {
		if (selectedEditPart instanceof GraphicalEditPart) {
			return (GraphicalEditPart) selectedEditPart;
		}
		return null;
	}
	
	
	protected void writeErrorMessageOnStatusBar(String message)
	{
		IViewSite site = (IViewSite) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart().getSite();
		
		IActionBars bars = site.getActionBars();

		if (bars != null) {
			bars.getStatusLineManager().setErrorMessage(message);
		}
		
	}
	
	protected void refreshTitleBar() {
		IPage ipage = null;
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();

		if (window != null) {
			IWorkbenchPage page = window.getActivePage();

			if (page != null) {
				IViewPart view = page
						.findView("org.eclipse.ui.views.PropertySheet");

				if (view != null) {

					if (view instanceof PropertySheet) {
						ipage = ((PropertySheet) view).getCurrentPage();

						if (ipage instanceof TabbedPropertySheetPage) {
							LabelProviderChangedEvent e = new LabelProviderChangedEvent(
									new SqlmodelSheetLabelProvider());
							((TabbedPropertySheetPage) ipage)
									.labelProviderChanged(e);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void aboutToBeShown() {

		super.aboutToBeShown();
		
		Notifier notifier = getTarget();
		
		if(notifier != null) {
			notifier.eAdapters().add(this);
			if(notifier instanceof ENamedElement) {
				sqlObjectName = ((ENamedElement)notifier).getName();
			}
		}

	}

	@Override
	public void aboutToBeHidden() {

		super.aboutToBeHidden();
		
		Notifier notifier = getTarget();
		
		if(notifier != null) {
			notifier.eAdapters().remove(this);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#getTarget()
	 */
	public Notifier getTarget() {
		
		if(this.getSelection() instanceof StructuredSelection) {
			StructuredSelection selection = (StructuredSelection)this.getSelection();
			if(selection.getFirstElement() instanceof EObject) {
				return ((EObject)selection.getFirstElement());
			}
		}
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(java.lang.Object)
	 */
	public boolean isAdapterForType(Object type) {
		
		return type == SQLObject.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void notifyChanged(Notification notification) {
		
		Notifier notifier = getTarget();
		
		if(notifier instanceof ENamedElement) {
			String currentName = ((ENamedElement)notifier).getName();
			if(currentName == null) currentName = "";
			if(sqlObjectName == null) sqlObjectName = "";
			if(!sqlObjectName.equals(currentName)) {
				refreshTitleBar();
				sqlObjectName = currentName;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#setTarget(org.eclipse.emf.common.notify.Notifier)
	 */
	public void setTarget(Notifier newTarget) {
		// TODO Auto-generated method stub
		
	}
}
