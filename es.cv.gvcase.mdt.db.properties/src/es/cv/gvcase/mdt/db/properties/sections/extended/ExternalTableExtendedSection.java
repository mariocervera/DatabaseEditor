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
package es.cv.gvcase.mdt.db.properties.sections.extended;

import es.cv.gvcase.mdt.common.sections.extended.AbstractExtendedBooleanPropertySection;
import es.cv.gvcase.mdt.common.util.ExtendedFeaturesIDs;

/**
 * A property section to edit the ExternalExtendedFeature in the DataBase
 * diagram.
 * 
 * @author <a href="mailto:fjcano@prodevelop.es">Francisco Javier Cano Muñoz</a>
 * 
 */
public class ExternalTableExtendedSection extends
		AbstractExtendedBooleanPropertySection {

	public ExternalTableExtendedSection() {
		super(ExtendedFeaturesIDs.ExternalExtendedFeatureID);
	}

	@Override
	protected String getLabelText() {
		return "External";
	}

}
