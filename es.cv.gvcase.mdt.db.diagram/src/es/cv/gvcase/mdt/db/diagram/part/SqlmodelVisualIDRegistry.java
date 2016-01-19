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
package es.cv.gvcase.mdt.db.diagram.part;

import org.eclipse.core.runtime.Platform;
import org.eclipse.datatools.modelbase.sql.constraints.SQLConstraintsPackage;
import org.eclipse.datatools.modelbase.sql.schema.SQLSchemaPackage;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.tables.SQLTablesPackage;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

import es.cv.gvcase.mdt.common.provider.ViewInfo;
import es.cv.gvcase.mdt.common.provider.ViewInfoRegistry;
import es.cv.gvcase.mdt.db.diagram.edit.parts.Column2EditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ColumnEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ForeignKeyEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ForeignKeyNameEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.PersistentTable2EditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.PersistentTableEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.PersistentTableNameEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.PersistentTablePersistentColumnTableCompartmentEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.SchemaEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableNameEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableViewedColumnsEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableViewedTablesEditPart;

/**
 * This registry is used to determine which type of visual object should be
 * created for the corresponding Diagram, Node, ChildNode or Link represented
 * by a domain model object.
 * 
 * @generated
 */
public class SqlmodelVisualIDRegistry {

	/**
	 * @generated
	 */
	private static final String DEBUG_KEY = "es.cv.gvcase.mdt.db.diagram/debug/visualID"; //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static int getVisualID(View view) {
		if (view instanceof Diagram) {
			if (SchemaEditPart.MODEL_ID.equals(view.getType())) {
				return SchemaEditPart.VISUAL_ID;
			} else {
				return -1;
			}
		}
		return es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry
				.getVisualID(view.getType());
	}

	/**
	 * @generated
	 */
	public static String getModelID(View view) {
		View diagram = view.getDiagram();
		while (view != diagram) {
			EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
			if (annotation != null) {
				return (String) annotation.getDetails().get("modelID"); //$NON-NLS-1$
			}
			view = (View) view.eContainer();
		}
		return diagram != null ? diagram.getType() : null;
	}

	/**
	 * @generated
	 */
	public static int getVisualID(String type) {
		try {
			return Integer.parseInt(type);
		} catch (NumberFormatException e) {
			if (Boolean.TRUE.toString().equalsIgnoreCase(
					Platform.getDebugOption(DEBUG_KEY))) {
				SqlmodelDiagramEditorPlugin.getInstance().logError(
						"Unable to parse view type as a visualID number: "
								+ type);
			}
		}
		return -1;
	}

	/**
	 * @generated
	 */
	public static String getType(int visualID) {
		return String.valueOf(visualID);
	}

	/**
	 * @generated
	 */
	public static int getDiagramVisualID(EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		if (SQLSchemaPackage.eINSTANCE.getSchema().isSuperTypeOf(
				domainElement.eClass())
				&& isDiagram((Schema) domainElement)) {
			return SchemaEditPart.VISUAL_ID;
		}

		return -1;
	}

	/**
	 * @generated
	 */
	public static int getNodeVisualID(View containerView, EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		String containerModelID = es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry
				.getModelID(containerView);
		if (!SchemaEditPart.MODEL_ID.equals(containerModelID)
				&& !"Sqlmodel".equals(containerModelID)) { //$NON-NLS-1$
			return -1;
		}
		int containerVisualID;
		if (SchemaEditPart.MODEL_ID.equals(containerModelID)) {
			containerVisualID = es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry
					.getVisualID(containerView);
		} else {
			if (containerView instanceof Diagram) {
				containerVisualID = SchemaEditPart.VISUAL_ID;
			} else {
				return -1;
			}
		}
		switch (containerVisualID) {
		case PersistentTablePersistentColumnTableCompartmentEditPart.VISUAL_ID:
			if (SQLTablesPackage.eINSTANCE.getColumn().isSuperTypeOf(
					domainElement.eClass())) {
				return ColumnEditPart.VISUAL_ID;
			}
			break;
		case ViewTableViewedColumnsEditPart.VISUAL_ID:
			if (SQLTablesPackage.eINSTANCE.getColumn().isSuperTypeOf(
					domainElement.eClass())) {
				return Column2EditPart.VISUAL_ID;
			}
			break;
		case ViewTableViewedTablesEditPart.VISUAL_ID:
			if (SQLTablesPackage.eINSTANCE.getPersistentTable().isSuperTypeOf(
					domainElement.eClass())) {
				return PersistentTable2EditPart.VISUAL_ID;
			}
			break;
		case SchemaEditPart.VISUAL_ID:
			if (SQLTablesPackage.eINSTANCE.getPersistentTable().isSuperTypeOf(
					domainElement.eClass())) {
				return PersistentTableEditPart.VISUAL_ID;
			}
			if (SQLTablesPackage.eINSTANCE.getViewTable().isSuperTypeOf(
					domainElement.eClass())) {
				return ViewTableEditPart.VISUAL_ID;
			}
			break;
		}
		return -1;
	}

	/**
	 * @generated
	 */
	public static boolean canCreateNode(View containerView, int nodeVisualID) {
		String containerModelID = es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry
				.getModelID(containerView);
		if (!SchemaEditPart.MODEL_ID.equals(containerModelID)
				&& !"Sqlmodel".equals(containerModelID)) { //$NON-NLS-1$
			return false;
		}
		int containerVisualID;
		if (SchemaEditPart.MODEL_ID.equals(containerModelID)) {
			containerVisualID = es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry
					.getVisualID(containerView);
		} else {
			if (containerView instanceof Diagram) {
				containerVisualID = SchemaEditPart.VISUAL_ID;
			} else {
				return false;
			}
		}
		switch (containerVisualID) {
		case PersistentTableEditPart.VISUAL_ID:
			if (PersistentTableNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (PersistentTablePersistentColumnTableCompartmentEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case ViewTableEditPart.VISUAL_ID:
			if (ViewTableNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ViewTableViewedColumnsEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ViewTableViewedTablesEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case PersistentTablePersistentColumnTableCompartmentEditPart.VISUAL_ID:
			if (ColumnEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case ViewTableViewedColumnsEditPart.VISUAL_ID:
			if (Column2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case ViewTableViewedTablesEditPart.VISUAL_ID:
			if (PersistentTable2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case SchemaEditPart.VISUAL_ID:
			if (PersistentTableEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ViewTableEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case ForeignKeyEditPart.VISUAL_ID:
			if (ForeignKeyNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		}
		return false;
	}

	/**
	 * @generated
	 */
	public static int getLinkWithClassVisualID(EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		if (SQLConstraintsPackage.eINSTANCE.getForeignKey().isSuperTypeOf(
				domainElement.eClass())) {
			return ForeignKeyEditPart.VISUAL_ID;
		}
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static boolean isDiagram(Schema element) {
		return true;
	}

	/**
	 * @generated
	 */
	private static ViewInfo diagramViewInfo = null;

	/**
	 * @generated
	 */
	public static ViewInfo getDiagramViewInfo() {
		if (diagramViewInfo == null) {
			diagramViewInfo = ViewInfoRegistry.getInstance()
					.getHeadViewInfoForEditor(SqlmodelDiagramEditor.ID);
		}
		return diagramViewInfo;
	}

}
