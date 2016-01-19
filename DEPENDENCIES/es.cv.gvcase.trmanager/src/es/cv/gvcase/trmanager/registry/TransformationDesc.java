/*******************************************************************************
 * Copyright (c) 2007,2009 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Javier Muñoz (Integranova) – Initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager.registry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;

// TODO: Auto-generated Javadoc
/**
 * A description of a Transformation.
 * 
 * @author jmunoz
 */
public class TransformationDesc {

	/** The transformation unique identifier. */
	private String id;

	/** The identifier of the kind of transformation that implements. */
	private String trId;

	/** The transformation priority. */
	private int priority;

	/** The name of the transformation. */
	private String name;

	/** The input resources. */
	private List<TransformedRes> inputResources;

	/** The output resources. */
	private List<TransformedRes> outputResources;

	/** The file extensions where this transformation can be applied. */
	private List<String> fileExtensions;

	/**
	 * The qualified name of the class which implements the transformation that
	 * is described by this class.
	 */
	private String class_;

	/** The transformations package. */
	private String package_;

	/**
	 * The qualified name of a class that is used to initialize the
	 * configuration of this transformation *.
	 */
	private String configInit;

	/**
	 * The configuration element which represents the transformation in an
	 * extension.
	 */
	private IConfigurationElement configElement;

	/** Specifies if default configuration can be selected or not. */
	private boolean defaultConfig;

	/** Specifies if transformation is available for the user. */
	private boolean userAvailable = true;

	/**
	 * Instantiates a new transformation description initializing basic
	 * transformation information.
	 * 
	 * @param trId
	 *            The identifier of the kind of transformation that implements.
	 * @param priority
	 *            The transformation priority.
	 * @param id
	 *            The transformation unique identifier.
	 * @param name
	 *            The name of the transformation.
	 * @param class_
	 *            The qualified name of the class which implements the
	 *            transformation.
	 * @param userAvailable
	 *            the user availability
	 * 
	 * @author jmunoz
	 */
	public TransformationDesc(String trId, String priority, String id,
			String name, String class_, boolean userAvailable) {
		this(trId, priority, id, name, class_, null, null, userAvailable);
	}

	/**
	 * Instantiates a new transformation description initializing basic
	 * transformation information.
	 * 
	 * @param trId
	 *            The identifier of the kind of transformation that implements.
	 * @param priority
	 *            The transformation priority.
	 * @param id
	 *            The transformation unique identifier.
	 * @param name
	 *            The name of the transformation.
	 * @param class_
	 *            The qualified name of the class which implements the
	 *            transformation.
	 * 
	 * @author jmunoz
	 */
	public TransformationDesc(String trId, String priority, String id,
			String name, String class_) {
		this(trId, priority, id, name, class_, null, null);
	}

	/**
	 * Instantiates a new transformation description initializing the
	 * transformation information.
	 * 
	 * @param trId
	 *            The identifier of the kind of transformation that implements.
	 * @param priority
	 *            The transformation priority.
	 * @param id
	 *            The transformation unique identifier.
	 * @param name
	 *            The name of the transformation.
	 * @param class_
	 *            The qualified name of the class which implements the
	 *            transformation.
	 * @param package_
	 *            The transformation package.
	 * @param configInit
	 *            the config init
	 * 
	 * @author jmunoz
	 */
	public TransformationDesc(String trId, String priority, String id,
			String name, String class_, String package_, String configInit) {
		this(trId, priority, id, name, class_, package_, configInit, true);
	}

	/**
	 * Instantiates a new transformation description initializing the
	 * transformation information.
	 * 
	 * @param trId
	 *            The identifier of the kind of transformation that implements.
	 * @param priority
	 *            The transformation priority.
	 * @param id
	 *            The transformation unique identifier.
	 * @param name
	 *            The name of the transformation.
	 * @param class_
	 *            The qualified name of the class which implements the
	 *            transformation.
	 * @param package_
	 *            The transformation package.
	 * @param configInit
	 *            the config init
	 * @param userAvailable
	 *            the user availability
	 * 
	 * @author jmunoz
	 */
	public TransformationDesc(String trId, String priority, String id,
			String name, String class_, String package_, String configInit,
			boolean userAvailable) {
		this.trId = trId;
		this.priority = new Integer(priority);
		this.id = id;
		this.name = name;
		this.class_ = class_;
		this.package_ = package_;
		this.configInit = configInit;
		this.inputResources = new ArrayList<TransformedRes>();
		this.outputResources = new ArrayList<TransformedRes>();
		this.fileExtensions = new ArrayList<String>();
		this.configElement = null;
		this.userAvailable = userAvailable;
	}

	/**
	 * Gets the transformation unique identifier. It must be unique among all
	 * the registered transformations.
	 * 
	 * @return the unique identifier
	 * 
	 * @author jmunoz
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the transformation name.
	 * 
	 * @return the name
	 * 
	 * @author jmunoz
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the qualified name of the class that implements this transformation.
	 * 
	 * @return the qualified name of the class.
	 * 
	 * @author jmunoz
	 */
	public String getClass_() {
		return class_;
	}

	/**
	 * Gets the identifier of the kind of transformation that implements this
	 * transformation.
	 * 
	 * @return the transformation kind identifier
	 * 
	 * @author jmunoz
	 */
	public String getTrId() {
		return trId;
	}

	/**
	 * Gets the transformation priority.
	 * 
	 * @return the priority
	 * 
	 * @author jmunoz
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Gets the transformation package. The package is describe as an string.
	 * All the transformation returning the same string are packaged together.
	 * 
	 * @return the package
	 */
	public String getPackage() {
		return package_;
	}

	/**
	 * Sets the transformation package. The package is describe as an string.
	 * All the transformation returning the same string are packaged together.
	 * 
	 * @param package_
	 *            the new package
	 */
	public void setPackage(String package_) {
		this.package_ = package_;
	}

	/**
	 * Gets a list with the description of the {@link TransformedRes} that are
	 * used as input to the transformation.
	 * 
	 * @return the input {@link TransformedRes}
	 */
	public List<TransformedRes> getInputResources() {
		return inputResources;
	}

	/**
	 * Sets a list with the description of the {@link TransformedRes} that are
	 * used as input to the transformation.
	 * 
	 * @param inputResources
	 *            the input {@link TransformedRes}
	 */
	public void setInputResources(List<TransformedRes> inputResources) {
		this.inputResources = inputResources;
	}

	/**
	 * Gets a list with the description of the {@link TransformedRes} that
	 * produced as input by the transformation.
	 * 
	 * @return a list of output {@link TransformedRes}
	 */
	public List<TransformedRes> getOutputResources() {
		return outputResources;
	}

	/**
	 * Sets a list with the description of the {@link TransformedRes} that are
	 * produced as output bye the transformation.
	 * 
	 * @param outputResources
	 *            the list of output {@link TransformedRes}
	 */
	public void setOutputResources(List<TransformedRes> outputResources) {
		this.outputResources = outputResources;
	}

	/**
	 * Gets the {@link IConfigurationElement} element that was used to create
	 * this transformation description.
	 * 
	 * @return the {@link IConfigurationElement} element
	 */
	public IConfigurationElement getConfigElement() {
		return configElement;
	}

	/**
	 * Sets the {@link IConfigurationElement} element that was used to create
	 * this transformation description.
	 * 
	 * @param configElement
	 *            the new {@link IConfigurationElement} element
	 */
	public void setConfigElement(IConfigurationElement configElement) {
		this.configElement = configElement;
	}

	/**
	 * Gets the qualified name of a class that is used to initialize the
	 * configuration of this transformation.
	 * 
	 * @return the qualified name or null if the transformation is not
	 *         configurable
	 */
	public String getConfigInit() {
		return this.configInit;
	}

	/**
	 * Sets the qualified name of a class that is used to initialize the
	 * configuration of this transformation.
	 * 
	 * @param configInit
	 *            the qualified name
	 */
	public void setConfigInit(String configInit) {
		this.configInit = configInit;
	}

	/**
	 * Checks if is the transformation is configurable.
	 * 
	 * @return true, if is configurable
	 */
	public boolean isConfigurable() {
		return getConfigInit() != null;
	}

	/**
	 * Gets the extensions of the files that can be input of this
	 * transformation.
	 * 
	 * @return the file extensions
	 */
	public List<String> getFileExtensions() {
		return fileExtensions;
	}

	/**
	 * Sets the extensions of the files that can be input of this
	 * transformation.
	 * 
	 * @param fileExtensions
	 *            the new file extensions
	 */
	public void setFileExtensions(List<String> fileExtensions) {
		this.fileExtensions = fileExtensions;
	}

	/**
	 * Checks for default configuration selectable in trmanager.
	 * 
	 * @return true, if successful
	 */
	public boolean hasDefaultConfig() {
		return defaultConfig;
	}

	/**
	 * Sets the value of the default configuration.
	 * 
	 * @param defaultConfig
	 */
	public void setDefaultConfig(boolean defaultConfig) {
		this.defaultConfig = defaultConfig;
	}

	/**
	 * Check is is user available
	 */
	public boolean isUserAvailable() {
		return userAvailable;
	}

	/**
	 * Check is is user available
	 */
	public void setUserAvailable(boolean userAvailable) {
		this.userAvailable = userAvailable;
	}

}
