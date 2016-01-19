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
package es.cv.gvcase.trmanager.ui;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import es.cv.gvcase.trmanager.TransformationExtraOperations;
import es.cv.gvcase.trmanager.registry.TransformationDesc;
import es.cv.gvcase.trmanager.ui.wizards.TransformationExecutionWizard;
import es.cv.gvcase.trmanager.ui.wizards.TransformationExecutionWizardDialog;

public class TransformationUIManager {

	public static String TRANSFORMATION_INFO = "es.cv.gvcase.trmanager.tranformationinfo";
	public static String PARAMETER_INPUT = "param_input";
	public static String PARAMETER_OUTPUT = "param_output";
	public static String OVERRIDE_OUTPUT = "override_output";
	public static String CONFIG_MODEL = "config_model";
	public static String LAST_RESOURCE = "last_resource";

	public static void startTransformation(String transformationId) {
		startInitializedTransformation(transformationId, "");

	}

	public static void startInitializedTransformation(String transformationId,
			IResource initialResource) {
		startInitializedTransformation(transformationId, initialResource, null);
	}

	public static void startInitializedTransformation(String transformationId,
			IResource initialResource,
			TransformationExtraOperations transformationExtOperations) {

		TransformationDesc transformation = es.cv.gvcase.trmanager.registry.TransformationsRegistry
				.getTransformationById(transformationId);

		TransformationExecutionWizard wizard = new TransformationExecutionWizard(
				initialResource);
		wizard.init(PlatformUI.getWorkbench(), transformation);

		WizardDialog dialog = new TransformationExecutionWizardDialog(Display
				.getCurrent().getActiveShell(), wizard,
				transformationExtOperations);
		dialog.open();
	}

	public static void startInitializedTransformation(String transformationId,
			String initialResourcePath) {
		IResource sourceResource = null;

		if (initialResourcePath != null && initialResourcePath.length() > 0) {
			if (initialResourcePath.startsWith("platform:")) {
				sourceResource = ResourcesPlugin.getWorkspace().getRoot()
						.findMember(
								initialResourcePath.replaceFirst(
										"platform:/resource", ""));
			} else {
				sourceResource = ResourcesPlugin.getWorkspace().getRoot()
						.findMember(initialResourcePath);
			}
		}

		startInitializedTransformation(transformationId, sourceResource);
	}

}
