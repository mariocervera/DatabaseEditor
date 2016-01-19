/*******************************************************************************
 * Copyright (c) 2008 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Javier Muñoz (Integranova) – Initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager.internal.ui.composites;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import es.cv.gvcase.trmanager.registry.TransformedRes;

public abstract class SelectTransfResourceComposite extends Composite {

	private final static String EMPTY_STRING = "";
		
	private ModifyListener mListener = null;
	
	public SelectTransfResourceComposite(Composite parent, int style, TransformedRes resource) {
		super(parent, style);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.verticalAlignment = SWT.TOP;
		data.grabExcessHorizontalSpace = true;
		this.setLayoutData(data);
		createControls(this,resource);
		
		this.setData(resource);
	}

	protected Text createTextField(Composite parent) {
		Text text = new Text(parent, SWT.SINGLE | SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.verticalAlignment = SWT.CENTER;// GridData.CENTER;
		data.grabExcessVerticalSpace = false;
		data.widthHint = IDialogConstants.ENTRY_FIELD_WIDTH;
		text.setLayoutData(data);
		return text;
	}
	
	protected Button createButton(Composite parent) {
		Button button = new Button(parent, SWT.PUSH);
		GridData data = new GridData();
		data.horizontalAlignment = SWT.RIGHT;
		button.setLayoutData(data);

		return button;
	}
	
	public boolean isResourceSelected(){
		return !getResourceSelected().equals(EMPTY_STRING);
	}
	
	protected abstract void setResourceSelected(String resourcePath);
	
	public abstract String getResourceSelected();
		
	public void setInitialResourcePath(String resourcePath){
		setResourceSelected(resourcePath);
	}
	
	public void addModifyListener(ModifyListener listener){
		this.mListener = listener;
	}
	
	public ModifyListener getModifyListener(){
		return this.mListener;
	}
	
	protected abstract void createControls(Composite parent, TransformedRes resource);
	
	public abstract void updateFieldDecorators();
	
	
}
