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
package es.cv.gvcase.mdt.db.diagram.navigator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITreePathLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.ViewerLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;

import es.cv.gvcase.mdt.db.diagram.edit.parts.Column2EditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ColumnEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ForeignKeyEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ForeignKeyNameEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.PersistentTable2EditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.PersistentTableEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.PersistentTableNameEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.SchemaEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableNameEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableViewedTables2EditPart;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelDiagramEditorPlugin;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry;
import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelElementTypes;
import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelParserProvider;

/**
 * @generated
 */
public class SqlmodelNavigatorLabelProvider extends LabelProvider implements
		ICommonLabelProvider, ITreePathLabelProvider {

	/**
	 * @generated
	 */
	static {
		SqlmodelDiagramEditorPlugin
				.getInstance()
				.getImageRegistry()
				.put(
						"Navigator?UnknownElement", ImageDescriptor.getMissingImageDescriptor()); //$NON-NLS-1$
		SqlmodelDiagramEditorPlugin
				.getInstance()
				.getImageRegistry()
				.put(
						"Navigator?ImageNotFound", ImageDescriptor.getMissingImageDescriptor()); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	public void updateLabel(ViewerLabel label, TreePath elementPath) {
		Object element = elementPath.getLastSegment();
		if (element instanceof SqlmodelNavigatorItem
				&& !isOwnView(((SqlmodelNavigatorItem) element).getView())) {
			return;
		}
		label.setText(getText(element));
		label.setImage(getImage(element));
	}

	/**
	 * @generated
	 */
	public Image getImage(Object element) {
		if (element instanceof SqlmodelNavigatorGroup) {
			SqlmodelNavigatorGroup group = (SqlmodelNavigatorGroup) element;
			return SqlmodelDiagramEditorPlugin.getInstance().getBundledImage(
					group.getIcon());
		}

		if (element instanceof SqlmodelNavigatorItem) {
			SqlmodelNavigatorItem navigatorItem = (SqlmodelNavigatorItem) element;
			if (!isOwnView(navigatorItem.getView())) {
				return super.getImage(element);
			}
			return getImage(navigatorItem.getView());
		}

		// Due to plugin.xml content will be called only for "own" views
		if (element instanceof IAdaptable) {
			View view = (View) ((IAdaptable) element).getAdapter(View.class);
			if (view != null && isOwnView(view)) {
				return getImage(view);
			}
		}

		return super.getImage(element);
	}

	/**
	 * @generated
	 */
	public Image getImage(View view) {
		switch (SqlmodelVisualIDRegistry.getVisualID(view)) {
		case SchemaEditPart.VISUAL_ID:
			return getImage(
					"Navigator?Diagram?http:///org/eclipse/datatools/modelbase/sql/schema.ecore?Schema", SqlmodelElementTypes.Schema_1000); //$NON-NLS-1$
		case PersistentTableEditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http:///org/eclipse/datatools/modelbase/sql/tables.ecore?PersistentTable", SqlmodelElementTypes.PersistentTable_1001); //$NON-NLS-1$
		case ViewTableEditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http:///org/eclipse/datatools/modelbase/sql/tables.ecore?ViewTable", SqlmodelElementTypes.ViewTable_1002); //$NON-NLS-1$
		case ColumnEditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http:///org/eclipse/datatools/modelbase/sql/tables.ecore?Column", SqlmodelElementTypes.Column_2001); //$NON-NLS-1$
		case Column2EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http:///org/eclipse/datatools/modelbase/sql/tables.ecore?Column", SqlmodelElementTypes.Column_2002); //$NON-NLS-1$
		case PersistentTable2EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http:///org/eclipse/datatools/modelbase/sql/tables.ecore?PersistentTable", SqlmodelElementTypes.PersistentTable_2003); //$NON-NLS-1$
		case ForeignKeyEditPart.VISUAL_ID:
			return getImage(
					"Navigator?Link?http:///org/eclipse/datatools/modelbase/sql/constraints.ecore?ForeignKey", SqlmodelElementTypes.ForeignKey_3001); //$NON-NLS-1$
		case ViewTableViewedTables2EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Link?http:///org/eclipse/datatools/modelbase/sql/tables.ecore?ViewTable?viewedTables", SqlmodelElementTypes.ViewTableViewedTables_3002); //$NON-NLS-1$
		}
		return getImage("Navigator?UnknownElement", null); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	private Image getImage(String key, IElementType elementType) {
		ImageRegistry imageRegistry = SqlmodelDiagramEditorPlugin.getInstance()
				.getImageRegistry();
		Image image = imageRegistry.get(key);
		if (image == null && elementType != null
				&& SqlmodelElementTypes.isKnownElementType(elementType)) {
			image = SqlmodelElementTypes.getImage(elementType);
			imageRegistry.put(key, image);
		}

		if (image == null) {
			image = imageRegistry.get("Navigator?ImageNotFound"); //$NON-NLS-1$
			imageRegistry.put(key, image);
		}
		return image;
	}

	/**
	 * @generated
	 */
	public String getText(Object element) {
		if (element instanceof SqlmodelNavigatorGroup) {
			SqlmodelNavigatorGroup group = (SqlmodelNavigatorGroup) element;
			return group.getGroupName();
		}

		if (element instanceof SqlmodelNavigatorItem) {
			SqlmodelNavigatorItem navigatorItem = (SqlmodelNavigatorItem) element;
			if (!isOwnView(navigatorItem.getView())) {
				return null;
			}
			return getText(navigatorItem.getView());
		}

		// Due to plugin.xml content will be called only for "own" views
		if (element instanceof IAdaptable) {
			View view = (View) ((IAdaptable) element).getAdapter(View.class);
			if (view != null && isOwnView(view)) {
				return getText(view);
			}
		}

		return super.getText(element);
	}

	/**
	 * @generated
	 */
	public String getText(View view) {
		if (view.getElement() != null && view.getElement().eIsProxy()) {
			return getUnresolvedDomainElementProxyText(view);
		}
		switch (SqlmodelVisualIDRegistry.getVisualID(view)) {
		case SchemaEditPart.VISUAL_ID:
			return getSchema_1000Text(view);
		case PersistentTableEditPart.VISUAL_ID:
			return getPersistentTable_1001Text(view);
		case ViewTableEditPart.VISUAL_ID:
			return getViewTable_1002Text(view);
		case ColumnEditPart.VISUAL_ID:
			return getColumn_2001Text(view);
		case Column2EditPart.VISUAL_ID:
			return getColumn_2002Text(view);
		case PersistentTable2EditPart.VISUAL_ID:
			return getPersistentTable_2003Text(view);
		case ForeignKeyEditPart.VISUAL_ID:
			return getForeignKey_3001Text(view);
		case ViewTableViewedTables2EditPart.VISUAL_ID:
			return getViewTableViewedTables_3002Text(view);
		}
		return getUnknownElementText(view);
	}

	/**
	 * @generated
	 */
	private String getSchema_1000Text(View view) {
		Schema domainModelElement = (Schema) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			SqlmodelDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 1000); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getPersistentTable_1001Text(View view) {
		IParser parser = SqlmodelParserProvider.getParser(
				SqlmodelElementTypes.PersistentTable_1001,
				view.getElement() != null ? view.getElement() : view,
				SqlmodelVisualIDRegistry
						.getType(PersistentTableNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			SqlmodelDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 4001); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getViewTable_1002Text(View view) {
		IParser parser = SqlmodelParserProvider.getParser(
				SqlmodelElementTypes.ViewTable_1002,
				view.getElement() != null ? view.getElement() : view,
				SqlmodelVisualIDRegistry
						.getType(ViewTableNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			SqlmodelDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 4002); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getColumn_2001Text(View view) {
		IParser parser = SqlmodelParserProvider.getParser(
				SqlmodelElementTypes.Column_2001,
				view.getElement() != null ? view.getElement() : view,
				SqlmodelVisualIDRegistry.getType(ColumnEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			SqlmodelDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 2001); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getColumn_2002Text(View view) {
		IParser parser = SqlmodelParserProvider.getParser(
				SqlmodelElementTypes.Column_2002,
				view.getElement() != null ? view.getElement() : view,
				SqlmodelVisualIDRegistry.getType(Column2EditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			SqlmodelDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 2002); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getPersistentTable_2003Text(View view) {
		IParser parser = SqlmodelParserProvider.getParser(
				SqlmodelElementTypes.PersistentTable_2003,
				view.getElement() != null ? view.getElement() : view,
				SqlmodelVisualIDRegistry
						.getType(PersistentTable2EditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			SqlmodelDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 2003); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getForeignKey_3001Text(View view) {
		IParser parser = SqlmodelParserProvider.getParser(
				SqlmodelElementTypes.ForeignKey_3001,
				view.getElement() != null ? view.getElement() : view,
				SqlmodelVisualIDRegistry
						.getType(ForeignKeyNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			SqlmodelDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 4003); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getViewTableViewedTables_3002Text(View view) {
		return ""; //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	private String getUnknownElementText(View view) {
		return "<UnknownElement Visual_ID = " + view.getType() + ">"; //$NON-NLS-1$  //$NON-NLS-2$
	}

	/**
	 * @generated
	 */
	private String getUnresolvedDomainElementProxyText(View view) {
		return "<Unresolved domain element Visual_ID = " + view.getType() + ">"; //$NON-NLS-1$  //$NON-NLS-2$
	}

	/**
	 * @generated
	 */
	public void init(ICommonContentExtensionSite aConfig) {
	}

	/**
	 * @generated
	 */
	public void restoreState(IMemento aMemento) {
	}

	/**
	 * @generated
	 */
	public void saveState(IMemento aMemento) {
	}

	/**
	 * @generated
	 */
	public String getDescription(Object anElement) {
		return null;
	}

	/**
	 * @generated
	 */
	private boolean isOwnView(View view) {
		return SchemaEditPart.MODEL_ID.equals(SqlmodelVisualIDRegistry
				.getModelID(view));
	}

}
