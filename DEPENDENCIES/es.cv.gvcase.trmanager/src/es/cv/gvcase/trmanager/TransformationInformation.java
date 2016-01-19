/*******************************************************************************
 * Copyright (c) 2007-2012 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *  Miguel Llacer (mllacer@prodevelop.es - Prodevelop S.L.) - Initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * The Class TransformationInformation contains all the information needed to
 * execute a transformation. It is used to execute previous and post operations
 * before and after launching the transformation.
 */
public class TransformationInformation {

	private String transformationId;

	private HashMap<String, TransformedResource> inputParams;

	private HashMap<String, TransformedResource> outputParams;

	private List<String> messagesList;

	private IProgressMonitor monitor;

	private TransformedResource configuration;

	private Map<String, Object> parameters;

	private boolean synchronize;

	public TransformationInformation(String transformationId,
			HashMap<String, TransformedResource> inputParams,
			HashMap<String, TransformedResource> outputParams,
			List<String> messagesList, IProgressMonitor monitor,
			TransformedResource configuration, Map<String, Object> parameters,
			boolean synchronize) {
		this.transformationId = transformationId;
		this.inputParams = inputParams;
		this.outputParams = outputParams;
		this.messagesList = messagesList;
		this.monitor = monitor;
		this.configuration = configuration;
		this.parameters = parameters;
		this.synchronize = synchronize;
	}

	public String getTransformationId() {
		return transformationId;
	}

	public HashMap<String, TransformedResource> getInputParams() {
		return inputParams;
	}

	public HashMap<String, TransformedResource> getOutputParams() {
		return outputParams;
	}

	public List<String> getMessagesList() {
		return messagesList;
	}

	public IProgressMonitor getMonitor() {
		return monitor;
	}

	public TransformedResource getConfiguration() {
		return configuration;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public boolean isSynchronize() {
		return synchronize;
	}

	public void setTransformationId(String transformationId) {
		this.transformationId = transformationId;
	}

	public void setInputParams(HashMap<String, TransformedResource> inputParams) {
		this.inputParams = inputParams;
	}

	public void setOutputParams(
			HashMap<String, TransformedResource> outputParams) {
		this.outputParams = outputParams;
	}

	public void setMessagesList(List<String> messagesList) {
		this.messagesList = messagesList;
	}

	public void setMonitor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}

	public void setConfiguration(TransformedResource configuration) {
		this.configuration = configuration;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public void setSynchronize(boolean synchronize) {
		this.synchronize = synchronize;
	}

}
