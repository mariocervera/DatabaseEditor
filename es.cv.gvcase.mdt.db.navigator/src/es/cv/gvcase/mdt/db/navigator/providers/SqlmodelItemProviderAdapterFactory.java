/***************************************************************************
* Copyright (c) 2007 Conselleria de Infraestructuras y Transporte,
* Generalitat de la Comunitat Valenciana . All rights reserved. This program
* and the accompanying materials are made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors: Mario Cervera Ubeda (Integranova) - Initial API and implementation
*
******************************************************************************/
package es.cv.gvcase.mdt.db.navigator.providers;

import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;

/**
 * A factory for creating SqlmodelItemProviderAdapter objects.
 */
public class SqlmodelItemProviderAdapterFactory extends
ResourceItemProviderAdapterFactory {
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory#isFactoryForType(java.lang.Object)
	 */
	@Override
	public boolean isFactoryForType(Object type) {
		return type instanceof Package || supportedTypes.contains(type);
	}
	
}
