/*******************************************************************************
 * Copyright (c) 2011 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Francisco Javier Cano Muñoz (Prodevelop S.L.) - initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager.ui.pages;

import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;

import es.cv.gvcase.trmanager.Transformation;

/**
 * A {@link WizardPage} that can provide parameters in a Map. <br>
 * These parameters are intended as parameters for the {@link Transformation}.
 * 
 * @author fjcano
 * 
 */
public interface IWizardPageParametersProvider {

	/**
	 * 
	 * @return A Map with the key _> value of the parameters.
	 */
	Map<String, Object> getParameters();

}
