/*******************************************************************************
 * Copyright (c) 2007-2009 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Javier Muñoz (Integranova) – Initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager;

/**
 * A resource that is consumed or produced by a transformation. It is described
 * as a name and a path.
 * 
 * @author jmunoz
 */
public class TransformedResource {

	/** The name of the resource. */
	private String name;

	/** The path where the resource is located. */
	private String path;

	/**
	 * Instantiates a new transformed resource.
	 * 
	 * @param name
	 *            the name of the resource.
	 * @param path
	 *            the path where the resource is located.
	 * 
	 * @author jmunoz
	 */
	public TransformedResource(String name, String path) {
		this.name = name;
		this.path = path;
	}

	/**
	 * Gets the name of the resource.
	 * 
	 * @return the name of the resource.
	 * 
	 * @author jmunoz
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the path where the resource is located.
	 * 
	 * @return the path where the resource is located.
	 * 
	 * @author jmunoz
	 */
	public String getPath() {
		return path;
	}

}
