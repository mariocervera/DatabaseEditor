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
package es.cv.gvcase.mdt.db.properties.filters;

import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.IFilter;

public class DescriptionFilter implements IFilter {

	public boolean select(Object toTest) {
		Object obj = toTest;
		if (obj instanceof EditPart) {
			obj = ((EditPart) toTest).getModel();
			View view = (View) obj;
			obj = view.getElement();
		}

		if (obj instanceof Table || obj instanceof Column) return true;
		else return false;
	}

}