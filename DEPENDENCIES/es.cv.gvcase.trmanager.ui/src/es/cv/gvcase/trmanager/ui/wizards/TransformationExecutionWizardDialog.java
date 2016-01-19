/*******************************************************************************
 * Copyright (c) 2007-2010 Conselleria de Infraestructuras y Transporte, Generalitat 
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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import es.cv.gvcase.trmanager.TransformationExtraOperations;
import es.cv.gvcase.trmanager.internal.ui.Messages;
import es.cv.gvcase.trmanager.internal.ui.pages.RequestConfigurationPage;
import es.cv.gvcase.trmanager.internal.ui.pages.ShowTransformationResultsPage;
import es.cv.gvcase.trmanager.internal.ui.pages.ValidateTransformationParametersPage;
import es.cv.gvcase.trmanager.ui.pages.IWizardPageHandlesNext;

/**
 * A {@link WizardDialog} for the execution of transformations. <br>
 * This dialog uses the {@link TransformationExecutionWizard} as the underlying
 * wizard to execute all the steps. <br>
 * This wizard handles the 'next pressed' event. Additional pages to the wizard
 * that have been added can implement {@link IWizardPageHandlesNext} to be
 * notified on 'next pressed' events.
 * 
 * @author jmunoz
 * 
 * @see TransformationExecutionWizard
 * @see IWizardPageHandlesNext
 */
public class TransformationExecutionWizardDialog extends WizardDialog {

	private TransformationExtraOperations transformationExtraOperations;

	public TransformationExecutionWizardDialog(Shell parentShell,
			IWizard newWizard) {
		this(parentShell, newWizard, null);
	}

	public TransformationExecutionWizardDialog(Shell parentShell,
			IWizard newWizard,
			TransformationExtraOperations transformationExtraOperations) {
		super(parentShell, newWizard);
		this.transformationExtraOperations = transformationExtraOperations;
	}

	@Override
	protected void nextPressed() {

		IWizardPage currentPage = this.getCurrentPage();

		if (currentPage instanceof es.cv.gvcase.trmanager.internal.ui.pages.RequestTransformationParametersPage) {
			((es.cv.gvcase.trmanager.internal.ui.pages.RequestTransformationParametersPage) currentPage)
					.rememberSelectedParameters();
			if (this
					.getWizard()
					.getPage(
							Messages
									.getString("TransformationExecutionWizardDialog.SelectConfigurationModelPageName")) == null) { //$NON-NLS-1$
				ValidateTransformationParametersPage validatePage = (ValidateTransformationParametersPage) this
						.getWizard()
						.getPage(
								Messages
										.getString("TransformationExecutionWizardDialog.TransformationParametersValidationPageName")); //$NON-NLS-1$

				validatePage.performValidation();
			}
		} else if (currentPage instanceof es.cv.gvcase.trmanager.internal.ui.pages.RequestConfigurationPage) {
			RequestConfigurationPage configPage = (RequestConfigurationPage) currentPage;
			configPage.rememberSelectedConfiguration();
			if (configPage.isNewConfiguration()) {
				List<String> errorList = new ArrayList<String>();
				errorList
						.add("A new configuration model could not be created. Please, check creation parameters.");
				boolean newConfigCreated = configPage
						.createNewConfiguration(errorList);
				if (!newConfigCreated) {
					String message = "";
					for (String s : errorList) {
						message = message + s + "\n";
					}
					MessageDialog.openError(this.getShell(),
							"Error creating new configuration", message);
					return;
				}

				IEditorDescriptor defaultEditor = PlatformUI.getWorkbench()
						.getEditorRegistry().getDefaultEditor(
								configPage.getConfigurationModel());
				if (defaultEditor == null) {
					return;
				}

				try {

					URI uri = URI.createURI(configPage.getConfigurationModel());
					if (uri.isPlatform()) {
						String platformStringURI = uri.isPlatform() ? uri
								.toPlatformString(false) : uri.toFileString();
						Path path = new Path(platformStringURI);
						IFile newConfigFile = null;
						newConfigFile = ResourcesPlugin.getWorkspace()
								.getRoot().getFile(path);

						org.eclipse.ui.ide.IDE.openEditor(PlatformUI
								.getWorkbench().getActiveWorkbenchWindow()
								.getActivePage(), newConfigFile, PlatformUI
								.getWorkbench().getEditorRegistry()
								.getDefaultEditor(
										configPage.getConfigurationModel())
								.getId(), true);

					} else {
						java.net.URI javaURI = java.net.URI.create(configPage
								.getConfigurationModel());
						org.eclipse.ui.ide.IDE.openEditor(PlatformUI
								.getWorkbench().getActiveWorkbenchWindow()
								.getActivePage(), javaURI, PlatformUI
								.getWorkbench().getEditorRegistry()
								.getDefaultEditor(
										configPage.getConfigurationModel())
								.getId(), true);
					}
					this.close();
					return;
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				ValidateTransformationParametersPage validatePage = (ValidateTransformationParametersPage) this
						.getWizard()
						.getPage(
								Messages
										.getString("TransformationExecutionWizardDialog.TransformationParametersValidation")); //$NON-NLS-1$

				validatePage.performValidation();
			}
		} else if (currentPage instanceof es.cv.gvcase.trmanager.internal.ui.pages.ValidateTransformationParametersPage) {
			ShowTransformationResultsPage resultsPage = (ShowTransformationResultsPage) this
					.getWizard()
					.getPage(
							Messages
									.getString("TransformationExecutionWizardDialog.TransformationResults")); //$NON-NLS-1$

			resultsPage
					.performTransformation(this.transformationExtraOperations);
		}
		// handle extra additional pages added that are ready to handle the next
		// pressed
		else if (currentPage instanceof IWizardPageHandlesNext) {
			if (((IWizardPageHandlesNext) currentPage)
					.canHandleNext(getWizard())) {
				((IWizardPageHandlesNext) currentPage).handleNext(getWizard());
			}
		}

		super.nextPressed();
	}

}
