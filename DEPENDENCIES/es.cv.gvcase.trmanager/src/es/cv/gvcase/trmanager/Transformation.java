/*******************************************************************************
 * Copyright (c) 2008-2009 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Javier Muñoz (Integranova) – Initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Definition of what is considered a Transformation by the Transformation
 * Manager.
 */
public abstract class Transformation {

	/**
	 * The monitor used in the transformations. Available only during the
	 * transformation in order to update it with custom messages
	 */
	public static IProgressMonitor monitor = null;

	/**
	 * Transforms some input resources into some output resources.
	 * 
	 * @param inputs
	 *            The input resources that must receive the transformation. The
	 *            key in the hashmap are the parameter name while the value are
	 *            {@link TransformedResource}.
	 * @param outputs
	 *            The output resources that are produced by the transformation.
	 *            The key in the hashmap are the parameter name while the value
	 *            are {@link TransformedResource}.
	 * @param messageList
	 *            A message list which informs about events that happened while
	 *            transforming.
	 * 
	 * @return true, if successful
	 * 
	 * @author jmunoz
	 */
	public abstract boolean transform(
			HashMap<String, TransformedResource> inputs,
			HashMap<String, TransformedResource> outputs,
			List<String> messageList);

	/**
	 * Transforms some input resources into some output resources with some
	 * configuration.
	 * 
	 * @param inputs
	 *            The input resources that must receive the transformation. The
	 *            key in the hashmap are the parameter name while the value are
	 *            {@link TransformedResource}.
	 * @param outputs
	 *            The output resources that are produced by the transformation.
	 *            The key in the hashmap are the parameter name while the value
	 *            are {@link TransformedResource}.
	 * @param messageList
	 *            the message list
	 * @param configuration
	 *            a {@link TransformedResource} that holds the transformation
	 *            configuration.
	 * 
	 * @return true, if successful
	 * 
	 * @author jmunoz
	 */
	public boolean transform(HashMap<String, TransformedResource> inputs,
			HashMap<String, TransformedResource> outputs,
			List<String> messageList, TransformedResource configuration) {
		Transformation.monitor = null;
		boolean result = transform(inputs, outputs, messageList);
		Transformation.monitor = null;
		return result;
	}

	public boolean transform(HashMap<String, TransformedResource> inputs,
			HashMap<String, TransformedResource> outputs,
			List<String> messageList, IProgressMonitor monitor) {
		Transformation.monitor = monitor;
		boolean result = transform(inputs, outputs, messageList);
		Transformation.monitor = null;
		return result;
	}

	public boolean transform(HashMap<String, TransformedResource> inputs,
			HashMap<String, TransformedResource> outputs,
			List<String> messageList, IProgressMonitor monitor,
			TransformedResource configuration) {
		Transformation.monitor = monitor;
		boolean result = transform(inputs, outputs, messageList, monitor);
		Transformation.monitor = null;
		return result;
	}

	/**
	 * This method checks if input resources are valid for the transformation
	 * 
	 * @param inputs
	 *            The input {@link TransformedResource} that are under
	 *            validation.
	 * @param errorList
	 *            A message list with the errors that were detected.
	 * 
	 * @return true, if inputs are valid and false otherwise
	 * 
	 * @author jmunoz
	 */
	public abstract boolean inputsValid(
			HashMap<String, TransformedResource> inputs, List<String> errorList);

	/**
	 * Contains the external parameters that the TransformationRegistry or other
	 * external entity may have set for this transformation.
	 */
	protected Map<String, Object> transformationParameters = null;

	/**
	 * Retrieves the parameters that this transformation has set.
	 * 
	 * @return A {@link Map} of String to object parameter. Never null.
	 */
	public Map<String, Object> getTransformationParameters() {
		if (transformationParameters == null) {
			return Collections.emptyMap();
		}
		return transformationParameters;
	}

	/**
	 * Sets the parameters for this transformation to use.
	 * 
	 * @param transformationParameters
	 */
	public void setTransformationParameters(
			Map<String, Object> transformationParameters) {
		this.transformationParameters = transformationParameters;
	}

	/**
	 * Gets the specific transformation extension to compound the transformation
	 * configuration name. It should be override to specify a unique name
	 * extension.
	 * 
	 * @return the transformation name extension
	 */
	public String getTransformationExtension() {
		return "";
	}

	/**
	 * Indicates if the transformation should override the output model or not.
	 * If false, a synchronization will be done; if true, a transformation will
	 * be done
	 */
	protected boolean synchronize = false;

	/**
	 * Set if the transformation should override the output model. True will
	 * perform a transformation, and false will perform a synchronization
	 */
	public void setSynchronize(boolean override) {
		this.synchronize = override;
	}

}
