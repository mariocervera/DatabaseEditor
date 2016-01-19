/*******************************************************************************
 * Copyright (c) 2008 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Javier Mu�oz (Integranova) � Initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager.internal.ui.composites;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.DecoratedField;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import es.cv.gvcase.trmanager.internal.ui.Messages;
import es.cv.gvcase.trmanager.registry.TransformedRes;

public abstract class  SelectSingleResourceComposite extends SelectTransfResourceComposite {

	private Text paramValueText;
	private Label paramNameLabel;
	private Button browseButton;
	private ControlDecoration decoration;
	
	public SelectSingleResourceComposite(Composite parent, int style,
			TransformedRes resource) {
		super(parent, style, resource);
	}
	
	protected void createControls(Composite parent, TransformedRes resource){
		GridLayout layout = new GridLayout(3,false);
		layout.horizontalSpacing = 10;
		this.setLayout(layout);
		
		paramNameLabel = new Label(parent, SWT.LEFT);
		paramNameLabel.setText(resource.getName() + " :");
		
		paramValueText = createTextField(parent);
		paramValueText.setData(resource);

		decoration = new ControlDecoration(paramValueText,SWT.LEFT);
		decoration.setMarginWidth(3);
		updateFieldDecorators();
		
		browseButton = createButton(parent);
		browseButton.setText(Messages.getString("SelectSingleResourceComposite.SelectResource")+"..."); //$NON-NLS-1$
		
		browseButton.addSelectionListener( getSelectionListener(browseButton,paramValueText,resource));
	}
	
	public void updateFieldDecorators(){
		String resourcePathString = paramValueText.getText();
		String decorationDescription = "";
		Image decorationImage = null;
		if (resourcePathString.equals("")){
			decorationDescription = "Resource path can not be empty";
			decorationImage = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
			this.decoration.show();
		} else {
			Collection<String> messages = new ArrayList<String>();
			boolean isValid = ((TransformedRes)paramValueText.getData()).resourceIsValid(paramValueText.getText(),messages); 
			if (!isValid ){
				if (messages.size() > 0 ){
					decorationDescription = (String) messages.toArray()[0];
				} else {
					decorationDescription = "Resource is not valid for "+((TransformedRes)this.paramValueText.getData()).getName();
				}
				decorationImage = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
				this.decoration.show();
			} else {
			this.decoration.hide();
			}
		}
		
		this.decoration.setDescriptionText(decorationDescription);
		this.decoration.setImage(decorationImage);
	}
	
	@Override
	public String getResourceSelected() {		
		return paramValueText.getText();
	}

	@Override
	protected void setResourceSelected(String resourcePath) {
		this.paramValueText.setText(resourcePath);		
	}

	@Override
	public void addModifyListener(ModifyListener listener) {
		super.addModifyListener(listener);
		if (this.getModifyListener() != null){
			paramValueText.addModifyListener(this.getModifyListener());
		}		
	}
	
	
	protected abstract SelectionListener getSelectionListener(Button button,  Text valueText, TransformedRes resource);



}
