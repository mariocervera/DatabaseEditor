/*******************************************************************************
 * Copyright (c) 2007-2011 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *  Javier Muñoz (Integranova) - Initial implementation
 *  Miguel Llacer (Prodevelop) - Adds DEFAULTCONFIG attribute to the extension point
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;

import es.cv.gvcase.trmanager.Transformation;
import es.cv.gvcase.trmanager.TransformedResource;
import es.cv.gvcase.trmanager.registry.internal.Messages;

/**
 * The TransformationsRegistry class, which manages resource transformations.
 * 
 * @author jmunoz
 */
public class TransformationsRegistry {

	/** The extension point identifier */
	private static String extensionPointId = "es.cv.gvcase.trmanager.transformation";

	/** The identifier of the TRID attribute in a configuration element. */
	private static String TRID_ATTRIBUTE = "trId";

	/** The identifier of the ID attribute in a configuration element. */
	private static String ID_ATTRIBUTE = "id";

	/** The identifier of the PRIORITY attribute in a configuration element. */
	private static String PRIORITY_ATTRIBUTE = "priority";

	/** The identifier of the NAME attribute in a configuration element. */
	private static String NAME_ATTRIBUTE = "name";

	/** The identifier of the CLASS attribute in a configuration element. */
	private static String CLASS_ATTRIBUTE = "class";

	/** The identifier of the PACKAGE attribute in a configuration element. */
	private static String PACKAGE_ATTRIBUTE = "package";

	/** The identifier of the NSURI attribute in a configuration element. */
	private static String METAMODELURI_ATTRIBUTE = "metamodelURI";

	/**
	 * The identifier of the PROJECTNATURE attribute in a configuration element.
	 */
	private static String PROJECTNATURE_ATTRIBUTE = "projectNature";

	private static String UPDATABLE_ATTRIBUTE = "updatable";

	/**
	 * The identifier of the CONFIGINIT attribute in a configuration element.
	 */
	private static String CONFIGINIT_ATTRIBUTE = "configInitializer";

	/**
	 * The identifier of the INPUTRESOURCES reference in a configuration
	 * element.
	 */
	private static String INPUTRESOURCES_REFERENCE = "inputResources";

	/**
	 * The identifier of the OUTPUTRESOURCES reference in a configuration
	 * element.
	 */
	private static String OUTPUTRESOURCES_REFERENCE = "outputResources";

	/**
	 * The identifier of the FILEEXTENSIONS reference in a configuration
	 * element.
	 */
	private static String FILEEXTENSIONS_REFERENCE = "fileExtensions";

	/**
	 * The identifier of the DEFAULTCONFIG attribute in a configuration element.
	 */
	private static String DEFAULTCONFIG_ATTRIBUTE = "defaultConfig";

	/**
	 * The identifier of the DEFAULTCONFIG attribute in a configuration element.
	 */
	private static String USERAVAILABLE_ATTRIBUTE = "userAvailable";

	/**
	 * Gets a description of the transformation that is identified by
	 * transfIdentifier. If more than one of the registered transformations
	 * implements the same kind of transformation, the one with higher priority
	 * is returned.
	 * 
	 * @param transfIdentifier
	 *            the identifier of the kind of transformation.
	 * 
	 * @return a {@link TransformationDesc} describing the transformation.
	 * 
	 * @author jmunoz
	 */
	public static TransformationDesc getTransformation(String transfIdentifier) {

		TransformationDesc tr = new TransformationDesc("NoTransf", "0", "00",
				"NoName", "NoClass");

		HashMap<String, ArrayList<TransformationDesc>> transfByTrId = getTransformationsByTransfId();

		for (TransformationDesc trIter : transfByTrId.get(transfIdentifier)) {
			if (trIter.getPriority() > tr.getPriority()) {
				tr = trIter;
			}
		}

		return tr;
	}

	/**
	 * Gets the description of the transformations indexed by their identifiers.
	 * Since more than one transformation could provide the same kind of
	 * transformation, a list of transformations is returned for every
	 * transformation identifier.
	 * 
	 * @return a HashMap with {@link TransformationDesc} indexed by their
	 *         identifiers.
	 * 
	 * @author jmunoz
	 */
	public static HashMap<String, ArrayList<TransformationDesc>> getTransformationsByTransfId() {
		HashMap<String, ArrayList<TransformationDesc>> transformationsMap = new HashMap<String, ArrayList<TransformationDesc>>();

		List<TransformationDesc> allTransformations = getAllTransformations();

		for (TransformationDesc tr : allTransformations) {
			if (!transformationsMap.containsKey(tr.getTrId())) {
				transformationsMap.put(tr.getTrId(),
						new ArrayList<TransformationDesc>());
			}
			transformationsMap.get(tr.getTrId()).add(tr);
		}

		return transformationsMap;
	}

	/**
	 * Gets a {@link List} with the description of the transformation that can
	 * be configured.
	 * 
	 * @return a {@link List} with {@link TransformationDesc}
	 * 
	 * @author jmunoz
	 */
	public static List<TransformationDesc> getConfigurableTransformations() {
		List<TransformationDesc> transformationsList = new ArrayList<TransformationDesc>();

		List<TransformationDesc> allTransformations = getAllTransformations();

		for (TransformationDesc tr : allTransformations) {
			if (tr.isConfigurable()) {
				transformationsList.add(tr);
			}
		}

		return transformationsList;

	}

	/**
	 * Gets a {@link List} with the description of the transformation that can
	 * be applied to a file extension.
	 * 
	 * @return a {@link List} with {@link TransformationDesc} that can be
	 *         applied to the file extension
	 * 
	 * @author jmunoz
	 */
	public static List<TransformationDesc> getTransformationsByFileExtension(
			String fileExtension) {
		List<TransformationDesc> transformationsList = new ArrayList<TransformationDesc>();

		List<TransformationDesc> allTransformations = getAllTransformations();

		for (TransformationDesc tr : allTransformations) {
			for (String transfExt : tr.getFileExtensions()) {
				if (fileExtension.equals(transfExt)) {
					if (tr.isUserAvailable()) {
						transformationsList.add(tr);
						break;
					}
				}
			}
		}

		return transformationsList;
	}

	/**
	 * Gets a {@link List} with all the description of the registered
	 * transformations.
	 * 
	 * @return a {@link List} with {@link TransformationDesc}
	 * 
	 * @author jmunoz
	 */
	public static List<TransformationDesc> getAllTransformations() {

		List<TransformationDesc> transformations = new ArrayList<TransformationDesc>();

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtension[] extensions = registry.getExtensionPoint(extensionPointId)
				.getExtensions();

		for (IExtension extension : extensions) {
			transformations.addAll(processExtension(extension));
		}

		return transformations;
	}

	/**
	 * Gets the description of the transformations indexed by their package.
	 * Since more than one transformation could be attached to the same package,
	 * a list of transformations is returned for every transformation package.
	 * 
	 * @return a {@link HashMap} with the {@link TransformationDesc} indexed by
	 *         their packages.
	 * 
	 * @author jmunoz
	 */
	public static HashMap<String, ArrayList<TransformationDesc>> getTransformationsByPackage() {

		HashMap<String, ArrayList<TransformationDesc>> transformationsMap = new HashMap<String, ArrayList<TransformationDesc>>();

		List<TransformationDesc> allTransformations = getAllTransformations();

		for (TransformationDesc tr : allTransformations) {
			if (!transformationsMap.containsKey(tr.getPackage())) {
				transformationsMap.put(tr.getPackage(),
						new ArrayList<TransformationDesc>());
			}
			transformationsMap.get(tr.getPackage()).add(tr);
		}

		return transformationsMap;
	}

	/**
	 * Gets the transformation description by its unique identifier.
	 * 
	 * @param transformationId
	 *            the transformation unique identifier
	 * 
	 * @return the {@link TransformationDesc} with the unique identifier
	 * 
	 * @author jmunoz
	 */
	public static TransformationDesc getTransformationById(
			String transformationId) {
		for (TransformationDesc tr : getAllTransformations()) {
			if (tr.getId().equals(transformationId))
				return tr;
		}
		return null;
	}

	/**
	 * Process an Eclipse extension which describes transformations and returns
	 * a list with a list of {@link TransformationDesc} objects.
	 * 
	 * @param extension
	 *            the extension to be parsed.
	 * 
	 * @return the list of {@link TransformationDesc}
	 * 
	 * @author jmunoz
	 */
	private static List<TransformationDesc> processExtension(
			IExtension extension) {
		ArrayList<TransformationDesc> transformations = new ArrayList<TransformationDesc>();
		TransformationDesc trDesc = new TransformationDesc("NoTransf", "5",
				"00", "NoName", "NoClass");

		for (IConfigurationElement configElement : extension
				.getConfigurationElements()) {
			boolean userAvailable = configElement
					.getAttribute(USERAVAILABLE_ATTRIBUTE) != null ? Boolean
					.valueOf(configElement
							.getAttribute(USERAVAILABLE_ATTRIBUTE)) : true;

			trDesc = new TransformationDesc(configElement
					.getAttribute(TRID_ATTRIBUTE), configElement
					.getAttribute(PRIORITY_ATTRIBUTE), configElement
					.getAttribute(ID_ATTRIBUTE), configElement
					.getAttribute(NAME_ATTRIBUTE), configElement
					.getAttribute(CLASS_ATTRIBUTE), userAvailable);
			String packageName = configElement.getAttribute(PACKAGE_ATTRIBUTE);
			if (packageName != null) {
				trDesc.setPackage(packageName);
			}
			trDesc.setConfigElement(configElement);

			String configInit = configElement
					.getAttribute(CONFIGINIT_ATTRIBUTE);
			if (configInit != null) {
				trDesc.setConfigInit(configInit);
			}

			String defaultConfig = configElement
					.getAttribute(DEFAULTCONFIG_ATTRIBUTE);
			trDesc.setDefaultConfig(defaultConfig == null
					|| defaultConfig.equals("true"));

			// Parse parameters
			List<TransformedRes> parameters;
			List<TransformedRes> tmpList;

			// Input parameters
			IConfigurationElement inputResource = null;
			if (configElement.getChildren(INPUTRESOURCES_REFERENCE).length > 0)
				inputResource = configElement
						.getChildren(INPUTRESOURCES_REFERENCE)[0];

			parameters = parseParameters(inputResource);

			tmpList = trDesc.getInputResources();
			tmpList.addAll(parameters);
			trDesc.setInputResources(tmpList);

			// Output parameters
			IConfigurationElement outputResource = null;

			if (configElement.getChildren(OUTPUTRESOURCES_REFERENCE).length > 0)
				outputResource = configElement
						.getChildren(OUTPUTRESOURCES_REFERENCE)[0];

			parameters = parseParameters(outputResource);

			tmpList = trDesc.getOutputResources();
			tmpList.addAll(parameters);
			trDesc.setOutputResources(tmpList);

			// File extensions
			IConfigurationElement fileExtensions = null;
			if (configElement.getChildren(FILEEXTENSIONS_REFERENCE).length > 0)
				fileExtensions = configElement
						.getChildren(FILEEXTENSIONS_REFERENCE)[0];

			List<String> extensions = parseFileExtensions(fileExtensions);

			trDesc.getFileExtensions().addAll(extensions);

			transformations.add(trDesc);
		}

		return transformations;

	}

	/**
	 * Auxiliary method for parsing the parameters of a transformation in an
	 * extension.
	 * 
	 * @param reference
	 *            a reference to the {@link IConfigurationElement} that holds
	 *            the parameters list.
	 * 
	 * @return a list of {@link TransformedRes} objects which represent the
	 *         transformation parameters.
	 * 
	 * @author jmunoz
	 */
	private static List<TransformedRes> parseParameters(
			IConfigurationElement reference) {
		List<TransformedRes> parametersList = new ArrayList<TransformedRes>();

		if (reference == null)
			return parametersList;

		String NAME_ATTRIBUTE = "name";
		String TYPE_ATTRIBUTE = "type";

		for (IConfigurationElement resource : reference.getChildren()) {

			TransformedRes trRes = new TransformedRes(resource
					.getAttribute(NAME_ATTRIBUTE), resource
					.getAttribute(TYPE_ATTRIBUTE));
			if (trRes.getType().equals(TransformedRes.MODEL)) {
				trRes.setMetamodelURI(resource
						.getAttribute(METAMODELURI_ATTRIBUTE));
			} else if (trRes.getType().equals(TransformedRes.PROJECT)) {
				trRes.setProjectNature(resource
						.getAttribute(PROJECTNATURE_ATTRIBUTE));
			}

			if (resource.getAttribute(UPDATABLE_ATTRIBUTE) != null) {
				trRes.setUpdatable(resource.getAttribute(UPDATABLE_ATTRIBUTE)
						.equals("true"));
			}

			parametersList.add(trRes);
		}

		return parametersList;
	}

	/**
	 * Auxiliary method for parsing the file extensions to which a
	 * transformation can be applied.
	 * 
	 * @param reference
	 *            a reference to the {@link IConfigurationElement} that holds
	 *            the file extensions list.
	 * 
	 * @return a list of Strings with the file extensions.
	 * 
	 * @author jmunoz
	 */
	private static List<String> parseFileExtensions(
			IConfigurationElement reference) {
		List<String> fileExtensionsList = new ArrayList<String>();

		if (reference == null)
			return fileExtensionsList;

		String NAME_ATTRIBUTE = "name";

		for (IConfigurationElement resource : reference.getChildren()) {
			fileExtensionsList.add(resource.getAttribute(NAME_ATTRIBUTE));
		}

		return fileExtensionsList;
	}

	/**
	 * Executes the kind of transformation that is identified by
	 * transformationId. It implements the steps of the execution strategy.
	 * 
	 * @param transformationId
	 *            the identifier of the kind of transformation to be executed.
	 * @param inputParams
	 *            the resources that are received as input to the
	 *            transformation.
	 * @param outputParams
	 *            the resources that are produced as output by the
	 *            transformation.
	 * @param messagesList
	 *            a list of messages that inform about events that happened
	 *            while executing the transformation.
	 * 
	 * @return true, if successful
	 * 
	 * @author jmunoz
	 */
	public static boolean executeTransformation(String transformationId,
			HashMap<String, TransformedResource> inputParams,
			HashMap<String, TransformedResource> outputParams,
			List<String> messagesList, boolean synchronize) {

		boolean transfResult = true;
		TransformationDesc transfD = getTransformationById(transformationId);

		// Check correct formal-actual parameters matching
		if (!checkParams(transfD, inputParams, outputParams, messagesList)) {
			return false;
		}

		// Check input models are valid
		if (!validateInputParams(transformationId, inputParams, messagesList)) {
			return false;
		}

		// Run transformation
		transfResult = runTransformation(transformationId, inputParams,
				outputParams, messagesList, null, synchronize);

		return transfResult;
	}

	/**
	 * Checks that actual transformation parameters match with formal
	 * parameters.
	 * 
	 * @param transfD
	 *            the transformation description.
	 * @param inputParams
	 *            the actual input parameters.
	 * @param outputParams
	 *            the actual output parameters.
	 * @param messagesList
	 *            a list with the errors or problems that are detected.
	 * 
	 * @return true, if successful
	 */
	private static boolean checkParams(TransformationDesc transfD,
			HashMap<String, TransformedResource> inputParams,
			HashMap<String, TransformedResource> outputParams,
			List<String> messagesList) {

		boolean result = true;

		if (inputParams.size() < transfD.getInputResources().size()) {
			messagesList.add(Messages
					.getString("TransformationsRegistry.Error_LessParameters") //$NON-NLS-1$
					+ transfD.getInputResources().size());
			return false;
		} else if (inputParams.size() > transfD.getInputResources().size()) {
			messagesList.add(Messages
					.getString("TransformationsRegistry.Error_MoreParameters") //$NON-NLS-1$
					+ transfD.getInputResources().size());
			return false;
		}
		if (outputParams.size() < transfD.getOutputResources().size()) {
			messagesList.add(Messages
					.getString("TransformationsRegistry.Error_LessParameters2") //$NON-NLS-1$
					+ transfD.getOutputResources().size());
			return false;
		} else if (outputParams.size() > transfD.getOutputResources().size()) {
			messagesList.add(Messages
					.getString("TransformationsRegistry.Error_MoreParameters2") //$NON-NLS-1$
					+ transfD.getOutputResources().size());
			return false;
		}

		// TODO: Check resource types match
		// TODO: Check resource properties match

		return result;
	}

	/**
	 * Runs the transformation that is identified by transformationId. It
	 * invokes the actual transformation object.
	 * 
	 * @param transformationId
	 *            the identifier of the kind of transformation be run
	 * @param inputParams
	 *            the resources that are received as input to the
	 *            transformation.
	 * @param outputParams
	 *            the resources that are produced as output by the
	 *            transformation.
	 * @param messagesList
	 *            a list of messages that inform about events that happened
	 *            while running the transformation.
	 * 
	 * @return true, if successful
	 * 
	 * @author jmunoz
	 */
	public static boolean runTransformation(String transformationId,
			HashMap<String, TransformedResource> inputParams,
			HashMap<String, TransformedResource> outputParams,
			List<String> messagesList, IProgressMonitor monitor,
			boolean synchronize) {
		boolean transfResult = false;
		TransformationDesc transfD = getTransformationById(transformationId);
		try {
			Transformation transf = (Transformation) transfD.getConfigElement()
					.createExecutableExtension(CLASS_ATTRIBUTE);
			transf.setSynchronize(synchronize);
			transfResult = transf.transform(inputParams, outputParams,
					messagesList, monitor);
			if (monitor != null)
				monitor.subTask("Refreshing the workspace");
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(
					IResource.DEPTH_INFINITE, null);
			if (monitor != null)
				monitor.worked(10);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return transfResult;
	}

	/**
	 * Runs the configuration transformation that is identified by
	 * transformationId. It invokes the actual transformation object.
	 * 
	 * @param transformationId
	 *            the identifier of the kind of transformation be run
	 * @param inputParams
	 *            the resources that are received as input to the
	 *            transformation.
	 * @param outputParams
	 *            the resources that are produced as output by the
	 *            transformation.
	 * @param messagesList
	 *            a list of messages that inform about events that happened
	 *            while running the transformation.
	 * 
	 * @return true, if successful
	 * 
	 * @author jmunoz
	 */
	// TODO: Change name to runConfigurableTransformation
	public static boolean runTransformation(String transformationId,
			HashMap<String, TransformedResource> inputParams,
			HashMap<String, TransformedResource> outputParams,
			List<String> messagesList, IProgressMonitor monitor,
			TransformedResource configuration, boolean synchronize) {
		boolean transfResult = false;
		TransformationDesc transfD = getTransformationById(transformationId);
		try {
			Transformation transf = (Transformation) transfD.getConfigElement()
					.createExecutableExtension(CLASS_ATTRIBUTE);
			transf.setSynchronize(synchronize);
			transfResult = transf.transform(inputParams, outputParams,
					messagesList, monitor, configuration);
			if (monitor != null)
				monitor.subTask("Refreshing the workspace");
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(
					IResource.DEPTH_INFINITE, null);
			if (monitor != null)
				monitor.worked(10);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return transfResult;
	}

	/**
	 * Runs the configuration transformation that is identified by
	 * transformationId. It invokes the actual transformation object.
	 * 
	 * @param transformationId
	 *            the identifier of the kind of transformation be run
	 * @param inputParams
	 *            the resources that are received as input to the
	 *            transformation.
	 * @param outputParams
	 *            the resources that are produced as output by the
	 *            transformation.
	 * @param messagesList
	 *            a list of messages that inform about events that happened
	 *            while running the transformation.
	 * @param monitor
	 *            a progress monitor to display during the execution of the
	 *            transformation
	 * @param parameters
	 *            optional parameters for the transformation
	 * 
	 * @return true, if successful
	 * 
	 * @author jmunoz
	 */
	public static boolean runTransformation(String transformationId,
			HashMap<String, TransformedResource> inputParams,
			HashMap<String, TransformedResource> outputParams,
			List<String> messagesList, IProgressMonitor monitor,
			Map<String, Object> parameters, boolean synchronize) {
		boolean transfResult = false;
		TransformationDesc transfD = getTransformationById(transformationId);
		try {
			Transformation transf = (Transformation) transfD.getConfigElement()
					.createExecutableExtension(CLASS_ATTRIBUTE);
			transf.setSynchronize(synchronize);
			// set the parameters for the transformation
			transf.setTransformationParameters(parameters);
			transfResult = transf.transform(inputParams, outputParams,
					messagesList, monitor);
			if (monitor != null)
				monitor.subTask("Refreshing the workspace");
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(
					IResource.DEPTH_INFINITE, null);
			if (monitor != null)
				monitor.worked(10);
		} catch (CoreException e) {
		}

		return transfResult;
	}

	/**
	 * Runs the configuration transformation that is identified by
	 * transformationId. It invokes the actual transformation object.
	 * 
	 * @param transformationId
	 *            the identifier of the kind of transformation be run
	 * @param inputParams
	 *            the resources that are received as input to the
	 *            transformation.
	 * @param outputParams
	 *            the resources that are produced as output by the
	 *            transformation.
	 * @param messagesList
	 *            a list of messages that inform about events that happened
	 *            while running the transformation.
	 * @param monitor
	 *            a progress monitor to display during the execution of the
	 *            transformation
	 * @param configuration
	 *            a configuration model
	 * @param parameters
	 *            optional parameters for the transformation
	 * 
	 * 
	 * @return true, if successful
	 * 
	 * @author jmunoz
	 */
	public static boolean runTransformation(String transformationId,
			HashMap<String, TransformedResource> inputParams,
			HashMap<String, TransformedResource> outputParams,
			List<String> messagesList, IProgressMonitor monitor,
			TransformedResource configuration, Map<String, Object> parameters,
			boolean synchronize) {
		boolean transfResult = false;
		TransformationDesc transfD = getTransformationById(transformationId);
		try {
			Transformation transf = (Transformation) transfD.getConfigElement()
					.createExecutableExtension(CLASS_ATTRIBUTE);
			transf.setSynchronize(synchronize);
			// set the parameters for the transformation
			transf.setTransformationParameters(parameters);
			transfResult = transf.transform(inputParams, outputParams,
					messagesList, monitor, configuration);
			if (monitor != null)
				monitor.subTask("Refreshing the workspace");
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(
					IResource.DEPTH_INFINITE, null);
			if (monitor != null)
				monitor.worked(10);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return transfResult;
	}

	/**
	 * Generates an initial configuration for the input parameters.
	 * 
	 * @param transformationId
	 *            the identifier of the kind of transformation
	 * @param inputParams
	 *            the resources that are received as input to the
	 *            transformation.
	 * @param configModel
	 *            the path where the configuration model must be created.
	 * @param messagesList
	 *            a list of messages that inform about events that happened
	 *            while initializing the configuration.
	 * 
	 * @return true, if successful
	 * 
	 * @author jmunoz
	 **/
	public static boolean generateConfiguration(String transformationId,
			HashMap<String, TransformedResource> inputParams,
			String configModel, List<String> messagesList) {
		boolean generationResult = false;
		TransformationDesc transfD = getTransformationById(transformationId);
		try {
			Transformation configInitializer = (Transformation) transfD
					.getConfigElement().createExecutableExtension(
							CONFIGINIT_ATTRIBUTE);

			TransformedResource configModelResource = new TransformedResource(
					"configModel", configModel);
			HashMap<String, TransformedResource> outputs = new HashMap<String, TransformedResource>();
			outputs.put("configModel", configModelResource);

			// Check input models are valid
			if (!validateInputParams(transformationId, inputParams,
					messagesList)) {
				return false;
			}

			// Run transformation
			generationResult = configInitializer.transform(inputParams,
					outputs, messagesList);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return generationResult;
	}

	/**
	 * Executes the actions to validate the parameters that are received as
	 * input to the transformation.
	 * 
	 * @param transformationId
	 *            the transformation id
	 * @param inputs
	 *            the resources that are received as input to the
	 *            transformation.
	 * @param errorList
	 *            a list of messages that inform about problems with the input
	 *            parameters.
	 * 
	 * @return true, if successful
	 * 
	 * @author jmunoz
	 */
	public static boolean validateInputParams(String transformationId,
			HashMap<String, TransformedResource> inputs, List<String> errorList) {
		boolean validationResults = false;

		TransformationDesc transfD = getTransformationById(transformationId);
		try {
			Transformation transf = (Transformation) transfD.getConfigElement()
					.createExecutableExtension("class");
			validationResults = transf.inputsValid(inputs, errorList);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			errorList
					.add(Messages
							.getString("TransformationsRegistry.Error_UnableCreateClass")); //$NON-NLS-1$
			errorList.add(e.getMessage());
		}

		return validationResults;
	}

}
