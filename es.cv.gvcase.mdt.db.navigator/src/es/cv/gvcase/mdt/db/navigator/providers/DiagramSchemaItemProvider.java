/***************************************************************************
 * Copyright (c) 2008 Conselleria de Infraestructuras y Transporte,
 * Generalitat de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Francisco Javier Cano Muñoz (Prodevelop) – initial api implementation
 * Mario Cervera Ubeda (Integranova)
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.navigator.providers;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.provider.EObjectItemProvider;
import org.eclipse.gmf.runtime.notation.Diagram;

// TODO: Auto-generated Javadoc
/**
 * The Class DiagramPackageItemProvider.
 */
public class DiagramSchemaItemProvider extends EObjectItemProvider {

	/**
	 * Instantiates a new diagram package item provider.
	 * 
	 * @param adapterFactory the adapter factory
	 */
	public DiagramSchemaItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.provider.EObjectItemProvider#getText(java.lang.Object)
	 */
	public String getText(Object object) {
		if (object instanceof Diagram) {
			Diagram diagram = (Diagram) object;
			String name = diagram.getName();
			String label = "Sqlmodel";
			name = label != null ? ("[" + label + "] - " + name) : name; 
			return name;
		}
		return super.getText(object);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getChildren(java.lang.Object)
	 */
	public Collection<?> getChildren(Object object) {
		return Collections.EMPTY_LIST;
	}
	
}
