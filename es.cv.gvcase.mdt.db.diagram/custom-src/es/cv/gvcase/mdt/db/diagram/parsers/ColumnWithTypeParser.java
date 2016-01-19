/*******************************************************************************
 * Copyright (c) 2007 - 2011 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Francisco Javier Cano Mu�oz (Prodevelop) - Initial API and implementation.
 * Mario Cervera Ubeda - Additional functionality
 *
 ******************************************************************************/

package es.cv.gvcase.mdt.db.diagram.parsers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.datatools.modelbase.sql.datatypes.CharacterStringDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.DataType;
import org.eclipse.datatools.modelbase.sql.datatypes.SQLDataTypesPackage;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelStructuralFeatureParser;

/**
 * The Class ColumnWithTypeAndConstrainsParser. This parser shows the name of
 * the {@link Column} followed by its data type.
 */
public class ColumnWithTypeParser extends SqlmodelStructuralFeatureParser {

	/** Map types. */
	static Map<String, EClass> mapNamesDataTypes = null;

	/** The map data types names. */
	static Map<EClass, String> mapDataTypesNames = null;
	static {

		EList<EObject> list = SQLDataTypesPackage.eINSTANCE.eContents();

		mapNamesDataTypes = new HashMap<String, EClass>();
		mapNamesDataTypes.put("", null);

		mapDataTypesNames = new HashMap<EClass, String>();

		for (EObject eob : list) {
			EClass aux = null;
			if (eob instanceof EClass) {
				aux = (EClass) eob;
				if (aux.getEAllSuperTypes().contains(
						SQLDataTypesPackage.eINSTANCE.getPredefinedDataType()))
					if (!aux.isAbstract()) {
						String name = aux.getName();
						mapNamesDataTypes.put(name, aux);
						mapDataTypesNames.put(aux, name);
					}
			}
		}
	}

	/**
	 * Instantiates a new column with type and constrains parser.
	 * 
	 * @param feature
	 *            the feature
	 */
	public ColumnWithTypeParser(EStructuralFeature feature) {
		super(feature);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.cv.gvcase.mdt.db.diagram.providers.SqlmodelAbstractParser#getPrintString
	 * (org.eclipse.core.runtime.IAdaptable, int)
	 */
	@Override
	public String getPrintString(IAdaptable adapter, int flags) {

		Column column = (Column) adapter.getAdapter(Column.class);
		if (column == null) {
			return super.getPrintString(adapter, flags);
		}
		String name = getColumnName(column);
		String type = getColumnTypeName(column);
		String printingString = "";

		printingString += name;
		printingString += (type.length() <= 0 ? ("") : (" : " + type));
		// get possible extra data and append it to the label to show
		String extraData = getColumnExtraData(column);
		if (extraData != null) {
			printingString += extraData;
		}

		return printingString;
	}

	/**
	 * Gets the column name.
	 * 
	 * @param column
	 *            the column
	 * 
	 * @return the column name
	 */
	private String getColumnName(Column column) {
		if (column != null) {
			String columnName = column.getName();
			return columnName != null ? columnName : "";
		}
		return "";
	}

	/**
	 * Gets the column type name.
	 * 
	 * @param column
	 *            the column
	 * 
	 * @return the column type name
	 */
	public String getColumnTypeName(Column column) {
		if (column != null) {
			DataType dataType = column.getDataType();
			if (dataType != null) {
				for (EClass dt : mapDataTypesNames.keySet()) {
					if (dt.getInstanceClass().isInstance(dataType)) {
						String s = mapDataTypesNames.get(dt);
						if (s.endsWith("DataType")) {
							return s.replace("DataType", "");
						}
						return s;
					}
				}
			}
		}
		return "";
	}

	/**
	 * Gets a String of extra data to be shown with the column name and
	 * datatype.
	 * 
	 * @param column
	 * @return
	 */
	protected String getColumnExtraData(Column column) {
		if (column == null) {
			return null;
		}
		String extraData = null;
		DataType columnDataType = column.getDataType();
		if (columnDataType != null) {
			// Get the length of a column with CharacterDataType
			if (columnDataType instanceof CharacterStringDataType) {
				CharacterStringDataType columnCharacterDataType = (CharacterStringDataType) columnDataType;
				int lenght = columnCharacterDataType.getLength();
				extraData = " [" + lenght + "]";
			}
		}
		return extraData;
	}

}
