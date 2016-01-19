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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.datatools.modelbase.sql.constraints.ForeignKey;
import org.eclipse.datatools.modelbase.sql.constraints.SQLConstraintsPackage;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.tables.BaseTable;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.PersistentTable;
import org.eclipse.datatools.modelbase.sql.tables.SQLTablesPackage;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.datatools.modelbase.sql.tables.ViewTable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.View;

import es.cv.gvcase.mdt.common.provider.IDiagramLinksViewInfo;
import es.cv.gvcase.mdt.common.provider.ILinkDescriptor;
import es.cv.gvcase.mdt.db.diagram.edit.parts.Column2EditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ColumnEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ForeignKeyEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.PersistentTable2EditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.PersistentTableEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.PersistentTablePersistentColumnTableCompartmentEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.SchemaEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableViewedColumnsEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableViewedTables2EditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableViewedTablesEditPart;
import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelElementTypes;

/**
 * @generated
 */
public class SqlmodelDiagramUpdater {

	/**
	 * @generated
	 */
	public static boolean isShortcutOrphaned(View view) {
		return !view.isSetElement() || view.getElement() == null
				|| view.getElement().eIsProxy();
	}

	/**
	 * @generated
	 */
	public static List getSemanticChildren(View view) {
		switch (SqlmodelVisualIDRegistry.getVisualID(view)) {
		case PersistentTablePersistentColumnTableCompartmentEditPart.VISUAL_ID:
			return getPersistentTablePersistentColumnTableCompartment_5001SemanticChildren(view);
		case ViewTableViewedColumnsEditPart.VISUAL_ID:
			return getViewTableViewedColumns_5002SemanticChildren(view);
		case ViewTableViewedTablesEditPart.VISUAL_ID:
			return getViewTableViewedTables_5003SemanticChildren(view);
		case SchemaEditPart.VISUAL_ID:
			return getSchema_1000SemanticChildren(view);
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getPersistentTablePersistentColumnTableCompartment_5001SemanticChildren(
			View view) {
		if (false == view.eContainer() instanceof View) {
			return Collections.EMPTY_LIST;
		}
		View containerView = (View) view.eContainer();
		if (!containerView.isSetElement()) {
			return Collections.EMPTY_LIST;
		}
		PersistentTable modelElement = (PersistentTable) containerView
				.getElement();
		List result = new LinkedList();
		for (Iterator it = modelElement.getColumns().iterator(); it.hasNext();) {
			Column childElement = (Column) it.next();
			int visualID = SqlmodelVisualIDRegistry.getNodeVisualID(view,
					childElement);
			if (visualID == ColumnEditPart.VISUAL_ID) {
				result.add(new SqlmodelNodeDescriptor(childElement, visualID));
				continue;
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	public static List getViewTableViewedColumns_5002SemanticChildren(View view) {
		if (false == view.eContainer() instanceof View) {
			return Collections.EMPTY_LIST;
		}
		View containerView = (View) view.eContainer();
		if (!containerView.isSetElement()) {
			return Collections.EMPTY_LIST;
		}
		ViewTable modelElement = (ViewTable) containerView.getElement();
		List result = new LinkedList();
		for (Iterator it = modelElement.getViewedColumns().iterator(); it
				.hasNext();) {
			Column childElement = (Column) it.next();
			int visualID = SqlmodelVisualIDRegistry.getNodeVisualID(view,
					childElement);
			if (visualID == Column2EditPart.VISUAL_ID) {
				result.add(new SqlmodelNodeDescriptor(childElement, visualID));
				continue;
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	public static List getViewTableViewedTables_5003SemanticChildren(View view) {
		if (false == view.eContainer() instanceof View) {
			return Collections.EMPTY_LIST;
		}
		View containerView = (View) view.eContainer();
		if (!containerView.isSetElement()) {
			return Collections.EMPTY_LIST;
		}
		ViewTable modelElement = (ViewTable) containerView.getElement();
		List result = new LinkedList();
		for (Iterator it = modelElement.getViewedTables().iterator(); it
				.hasNext();) {
			Table childElement = (Table) it.next();
			int visualID = SqlmodelVisualIDRegistry.getNodeVisualID(view,
					childElement);
			if (visualID == PersistentTable2EditPart.VISUAL_ID) {
				result.add(new SqlmodelNodeDescriptor(childElement, visualID));
				continue;
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	public static List getSchema_1000SemanticChildren(View view) {
		if (!view.isSetElement()) {
			return Collections.EMPTY_LIST;
		}
		Schema modelElement = (Schema) view.getElement();
		List result = new LinkedList();
		for (Iterator it = modelElement.getTables().iterator(); it.hasNext();) {
			Table childElement = (Table) it.next();
			int visualID = SqlmodelVisualIDRegistry.getNodeVisualID(view,
					childElement);
			if (visualID == PersistentTableEditPart.VISUAL_ID) {
				result.add(new SqlmodelNodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == ViewTableEditPart.VISUAL_ID) {
				result.add(new SqlmodelNodeDescriptor(childElement, visualID));
				continue;
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	public static List getContainedLinks(View view) {
		switch (SqlmodelVisualIDRegistry.getVisualID(view)) {
		case SchemaEditPart.VISUAL_ID:
			return getSchema_1000ContainedLinks(view);
		case PersistentTableEditPart.VISUAL_ID:
			return getPersistentTable_1001ContainedLinks(view);
		case ViewTableEditPart.VISUAL_ID:
			return getViewTable_1002ContainedLinks(view);
		case ColumnEditPart.VISUAL_ID:
			return getColumn_2001ContainedLinks(view);
		case Column2EditPart.VISUAL_ID:
			return getColumn_2002ContainedLinks(view);
		case PersistentTable2EditPart.VISUAL_ID:
			return getPersistentTable_2003ContainedLinks(view);
		case ForeignKeyEditPart.VISUAL_ID:
			return getForeignKey_3001ContainedLinks(view);
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getIncomingLinks(View view) {
		switch (SqlmodelVisualIDRegistry.getVisualID(view)) {
		case PersistentTableEditPart.VISUAL_ID:
			return getPersistentTable_1001IncomingLinks(view);
		case ViewTableEditPart.VISUAL_ID:
			return getViewTable_1002IncomingLinks(view);
		case ColumnEditPart.VISUAL_ID:
			return getColumn_2001IncomingLinks(view);
		case Column2EditPart.VISUAL_ID:
			return getColumn_2002IncomingLinks(view);
		case PersistentTable2EditPart.VISUAL_ID:
			return getPersistentTable_2003IncomingLinks(view);
		case ForeignKeyEditPart.VISUAL_ID:
			return getForeignKey_3001IncomingLinks(view);
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getOutgoingLinks(View view) {
		switch (SqlmodelVisualIDRegistry.getVisualID(view)) {
		case PersistentTableEditPart.VISUAL_ID:
			return getPersistentTable_1001OutgoingLinks(view);
		case ViewTableEditPart.VISUAL_ID:
			return getViewTable_1002OutgoingLinks(view);
		case ColumnEditPart.VISUAL_ID:
			return getColumn_2001OutgoingLinks(view);
		case Column2EditPart.VISUAL_ID:
			return getColumn_2002OutgoingLinks(view);
		case PersistentTable2EditPart.VISUAL_ID:
			return getPersistentTable_2003OutgoingLinks(view);
		case ForeignKeyEditPart.VISUAL_ID:
			return getForeignKey_3001OutgoingLinks(view);
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getSchema_1000ContainedLinks(View view) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getPersistentTable_1001ContainedLinks(View view) {
		PersistentTable modelElement = (PersistentTable) view.getElement();
		List result = new LinkedList();
		result
				.addAll(getContainedTypeModelFacetLinks_ForeignKey_3001(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List getViewTable_1002ContainedLinks(View view) {
		ViewTable modelElement = (ViewTable) view.getElement();
		List result = new LinkedList();
		result
				.addAll(getOutgoingFeatureModelFacetLinks_ViewTable_ViewedTables_3002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List getColumn_2001ContainedLinks(View view) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getColumn_2002ContainedLinks(View view) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getPersistentTable_2003ContainedLinks(View view) {
		PersistentTable modelElement = (PersistentTable) view.getElement();
		List result = new LinkedList();
		result
				.addAll(getContainedTypeModelFacetLinks_ForeignKey_3001(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List getForeignKey_3001ContainedLinks(View view) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getPersistentTable_1001IncomingLinks(View view) {
		PersistentTable modelElement = (PersistentTable) view.getElement();
		Map crossReferences = EcoreUtil.CrossReferencer.find(view.eResource()
				.getResourceSet().getResources());
		List result = new LinkedList();
		result.addAll(getIncomingTypeModelFacetLinks_ForeignKey_3001(
				modelElement, crossReferences));
		result
				.addAll(getIncomingFeatureModelFacetLinks_ViewTable_ViewedTables_3002(
						modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List getViewTable_1002IncomingLinks(View view) {
		ViewTable modelElement = (ViewTable) view.getElement();
		Map crossReferences = EcoreUtil.CrossReferencer.find(view.eResource()
				.getResourceSet().getResources());
		List result = new LinkedList();
		result
				.addAll(getIncomingFeatureModelFacetLinks_ViewTable_ViewedTables_3002(
						modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List getColumn_2001IncomingLinks(View view) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getColumn_2002IncomingLinks(View view) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getPersistentTable_2003IncomingLinks(View view) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getForeignKey_3001IncomingLinks(View view) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getPersistentTable_1001OutgoingLinks(View view) {
		PersistentTable modelElement = (PersistentTable) view.getElement();
		List result = new LinkedList();
		result
				.addAll(getOutgoingTypeModelFacetLinks_ForeignKey_3001(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List getViewTable_1002OutgoingLinks(View view) {
		ViewTable modelElement = (ViewTable) view.getElement();
		List result = new LinkedList();
		result
				.addAll(getOutgoingFeatureModelFacetLinks_ViewTable_ViewedTables_3002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List getColumn_2001OutgoingLinks(View view) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getColumn_2002OutgoingLinks(View view) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getPersistentTable_2003OutgoingLinks(View view) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public static List getForeignKey_3001OutgoingLinks(View view) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	private static Collection getContainedTypeModelFacetLinks_ForeignKey_3001(
			BaseTable container) {
		Collection result = new LinkedList();
		for (Iterator links = container.getConstraints().iterator(); links
				.hasNext();) {
			EObject linkObject = (EObject) links.next();
			if (false == linkObject instanceof ForeignKey) {
				continue;
			}
			ForeignKey link = (ForeignKey) linkObject;
			if (ForeignKeyEditPart.VISUAL_ID != SqlmodelVisualIDRegistry
					.getLinkWithClassVisualID(link)) {
				continue;
			}
			BaseTable dst = link.getReferencedTable();
			BaseTable src = link.getBaseTable();
			result.add(new SqlmodelLinkDescriptor(src, dst, link,
					SqlmodelElementTypes.ForeignKey_3001,
					ForeignKeyEditPart.VISUAL_ID));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection getIncomingTypeModelFacetLinks_ForeignKey_3001(
			BaseTable target, Map crossReferences) {
		Collection result = new LinkedList();
		Collection settings = (Collection) crossReferences.get(target);
		for (Iterator it = settings.iterator(); it.hasNext();) {
			EStructuralFeature.Setting setting = (EStructuralFeature.Setting) it
					.next();
			if (setting.getEStructuralFeature() != SQLConstraintsPackage.eINSTANCE
					.getForeignKey_ReferencedTable()
					|| false == setting.getEObject() instanceof ForeignKey) {
				continue;
			}
			ForeignKey link = (ForeignKey) setting.getEObject();
			if (ForeignKeyEditPart.VISUAL_ID != SqlmodelVisualIDRegistry
					.getLinkWithClassVisualID(link)) {
				continue;
			}
			BaseTable src = link.getBaseTable();
			result.add(new SqlmodelLinkDescriptor(src, target, link,
					SqlmodelElementTypes.ForeignKey_3001,
					ForeignKeyEditPart.VISUAL_ID));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection getIncomingFeatureModelFacetLinks_ViewTable_ViewedTables_3002(
			Table target, Map crossReferences) {
		Collection result = new LinkedList();
		Collection settings = (Collection) crossReferences.get(target);
		for (Iterator it = settings.iterator(); it.hasNext();) {
			EStructuralFeature.Setting setting = (EStructuralFeature.Setting) it
					.next();
			if (setting.getEStructuralFeature() == SQLTablesPackage.eINSTANCE
					.getViewTable_ViewedTables()) {
				result.add(new SqlmodelLinkDescriptor(setting.getEObject(),
						target,
						SqlmodelElementTypes.ViewTableViewedTables_3002,
						ViewTableViewedTables2EditPart.VISUAL_ID));
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection getOutgoingTypeModelFacetLinks_ForeignKey_3001(
			BaseTable source) {
		BaseTable container = null;
		// Find container element for the link.
		// Climb up by containment hierarchy starting from the source
		// and return the first element that is instance of the container class.
		for (EObject element = source; element != null && container == null; element = element
				.eContainer()) {
			if (element instanceof BaseTable) {
				container = (BaseTable) element;
			}
		}
		if (container == null) {
			return Collections.EMPTY_LIST;
		}
		Collection result = new LinkedList();
		for (Iterator links = container.getConstraints().iterator(); links
				.hasNext();) {
			EObject linkObject = (EObject) links.next();
			if (false == linkObject instanceof ForeignKey) {
				continue;
			}
			ForeignKey link = (ForeignKey) linkObject;
			if (ForeignKeyEditPart.VISUAL_ID != SqlmodelVisualIDRegistry
					.getLinkWithClassVisualID(link)) {
				continue;
			}
			BaseTable dst = link.getReferencedTable();
			BaseTable src = link.getBaseTable();
			if (src != source) {
				continue;
			}
			result.add(new SqlmodelLinkDescriptor(src, dst, link,
					SqlmodelElementTypes.ForeignKey_3001,
					ForeignKeyEditPart.VISUAL_ID));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection getOutgoingFeatureModelFacetLinks_ViewTable_ViewedTables_3002(
			ViewTable source) {
		Collection result = new LinkedList();
		for (Iterator destinations = source.getViewedTables().iterator(); destinations
				.hasNext();) {
			Table destination = (Table) destinations.next();
			result.add(new SqlmodelLinkDescriptor(source, destination,
					SqlmodelElementTypes.ViewTableViewedTables_3002,
					ViewTableViewedTables2EditPart.VISUAL_ID));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static IDiagramLinksViewInfo diagramLinksViewInfo = new IDiagramLinksViewInfo() {
		/**
		 * @generated
		 */
		public List<ILinkDescriptor> getAllLinks(View view) {
			List<ILinkDescriptor> allLinks = new ArrayList<ILinkDescriptor>();
			allLinks.addAll(getContainedLinks(view));
			allLinks.addAll(getIncomingLinks(view));
			allLinks.addAll(getOutgoingLinks(view));
			return allLinks;
		}

		/**
		 * @generated
		 */
		public List<ILinkDescriptor> getContainedLinks(View view) {
			return SqlmodelDiagramUpdater.this.getContainedLinks(view);
		}

		/**
		 * @generated
		 */
		public List<ILinkDescriptor> getIncomingLinks(View view) {
			return SqlmodelDiagramUpdater.this.getIncomingLinks(view);
		}

		/**
		 * @generated
		 */
		public List<ILinkDescriptor> getOutgoingLinks(View view) {
			return SqlmodelDiagramUpdater.this.getOutgoingLinks(view);
		}

	};

	/**
	 * @generated
	 */
	public static IDiagramLinksViewInfo getDiagramLinksViewInfo() {
		return diagramLinksViewInfo;
	}

}
