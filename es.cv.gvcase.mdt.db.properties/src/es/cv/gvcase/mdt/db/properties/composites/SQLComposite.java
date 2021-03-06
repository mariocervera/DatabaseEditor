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

import org.eclipse.datatools.modelbase.sql.constraints.CheckConstraint;
import org.eclipse.datatools.modelbase.sql.expressions.SQLExpressionsPackage;
import org.eclipse.datatools.modelbase.sql.expressions.SearchConditionDefault;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.ecoretools.tabbedproperties.utils.TextChangeListener;
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

public class SQLComposite extends DetailComposite {
	
	/**
	 * A helper to listen for events that indicate that a text field has been
	 * changed.
	 */
	private TextChangeListener listener;
	
	/** Widgets */
	
	/** The text control for the section. */
	private Text text;

	/** The label */
	private CLabel label;
	
	public SQLComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void createWidgets(Composite composite, TabbedPropertySheetWidgetFactory widgetFactory) {
		
		super.createWidgets(composite, widgetFactory);
		
		this.setLayout(new FormLayout());
		
		label = widgetFactory.createCLabel(this, "SQL:");
		
		text = widgetFactory.createText(this, "", SWT.BORDER);

		if (getFeature() != null) {
			boolean isChangeable = getFeature().isChangeable();

			text.setEditable(isChangeable);
			text.setEnabled(isChangeable);
		}
	}
	
	@Override
	public void setSectionData(Composite composite) {
		
		super.setSectionData(composite);
		
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(text, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(text, 0, SWT.CENTER);
		label.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(0, getStandardLabelWidth(composite, new String[] { "SQL:" }));
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(label, 0, SWT.CENTER);
		text.setLayoutData(data);
		
	}
	
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

	@Override
	public void loadData() {
		
		refresh();
	}
	
	public void refresh() {
		
		if(text != null) {
			text.setText(getElementSQL());
		}
	}

	
	protected void handleTextModified() {
		
		if(getEMFEditDomain() == null) return;
		
		if(getElement() != null && getFeature() != null) {
			SetCommand command = (SetCommand) SetCommand.create(getEMFEditDomain(),
					getElement(), getFeature(), text.getText());
			getEMFEditDomain().getCommandStack().execute(command);
		}
		
	}
	
	@Override
	public SQLObject getElement() {
		SQLObject sqlobject = (SQLObject)super.getElement();
		if(sqlobject instanceof CheckConstraint) {
			CheckConstraint ch = (CheckConstraint) sqlobject;
			if(ch.getSearchCondition() != null
					&& ch.getSearchCondition() instanceof SearchConditionDefault) {
				return (SearchConditionDefault)ch.getSearchCondition();
			}
		}
		
		return null;
	}
	
	private EStructuralFeature getFeature() {
		return SQLExpressionsPackage.eINSTANCE.getSearchConditionDefault_SQL();
	}

	private String getElementSQL() {
		String sql = "";
		if(getElement() instanceof SearchConditionDefault) {
			SearchConditionDefault scd = (SearchConditionDefault) getElement();
			if(scd.getSQL() != null) {
				sql = scd.getSQL();
			}
		}
		return sql;
	}
}
