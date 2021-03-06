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

import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import es.cv.gvcase.trmanager.internal.ui.Messages;
import es.cv.gvcase.trmanager.internal.ui.dialogs.LoadFolderDialog;
import es.cv.gvcase.trmanager.registry.TransformedRes;

public class SelectFolderComposite extends SelectSingleResourceComposite {

	
	public SelectFolderComposite(Composite parent, int style,
			TransformedRes resource) {
		super(parent, style, resource);
	}

	protected SelectionListener getSelectionListener(Button button,  Text valueText, TransformedRes resource){
		button.setText(Messages.getString("SelectNewResourceComposite.SelectFolder")+"...");
		return new ButtonFolderSelectionListener(button, new LoadFolderDialog(getShell()), valueText);
	}
	
	private class ButtonFolderSelectionListener implements SelectionListener {

		// Button browseButton;
		LoadFolderDialog dialog;
		Text textField;

		ButtonFolderSelectionListener(Button browseButton, LoadFolderDialog dialog, Text textField) {
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
