/*******************************************************************************
 * Copyright (c) 2010 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Carlos Sánchez Periñán (Prodevelop) - Initial API implementation 
 * Francisco Javier Cano Muñoz (Prodevelop) - factory adaptation
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.diagram.part;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.datatools.modelbase.sql.datatypes.AttributeDefinition;
import org.eclipse.datatools.modelbase.sql.datatypes.DataType;
import org.eclipse.datatools.modelbase.sql.datatypes.SQLDataTypesFactory;
import org.eclipse.datatools.modelbase.sql.datatypes.StructuredUserDefinedType;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.SQLSchemaFactory;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import es.cv.gvcase.mdt.common.model.AvailableValue;
import es.cv.gvcase.mdt.common.model.ITypesFactory;
import es.cv.gvcase.mdt.common.model.Property;
import es.cv.gvcase.mdt.common.model.Type;
import es.cv.gvcase.mdt.common.model.TypesGroup;

/**
 * {@link ITypesFactory} for SQL model.
 * 
 * @author <a href="mailto:csanchez@prodevelop.es">Carlos Sánchez Periñán</a>
 * @author <a href="mailto:fjcano@prodevelop.es">Francisco Javier Cano Muñoz</a>
 * 
 */
public class SQLAbstractTypesFactory implements ITypesFactory {
	

	public EObject createType(Type type) {
		return createTypeInModel(type, null);
	}

	public Collection<EObject> createTypeGroup(TypesGroup group) {
		return createTypeGroupInModel(group, null);
	}

	public Collection<EObject> createTypeGroupInModel(TypesGroup group,
			EObject model) {
		if (group == null || model == null || group.Type == null
				|| group.Type.isEmpty()) {
			return Collections.emptyList();
		}
		Schema schema = getSchemaFromModelRoot(model);
		Collection<EObject> createdTypes = new ArrayList<EObject>();
		for (Object o : group.Type) {
			Type type = (Type) o;
			EObject createdType = createTypeInModel(type, schema);
			if (createdType != null) {
				createdTypes.add(createdType);
			}
		}
		return createdTypes;
	}

	public EObject createTypeInModel(Type type, EObject model) {
		Schema schema = getSchemaFromModelRoot(model);
		return createSQLStructuredType(type, schema);
	}

	/**
	 * Create new StructuredUserDefinedType from one define type from a group
	 * 
	 * @param type
	 * @return the new User Type
	 */
	private static StructuredUserDefinedType createSQLStructuredType(Type type,
			Schema schema) {
		StructuredUserDefinedType datatype = SQLDataTypesFactory.eINSTANCE
				.createStructuredUserDefinedType();
		datatype.setName(type.name);
		datatype.setSchema(schema);
		List<AttributeDefinition> listAttributes = datatype.getAttributes();
		if (type.Property != null) {
			Iterator<Object> it = type.Property.iterator();
			while (it.hasNext()) {
				AttributeDefinition attribute = createAttribute((Property) it
						.next(), schema);
				listAttributes.add(attribute);
			}
		}
		return datatype;
	}

	/**
	 * create attribute from type which can contain type properties and
	 * enumerations
	 * 
	 * @param prop
	 * @return attribute from type
	 */
	private static AttributeDefinition createAttribute(Property prop,
			Schema schema) {
		AttributeDefinition attribute = SQLDataTypesFactory.eINSTANCE
				.createAttributeDefinition();
		attribute.setName(prop.name);
		if (prop.AvailableValue != null) {
			StructuredUserDefinedType subdatatype = SQLDataTypesFactory.eINSTANCE
					.createStructuredUserDefinedType();
			subdatatype.setName(prop.name);
			subdatatype.setSchema(schema);
			EList<AttributeDefinition> listSubattributes = subdatatype
					.getAttributes();
			Iterator<Object> it = prop.AvailableValue.iterator();
			while (it.hasNext()) {
				AvailableValue value = (AvailableValue) it.next();
				AttributeDefinition subattribute = SQLDataTypesFactory.eINSTANCE
						.createAttributeDefinition();
				subattribute.setDataType(getSQLType(prop.type));
				subattribute.setName(value.value);
				subattribute.setDefaultValue(value.value);
				listSubattributes.add(subattribute);
			}
			attribute.setDataType(subdatatype);
		} else {
			attribute.setDataType(getSQLType(prop.type));
		}
		return attribute;
	}

	/**
	 * This method match string with SQL primitive types
	 * 
	 * @param type
	 * @return the sql primitive type
	 */
	private static DataType getSQLType(String type) {
		if (type.compareToIgnoreCase(Property.TYPE_STRING) == 0)
			return SQLDataTypesFactory.eINSTANCE
					.createCharacterStringDataType();
		else if (type.compareToIgnoreCase(Property.TYPE_BOOLEAN) == 0)
			return SQLDataTypesFactory.eINSTANCE.createBooleanDataType();
		else if (type.compareToIgnoreCase(Property.TYPE_DATE) == 0)
			return SQLDataTypesFactory.eINSTANCE.createDateDataType();
		else if (type.compareToIgnoreCase(Property.TYPE_DOUBLE) == 0)
			return SQLDataTypesFactory.eINSTANCE.createFixedPrecisionDataType();
		else if (type.compareToIgnoreCase(Property.TYPE_INTEGER) == 0)
			return SQLDataTypesFactory.eINSTANCE.createIntegerDataType();
		return null;
	}

	/**
	 * Gets the schema to be create the types in.
	 * 
	 * @param modelRoot
	 * @return
	 */
	protected Schema getSchemaFromModelRoot(EObject modelRoot) {
		if (modelRoot instanceof Schema) {
			return (Schema) modelRoot;
		} else if (modelRoot instanceof Database) {
			Database database = (Database) modelRoot;
			if (database.getSchemas() != null
					&& !database.getSchemas().isEmpty()) {
				return database.getSchemas().get(0);
			}
		}
		return SQLSchemaFactory.eINSTANCE.createSchema();
	}

}
