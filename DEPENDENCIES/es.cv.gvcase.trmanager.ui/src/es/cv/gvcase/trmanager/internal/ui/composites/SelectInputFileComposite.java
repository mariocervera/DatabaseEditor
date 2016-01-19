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

import org.eclipse.emf.edit.ui.action.LoadResourceAction.LoadResourceDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import es.cv.gvcase.trmanager.registry.TransformedRes;

public class SelectInputFileComposite extends SelectSingleResourceComposite {

	
	public SelectInputFileComposite(Composite parent, int style,
			TransformedRes resource) {
		super(parent, style, resource);
	}
	
	
	protected SelectionListener getSelectionListener(Button button,  Text valueText, TransformedRes resource){
		return  new ButtonFileSelectionListener(button, new LoadResourceDialog(getShell()), valueText);	}

	private class ButtonFileSelectionListener implements SelectionListener {

		// Button browseButton;
		LoadResourceDialog dialog;
		Text textField;

		ButtonFileSelectionListener(Button browseButton, LoadResourceDialog dialog, Text textField) {
			// this.browseButton = browseButton;
			this.dialog = dialog;
			this.textField = textField;
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);

		}

		public void widgetSelected(SelectionEvent e) {
			if (dialog.open() == Window.CANCEL){
				return;
			}
			String resource = dialog.getURIText() == null ? "" : dialog.getURIText();
			setResourceSelected(resource);
			textField.setText(resource);
		}
	}	


}
