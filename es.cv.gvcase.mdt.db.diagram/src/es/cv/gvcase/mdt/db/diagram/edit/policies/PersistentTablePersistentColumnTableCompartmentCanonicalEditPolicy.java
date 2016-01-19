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
package es.cv.gvcase.mdt.db.diagram.edit.policies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.datatools.modelbase.sql.tables.SQLTablesPackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

import es.cv.gvcase.mdt.common.util.MDTUtil;
import es.cv.gvcase.mdt.common.util.MultiDiagramUtil;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ColumnEditPart;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelDiagramUpdater;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelNodeDescriptor;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry;

/**
 * @generated
 */
public class PersistentTablePersistentColumnTableCompartmentCanonicalEditPolicy
		extends CanonicalEditPolicy {

	/**
	 * @generated
	 */
	Set myFeaturesToSynchronize;

	/**
	 * @generated
	 */
	protected List getSemanticChildrenList() {
		View viewObject = (View) getHost().getModel();
		List<EObject> result = new ArrayList<EObject>();
		for (Iterator it = SqlmodelDiagramUpdater
				.getPersistentTablePersistentColumnTableCompartment_5001SemanticChildren(
						viewObject).iterator(); it.hasNext();) {
			EObject nextValue = ((SqlmodelNodeDescriptor) it.next())
					.getModelElement();
			result.add(nextValue);
		}

		return MultiDiagramUtil.trimElementsToShow(result, this);
	}

	/**
	 * @generated
	 */
	protected boolean isOrphaned(Collection semanticChildren, final View view) {

		if (view.getType() == ViewType.NOTE || view.getType() == ViewType.TEXT) {
			return false;
		}
		if (MDTUtil.isOrphanView(view)) {
			return true;
		}
		if (view.getElement() != null) {
			int actualID = SqlmodelVisualIDRegistry.getVisualID(view);
			int suggestedID = SqlmodelVisualIDRegistry.getNodeVisualID(
					(View) getHost().getModel(), view.getElement());
			switch (actualID) {
			case ColumnEditPart.VISUAL_ID:
				return actualID != suggestedID
						|| !semanticChildren.contains(view.getElement());
			}
			return false;
		}
		return true;
	}

	/**
	 * @generated
	 */
	protected String getDefaultFactoryHint() {
		return null;
	}

	/**
	 * @generated
	 */
	protected Set getFeaturesToSynchronize() {
		if (myFeaturesToSynchronize == null) {
			myFeaturesToSynchronize = new HashSet();
			myFeaturesToSynchronize.add(SQLTablesPackage.eINSTANCE
					.getTable_Columns());
		}
		return myFeaturesToSynchronize;
	}

	/**
	 * @generated
	 */
	@Override
	protected void refreshSemantic() {
		super.refreshSemantic();
		// this will take care of the filtering of views
		Diagram diagram = MDTUtil.getHostDiagram(this);
		if (diagram != null) {

			MDTUtil.filterDiagramViews(diagram);
		}
	}

	/**
	 * @generated
	 */
	@Override
	protected CreateViewRequest.ViewDescriptor getViewDescriptor(
			IAdaptable elementAdapter, Class viewKind, String hint, int index) {

		return new CreateViewRequest.ViewDescriptor(elementAdapter, viewKind,
				hint, index, true, host().getDiagramPreferencesHint());
	}

	/**
	 * @generated
	 */
	@Override
	protected CreateViewRequest.ViewDescriptor getViewDescriptor(EObject element) {
		// Override to explicitly indicate the hint value to the ViewDescriptor
		String factoryHint = getDefaultFactoryHint();
		IAdaptable elementAdapter = new CanonicalElementAdapter(element,
				factoryHint);

		int pos = getViewIndexFor(element);
		CreateViewRequest.ViewDescriptor descriptor = getViewDescriptor(
				elementAdapter, Node.class, String
						.valueOf(SqlmodelVisualIDRegistry.getNodeVisualID(
								(View) getHost().getModel(), element)), pos);

		// set persisted to true
		descriptor.setPersisted(true);
		return descriptor;
	}

}
