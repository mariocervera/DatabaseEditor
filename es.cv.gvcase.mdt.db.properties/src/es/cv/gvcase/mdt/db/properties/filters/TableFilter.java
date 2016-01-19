/*******************************************************************************
 * Copyright (c) 2010 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Francisco Javier Cano Muñoz (Prodevelop) - initial API and implementation
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.properties.filters;

import org.eclipse.datatools.modelbase.sql.tables.Table;

import es.cv.gvcase.mdt.common.properties.filters.AbstractGMFFilter;

/**
 * A filter for all {@link Table}s.
 * 
 * @author <a href="mailto:fjcano@prodevelop.es">Francisco Javier Cano Muñoz</a>
 *
 */
public class TableFilter extends AbstractGMFFilter {

	public TableFilter() {
		super(new Class[] { Table.class });
	}

}
