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

import org.eclipse.datatools.modelbase.sql.datatypes.CharacterStringDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.PrimitiveType;
import org.eclipse.datatools.modelbase.sql.datatypes.SQLDataTypesPackage;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
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

/**
 * The composite that hosts the widgets to manage the details of the CharacterStringDataType
 * 
 * @author mcervera
 */
public class CharacterStringDetailsComposite extends DetailComposite {
	
	/** A helper to listen for events that indicate that a text field has been changed. */
	private TextChangeListener textListener;
	
	/** Listen events when the check box is selected. */
	private SelectionListener checkBoxListener;
	
	/** Widgets. */
	
	private CLabel textlabel;
	
	/** The text. */
	private Text text;
	
	/** The check button. */
	private Button checkButton;
	
	
	/**
	 * Constructor
	 * 
	 * @param the parent composite
	 * @param the style of the composite
	 * 
	 * @author mcervera
	 */
	public CharacterStringDetailsComposite(Composite parent, int style) {
		super(parent, style);
	}

	/* (non-Javadoc)
	 * @see es.cv.gvcase.mdt.common.composites.DetailComposite#createWidgets(org.eclipse.swt.widgets.Composite, org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory)
	 */
	@Override
	public void createWidgets(Composite composite, TabbedPropertySheetWidgetFactory widgetFactory) {
		
		super.createWidgets(composite, widgetFactory);
		
		this.setLayout(new FormLayout());
		
		textlabel = widgetFactory.createCLabel(this, "Length:");
		
		text = widgetFactory.createText(this, "", getStyle() | SWT.BORDER);

		if (getLengthFeature() != null) {
			boolean isChangeable = getLengthFeature().isChangeable();

			text.setEditable(isChangeable);
			text.setEnabled(isChangeable);
		}
		
		checkButton = widgetFactory.createButton(this, "Fixed length", SWT.CHECK);

		if (getFixedLengthFeature() != null) {
			checkButton.setEnabled(getFixedLengthFeature().isChangeable());
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
		data.bottom = new FormAttachment(50, 0);
		textlabel.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(0, getStandardLabelWidth(composite, new String[] { "Length:" }));
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

	/* (non-Javadoc)
	 * @see es.cv.gvcase.mdt.common.composites.DetailComposite#hookListeners()
	 */
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

	/* (non-Javadoc)
	 * @see es.cv.gvcase.mdt.common.composites.DetailComposite#loadData()
	 */
	@Override
	public void loadData() {
		
		refresh();
	}
	
	public void refresh() {
		
		if(text != null) {
			text.setText(Integer.toString(getElementLength()));
		}
		if(checkButton != null) {
			checkButton.setSelection(getElementFixedLength());
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
			if(getElement() != null && getLengthFeature() != null) {
				SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
						getElement(), getLengthFeature(), new Integer(text));
				getEMFEditDomain().getCommandStack().execute(command);
			}
		}
		else {
			writeErrorMessageOnStatusBar("The length must be an integer number");
			refresh();
		}
		
	}
	
	/**
	 * Handle the check button widget selection
	 * 
	 * @author mcervera
	 */
	private void handleCheckButtonModified() {
		
		if(getEMFEditDomain() == null) return;
		
		boolean newValue = checkButton.getSelection();
		
		CompoundCommand cc = new CompoundCommand("Change fixed length");
		
		SetCommand command1 = (SetCommand) SetCommand.create(getEMFEditDomain(),
				getElement(), getFixedLengthFeature(), new Boolean(newValue));
		cc.append(command1);
		
		PrimitiveType primitiveType = null;
		String name = "";
		if(newValue) {
			primitiveType = PrimitiveType.CHARACTER_LITERAL;
			name = "Char";
		}
		else {
			primitiveType = PrimitiveType.CHARACTER_VARYING_LITERAL;
			name = "Varchar";
		}
		
		SetCommand command2 = (SetCommand) SetCommand.create(getEMFEditDomain(),
				getElement(), getPrimitiveTypeFeature(), primitiveType);
		cc.append(command2);
		
		SetCommand command3 = (SetCommand) SetCommand.create(getEMFEditDomain(),
				getElement(), EcorePackage.eINSTANCE.getENamedElement_Name(), name);
		cc.append(command3);
		
		getEMFEditDomain().getCommandStack().execute(cc);
	}
	
	/**
	 * Gets the length feature.
	 * 
	 * @return the length feature
	 * 
	 * @author mcervera
	 */
	private EStructuralFeature getLengthFeature() {
		return SQLDataTypesPackage.eINSTANCE.getCharacterStringDataType_Length();
	}
	
	/**
	 * Gets the fixed length feature.
	 * 
	 * @return the fixed length feature
	 * 
	 * @author mcervera
	 */
	private EStructuralFeature getFixedLengthFeature() {
		return SQLDataTypesPackage.eINSTANCE.getCharacterStringDataType_FixedLength();
	}
	
	/**
	 * Gets the primitive type feature.
	 * 
	 * @return the primitive type feature
	 * 
	 * @author mcervera
	 */
	private EStructuralFeature getPrimitiveTypeFeature() {
		return SQLDataTypesPackage.eINSTANCE.getPredefinedDataType_PrimitiveType();
	}
	
	
	/**
	 * Gets the property length of the selected CharacterStringDataType
	 * 
	 * @return the element length
	 * 
	 * @author mcervera
	 */
	private int getElementLength() {
		if(getElement() != null && getElement() instanceof CharacterStringDataType) {
			return ((CharacterStringDataType)getElement()).getLength();
		}
		
		return -1;
	}
	
	/**
	 * Gets the property fixedLength of the selected CharacterStringDataType
	 * 
	 * @return the element fixedLength
	 * 
	 * @author mcervera
	 */
	private boolean getElementFixedLength() {
		if(getElement() != null && getElement() instanceof CharacterStringDataType) {
			return ((CharacterStringDataType)getElement()).isFixedLength();
		}
		
		return false;
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

