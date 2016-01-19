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

import java.math.BigInteger;

import org.eclipse.datatools.modelbase.sql.schema.IdentitySpecifier;
import org.eclipse.datatools.modelbase.sql.schema.SQLSchemaPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecoretools.tabbedproperties.providers.TabbedPropertiesLabelProvider;
import org.eclipse.emf.ecoretools.tabbedproperties.utils.TextChangeListener;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

import es.cv.gvcase.mdt.common.composites.DetailComposite;

/**
 * The composite that hosts the widgets to manage the details of the IdentitySpecifier
 * 
 * @author mcervera
 */
public class IdentitySpecifierDetailsComposite extends DetailComposite {

	/** Listeners for text widgets. */
	private TextChangeListener startValueTextListener;
	private TextChangeListener incrementTextListener;
	private TextChangeListener minimumTextListener;
	private TextChangeListener maximumTextListener;
	
	/** Listen events when the cycleCheckBox is selected. */
	private SelectionListener cycleCheckBoxListener;
	
	/** Widgets. */
	
	private CLabel startValueLabel;
	
	private Text startValueText;
	
	private CLabel incrementLabel;
	
	private Text incrementText;
	
	private CLabel minimumLabel;
	
	private Text minimumText;
	
	private CLabel maximumLabel;
	
	private Text maximumText;
	
	private Button cycleCheckButton;
	
	/**
	 * Constructor
	 * 
	 * @param the parent composite
	 * @param the style of the composite
	 * 
	 * @author mcervera
	 */
	public IdentitySpecifierDetailsComposite(Composite parent, int style) {
		super(parent, style);
	}
	
	@Override
	public void createWidgets(Composite composite, TabbedPropertySheetWidgetFactory widgetFactory) {
		
		super.createWidgets(composite, widgetFactory);
		
		widgetFactory.adapt(this);
		
		this.setLayout(new GridLayout(2, false));
		
		startValueLabel = widgetFactory.createCLabel(this, "Start value:");
		startValueText = widgetFactory.createText(this, "", getStyle());
		
		incrementLabel = widgetFactory.createCLabel(this, "Increment:");
		incrementText = widgetFactory.createText(this, "", getStyle());
		
		minimumLabel = widgetFactory.createCLabel(this, "Minimum:");
		minimumText = widgetFactory.createText(this, "", getStyle());
		
		maximumLabel = widgetFactory.createCLabel(this, "Maximum:");
		maximumText = widgetFactory.createText(this, "", getStyle());
		
		cycleCheckButton = widgetFactory.createButton(this, "Cycle", SWT.CHECK);
	}
	
	@Override
	public void setSectionData(Composite composite) {
		
		super.setSectionData(composite);
		
		GridData data = new GridData();
		data.widthHint = getStandardLabelWidth(composite, new String[] { "Start value:" });
		startValueLabel.setLayoutData(data);

		data = new GridData(GridData.FILL_HORIZONTAL);
		startValueText.setLayoutData(data);
		
		data = new GridData();
		data.widthHint = getStandardLabelWidth(composite, new String[] { "Increment:" });
		incrementLabel.setLayoutData(data);

		data = new GridData(GridData.FILL_HORIZONTAL);
		incrementText.setLayoutData(data);
		
		data = new GridData();
		data.widthHint = getStandardLabelWidth(composite, new String[] { "Minimum:" });
		minimumLabel.setLayoutData(data);

		data = new GridData(GridData.FILL_HORIZONTAL);
		minimumText.setLayoutData(data);
		
		data = new GridData();
		data.widthHint = getStandardLabelWidth(composite, new String[] { "Maximum:" });
		maximumLabel.setLayoutData(data);

		data = new GridData(GridData.FILL_HORIZONTAL);
		maximumText.setLayoutData(data);
	}
	
	@Override
	public void hookListeners() {
		
		super.hookListeners();
		
		startValueTextListener = new TextChangeListener() {

			public void textChanged(Control control) {
				handleStartValueTextModified();
			}
		};
		startValueTextListener.startListeningTo(startValueText);
		
		incrementTextListener = new TextChangeListener() {

			public void textChanged(Control control) {
				handleIncrementTextModified();
			}
		};
		incrementTextListener.startListeningTo(incrementText);
		
		minimumTextListener = new TextChangeListener() {

			public void textChanged(Control control) {
				handleMinimumTextModified();
			}
		};
		minimumTextListener.startListeningTo(minimumText);
		
		maximumTextListener = new TextChangeListener() {

			public void textChanged(Control control) {
				handleMaximumTextModified();
			}
		};
		maximumTextListener.startListeningTo(maximumText);
		
		cycleCheckBoxListener = new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				handleCheckButtonModified();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// Do nothing
			}
		};
		
		cycleCheckButton.addSelectionListener(cycleCheckBoxListener);
	}
	
	@Override
	public void loadData() {
		
		refresh();
	}
	
	public void refresh() {
		
		if(startValueText != null && !startValueText.isDisposed()) {
			startValueText.setText(getElementStartValue());
		}
		if(incrementText != null && !incrementText.isDisposed()) {
			incrementText.setText(getElementIncrement());
		}
		if(minimumText != null && !minimumText.isDisposed()) {
			minimumText.setText(getElementMinimum());
		}
		if(maximumText != null && !maximumText.isDisposed()) {
			maximumText.setText(getElementMaximum());
		}
		if(cycleCheckButton != null && !cycleCheckButton.isDisposed()) {
			cycleCheckButton.setSelection(getElementCycle());
		}
	}
	
	protected void handleStartValueTextModified() {
		
		if(getEMFEditDomain() == null) return;
		
		String text = this.startValueText.getText();
		
		if(isInteger(text)) {
			if(getElement() != null && getStartValueFeature() != null) {
				SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
						getElement(), getStartValueFeature(), new BigInteger(text));
				getEMFEditDomain().getCommandStack().execute(command);
			}
		}
		else if (text.equals("")) {
			SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
					getElement(), getStartValueFeature(), null);
			getEMFEditDomain().getCommandStack().execute(command);
		}
		else {
			writeErrorMessageOnStatusBar("Start value must be an integer number");
			refresh();
		}
		
	}
	
	protected void handleIncrementTextModified() {
		
		if(getEMFEditDomain() == null) return;
		
		String text = this.incrementText.getText();
		
		if(isInteger(text)) {
			if(getElement() != null && getIncrementFeature() != null) {
				SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
						getElement(), getIncrementFeature(), new BigInteger(text));
				getEMFEditDomain().getCommandStack().execute(command);
			}
		}
		else if (text.equals("")) {
			SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
					getElement(), getIncrementFeature(), null);
			getEMFEditDomain().getCommandStack().execute(command);
		}
		else {
			writeErrorMessageOnStatusBar("Increment must be an integer number");
			refresh();
		}
		
	}
	
	protected void handleMinimumTextModified() {
		
		if(getEMFEditDomain() == null) return;
		
		String text = this.minimumText.getText();
		
		if(isInteger(text)) {
			if(getElement() != null && getMinimumFeature() != null) {
				SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
						getElement(), getMinimumFeature(), new BigInteger(text));
				getEMFEditDomain().getCommandStack().execute(command);
			}
		}
		else if (text.equals("")) {
			SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
					getElement(), getMinimumFeature(), null);
			getEMFEditDomain().getCommandStack().execute(command);
		}
		else {
			writeErrorMessageOnStatusBar("Minimum must be an integer number");
			refresh();
		}
		
	}
	
	protected void handleMaximumTextModified() {
		
		if(getEMFEditDomain() == null) return;
		
		String text = this.maximumText.getText();
		
		if(isInteger(text)) {
			if(getElement() != null && getMaximumFeature() != null) {
				SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
						getElement(), getMaximumFeature(), new BigInteger(text));
				getEMFEditDomain().getCommandStack().execute(command);
			}
		}
		else if (text.equals("")) {
			SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
					getElement(), getMaximumFeature(), null);
			getEMFEditDomain().getCommandStack().execute(command);
		}
		else {
			writeErrorMessageOnStatusBar("Maximum must be an integer number");
			refresh();
		}
		
	}
	
	private void handleCheckButtonModified() {
		
		if(getEMFEditDomain() == null) return;
		
		boolean newValue = cycleCheckButton.getSelection();
		
		SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
				getElement(), getCycleFeature(), new Boolean(newValue));
		getEMFEditDomain().getCommandStack().execute(command);
	}
	
	private EStructuralFeature getStartValueFeature() {
		return SQLSchemaPackage.eINSTANCE.getIdentitySpecifier_StartValue();
	}
	
	private EStructuralFeature getIncrementFeature() {
		return SQLSchemaPackage.eINSTANCE.getIdentitySpecifier_Increment();
	}
	
	private EStructuralFeature getMinimumFeature() {
		return SQLSchemaPackage.eINSTANCE.getIdentitySpecifier_Minimum();
	}
	
	private EStructuralFeature getMaximumFeature() {
		return SQLSchemaPackage.eINSTANCE.getIdentitySpecifier_Maximum();
	}
	
	private EStructuralFeature getCycleFeature() {
		return SQLSchemaPackage.eINSTANCE.getIdentitySpecifier_CycleOption();
	}
	
	/**
	 * Gets the property startValue of the selected IdentitySpecifier as a String
	 * 
	 * @return the element startValue
	 * 
	 * @author mcervera
	 */
	private String getElementStartValue() {
		if(getElement() != null && getElement() instanceof IdentitySpecifier) {
			if(((IdentitySpecifier)getElement()).getStartValue() != null) {
				return Integer.toString(
						((IdentitySpecifier)getElement()).getStartValue().intValue());
			}
		}
		
		return "";
	}
	
	/**
	 * Gets the property increment of the selected IdentitySpecifier as a String
	 * 
	 * @return the element increment
	 * 
	 * @author mcervera
	 */
	private String getElementIncrement() {
		if(getElement() != null && getElement() instanceof IdentitySpecifier) {
			if(((IdentitySpecifier)getElement()).getIncrement() != null) {
				return Integer.toString(
						((IdentitySpecifier)getElement()).getIncrement().intValue());
			}
		}
		
		return "";
	}
	
	/**
	 * Gets the property minimum of the selected IdentitySpecifier as a String
	 * 
	 * @return the element minimum
	 * 
	 * @author mcervera
	 */
	private String getElementMinimum() {
		if(getElement() != null && getElement() instanceof IdentitySpecifier) {
			if(((IdentitySpecifier)getElement()).getMinimum() != null) {
				return Integer.toString(
						((IdentitySpecifier)getElement()).getMinimum().intValue());
			}
		}
		
		return "";
	}
	
	/**
	 * Gets the property maximum of the selected IdentitySpecifier as a String
	 * 
	 * @return the element maximum
	 * 
	 * @author mcervera
	 */
	private String getElementMaximum() {
		if(getElement() != null && getElement() instanceof IdentitySpecifier) {
			if(((IdentitySpecifier)getElement()).getMaximum() != null) {
				return Integer.toString(
						((IdentitySpecifier)getElement()).getMaximum().intValue());
			}
		}
		
		return "";
	}
	
	/**
	 * Gets the property cycleOption of the selected DataLinkDataType
	 * 
	 * @return the element cycleOption
	 * 
	 * @author mcervera
	 */
	private boolean getElementCycle() {
		if(getElement() != null && getElement() instanceof IdentitySpecifier) {
			return ((IdentitySpecifier)getElement()).isCycleOption();
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
	
	protected ILabelProvider getLabelProvider() {
		return new TabbedPropertiesLabelProvider(new EcoreItemProviderAdapterFactory());
	}
	
}
