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
package es.cv.gvcase.mdt.db.diagram.sheet;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.datatools.modelbase.sql.schema.provider.SqlmodelEditPlugin;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;

import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableViewedTables2EditPart;
import es.cv.gvcase.mdt.db.diagram.navigator.SqlmodelNavigatorGroup;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry;
import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelElementTypes;

/**
 * @generated
 */
public class SqlmodelSheetLabelProvider extends BaseLabelProvider implements
		ILabelProvider {

	/**
	 * @generated NOT ; modified for special ViewTableCiewedTables2EditPart
	 *            treatment
	 */
	public String getText(Object element) {
		// fjcano :: special treatment for ViewTableViewedTables2EditPart
		Object selected = unwrap(element);
		if (selected instanceof SqlmodelNavigatorGroup) {
			return ((SqlmodelNavigatorGroup) selected).getGroupName();
		}
		if (element instanceof StructuredSelection) {
			StructuredSelection structuredSelection = (StructuredSelection) element;
			if (structuredSelection.getFirstElement() instanceof ViewTableViewedTables2EditPart) {
				return "Viewed Table";
			} else if (selected instanceof SQLObject) {
				SQLObject sqlobject = (SQLObject) selected;
				String className = sqlobject.getClass().getSimpleName();
				// Remove the "Impl" part of the name
				className = className.substring(0, className.length() - 4);
				String name = "";
				if (sqlobject.getName() != null) {
					name = sqlobject.getName();
				}
				return "<" + className + "> " + name;
			}
		}
		// original treatment
		element = unwrap(element);
		if (element instanceof SqlmodelNavigatorGroup) {
			return ((SqlmodelNavigatorGroup) element).getGroupName();
		}
		IElementType etype = getElementType(getView(element));
		return etype == null ? "" : etype.getDisplayName();
	}

	/**
	 * @generated NOT ; modified for ViewTableViewedTables2EditPart treatment
	 */
	public Image getImage(Object element) {
		// fjcano :: special treatment for ViewTableViewedTables2EditPart
		if (element instanceof StructuredSelection) {
			StructuredSelection structuredSelection = (StructuredSelection) element;
			if (structuredSelection.getFirstElement() instanceof ViewTableViewedTables2EditPart) {
				return ExtendedImageRegistry.getInstance().getImage(
						SqlmodelEditPlugin.INSTANCE
								.getImage("full/obj16/viewedTable"));
			}
		}
		// original treatment
		IElementType etype = getElementType(getView(unwrap(element)));
		return etype == null ? null : SqlmodelElementTypes.getImage(etype);
	}

	/**
	 * @generated
	 */
	private Object unwrap(Object element) {
		if (element instanceof IStructuredSelection) {
			return ((IStructuredSelection) element).getFirstElement();
		}
		return element;
	}

	/**
	 * @generated
	 */
	private View getView(Object element) {
		if (element instanceof View) {
			return (View) element;
		}
		if (element instanceof IAdaptable) {
			return (View) ((IAdaptable) element).getAdapter(View.class);
		}
		return null;
	}

	/**
	 * @generated
	 */
	private IElementType getElementType(View view) {
		// For intermediate views climb up the containment hierarchy to find the one associated with an element type.
		while (view != null) {
			int vid = SqlmodelVisualIDRegistry.getVisualID(view);
			IElementType etype = SqlmodelElementTypes.getElementType(vid);
			if (etype != null) {
				return etype;
			}
			view = view.eContainer() instanceof View ? (View) view.eContainer()
					: null;
		}
		return null;
	}

}
