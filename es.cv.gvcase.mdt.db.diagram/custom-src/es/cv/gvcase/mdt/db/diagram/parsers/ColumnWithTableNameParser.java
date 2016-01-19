/*******************************************************************************
 * Copyright (c) 2007 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Francisco Javier Cano Mu�oz (Prodevelop) - Initial API 
 * implementation.
 *
 ******************************************************************************/

package es.cv.gvcase.mdt.db.diagram.parsers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.emf.ecore.EStructuralFeature;

import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelStructuralFeatureParser;

/**
 * Adds table name before column name in printing string.
 * 
 * @author <a href="mailto:fjcano@prodeelop.es">Francisco Javier Cano Muñoz</a>
 */
public class ColumnWithTableNameParser extends SqlmodelStructuralFeatureParser {

	/**
	 * Instantiates a new column with table name parser.
	 * 
	 * @param feature the feature
	 */
	public ColumnWithTableNameParser(EStructuralFeature feature) {
		super(feature);
	}
	
	/* (non-Javadoc)
	 * @see es.cv.gvcase.mdt.db.diagram.providers.SqlmodelAbstractParser#getPrintString(org.eclipse.core.runtime.IAdaptable, int)
	 */
	@Override
	public String getPrintString(IAdaptable adapter, int flags) {
		String printingString = super.getPrintString(adapter, flags);
		
		Column column = (Column) adapter.getAdapter(Column.class);
		if (column != null) {
			if (column.getTable() != null) {
				printingString = column.getTable().getName() + "::" + printingString;
			}
		}
		
		return printingString;
	}
	
}
