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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

public class FixedPrecisionDetailsComposite extends IntegerDetailsComposite {

	/**
	 * A helper to listen for events that indicate that a text field has been
	 * changed.
	 */
	private TextChangeListener listener;

	private CLabel labelScale;

	private Text textScale;

	public FixedPrecisionDetailsComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void createWidgets(Composite composite,
			TabbedPropertySheetWidgetFactory widgetFactory) {

		super.createWidgets(composite, widgetFactory);

		labelScale = widgetFactory.createCLabel(this, "Scale:");

		textScale = widgetFactory.createText(this, "", getStyle() | SWT.BORDER);

		if (getScaleFeature() != null) {
			boolean isChangeable = getScaleFeature().isChangeable();

			textScale.setEditable(isChangeable);
			textScale.setEnabled(isChangeable);
		}
	}

	@Override
	public void setSectionData(Composite composite) {

		super.setSectionData(composite);

		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(textScale,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(60, 0);
		labelScale.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(0, getStandardLabelWidth(composite,
				new String[] { "Scale:" }));
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(60, 0);
		textScale.setLayoutData(data);
	}

	@Override
	public void hookListeners() {

		super.hookListeners();

		listener = new TextChangeListener() {

			public void textChanged(Control control) {
				handleTextScaleModified();
			}
		};
		listener.startListeningTo(textScale);
	}

	@Override
	public void refresh() {

		super.refresh();

		if (textScale != null) {
			textScale.setText(Integer.toString(getElementPrecision()));
		}
	}

	protected void handleTextScaleModified() {

		if (getEMFEditDomain() == null)
			return;

		String text = this.textScale.getText();

		if (isInteger(text)) {
			if (getElement() != null && getScaleFeature() != null) {
				SetCommand command = (SetCommand) SetCommand.create(
						getEMFEditDomain(), getElement(), getScaleFeature(),
						new Integer(text));
				getEMFEditDomain().getCommandStack().execute(command);
				writeErrorMessageOnStatusBar("");
			}
		} else {
			writeErrorMessageOnStatusBar("The scale must be a positive integer number");
			refresh();
		}
	}

	private EStructuralFeature getScaleFeature() {
		return SQLDataTypesPackage.eINSTANCE.getExactNumericDataType_Scale();
	}

	private int getElementPrecision() {
		if (getElement() != null
				&& getElement() instanceof ExactNumericDataType) {
			return ((ExactNumericDataType) getElement()).getScale();
		}

		return 0;
	}

}
