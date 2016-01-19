/*******************************************************************************
 * Copyright (c) 2011 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 			Francisco Javier Cano Muñoz (Prodevelop) - factory adaptation
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.diagram.providers;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;

import es.cv.gvcase.mdt.common.Activator;

/**
 * Provides validation operations for the SQL MOSKtit diagram editor.
 * 
 * @author <a href="mailto:fjcano@prodevelop.es">Francisco Javier Cano Muñoz</a>
 * 
 */
public class ValidationProvider {

	/**
	 * Checks that the given element has a valid name for the SQL MOSKitt
	 * diagram editor.
	 * 
	 * @param element
	 * @return
	 */
	public static IStatus validateUniqueName(ENamedElement element) {
		IStatus status = null;
		if (element != null) {
			boolean unique = checkUniqueName(element);
			if (!unique) {
				status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						"An element with name \"" + element.getName()
								+ "\" already exists.");
			} else {
				status = new Status(IStatus.OK, Activator.PLUGIN_ID,
						"Unique name.");
			}
		} else {
			status = new Status(IStatus.WARNING, Activator.PLUGIN_ID,
					"Element to validate is null");
		}
		return status;
	}

	/**
	 * Checks that the name of the given element is unique in its container.
	 * 
	 * @param namedElement
	 * @return
	 */
	public static boolean checkUniqueName(ENamedElement namedElement) {
		if (namedElement == null) {
			return false;
		}
		String namedElementName = namedElement.getName();
		if (namedElementName == null) {
			return true;
		}
		if (namedElement.eContainer() != null) {
			for (EObject eObject : namedElement.eContainer().eContents()) {
				if (eObject != null && eObject != namedElement
						&& eObject instanceof ENamedElement
						&& areRelatives(eObject, namedElement)) {
					String name = ((ENamedElement) eObject).getName();
					if (namedElementName.equals(name)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * This operation returns true if eObj1 and eObj2 are relatives. This will
	 * happen if eObj2 is child of eObj1.Class or if eObj2.Class is superType of
	 * eObj1.Class. Parameters cannot be null
	 * 
	 * @author gmerin
	 * @param eObj1
	 * @param eObj2
	 * @return
	 */
	public static boolean areRelatives(EObject eObj1, EObject eObj2) {
		return areRelatives(eObj1.eClass(), eObj2);
	}

	/**
	 * This operation returns true if eObj1 and eObj2 are relatives. This will
	 * happen if eObj2 is child of eObj1.Class or if eObj2.Class is superType of
	 * eObj1.Class. Parameters cannot be null
	 * 
	 * @author gmerin
	 * @param eObj1Class
	 * @param eObj2
	 * @return
	 */
	public static boolean areRelatives(EClass eObj1Class, EObject eObj2) {
		// is eObj2 InstanceOf eObj1.Class ?
		if (eObj1Class.isInstance(eObj2))
			return true;

		// is eObj2.Class SuperTypeOf eObj1.Class ?
		if (eObj2.eClass().isSuperTypeOf(eObj1Class))
			return true;

		// If they are not related in any way
		return false;
	}

}
