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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.datatools.modelbase.sql.datatypes.ApproximateNumericDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.BinaryStringDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.BooleanDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.CharacterStringDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.DataLinkDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.DataType;
import org.eclipse.datatools.modelbase.sql.datatypes.DateDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.FixedPrecisionDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.IntegerDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.IntervalDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.PrimitiveType;
import org.eclipse.datatools.modelbase.sql.datatypes.SQLDataTypesFactory;
import org.eclipse.datatools.modelbase.sql.datatypes.SQLDataTypesPackage;
import org.eclipse.datatools.modelbase.sql.datatypes.StructuredUserDefinedType;
import org.eclipse.datatools.modelbase.sql.datatypes.TimeDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.UserDefinedType;
import org.eclipse.datatools.modelbase.sql.datatypes.XMLDataType;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.datatools.modelbase.sql.schema.SQLSchemaPackage;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;

import es.cv.gvcase.mdt.common.composites.DetailComposite;
import es.cv.gvcase.mdt.common.composites.DetailsCompositeProviderRegistry;
import es.cv.gvcase.mdt.common.runnable.HookRunner;
import es.cv.gvcase.mdt.common.sections.AbstractDetailedChooserPropertySection;
import es.cv.gvcase.mdt.common.util.MDTUtil;
import es.cv.gvcase.mdt.db.properties.composites.ApproximateNumericDetailsComposite;
import es.cv.gvcase.mdt.db.properties.composites.BinaryStringDetailsComposite;
import es.cv.gvcase.mdt.db.properties.composites.CharacterStringDetailsComposite;
import es.cv.gvcase.mdt.db.properties.composites.DataLinkDetailsComposite;
import es.cv.gvcase.mdt.db.properties.composites.FixedPrecisionDetailsComposite;
import es.cv.gvcase.mdt.db.properties.composites.IntegerDetailsComposite;
import es.cv.gvcase.mdt.db.properties.composites.IntervalDetailsComposite;
import es.cv.gvcase.mdt.db.properties.composites.TimeDetailsComposite;
import es.cv.gvcase.mdt.db.properties.providers.DataTypeLabelProvider;

public class ColumnDataTypePropertySection extends
		AbstractDetailedChooserPropertySection {
	// The number of basis types it's 12, latter new types group can be added.
	final static int BASICTYPES = 12;
	private static int numTypes;
	private static Object[] datatypes = null;
	private boolean createdTypes = false;
	private boolean userDefined = false;

	@Override
	protected ILabelProvider getLabelProvider() {
		return new DataTypeLabelProvider(new EcoreItemProviderAdapterFactory());
	}

	@Override
	protected String getLabelText() {
		return "Data type:";
	}

	@Override
	protected String getDetailsLabelText() {
		return "Data type details";
	}

	// //
	// Hook points identifiers
	// //
	public static final String hookPointBefore = "es.cv.gvcase.mdt.db.properties.sections.ColumnDataTypePropertySection.HandleComboModified.Before";
	public static final String hookPointAfter = "es.cv.gvcase.mdt.db.properties.sections.ColumnDataTypePropertySection.HandleComboModified.After";

	@Override
	/**
	 * Check out if the type was created and 
	 * if it's an UserDefinedTyped needs attribute 
	 * ReferencedType to execute the creation operation
	 * else for primitiveTypes needs containedType
	 */
	protected EStructuralFeature getFeature() {
		if (/* createdTypes&& */userDefined)
			return SQLSchemaPackage.eINSTANCE.getTypedElement_ReferencedType();
		else
			return SQLSchemaPackage.eINSTANCE.getTypedElement_ContainedType();
	}

	@Override
	protected Object getFeatureValue() {

		SQLObject object = (SQLObject) getEObject();

		if (object instanceof Column) {
			if (((Column) object).getContainedType() != null)
				return ((Column) object).getContainedType();
			else
				return ((Column) object).getReferencedType();
		}

		return null;
	}

	@Override
	protected Object[] getComboFeatureValues() {
		EFactory factory = SQLDataTypesFactory.eINSTANCE;

		// get the UserDefinedTypes
		List<EObject> userTypes = new ArrayList<EObject>(
				((Column) getEObject()).getTable().getSchema()
						.getUserDefinedTypes());
		// remove those that should not be shown
		MDTUtil.filterHidableElements(userTypes);
		numTypes = BASICTYPES;
		if (userTypes.size() > 0) {
			numTypes += userTypes.size();
		}

		datatypes = new Object[numTypes];

		factory = SQLDataTypesFactory.eINSTANCE;

		datatypes[0] = null;
		datatypes[1] = factory.create(SQLDataTypesPackage.eINSTANCE
				.getApproximateNumericDataType());
		((ApproximateNumericDataType) datatypes[1])
				.setPrimitiveType(PrimitiveType.FLOAT_LITERAL);
		((ApproximateNumericDataType) datatypes[1]).setName("Float");
		datatypes[2] = factory.create(SQLDataTypesPackage.eINSTANCE
				.getBinaryStringDataType());
		((BinaryStringDataType) datatypes[2])
				.setPrimitiveType(PrimitiveType.BINARY_VARYING_LITERAL);
		((BinaryStringDataType) datatypes[2]).setName("Char");
		datatypes[3] = factory.create(SQLDataTypesPackage.eINSTANCE
				.getBooleanDataType());
		((BooleanDataType) datatypes[3])
				.setPrimitiveType(PrimitiveType.BOOLEAN_LITERAL);
		((BooleanDataType) datatypes[3]).setName("Boolean");
		datatypes[4] = factory.create(SQLDataTypesPackage.eINSTANCE
				.getCharacterStringDataType());
		((CharacterStringDataType) datatypes[4])
				.setPrimitiveType(PrimitiveType.CHARACTER_VARYING_LITERAL);
		((CharacterStringDataType) datatypes[4]).setName("Varchar");
		datatypes[5] = factory.create(SQLDataTypesPackage.eINSTANCE
				.getDataLinkDataType());
		((DataLinkDataType) datatypes[5])
				.setPrimitiveType(PrimitiveType.DATALINK_LITERAL);
		((DataLinkDataType) datatypes[5]).setName("DataLink");
		datatypes[6] = factory.create(SQLDataTypesPackage.eINSTANCE
				.getDateDataType());
		((DateDataType) datatypes[6])
				.setPrimitiveType(PrimitiveType.DATE_LITERAL);
		((DateDataType) datatypes[6]).setName("Date");
		datatypes[7] = factory.create(SQLDataTypesPackage.eINSTANCE
				.getFixedPrecisionDataType());
		((FixedPrecisionDataType) datatypes[7])
				.setPrimitiveType(PrimitiveType.FLOAT_LITERAL);
		((FixedPrecisionDataType) datatypes[7]).setName("Double");
		datatypes[8] = factory.create(SQLDataTypesPackage.eINSTANCE
				.getIntegerDataType());
		((IntegerDataType) datatypes[8])
				.setPrimitiveType(PrimitiveType.INTEGER_LITERAL);
		((IntegerDataType) datatypes[8]).setName("Integer");
		datatypes[9] = factory.create(SQLDataTypesPackage.eINSTANCE
				.getIntervalDataType());
		((IntervalDataType) datatypes[9])
				.setPrimitiveType(PrimitiveType.INTERVAL_LITERAL);
		((IntervalDataType) datatypes[9]).setName("Varchar");
		datatypes[10] = factory.create(SQLDataTypesPackage.eINSTANCE
				.getTimeDataType());
		((TimeDataType) datatypes[10])
				.setPrimitiveType(PrimitiveType.TIMESTAMP_LITERAL);
		((TimeDataType) datatypes[10]).setName("Time");
		datatypes[11] = factory.create(SQLDataTypesPackage.eINSTANCE
				.getXMLDataType());
		((XMLDataType) datatypes[11])
				.setPrimitiveType(PrimitiveType.XML_TYPE_LITERAL);
		((XMLDataType) datatypes[11]).setName("XML");
		int pos = 12;
		int index = 0;

		while (index < userTypes.size()) {
			StructuredUserDefinedType type = (StructuredUserDefinedType) userTypes
					.get(index);
			datatypes[pos] = type;// factory.create(SQLDataTypesPackage.eINSTANCE.getStructuredUserDefinedType());
			// ((StructuredUserDefinedType)datatypes[pos]).setContainer((TypedElement)
			// type);
			// ((StructuredUserDefinedType)datatypes[pos]).setName(type.getName());

			pos++;
			index++;
		}
		return datatypes;
	}

	@Override
	/********************************************************************
	 * This method creates a composite inside column data type properties
	 * depending on the type selected. 
	 ********************************************************************/
	protected DetailComposite getComposite(Composite parent) {

		DetailComposite composite = null;

		EObject objectSelected = getEObject();

		if (objectSelected instanceof Column) {
			DataType dt = ((Column) objectSelected).getDataType();
			if (dt != null) {
				if (dt instanceof IntegerDataType) {
					composite = new IntegerDetailsComposite(parent, parent
							.getStyle());
				} else if (dt instanceof FixedPrecisionDataType) {
					composite = new FixedPrecisionDetailsComposite(parent,
							parent.getStyle());
				} else if (dt instanceof ApproximateNumericDataType) {
					composite = new ApproximateNumericDetailsComposite(parent,
							parent.getStyle());
				} else if (dt instanceof BinaryStringDataType) {
					composite = new BinaryStringDetailsComposite(parent, parent
							.getStyle());
				} else if (dt instanceof CharacterStringDataType) {
					composite = new CharacterStringDetailsComposite(parent,
							parent.getStyle());
				} else if (dt instanceof TimeDataType) {
					composite = new TimeDetailsComposite(parent, parent
							.getStyle());
				} else if (dt instanceof DataLinkDataType) {
					composite = new DataLinkDetailsComposite(parent, parent
							.getStyle(), this);
				} else if (dt instanceof IntervalDataType) {
					composite = new IntervalDetailsComposite(parent, parent
							.getStyle(), this);
				} else if (dt instanceof UserDefinedType) {
					composite = DetailsCompositeProviderRegistry.getInstance()
							.getCompositeFor(dt, parent, parent.getStyle(),
									objectSelected, getEditingDomain());
				}
				if (composite != null) {
					composite.setElement(dt);
				}
			}
		}

		return composite;
	}

	@Override
	/**
	 * csanchez :: added a hook runner to perform actions after the column datatype
	 * has changed.
	 */
	protected void handleComboModified() {
		Object selection = getCSingleObjectChooser().getSelection();
		if (selection instanceof UserDefinedType)
			userDefined = true;
		else
			userDefined = false;
		// execute any possible hooks before the change of property
		HookRunner.getInstance().runRunnablesWithInfoForHook(hookPointBefore,
				getEObject());

		// perform the change of the property type
		super.handleComboModified();
		Object featureValue = getFeatureValue();

		if (!isRefreshing() && featureValue != selection) {
			EditingDomain editingDomain = getEditingDomain();
			setLengthValue(featureValue, selection, editingDomain);
			if (getEObjectList().size() == 1) {
				/* apply the property change to single selected object */
				editingDomain.getCommandStack().execute(
						SetCommand.create(editingDomain, getEObject(),
								getFeature(), selection));
			} else {
				CompoundCommand compoundCommand = new CompoundCommand();
				/* apply the property change to all selected elements */
				for (EObject nextObject : getEObjectList()) {
					compoundCommand.append(SetCommand.create(editingDomain,
							nextObject, getFeature(), selection));
				}
				editingDomain.getCommandStack().execute(compoundCommand);
			}
		}
		// execute any possible hooks after the change of property
		HookRunner.getInstance().runRunnablesWithInfoForHook(hookPointAfter,
				getEObject());
	}

	/*
	 * This method goal is to preserve the feature "length" from the oldDataType
	 * to the newDataType
	 */
	private void setLengthValue(Object oldDataType, Object newDataType,
			EditingDomain editingDomain) {

		if (!(oldDataType instanceof DataType && newDataType instanceof DataType))
			return;

		DataType oldD = (DataType) oldDataType;
		DataType newD = (DataType) newDataType;

		EStructuralFeature length1 = SQLDataTypesPackage.eINSTANCE
				.getBinaryStringDataType_Length();
		EStructuralFeature length2 = SQLDataTypesPackage.eINSTANCE
				.getCharacterStringDataType_Length();
		EStructuralFeature length3 = SQLDataTypesPackage.eINSTANCE
				.getDataLinkDataType_Length();

		if (containsFeature(oldD, length1)) {
			// eGet returns the length regardless of whether it is
			// BinaryStringDataType,
			// CharacterStringDataType or DataLinkDataType
			Integer l1 = (Integer) oldD.eGet(length1);
			if (l1 != null) {
				if (containsFeature(newD, length1)) {
					if (newDataType instanceof BinaryStringDataType) {
						editingDomain.getCommandStack().execute(
								SetCommand.create(editingDomain, newDataType,
										length1, l1));
					} else if (newDataType instanceof CharacterStringDataType) {
						editingDomain.getCommandStack().execute(
								SetCommand.create(editingDomain, newDataType,
										length2, l1));
					} else if (newDataType instanceof DataLinkDataType) {
						editingDomain.getCommandStack().execute(
								SetCommand.create(editingDomain, newDataType,
										length3, l1));
					}
				}
			}
		}
	}

	private boolean containsFeature(DataType datatype,
			EStructuralFeature feature) {

		if (datatype == null || feature == null)
			return false;

		if (feature.equals(SQLDataTypesPackage.eINSTANCE
				.getBinaryStringDataType_Length())
				&& (datatype instanceof BinaryStringDataType
						|| datatype instanceof CharacterStringDataType || datatype instanceof DataLinkDataType))
			return true;
		return false;
	}

	@Override
	public boolean shouldUseExtraSpace() {

		return false;
	}

}
