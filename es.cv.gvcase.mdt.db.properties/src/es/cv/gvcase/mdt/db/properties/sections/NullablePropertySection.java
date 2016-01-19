/*******************************************************************************
 * Copyright (c) 2008 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Mario Cervera Ubeda (Prodevelop) - initial API and implementation
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.properties.sections;

import org.eclipse.datatools.modelbase.sql.tables.SQLTablesPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import es.cv.gvcase.mdt.common.sections.AbstractBooleanPropertySection;

public class NullablePropertySection extends AbstractBooleanPropertySection {

	@Override
	protected EStructuralFeature getFeature() {

		return SQLTablesPackage.eINSTANCE.getColumn_Nullable();
	}
	
	@Override
	protected String getLabelText() {
		return "Nullable";
	}

}
