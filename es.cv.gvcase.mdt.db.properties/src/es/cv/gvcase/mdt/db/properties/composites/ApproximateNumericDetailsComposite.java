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

import org.eclipse.datatools.modelbase.sql.datatypes.ApproximateNumericDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.SQLDataTypesPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecoretools.tabbedproperties.utils.TextChangeListener;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

import es.cv.gvcase.mdt.common.composites.DetailComposite;

/**
 * The composite that hosts the widgets to manage the details of the ApproximateNumericDataType
 * 
 * @author mcervera
 */
public class ApproximateNumericDetailsComposite extends DetailComposite {
	
	/** A helper to listen for events that indicate that a text field has been changed. */
	private TextChangeListener listener;
	
	/** Widgets. */
	
	/** The text control for the section. */
	private Text text;

	/** The label. */
	private CLabel label;
	
	/**
	 * Constructor
	 * 
	 * @param the parent composite
	 * @param the style of the composite
	 * 
	 * @author mcervera
	 */
	public ApproximateNumericDetailsComposite(Composite parent, int style) {
		super(parent, style);
	}

	/* (non-Javadoc)
	 * @see es.cv.gvcase.mdt.common.composites.DetailComposite#createWidgets(org.eclipse.swt.widgets.Composite, org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory)
	 */
	@Override
	public void createWidgets(Composite composite, TabbedPropertySheetWidgetFactory widgetFactory) {
		
		super.createWidgets(composite, widgetFactory);
		
		this.setLayout(new FormLayout());
		
		label = widgetFactory.createCLabel(this, "Precision:");
		
		text = widgetFactory.createText(this, "", getStyle() | SWT.BORDER);

		if (getFeature() != null) {
			boolean isChangeable = getFeature().isChangeable();

			text.setEditable(isChangeable);
			text.setEnabled(isChangeable);
		}
	}

	/* (non-Javadoc)
	 * @see es.cv.gvcase.mdt.common.composites.DetailComposite#setSectionData(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void setSectionData(Composite composite) {
		
		super.setSectionData(composite);
		
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(text, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(text, 0, SWT.CENTER);
		label.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(0, getStandardLabelWidth(composite, new String[] { "Precision:" }));
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(label, 0, SWT.CENTER);
		text.setLayoutData(data);
		
	}

	/* (non-Javadoc)
	 * @see es.cv.gvcase.mdt.common.composites.DetailComposite#hookListeners()
	 */
	@Override
	public void hookListeners() {
		
		super.hookListeners();
		
		listener = new TextChangeListener() {

			public void textChanged(Control control) {
				handleTextModified();
			}
		};
		listener.startListeningTo(text);
	}

	/* (non-Javadoc)
	 * @see es.cv.gvcase.mdt.common.composites.DetailComposite#loadData()
	 */
	@Override
	public void loadData() {
		
		refresh();
	}
	
	public void refresh() {
		
		if(text != null) {
			text.setText(Integer.toString(getElementPrecision()));
		}
	}

	
	/**
	 * Handle the modify event of the text widget
	 * 
	 * @author mcervera
	 */
	protected void handleTextModified() {
		
		if(getEMFEditDomain() == null) return;
		
		String text = this.text.getText();
		
		if(isInteger(text)) {
			if(getElement() != null && getFeature() != null) {
				SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
						getElement(), getFeature(), new Integer(text));
				getEMFEditDomain().getCommandStack().execute(command);
			}
		}
		else {
			writeErrorMessageOnStatusBar("The precision must be an integer number");
			refresh();
		}
		
	}
	
	private EStructuralFeature getFeature() {
		return SQLDataTypesPackage.eINSTANCE.getNumericalDataType_Precision();
	}
	
	
	/**
	 * Gets the property precision of the selected ApproximateNumericDataType
	 * 
	 * @return the element precision
	 * 
	 * @author mcervera
	 */
	private int getElementPrecision() {
		if(getElement() != null && getElement() instanceof ApproximateNumericDataType) {
			return ((ApproximateNumericDataType)getElement()).getPrecision();
		}
		
		return -1;
	}
	
	/**
	 * Checks if the String passed as parameter is of type Integer.
	 * 
	 * @param s, the string to be checked
	 * 
	 * @return true, if is integer
	 * 
	 * @author mcervera
	 */
	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	
}

