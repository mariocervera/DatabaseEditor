/*******************************************************************************
 * Copyright (c) 2007 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Francisco Javier Cano Mu�oz (Prodevelop) - Initial API 
 * implementation.
 *
 ******************************************************************************/

package es.cv.gvcase.mdt.db.diagram.sheet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;


// TODO: Auto-generated Javadoc
/**
 * Has methods to create widgets and controls on composites.
 * 
 * @author <a href="mailto:fjcano@prodeelop.es">Francisco Javier Cano Muñoz</a>
 */
public class SqlmodelPropertySectionControlsFactory {

	/**
	 * Creates a new SqlmodelPropertySectionControls object.
	 * 
	 * @param factory the factory
	 * @param composite the composite
	 * 
	 * @return the composite
	 */
	public static Composite createFlatComposite(TabbedPropertySheetWidgetFactory factory,
			Composite composite) {
		Composite flatComposite = factory.createFlatFormComposite(composite);
		FormLayout layout = new FormLayout();
		flatComposite.setLayout(layout);
		return flatComposite;
	}
	
	/**
	 * Creates a new SqlmodelPropertySectionControls object.
	 * 
	 * @param factory the factory
	 * @param composite the composite
	 * 
	 * @return the composite
	 */
	public static Composite createScrolledComposite(TabbedPropertySheetWidgetFactory factory,
			Composite composite) {
		Composite scrolledComposite = factory.createScrolledComposite(composite, SWT.SCROLL_PAGE);
		FormLayout layout = new FormLayout();
		scrolledComposite.setLayout(layout);
		return scrolledComposite;
	}
	
	/**
	 * Creates a new SqlmodelPropertySectionControls object.
	 * 
	 * @param factory the factory
	 * @param composite the composite
	 * 
	 * @return the scrolled form
	 */
	public static ScrolledForm createScrolledForm(TabbedPropertySheetWidgetFactory factory,
			Composite composite) {
		ScrolledForm form = factory.createScrolledForm(composite);
		FormLayout layout = new FormLayout();
		form.setLayout(layout);
		return form;
	}
	
	/**
	 * Creates a new SqlmodelPropertySectionControls object.
	 * 
	 * @param text the text
	 * @param factory the factory
	 * @param composite the composite
	 * 
	 * @return the label
	 */
	public static Label createLabel(String text, 
			TabbedPropertySheetWidgetFactory factory, Composite composite) {  
		Label label = factory.createLabel(composite, text, SWT.LEAD);
		label.setAlignment(SWT.VERTICAL | SWT.CENTER);
		label.setAlignment(SWT.HORIZONTAL | SWT.LEAD);
		return label;
	}
	
	
	/**
	 * Creates a new SqlmodelPropertySectionControls object.
	 * 
	 * @param text the text
	 * @param factory the factory
	 * @param composite the composite
	 * 
	 * @return the text
	 */
	public static Text createText(String text, 
			TabbedPropertySheetWidgetFactory factory, Composite composite) {
		Text widget = factory.createText(composite, text, SWT.BORDER);
		return widget;
	}
	
	
	/**
	 * Creates a new SqlmodelPropertySectionControls object.
	 * 
	 * @param text the text
	 * @param factory the factory
	 * @param composite the composite
	 * 
	 * @return the button
	 */
	public static Button createCheckBox(String text,
			TabbedPropertySheetWidgetFactory factory, Composite composite) {
		Button checkBox = factory.createButton(composite, text, SWT.CHECK | SWT.LEAD);
		return checkBox;
	}
	
	/**
	 * Creates a new SqlmodelPropertySectionControls object.
	 * 
	 * @param factory the factory
	 * @param composite the composite
	 * @param style the style
	 * 
	 * @return the list
	 */
	public static List createList(TabbedPropertySheetWidgetFactory factory,
			Composite composite, int style) {
		List list = factory.createList(composite, style);
		return list;
	}
	
	/**
	 * Creates a new SqlmodelPropertySectionControls object.
	 * 
	 * @param text the text
	 * @param factory the factory
	 * @param composite the composite
	 * 
	 * @return the button
	 */
	public static Button createButton(String text, TabbedPropertySheetWidgetFactory factory,
			Composite composite) {
		Button button = factory.createButton(composite, text, SWT.PUSH);
		return button;
	}
	
	/**
	 * Creates a new SqlmodelPropertySectionControls object.
	 * 
	 * @param factory the factory
	 * @param composite the composite
	 * 
	 * @return the c combo
	 */
	public static CCombo createCombo(TabbedPropertySheetWidgetFactory factory, 
			Composite composite) {
		CCombo combo = factory.createCCombo(composite, SWT.BORDER);
		return combo;
	}
	
	/**
	 * Creates a new SqlmodelPropertySectionControls object.
	 * 
	 * @param factory the factory
	 * @param composite the composite
	 * 
	 * @return the table
	 */
	public static Table createTable(TabbedPropertySheetWidgetFactory factory, Composite composite) {
		
		Table table = factory.createTable(composite, SWT.MULTI | SWT.FULL_SELECTION);
		return table;
	}
	
	
	/**
	 * Sets the form layout data.
	 * 
	 * @param control the control
	 * @param topLeft the top left
	 * @param bottomRight the bottom right
	 * @param margin the margin
	 */
	public static void setFormLayoutData(Control control, Point topLeft, Point bottomRight, int margin) {
		FormData data = new FormData();
		data.top = new FormAttachment(topLeft.y, margin);
		data.bottom = new FormAttachment(bottomRight.y, -margin);
		data.left = new FormAttachment(topLeft.x, margin);
		data.right = new FormAttachment(bottomRight.x, -margin);
		control.setLayoutData(data);
	}
	
}
