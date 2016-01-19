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

import org.eclipse.datatools.modelbase.sql.datatypes.ExactNumericDataType;
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

public class IntegerDetailsComposite extends DetailComposite {

	/**
	 * A helper to listen for events that indicate that a text field has been
	 * changed.
	 */
	private TextChangeListener listener;

	/** Widgets */
	private CLabel labelPrecision;

	private Text textPrecision;

	public IntegerDetailsComposite(Composite parent, int style) {
		super(parent, style);
	}

	public Text getTextPrecision() {
		return textPrecision;
	}

	public CLabel getLabelPrecision() {
		return labelPrecision;
	}

	@Override
	public void createWidgets(Composite composite,
			TabbedPropertySheetWidgetFactory widgetFactory) {

		super.createWidgets(composite, widgetFactory);

		this.setLayout(new FormLayout());

		labelPrecision = widgetFactory.createCLabel(this, "Precision:");

		textPrecision = widgetFactory.createText(this, "", getStyle() | SWT.BORDER);

		if (getPrecisionFeature() != null) {
			boolean isChangeable = getPrecisionFeature().isChangeable();

			textPrecision.setEditable(isChangeable);
			textPrecision.setEnabled(isChangeable);
		}
	}

	@Override
	public void setSectionData(Composite composite) {

		super.setSectionData(composite);

		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(textPrecision,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(textPrecision, 0, SWT.CENTER);
		labelPrecision.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(0, getStandardLabelWidth(composite,
				new String[] { "Precision:" }));
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(labelPrecision, 0, SWT.CENTER);
		textPrecision.setLayoutData(data);

	}

	@Override
	public void hookListeners() {

		super.hookListeners();

		listener = new TextChangeListener() {

			public void textChanged(Control control) {
				handleTextPrecisionModified();
			}
		};
		listener.startListeningTo(textPrecision);
	}

	@Override
	public void loadData() {

		refresh();
	}

	public void refresh() {

		if (textPrecision != null) {
			textPrecision.setText(Integer.toString(getElementPrecision()));
		}
	}

	protected void handleTextPrecisionModified() {

		if (getEMFEditDomain() == null)
			return;

		String text = this.textPrecision.getText();

		if (isInteger(text)) {
			if (getElement() != null && getPrecisionFeature() != null) {
				SetCommand command = (SetCommand) SetCommand.create(
						getEMFEditDomain(), getElement(),
						getPrecisionFeature(), new Integer(text));
				getEMFEditDomain().getCommandStack().execute(command);
				writeErrorMessageOnStatusBar("");
			}
		} else {
			writeErrorMessageOnStatusBar("The precision must be a positive integer number");
			refresh();
		}
	}

	private EStructuralFeature getPrecisionFeature() {
		return SQLDataTypesPackage.eINSTANCE.getNumericalDataType_Precision();
	}

	private int getElementPrecision() {
		if (getElement() != null
				&& getElement() instanceof ExactNumericDataType) {
			return ((ExactNumericDataType) getElement()).getPrecision();
		}

		return -1;
	}

	protected boolean isInteger(String s) {
		try {
			return Integer.parseInt(s) > 0;
		} catch (Exception e) {
			return false;
		}
	}

}
