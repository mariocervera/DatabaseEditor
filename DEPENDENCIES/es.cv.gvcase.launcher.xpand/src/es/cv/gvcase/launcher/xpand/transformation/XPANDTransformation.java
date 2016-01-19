/***************************************************************************
 * Copyright (c) 2010 Conselleria de Infraestructuras y Transporte,
 * Generalitat de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: 
 *   Miguel Llacer San Fernando (Prodevelop) - Initial API and implementation
 *
 **************************************************************************/
package es.cv.gvcase.launcher.xpand.transformation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;

import es.cv.gvcase.launcher.xpand.XPand2_Launcher;
import es.cv.gvcase.trmanager.Transformation;
import es.cv.gvcase.trmanager.TransformedResource;
import es.cv.gvcase.trmanager.registry.TransformationDesc;
import es.cv.gvcase.trmanager.registry.TransformationsRegistry;
import es.cv.gvcase.trmanager.registry.TransformedRes;

public abstract class XPANDTransformation extends Transformation {

	private ResourceSet resourceSet;

	public static final String CONFIG_EXTENSION = ".transformationconfiguration";

	@Override
	public boolean inputsValid(HashMap<String, TransformedResource> inputs,
			List<String> errorList) {

		if (inputs.values().isEmpty()) {
			errorList.add("Input model must be set");
			return false;
		}

		// Get resource to validate
		String inputModelPath = ((TransformedResource) inputs.values()
				.toArray()[0]).getPath();

		URI uri = URI.createURI(inputModelPath);
		Resource resource = null;

		try {
			resource = getResourceSet().getResource(uri, true);
		} catch (Exception e) {
			errorList.add("<" + inputModelPath + "> doesn't exist");
			return false;
		}

		if (resource == null || resource.getContents().isEmpty()) {
			errorList.add("<" + inputModelPath + "> is empty");
			return false;
		}

		// Validate the model with EMFValidator
		IBatchValidator validator = (IBatchValidator) ModelValidationService
				.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setIncludeLiveConstraints(true);

		IStatus status = validator.validate(resource.getContents().get(0));

		if (!status.isOK()) {
			errorList.add(status.getMessage());
			return false;
		}

		// Validate the model with specific constraints for each transformation
		return modelValidator(resource, errorList);

	}

	@Override
	public boolean transform(HashMap<String, TransformedResource> inputs,
			HashMap<String, TransformedResource> outputs,
			List<String> messageList) {
		Transformation.monitor = null;
		boolean result = transform(inputs, outputs, messageList,
				new NullProgressMonitor(), null);
		Transformation.monitor = null;
		return result;
	}

	@Override
	public boolean transform(HashMap<String, TransformedResource> inputs,
			HashMap<String, TransformedResource> outputs,
			List<String> messageList, IProgressMonitor monitor) {
		Transformation.monitor = monitor;
		boolean result = transform(inputs, outputs, messageList, monitor, null);
		Transformation.monitor = null;
		return result;
	}

	@Override
	public boolean transform(HashMap<String, TransformedResource> inputs,
			HashMap<String, TransformedResource> outputs,
			List<String> messageList, TransformedResource configuration) {
		Transformation.monitor = null;
		boolean result = transform(inputs, outputs, messageList,
				new NullProgressMonitor(), configuration);
		Transformation.monitor = null;
		return result;
	}

	@Override
	public boolean transform(HashMap<String, TransformedResource> inputs,
			HashMap<String, TransformedResource> outputs,
			List<String> messageList, IProgressMonitor monitor,
			TransformedResource configuration) {
		boolean result = false;

		String workflowPath = "/" + getWorkflowName();

		String userInputModelPath = ((TransformedResource) inputs.values()
				.toArray()[0]).getPath();
		String userDestinationFile = ((TransformedResource) outputs.values()
				.toArray()[0]).getPath();
		String confModelPath = "";
		if (configuration != null && configuration.getPath() != null) {
			confModelPath = configuration.getPath();
		}

		URL urlInputModelPath, urlDestinationFile;
		URL urlConfModelPath = null;

		try {
			urlInputModelPath = new URL(userInputModelPath);
			urlDestinationFile = new URL(userDestinationFile);
			if (!confModelPath.equals("")) {
				urlConfModelPath = new URL(confModelPath);
			}
		} catch (MalformedURLException e) {
			messageList.add(e.getMessage());
			return result;
		}

		String inputModelPath, destinationFile;
		String configModelPath = "";

		try {
			inputModelPath = FileLocator.toFileURL(urlInputModelPath).getFile();
			destinationFile = FileLocator.toFileURL(urlDestinationFile)
					.getFile();

			if (urlConfModelPath != null) {
				configModelPath = FileLocator.toFileURL(urlConfModelPath)
						.getFile();
			} else {
				if (hasConfiguration()) {
					// configuration model doesn't exist so a default one is
					// provided
					configModelPath = getConfigModelName(userInputModelPath);
					// if (!existsConfigModel(configModelPath)) {
					createConfigurationModel(userInputModelPath,
							configModelPath);
					// }
					configModelPath = inputModelPath.substring(0,
							inputModelPath.lastIndexOf("/") + 1)
							+ configModelPath.substring(configModelPath
									.lastIndexOf("/") + 1, configModelPath
									.length());
				}
			}
		} catch (IOException e) {
			messageList.add(e.getMessage());
			return result;
		}

		doPreviousOperations(inputModelPath, destinationFile, configModelPath,
				monitor);

		if (monitor != null) {
			monitor.subTask("Executing transformation");
			monitor.worked(40);
		}

		Map<String, String> oawProperties = getOAWProperties(inputModelPath,
				destinationFile, configModelPath);
		Map<String, ?> oawSlotContents = getOAWSlotContents(
				inputModelPath, destinationFile, configModelPath);

		// launch the M2T transformation
		Transformation.monitor = monitor;
		result = XPand2_Launcher.runTransformation(workflowPath,
				destinationFile, inputModelPath, configModelPath, null,
				oawProperties, oawSlotContents, messageList);
		Transformation.monitor = null;

		String aux[] = urlInputModelPath.getPath().split("/");
		String projectName = null;

		if (aux.length >= 2) {
			projectName = aux[2];
		}

		if (projectName == null) {
			return result;
		}

		try {
			ResourcesPlugin.getWorkspace().getRoot().getProject(projectName)
					.refreshLocal(IResource.DEPTH_INFINITE, monitor);
		} catch (CoreException e) {
			messageList.add(e.getMessage());
			return false;
		}
		return result;

	}

	/**
	 * Gets the configuration model name.
	 * 
	 * @param inputModelPath
	 *            the input model name
	 * 
	 * @return the configuration model name
	 */
	protected String getConfigModelName(String inputModelPath) {
		if (inputModelPath.lastIndexOf("/") == -1) {
			return null;
		}
		String inputModelName = inputModelPath.substring(inputModelPath
				.lastIndexOf("/") + 1);
		if (inputModelName.lastIndexOf(".") != -1) {
			inputModelName = inputModelName.substring(0, inputModelName
					.lastIndexOf("."));
			if (getTransformationExtension() != "") {
				inputModelName += "_" + getTransformationExtension();
			}
		}

		String confModelPath = inputModelPath.substring(0, inputModelPath
				.lastIndexOf("/") + 1);
		String confModelName = confModelPath + inputModelName
				+ CONFIG_EXTENSION;

		return confModelName;
	}

	protected boolean existsConfigModel(String confPath) {
		if (confPath.startsWith("platform:/resource")) {
			IPath path = new Path(confPath.replace("platform:/resource", ""));
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			if (file != null && file.exists()) {
				return true;
			}
		} else if (confPath.startsWith("file:")) {
			IPath path = null;
			if (Platform.getOS().equals(Platform.OS_WIN32)) {
				path = new Path(confPath.replace("file:/", ""));
			} else {
				path = new Path(confPath.replace("file:", ""));
			}
			if (path.toFile() != null && path.toFile().exists()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Creates the default configuration model for the input model.
	 * 
	 * @param inputModel
	 *            the input model path
	 * @param configModel
	 *            the configuration model path
	 */
	protected void createConfigurationModel(String inputModel,
			String configModel) {

		HashMap<String, TransformedResource> inputParams = new HashMap<String, TransformedResource>();
		List<String> messagesList = new ArrayList<String>();

		TransformationDesc trDesc = TransformationsRegistry
				.getTransformationById(getTransformationId());
		Iterator<TransformedRes> itTrRes = trDesc.getInputResources()
				.iterator();

		while (itTrRes.hasNext()) {
			String name = itTrRes.next().getName();
			inputParams.put(name, new TransformedResource(name, inputModel));
		}

		TransformationsRegistry.generateConfiguration(getTransformationId(),
				inputParams, configModel, messagesList);
	}

	/**
	 * Model validator. It has to be override if you need validate specific
	 * constraints for the transformation input model
	 * 
	 * @param resource
	 *            the resource
	 * @param errorList
	 *            the error list
	 * 
	 * @return true, if successful
	 */
	protected boolean modelValidator(Resource resource, List<String> errorList) {
		return true;
	}

	/**
	 * Do previous operations if it is necessary.
	 * 
	 * @param inputModelPath
	 *            the input model path
	 * @param destinationFile
	 *            the destination file
	 * @param configModelPath
	 *            the configuration model path
	 * @param monitor
	 *            the progress monitor
	 */
	protected void doPreviousOperations(String inputModelPath,
			String destinationFile, String configModelPath,
			IProgressMonitor monitor) {
	}

	protected Map<String, String> getOAWProperties(String inputModelPath,
			String destinationFile, String configModelPath) {
		return new HashMap<String, String>();
	}

	protected Map<String, ?> getOAWSlotContents(String inputModelPath,
			String destinationFile, String configModelPath) {
		return new HashMap<String, String>();
	}

	/**
	 * Gets the resource set.
	 * 
	 * @return the resource set
	 */
	protected ResourceSet getResourceSet() {
		if (resourceSet == null) {
			resourceSet = new ResourceSetImpl();
		}
		return resourceSet;
	}

	/**
	 * Checks if the specific transformation has configuration.
	 * 
	 * @return true, if successful
	 */
	protected boolean hasConfiguration() {
		return false;
	}

	/**
	 * Gets the specific transformation id.
	 * 
	 * @return the transformation id
	 */
	protected abstract String getTransformationId();

	/**
	 * Gets the specific workflow name.
	 * 
	 * @return the workflow name
	 */
	protected abstract String getWorkflowName();

}
