/*******************************************************************************
 * Copyright (c) 2010 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Francisco Javier Cano Muñoz (Prodevelop S.L.) - initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager.ui.pages;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;

import es.cv.gvcase.trmanager.ui.wizards.TransformationExecutionWizard;
import es.cv.gvcase.trmanager.ui.wizards.extended.AdditionalTransformationWizardPagesRegistry;

/**
 * Interface to be implements by those additional {@link WizardPage}s that can
 * and will handle the 'next pressed' event in the transformation wizard.
 * 
 * @author <a href="mailto:fjcano@prodevelop.es">Francisco Javier Cano Muñoz</a>
 * 
 * @see AdditionalTransformationWizardPagesRegistry
 */
public interface IWizardPageHandlesNext {

	/**
	 * True to handle the 'next pressed' wizard event.
	 * 
	 * @param wizard
	 * @return
	 */
	boolean canHandleNext(IWizard wizard);

	/**
	 * Handle the 'next pressed' wizard event. <br>
	 * Note that the {@link TransformationExecutionWizard} is an
	 * {@link IAdaptable} that can adapt to Objects that previous pages have
	 * stored in it via {@link TransformationExecutionWizard#addAdaptee(Object)}
	 * . This allow interpage communication relying on the getAdapter(Class)
	 * method.
	 * 
	 * @param wizard
	 */
	void handleNext(IWizard wizard);

}
