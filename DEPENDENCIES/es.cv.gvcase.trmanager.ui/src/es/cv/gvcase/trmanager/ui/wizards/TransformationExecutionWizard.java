/*******************************************************************************
 * Copyright (c) 2007 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Javier Mu�oz (Integranova) � Initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager.ui.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.IWorkbench;

import es.cv.gvcase.trmanager.internal.ui.Messages;
import es.cv.gvcase.trmanager.internal.ui.pages.RequestConfigurationPage;
import es.cv.gvcase.trmanager.internal.ui.pages.RequestTransformationParametersPage;
import es.cv.gvcase.trmanager.internal.ui.pages.ShowTransformationResultsPage;
import es.cv.gvcase.trmanager.internal.ui.pages.ValidateTransformationParametersPage;
import es.cv.gvcase.trmanager.registry.TransformationDesc;
import es.cv.gvcase.trmanager.ui.wizards.extended.AdditionalTransformationWizardPagesRegistry;

/**
 * A {@link Wizard} that assists during the execution of a Transformation. <br>
 * Transformations executed via this wizard have been previously registered in
 * the MOSKitt transformation registry via contributions to a extension point. <br>
 * This wizard consists, in its basic form, in four pages:
 * <ol>
 * <li>Parameters page: for input and output selection of the transformation.</li>
 * <li>Validation page: checks the correctness of the model, informs of errors
 * and warnings.</li>
 * <li>Configuration page: allows creating or selecting an existing
 * configuration model for the input model.</li>
 * <li>Results page: shows the results of the execution of the transformation.
 * Informs of any errors and/or warnings in the execution.</li>
 * </ol>
 * This wizard has a storage where pages can store Objects for other pages to
 * retrieve them via the {@link IAdaptable#getAdapter(Class)} method. This
 * allows interpage communication. <br>
 * Additional pages can be added to this transformation wizard via contributions
 * to the additionalTransformationWizardPages extension point. <br>
 * 
 * @author jmunoz
 * 
 * @see TransformationExecutionWizardDialog
 * @see AdditionalTransformationWizardPagesRegistry
 */
public class TransformationExecutionWizard extends Wizard implements IAdaptable {

	private TransformationDesc transformation;

	private IResource sourceResource = null;

	public TransformationExecutionWizard() {
		constructorCommonActions();
	}

	public TransformationExecutionWizard(IResource sourceResource) {
		constructorCommonActions();
		this.sourceResource = sourceResource;
	}

	public void init(IWorkbench workbench, TransformationDesc transformation) {
		this.transformation = transformation;
	}

	private void constructorCommonActions() {
		setNeedsProgressMonitor(false);
		setWindowTitle(Messages
				.getString("TransformationExecutionWizard.EjecutaTransformacion")); //$NON-NLS-1$		
	}

	public void addPages() {
		int width = 500;
		int height = 480;
		getShell().setMinimumSize(width, height);

		AdditionalTransformationWizardPagesRegistry pagesAdder = AdditionalTransformationWizardPagesRegistry
				.getInstance();
		String transformationID = transformation.getId();
		// add additional pages at start
		for (WizardPage page : pagesAdder
				.getAllStartWizardPages(transformationID)) {
			addPage(page);
		}

		IWizardPage pageRequestParams = new RequestTransformationParametersPage(
				Messages
						.getString("TransformationExecutionWizard.EnterTransformationParameters"), //$NON-NLS-1$
				this.transformation, this.sourceResource);
		addPage(pageRequestParams);

		// add additional pages after paremeters
		for (WizardPage page : pagesAdder
				.getAllAfterParametersWizardPages(transformationID)) {
			addPage(page);
		}

		if (this.transformation.getConfigInit() != null) {
			IWizardPage pageRequestConfig = new RequestConfigurationPage(
					Messages
							.getString("TransformationExecutionWizard.SelectConfigurationModel"), this.transformation, this.sourceResource); //$NON-NLS-1$
			addPage(pageRequestConfig);
		}

		// add additional pages after configuration
		for (WizardPage page : pagesAdder
				.getAllAfterConfigurationWizardPages(transformationID)) {
			addPage(page);
		}

		IWizardPage pageValidationResults = new ValidateTransformationParametersPage(
				Messages
						.getString("TransformationExecutionWizard.TransformationParametersValidation"), //$NON-NLS-1$
				this.transformation);
		addPage(pageValidationResults);

		// add additional pages after validation
		for (WizardPage page : pagesAdder
				.getAllAfterValidationWizardPages(transformationID)) {
			addPage(page);
		}

		IWizardPage pageTransformationResults = new ShowTransformationResultsPage(
				Messages
						.getString("TransformationExecutionWizard.TransformationResults"), this.transformation); //$NON-NLS-1$
		addPage(pageTransformationResults);

		// add additional pages at end
		for (WizardPage page : pagesAdder
				.getAllBeforeEndWizardPages(transformationID)) {
			addPage(page);
		}
	}

	public boolean canFinish() {
		return this.getPages()[this.getPages().length - 1].isPageComplete();
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	// //
	// IAdaptable. This is used by additional pages to store and get information
	// between them
	// //

	private List<Object> adaptees = null;

	public void addAdaptee(Object o) {
		if (adaptees == null) {
			adaptees = new ArrayList<Object>();
		}
		adaptees.add(o);
	}

	public Object getAdapter(Class adapter) {
		if (adaptees != null) {
			for (Object o : adaptees) {
				if (adapter.isAssignableFrom(o.getClass())) {
					return o;
				}
			}
		}
		if (adapter.isAssignableFrom(ITransformationWizardInputResources.class)) {
			IWizardPage pageRequestParams = getPage(Messages
					.getString("TransformationExecutionWizard.EnterTransformationParameters")); //$NON-NLS-1$
			if (pageRequestParams instanceof RequestTransformationParametersPage) {
				return new ITransformationWizardInputResourcesImpl(
						((RequestTransformationParametersPage) pageRequestParams)
								.getInputParams());
			}
		}
		return null;
	}

}
