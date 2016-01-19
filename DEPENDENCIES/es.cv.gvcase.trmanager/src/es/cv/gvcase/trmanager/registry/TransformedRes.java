/*******************************************************************************
 * Copyright (c) 2007-2009 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Javier Muñoz (Integranova) - Initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager.registry;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * A description of a resource that is consumed or produced by a transformation
 * 
 * @author jmunoz
 */
public class TransformedRes {

	/** The name of the resource. */
	private String name;

	/**
	 * The type of the resource. It must be one of MODEL, FOLDER, PROJECT, FILE
	 * or MODEL_ELEMENT.
	 */
	private String type;

	/** Used if type is PROJECT. The project nature. */
	private String projectNature;

	/** Used if type is MODEL or MODEL_ELEMENT. The metamodel URI. */
	private String metamodelURI;

	private boolean updatable;

	/** Constant to specify that the TransformedRes type is MODEL. */
	public static final String MODEL = "MODEL";

	/** Constant to specify that the TransformedRes type is MODEL_ELEMENT. */
	public static final String MODEL_ELEMENT = "MODEL_ELEMENT";

	/** Constant to specify that the TransformedRes type is FOLDER. */
	public static final String FOLDER = "FOLDER";

	/** Constant to specify that the TransformedRes type is PROJECT. */
	public static final String PROJECT = "PROJECT";

	/** Constant to specify that the TransformedRes type is FILE. */
	public static final String FILE = "FILE";

	/**
	 * Instantiates a new description of a transformed resource.
	 * 
	 * @param name
	 *            the name of the resource.
	 * @param type
	 *            the type of the resource.
	 * @author jmunoz
	 */
	public TransformedRes(String name, String type) {
		this.name = name;
		this.type = type;
		this.projectNature = null;
		this.metamodelURI = null;
		this.updatable = false;
	}

	/**
	 * When the the resource type is PROJECT, gets the project nature. Undefined
	 * in other case.
	 * 
	 * @return the project nature
	 * @author jmunoz
	 */
	public String getProjectNature() {
		return projectNature;
	}

	/**
	 * When the the resource type is PROJECT, sets the project nature.
	 * 
	 * @param projectNature
	 *            the new project nature
	 * @author jmunoz
	 */
	public void setProjectNature(String projectNature) {
		this.projectNature = projectNature;
	}

	/**
	 * When the type is MODEL or MODEL_ELEMENT, gets the metamodel URI.
	 * 
	 * @return the metamodel URI
	 * @author jmunoz
	 */
	public String getMetamodelURI() {
		return metamodelURI;
	}

	/**
	 * When the type is MODEL or MODEL_ELEMENT, sets the metamodel URI.
	 * 
	 * @param metamodelURI
	 *            the new metamodel URI
	 * @author jmunoz
	 */
	public void setMetamodelURI(String metamodelURI) {
		this.metamodelURI = metamodelURI;
	}

	/**
	 * Gets the resource name.
	 * 
	 * @return the resource name
	 * @author jmunoz
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the transformation type. It is one of {@link TransformedRes#FILE},
	 * {@link TransformedRes#FOLDER}, {@link TransformedRes#MODEL},
	 * {@link TransformedRes#MODEL_ELEMENT} or {@link TransformedRes#PROJECT}
	 * 
	 * @return the transformation type
	 * @author jmunoz
	 */
	public String getType() {
		return type;
	}

	public boolean isUpdatable() {
		return this.updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	private List<String> fileExtensions = null;

	public boolean resourceInputIsValid(String resourcePath,
			List<String> fileExtension) {
		this.fileExtensions = fileExtension;
		return this.resourceIsValid(resourcePath);
	}

	public boolean resourceIsValid(String resourcePath) {
		return resourceIsValid(resourcePath, new ArrayList<String>());
	}

	/**
	 * Checks if the resource located in <b>resourcePath</b> is valid for this
	 * resource description. It must exist. If the resource describes a
	 * {@link TransformedRes#MODEL}, {@link TransformedRes#MODEL_ELEMENT}, the
	 * resource must comply with the specified metamodel URI. If the resource
	 * describes a {@link TransformedRes#PROJECT} it must be of the nature
	 * specified.
	 * 
	 * @param resourcePath
	 * @return true if the resource valid or false in other case
	 * @author jmunoz
	 */
	public boolean resourceIsValid(String resourcePath,
			Collection<String> errorMessages) {

		// TODO: Return feedback in a messages list

		if (this.getType().equals(FILE)) {
			try {
				FileLocator.toFileURL(new URL(resourcePath)).getFile();
			} catch (MalformedURLException e) {
				errorMessages.add("Unable to parse the file path");
				return false;
			} catch (IOException e) {
				errorMessages.add("Unable to read the file");
				return false;
			}

		} else if (this.getType().equals(MODEL)) {
			if (fileExtensions == null) {
				return true;
			}

			for (String ext : fileExtensions) {
				if (resourcePath.endsWith(ext)) {
					return true;
				}
			}

			if (conformsToMetamodelURI(resourcePath)) {
				return true;
			}

			errorMessages
					.add("Model do not conforms to the required metamodel: "
							+ this.getMetamodelURI());
			return false;
		} else if (this.getType().equals(FOLDER)) {

			IPath folderPath = new Path(resourcePath);
			IFolder folder = null;

			try {
				folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(
						folderPath);
			} catch (IllegalArgumentException e) {
				// folderPath 'file:/C:/' is not supported because it throws an
				// IllegalArgumentException.
				//
				// When trManager try to read the previous parameters with this
				// folderPath, it throws an IllegalArgumentExcpetion and
				// trManager is not opened.
				errorMessages.add(resourcePath + " is not a valid folder");
				return false;
			}

			if (folder == null) {
				errorMessages.add("Unable to read the folder");
				return false;
			}

		} else if (this.getType().equals(PROJECT)) {

			String nature = this.getProjectNature();

			String trResourcePath = resourcePath;
			if (trResourcePath.contains("platform:/resource"))
				trResourcePath = trResourcePath.replace("platform:/resource",
						"");

			IProject project = null;
			try {
				project = ResourcesPlugin.getWorkspace().getRoot().getProject(
						trResourcePath);
			} catch (IllegalArgumentException e) {
			}

			if (project == null) {
				errorMessages.add("Unable to read the project");
				return false;
			}

			try {

				if (project.getNature(nature) == null) {
					errorMessages.add("Project is not of " + nature + "nature");
				}
				return true;
			} catch (CoreException e) {
				errorMessages.add("Unable to read the project");
				return false;
			}

		}

		return true;
	}

	/**
	 * Checks if resourcePath conforms to metamodel URI.
	 * 
	 * @param resourcePath
	 *            the resource path
	 * 
	 * @return true, if successful
	 */
	protected boolean conformsToMetamodelURI(String resourcePath) {
		try {
			URI uri = URI.createURI(resourcePath);
			Resource resource = getResourceSet().getResource(uri, true);
			if (resource.getContents().get(0).eClass().getEPackage().getNsURI()
					.equals(this.getMetamodelURI())) {
				return true;
			}
		} catch (Exception e) {
			// Nothing to do
		}

		return false;
	}

	private ResourceSet resourceSet;

	protected ResourceSet getResourceSet() {
		if (resourceSet == null) {
			resourceSet = new ResourceSetImpl();
		}
		return resourceSet;
	}
}
