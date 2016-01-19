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

import org.eclipse.datatools.modelbase.sql.datatypes.IntervalDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.IntervalQualifierType;
import org.eclipse.datatools.modelbase.sql.datatypes.SQLDataTypesPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecoretools.tabbedproperties.providers.TabbedPropertiesLabelProvider;
import org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractTabbedPropertySection;
import org.eclipse.emf.ecoretools.tabbedproperties.sections.widgets.CSingleObjectChooser;
import org.eclipse.emf.ecoretools.tabbedproperties.utils.TextChangeListener;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

import es.cv.gvcase.mdt.common.composites.DetailComposite;

public class IntervalDetailsComposite extends DetailComposite {
	
	private TextChangeListener leadingFieldPrecisionTextListener;
	
	private TextChangeListener trailingFieldPrecisionTextListener;
	
	private TextChangeListener fractionalSecondsPrecisionTextListener;
	
	/** Widgets */
	
	private CLabel leadingQualifierLabel;
	
	private CSingleObjectChooser leadingQualifierChooser;
	
	private CLabel trailingQualifierLabel;
	
	private CSingleObjectChooser trailingQualifierChooser;
	
	private CLabel leadingFieldPrecisionLabel;
	
	private Text leadingFieldPrecisionText;
	
	private CLabel trailingFieldPrecisionLabel;
	
	private Text trailingFieldPrecisionText;
	
	private CLabel fractionalSecondsPrecisionLabel;
	
	private Text fractionalSecondsPrecisionText;
	
	private AbstractTabbedPropertySection section;
	
	
	public IntervalDetailsComposite(Composite parent, int style) {
		super(parent, style);
		
	}
	
	public IntervalDetailsComposite(Composite parent, int style, AbstractTabbedPropertySection section) {
		super(parent, style);
		
		this.section = section;
	}

	@Override
	public void createWidgets(Composite composite, TabbedPropertySheetWidgetFactory widgetFactory) {
		
		super.createWidgets(composite, widgetFactory);
		
		this.setLayout(new GridLayout(2, false));
		
		leadingQualifierLabel = widgetFactory.createCLabel(this, "Leading interval:");

		leadingQualifierChooser = new CSingleObjectChooser(this, widgetFactory, SWT.NONE);
		leadingQualifierChooser.setChoices(getLeadingQualifierChoices());
		leadingQualifierChooser.setLabelProvider(getLabelProvider());
		leadingQualifierChooser.setSection(section);

		if (getLeadingQualifierFeature() != null) {
			leadingQualifierChooser.setChangeable(getLeadingQualifierFeature().isChangeable());
		}
		
		trailingQualifierLabel = widgetFactory.createCLabel(this, "Trailing interval:");

		trailingQualifierChooser = new CSingleObjectChooser(this, widgetFactory, SWT.NONE);
		trailingQualifierChooser.setChoices(getTrailingQualifierChoices());
		trailingQualifierChooser.setLabelProvider(getLabelProvider());
		trailingQualifierChooser.setSection(section);

		if (getTrailingQualifierFeature() != null) {
			trailingQualifierChooser.setChangeable(getTrailingQualifierFeature().isChangeable());
		}
		
		leadingFieldPrecisionLabel = widgetFactory.createCLabel(this, "Leading Field Precision:");
		
		leadingFieldPrecisionText = widgetFactory.createText(this, "", getStyle() | SWT.BORDER);

		if (getLeadingFieldPrecisionFeature() != null) {
			boolean isChangeable = getLeadingFieldPrecisionFeature().isChangeable();

			leadingFieldPrecisionText.setEditable(isChangeable);
			leadingFieldPrecisionText.setEnabled(isChangeable);
		}
		
		trailingFieldPrecisionLabel = widgetFactory.createCLabel(this, "Trailing Field Precision:");
		
		trailingFieldPrecisionText = widgetFactory.createText(this, "", getStyle() | SWT.BORDER);

		if (getTrailingFieldPrecisionFeature() != null) {
			boolean isChangeable = getTrailingFieldPrecisionFeature().isChangeable();

			trailingFieldPrecisionText.setEditable(isChangeable);
			trailingFieldPrecisionText.setEnabled(isChangeable);
		}
		
		fractionalSecondsPrecisionLabel = widgetFactory.createCLabel(this, "Fractional Seconds Precision:");
		
		fractionalSecondsPrecisionText = widgetFactory.createText(this, "", getStyle() | SWT.BORDER);

		if (getFractionalSecondsPrecisionFeature() != null) {
			boolean isChangeable = getFractionalSecondsPrecisionFeature().isChangeable();

			fractionalSecondsPrecisionText.setEditable(isChangeable);
			fractionalSecondsPrecisionText.setEnabled(isChangeable);
		}
		
	}

	@Override
	public void setSectionData(Composite composite) {
		
		super.setSectionData(composite);
		
		GridData data = new GridData();
		data.widthHint = getStandardLabelWidth(composite, new String[] { "Leading interval:" });
		leadingQualifierLabel.setLayoutData(data);

		data = new GridData(GridData.FILL_HORIZONTAL);
		leadingQualifierChooser.setLayoutData(data);
		
		data = new GridData();
		data.widthHint = getStandardLabelWidth(composite, new String[] { "Trailing interval:" });
		trailingQualifierLabel.setLayoutData(data);

		data = new GridData(GridData.FILL_HORIZONTAL);
		trailingQualifierChooser.setLayoutData(data);
		
		data = new GridData();
		data.widthHint = getStandardLabelWidth(composite, new String[] { "Leading Field Precision:" });
		leadingFieldPrecisionLabel.setLayoutData(data);

		data = new GridData(GridData.FILL_HORIZONTAL);
		leadingFieldPrecisionText.setLayoutData(data);
		
		data = new GridData();
		data.widthHint = getStandardLabelWidth(composite, new String[] { "Trailing Field Precision:" });
		trailingFieldPrecisionLabel.setLayoutData(data);

		data = new GridData(GridData.FILL_HORIZONTAL);
		trailingFieldPrecisionText.setLayoutData(data);
		
		data = new GridData();
		data.widthHint = getStandardLabelWidth(composite, new String[] { "Fractional Seconds Precision:" });
		fractionalSecondsPrecisionLabel.setLayoutData(data);

		data = new GridData(GridData.FILL_HORIZONTAL);
		fractionalSecondsPrecisionText.setLayoutData(data);
		
	}

	@Override
	public void hookListeners() {
		
		super.hookListeners();
		
		leadingQualifierChooser.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleLeadingQualifierModified();
			}
		});
		
		trailingQualifierChooser.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleTrailingQualifierModified();
			}
		});
		
		leadingFieldPrecisionTextListener = new TextChangeListener() {

			public void textChanged(Control control) {
				handleLeadingFieldPrecisionModified();
			}
		};
		leadingFieldPrecisionTextListener.startListeningTo(leadingFieldPrecisionText);
		
		trailingFieldPrecisionTextListener = new TextChangeListener() {

			public void textChanged(Control control) {
				handleTrailingFieldPrecisionModified();
			}
		};
		trailingFieldPrecisionTextListener.startListeningTo(trailingFieldPrecisionText);
		
		fractionalSecondsPrecisionTextListener = new TextChangeListener() {

			public void textChanged(Control control) {
				handleFractionalSecondsPrecisionModified();
			}
		};
		fractionalSecondsPrecisionTextListener.startListeningTo(fractionalSecondsPrecisionText);
		
	}

	@Override
	public void loadData() {
		
		refresh();
	}
	
	public void refresh() {
		
		if(leadingQualifierChooser != null) {
			leadingQualifierChooser.setSelection(getElementLeadingQualifier());
		}
		if(trailingQualifierChooser != null) {
			trailingQualifierChooser.setSelection(getElementTrailingQualifier());
		}
		if(leadingFieldPrecisionText != null) {
			leadingFieldPrecisionText.setText(Integer.toString(getElementLeadingFieldPrecision()));
		}
		if(trailingFieldPrecisionText != null) {
			trailingFieldPrecisionText.setText(Integer.toString(getElementTrailingFieldPrecision()));
		}
		if(fractionalSecondsPrecisionText != null) {
			fractionalSecondsPrecisionText.setText(Integer.toString(getElementFractionalSecondsPrecision()));
		}
		
	}
	
	private void handleLeadingQualifierModified() {
		
		if(getEMFEditDomain() == null) return;
		
		IntervalQualifierType iqt = (IntervalQualifierType) leadingQualifierChooser.getSelection();
		
		SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
				getElement(), getLeadingQualifierFeature(), iqt);
		getEMFEditDomain().getCommandStack().execute(command);
	}
	
	private void handleTrailingQualifierModified() {
		
		if(getEMFEditDomain() == null) return;
		
		IntervalQualifierType iqt = (IntervalQualifierType) trailingQualifierChooser.getSelection();
		
		SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
				getElement(), getTrailingQualifierFeature(), iqt);
		getEMFEditDomain().getCommandStack().execute(command);
	}
	
	private void handleLeadingFieldPrecisionModified() {
		
		if(getEMFEditDomain() == null) return;
		
		String text = this.leadingFieldPrecisionText.getText();
		
		if(isInteger(text)) {
			if(getElement() != null && getLeadingFieldPrecisionFeature() != null) {
				SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
						getElement(), getLeadingFieldPrecisionFeature(), new Integer(text));
				getEMFEditDomain().getCommandStack().execute(command);
			}
		}
		else {
			writeErrorMessageOnStatusBar("The leading field precision must be an integer number");
			refresh();
		}
		
	}
	
	private void handleTrailingFieldPrecisionModified() {
		
		if(getEMFEditDomain() == null) return;
		
		String text = this.trailingFieldPrecisionText.getText();
		
		if(isInteger(text)) {
			if(getElement() != null && getTrailingFieldPrecisionFeature() != null) {
				SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
						getElement(), getTrailingFieldPrecisionFeature(), new Integer(text));
				getEMFEditDomain().getCommandStack().execute(command);
			}
		}
		else {
			writeErrorMessageOnStatusBar("The trailing field precision must be an integer number");
			refresh();
		}
		
	}

	private void handleFractionalSecondsPrecisionModified() {
		
		if(getEMFEditDomain() == null) return;
		
		String text = this.fractionalSecondsPrecisionText.getText();
		
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
	
	
	private EStructuralFeature getLeadingQualifierFeature() {
		return SQLDataTypesPackage.eINSTANCE.getIntervalDataType_LeadingQualifier();
	}
	
	private EStructuralFeature getTrailingQualifierFeature() {
		return SQLDataTypesPackage.eINSTANCE.getIntervalDataType_TrailingQualifier();
	}
	
	private EStructuralFeature getLeadingFieldPrecisionFeature() {
		return SQLDataTypesPackage.eINSTANCE.getIntervalDataType_LeadingFieldPrecision();
	}
	
	private EStructuralFeature getTrailingFieldPrecisionFeature() {
		return SQLDataTypesPackage.eINSTANCE.getIntervalDataType_TrailingFieldPrecision();
	}
	
	private EStructuralFeature getFractionalSecondsPrecisionFeature() {
		return SQLDataTypesPackage.eINSTANCE.getIntervalDataType_FractionalSecondsPrecision();
	}
	
	private IntervalQualifierType getElementLeadingQualifier() {
		if(getElement() != null && getElement() instanceof IntervalDataType) {
			return ((IntervalDataType)getElement()).getLeadingQualifier();
		}
		
		return null;
	}
	
	private IntervalQualifierType getElementTrailingQualifier() {
		if(getElement() != null && getElement() instanceof IntervalDataType) {
			return ((IntervalDataType)getElement()).getTrailingQualifier();
		}
		
		return null;
	}
	
	private int getElementLeadingFieldPrecision() {
		if(getElement() != null && getElement() instanceof IntervalDataType) {
			return ((IntervalDataType)getElement()).getLeadingFieldPrecision();
		}
		
		return -1;
	}
	
	private int getElementTrailingFieldPrecision() {
		if(getElement() != null && getElement() instanceof IntervalDataType) {
			return ((IntervalDataType)getElement()).getTrailingFieldPrecision();
		}
		
		return -1;
	}
	
	private int getElementFractionalSecondsPrecision() {
		if(getElement() != null && getElement() instanceof IntervalDataType) {
			return ((IntervalDataType)getElement()).getFractionalSecondsPrecision();
		}
		
		return -1;
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
	
	protected ILabelProvider getLabelProvider() {
		return new TabbedPropertiesLabelProvider(new EcoreItemProviderAdapterFactory());
	}
	
	private Object[] getLeadingQualifierChoices() {
		Object[] result = new Object[7];
		
		result[0] = IntervalQualifierType.YEAR_LITERAL;
		result[1] = IntervalQualifierType.HOUR_LITERAL;
		result[2] = IntervalQualifierType.SECOND_LITERAL;
		result[3] = IntervalQualifierType.MINUTE_LITERAL;
		result[4] = IntervalQualifierType.DAY_LITERAL;
		result[5] = IntervalQualifierType.MONTH_LITERAL;
		result[6] = IntervalQualifierType.FRACTION_LITERAL;
		
		return result;
	}
	
	private Object[] getTrailingQualifierChoices() {
		Object[] result = new Object[7];
		
		result[0] = IntervalQualifierType.YEAR_LITERAL;
		result[1] = IntervalQualifierType.HOUR_LITERAL;
		result[2] = IntervalQualifierType.SECOND_LITERAL;
		result[3] = IntervalQualifierType.MINUTE_LITERAL;
		result[4] = IntervalQualifierType.DAY_LITERAL;
		result[5] = IntervalQualifierType.MONTH_LITERAL;
		result[6] = IntervalQualifierType.FRACTION_LITERAL;
		
		return result;
	}

}

