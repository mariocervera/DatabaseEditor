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
package es.cv.gvcase.mdt.db.properties.providers;

import org.eclipse.datatools.modelbase.sql.datatypes.DataType;
import org.eclipse.datatools.modelbase.sql.datatypes.StructuredUserDefinedType;
import org.eclipse.datatools.modelbase.sql.schema.provider.SqlmodelEditPlugin;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecoretools.tabbedproperties.providers.TabbedPropertiesLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;

public class DataTypeLabelProvider extends TabbedPropertiesLabelProvider {

	public DataTypeLabelProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	public String getText(Object element) {

		if (element instanceof DataType) {
			DataType dt = (DataType) element;
			if (element instanceof StructuredUserDefinedType) {
				return dt.getName();
			}
			String typeName = dt.getClass().getSimpleName();
			if (typeName.endsWith("DataType")) {
				return typeName.replace("DataType", "");
			} else if (typeName.endsWith("DataTypeImpl")) {
				return typeName.replace("DataTypeImpl", "");
			}
		}

		return "";
	}

	@Override
	public Image getImage(Object object) {

		if (object instanceof DataType) {
			return ExtendedImageRegistry.getInstance()
					.getImage(
							SqlmodelEditPlugin.INSTANCE
									.getImage("full/obj16/DataType"));
		} else
			return super.getImage(object);

	}

}
