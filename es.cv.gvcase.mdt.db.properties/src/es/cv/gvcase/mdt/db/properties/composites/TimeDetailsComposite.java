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

import org.eclipse.datatools.modelbase.sql.datatypes.SQLDataTypesPackage;
import org.eclipse.datatools.modelbase.sql.datatypes.TimeDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecoretools.tabbedproperties.utils.TextChangeListener;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

import es.cv.gvcase.mdt.common.composites.DetailComposite;

public class TimeDetailsComposite extends DetailComposite {
	
	/**
	 * A helper to listen for events that indicate that a text field has been
	 * changed.
	 */
	private TextChangeListener textListener;
	
	/**
	 * Listen events when the check box is selected
	 */
	private SelectionListener checkBoxListener;
	
	/** Widgets */
	
	private CLabel textlabel;
	
	private Text text;
	
	private Button checkButton;
	
	
	public TimeDetailsComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void createWidgets(Composite composite, TabbedPropertySheetWidgetFactory widgetFactory) {
		
		super.createWidgets(composite, widgetFactory);
		
		this.setLayout(new FormLayout());
		
		textlabel = widgetFactory.createCLabel(this, "Fractional seconds precision:");
		
		text = widgetFactory.createText(this, "", getStyle() | SWT.BORDER);

		if (getFractionalSecondsPrecisionFeature() != null) {
			boolean isChangeable = getFractionalSecondsPrecisionFeature().isChangeable();

			text.setEditable(isChangeable);
			text.setEnabled(isChangeable);
		}
		
		checkButton = widgetFactory.createButton(this, "Is time zone", SWT.CHECK);

		if (getIsTimeZoneFeature() != null) {
			checkButton.setEnabled(getIsTimeZoneFeature().isChangeable());
		}
	}

	@Override
	public void setSectionData(Composite composite) {
		
		super.setSectionData(composite);
		
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(text, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(text, 0, SWT.CENTER);
		data.bottom = new FormAttachment(50, 0);
		textlabel.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(0, getStandardLabelWidth(composite, new String[] { "Fractional seconds precision:" }));
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(textlabel, 0, SWT.CENTER);
		data.bottom = new FormAttachment(40, 0);
		text.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(60, 0);
		data.bottom = new FormAttachment(100, 0);
		checkButton.setLayoutData(data);
		
	}

	@Override
	public void hookListeners() {
		
		super.hookListeners();
		
		textListener = new TextChangeListener() {

			public void textChanged(Control control) {
				handleTextModified();
			}
		};
		textListener.startListeningTo(text);
		
		checkBoxListener = new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				handleCheckButtonModified();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// Do nothing
			}
		};
		
		checkButton.addSelectionListener(checkBoxListener);
		
		
	}

	@Override
	public void loadData() {
		
		refresh();
	}
	
	public void refresh() {
		
		if(text != null) {
			text.setText(Integer.toString(getElementPrecision()));
		}
		if(checkButton != null) {
			checkButton.setSelection(getElementTimeZone());
		}
	}

	
	protected void handleTextModified() {
		
		if(getEMFEditDomain() == null) return;
		
		String text = this.text.getText();
		
		if(isInteger(text)) {
			if(getElement() != null && getFractionalSecondsPrecisionFeature() != null) {
				SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
						getElement(), getFractionalSecondsPrecisionFeature(), new Integer(text));
				getEMFEditDomain().getCommandStack().execute(command);
			}
		}
		else {
			writeErrorMessageOnStatusBar("The fractional seconds precision must be an integer number");
			refresh();
		}
		
	}
	
	private void handleCheckButtonModified() {
		
		if(getEMFEditDomain() == null) return;
		
		boolean newValue = checkButton.getSelection();
		
		SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
				getElement(), getIsTimeZoneFeature(), new Boolean(newValue));
		getEMFEditDomain().getCommandStack().execute(command);
	}
	
	private EStructuralFeature getFractionalSecondsPrecisionFeature() {
		return SQLDataTypesPackage.eINSTANCE.getTimeDataType_FractionalSecondsPrecision();
	}
	
	private EStructuralFeature getIsTimeZoneFeature() {
		return SQLDataTypesPackage.eINSTANCE.getTimeDataType_TimeZone();
	}
	
	
	private int getElementPrecision() {
		if(getElement() != null && getElement() instanceof TimeDataType) {
			return ((TimeDataType)getElement()).getFractionalSecondsPrecision();
		}
		
		return -1;
	}
	
	private boolean getElementTimeZone() {
		if(getElement() != null && getElement() instanceof TimeDataType) {
			return ((TimeDataType)getElement()).isTimeZone();
		}
		
		return false;
	}
	
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

