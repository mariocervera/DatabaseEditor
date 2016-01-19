/***************************************************************************
 * Copyright (c) 2007 Conselleria de Infraestructuras y Transporte,
 * Generalitat de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Mario Cervera Ubeda (Integranova)
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.diagram.providers;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.datatools.modelbase.sql.constraints.SQLConstraintsPackage;
import org.eclipse.datatools.modelbase.sql.schema.SQLSchemaPackage;
import org.eclipse.datatools.modelbase.sql.tables.SQLTablesPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

import es.cv.gvcase.mdt.common.provider.BaseModelTypeProvider;
import es.cv.gvcase.mdt.common.provider.ModelTypesProviderFactory;
import es.cv.gvcase.mdt.db.diagram.edit.parts.Column2EditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ColumnEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ForeignKeyEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.PersistentTable2EditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.PersistentTableEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.SchemaEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableViewedTables2EditPart;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelDiagramEditor;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelDiagramEditorPlugin;

/**
 * @generated
 */
public class SqlmodelElementTypes extends ElementInitializers {

	/**
	 * @generated
	 */
	private SqlmodelElementTypes() {
	}

	/**
	 * @generated
	 */
	private static Map elements;

	/**
	 * @generated
	 */
	private static ImageRegistry imageRegistry;

	/**
	 * @generated
	 */
	private static Set KNOWN_ELEMENT_TYPES;

	/**
	 * @generated
	 */
	public static final IElementType Schema_1000 = getElementType("es.cv.gvcase.mdt.db.diagram.Schema_1000"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType PersistentTable_1001 = getElementType("es.cv.gvcase.mdt.db.diagram.PersistentTable_1001"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType ViewTable_1002 = getElementType("es.cv.gvcase.mdt.db.diagram.ViewTable_1002"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType Column_2001 = getElementType("es.cv.gvcase.mdt.db.diagram.Column_2001"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType Column_2002 = getElementType("es.cv.gvcase.mdt.db.diagram.Column_2002"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType PersistentTable_2003 = getElementType("es.cv.gvcase.mdt.db.diagram.PersistentTable_2003"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType ForeignKey_3001 = getElementType("es.cv.gvcase.mdt.db.diagram.ForeignKey_3001"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType ViewTableViewedTables_3002 = getElementType("es.cv.gvcase.mdt.db.diagram.ViewTableViewedTables_3002"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	private static ImageRegistry getImageRegistry() {
		if (imageRegistry == null) {
			imageRegistry = new ImageRegistry();
		}
		return imageRegistry;
	}

	/**
	 * @generated
	 */
	private static String getImageRegistryKey(ENamedElement element) {
		return element.getName();
	}

	/**
	 * @generated
	 */
	private static ImageDescriptor getProvidedImageDescriptor(
			ENamedElement element) {
		if (element instanceof EStructuralFeature) {
			EStructuralFeature feature = ((EStructuralFeature) element);
			EClass eContainingClass = feature.getEContainingClass();
			EClassifier eType = feature.getEType();
			if (eContainingClass != null && !eContainingClass.isAbstract()) {
				element = eContainingClass;
			} else if (eType instanceof EClass
					&& !((EClass) eType).isAbstract()) {
				element = eType;
			}
		}
		if (element instanceof EClass) {
			EClass eClass = (EClass) element;
			if (!eClass.isAbstract()) {
				return SqlmodelDiagramEditorPlugin.getInstance()
						.getItemImageDescriptor(
								eClass.getEPackage().getEFactoryInstance()
										.create(eClass));
			}
		}
		// TODO : support structural features
		return null;
	}

	/**
	 * @generated
	 */
	public static ImageDescriptor getImageDescriptor(ENamedElement element) {
		String key = getImageRegistryKey(element);
		ImageDescriptor imageDescriptor = getImageRegistry().getDescriptor(key);
		if (imageDescriptor == null) {
			imageDescriptor = getProvidedImageDescriptor(element);
			if (imageDescriptor == null) {
				imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
			}
			getImageRegistry().put(key, imageDescriptor);
		}
		return imageDescriptor;
	}

	/**
	 * @generated
	 */
	public static Image getImage(ENamedElement element) {
		String key = getImageRegistryKey(element);
		Image image = getImageRegistry().get(key);
		if (image == null) {
			ImageDescriptor imageDescriptor = getProvidedImageDescriptor(element);
			if (imageDescriptor == null) {
				imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
			}
			getImageRegistry().put(key, imageDescriptor);
			image = getImageRegistry().get(key);
		}
		return image;
	}

	/**
	 * @generated
	 */
	public static ImageDescriptor getImageDescriptor(IAdaptable hint) {
		ENamedElement element = getElement(hint);
		if (element == null) {
			return null;
		}
		return getImageDescriptor(element);
	}

	/**
	 * @generated
	 */
	public static Image getImage(IAdaptable hint) {
		ENamedElement element = getElement(hint);
		if (element == null) {
			return null;
		}
		return getImage(element);
	}

	/**
	 * Returns 'type' of the ecore object associated with the hint.
	 * 
	 * @generated
	 */
	public static ENamedElement getElement(IAdaptable hint) {
		Object type = hint.getAdapter(IElementType.class);
		if (elements == null) {
			elements = new IdentityHashMap();

			elements.put(Schema_1000, SQLSchemaPackage.eINSTANCE.getSchema());

			elements.put(PersistentTable_1001, SQLTablesPackage.eINSTANCE
					.getPersistentTable());

			elements.put(ViewTable_1002, SQLTablesPackage.eINSTANCE
					.getViewTable());

			elements.put(Column_2001, SQLTablesPackage.eINSTANCE.getColumn());

			elements.put(Column_2002, SQLTablesPackage.eINSTANCE.getColumn());

			elements.put(PersistentTable_2003, SQLTablesPackage.eINSTANCE
					.getPersistentTable());

			elements.put(ForeignKey_3001, SQLConstraintsPackage.eINSTANCE
					.getForeignKey());

			elements.put(ViewTableViewedTables_3002, SQLTablesPackage.eINSTANCE
					.getViewTable_ViewedTables());
		}
		return (ENamedElement) elements.get(type);
	}

	/**
	 * @generated
	 */
	private static IElementType getElementType(String id) {
		return ElementTypeRegistry.getInstance().getType(id);
	}

	/**
	 * @generated
	 */
	public static boolean isKnownElementType(IElementType elementType) {
		if (KNOWN_ELEMENT_TYPES == null) {
			KNOWN_ELEMENT_TYPES = new HashSet();
			KNOWN_ELEMENT_TYPES.add(Schema_1000);
			KNOWN_ELEMENT_TYPES.add(PersistentTable_1001);
			KNOWN_ELEMENT_TYPES.add(ViewTable_1002);
			KNOWN_ELEMENT_TYPES.add(Column_2001);
			KNOWN_ELEMENT_TYPES.add(Column_2002);
			KNOWN_ELEMENT_TYPES.add(PersistentTable_2003);
			KNOWN_ELEMENT_TYPES.add(ForeignKey_3001);
			KNOWN_ELEMENT_TYPES.add(ViewTableViewedTables_3002);
		}
		return KNOWN_ELEMENT_TYPES.contains(elementType);
	}

	/**
	 * @generated
	 */
	public static IElementType getElementType(int visualID) {
		switch (visualID) {
		case SchemaEditPart.VISUAL_ID:
			return Schema_1000;
		case PersistentTableEditPart.VISUAL_ID:
			return PersistentTable_1001;
		case ViewTableEditPart.VISUAL_ID:
			return ViewTable_1002;
		case ColumnEditPart.VISUAL_ID:
			return Column_2001;
		case Column2EditPart.VISUAL_ID:
			return Column_2002;
		case PersistentTable2EditPart.VISUAL_ID:
			return PersistentTable_2003;
		case ForeignKeyEditPart.VISUAL_ID:
			return ForeignKey_3001;
		case ViewTableViewedTables2EditPart.VISUAL_ID:
			return ViewTableViewedTables_3002;
		}
		return null;
	}

	/**
	 * @generated
	 */
	static {
		ModelTypesProviderFactory.registerModelTypesProvider(
				SqlmodelDiagramEditor.ID, new BaseModelTypeProvider() {
					@Override
					public Image getImageHelper(IAdaptable hint) {
						return getImage(hint);
					}
				});
	}

}
