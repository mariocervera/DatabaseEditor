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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.datatools.modelbase.sql.constraints.ForeignKey;
import org.eclipse.datatools.modelbase.sql.schema.SQLSchemaPackage;
import org.eclipse.datatools.modelbase.sql.tables.PersistentTable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.DeferredLayoutCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalConnectionEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

import es.cv.gvcase.mdt.common.util.DiagramEditPartsUtil;
import es.cv.gvcase.mdt.common.util.MDTUtil;
import es.cv.gvcase.mdt.common.util.MultiDiagramUtil;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ForeignKeyEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.PersistentTableEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.SchemaEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableEditPart;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelDiagramUpdater;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelLinkDescriptor;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelNodeDescriptor;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry;

/**
 * @generated
 */
public class SchemaCanonicalEditPolicy extends CanonicalConnectionEditPolicy {

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
				.getSchema_1000SemanticChildren(viewObject).iterator(); it
				.hasNext();) {
			EObject nextValue = ((SqlmodelNodeDescriptor) it.next())
					.getModelElement();
			result.add(nextValue);
		}

		return MultiDiagramUtil.trimElementsToShow(result, this);
	}

	/**
	 * @generated
	 */
	protected boolean shouldDeleteView(View view) {
		return true;
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
			case PersistentTableEditPart.VISUAL_ID:
			case ViewTableEditPart.VISUAL_ID:
				return actualID != suggestedID;
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
			myFeaturesToSynchronize.add(SQLSchemaPackage.eINSTANCE
					.getSchema_Tables());
		}
		return myFeaturesToSynchronize;
	}

	/**
	 * @generated
	 */
	protected List getSemanticConnectionsList() {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	protected EObject getSourceElement(EObject relationship) {
		return null;
	}

	/**
	 * @generated
	 */
	protected EObject getTargetElement(EObject relationship) {
		return null;
	}

	/**
	 * @generated
	 */
	protected boolean shouldIncludeConnection(Edge connector,
			Collection children) {
		return false;
	}

	/**
	 * @generated
	 */
	protected void refreshSemantic() {
		List createdViews = new LinkedList();
		createdViews.addAll(refreshSemanticChildren());
		List createdConnectionViews = new LinkedList();
		createdConnectionViews.addAll(refreshSemanticConnections());
		createdConnectionViews.addAll(refreshConnections());

		if (createdViews.size() > 1) {
			// perform a layout of the container
			DeferredLayoutCommand layoutCmd = new DeferredLayoutCommand(host()
					.getEditingDomain(), createdViews, host());
			executeCommand(new ICommandProxy(layoutCmd));
		}

		createdViews.addAll(createdConnectionViews);
		makeViewsImmutable(createdViews);
		Diagram diagram = MDTUtil.getHostDiagram(this);

		if (diagram != null) {
			MDTUtil.filterDiagramViews(diagram);
		}
	}

	/**
	 * @generated
	 */
	private Diagram getDiagram() {
		return ((View) getHost().getModel()).getDiagram();
	}

	/**
	 * @generated NOT
	 */
	private Collection refreshConnections() {
		Map domain2NotationMap = new HashMap();
		Collection linkDescriptors = collectAllLinks(getDiagram(),
				domain2NotationMap);
		Collection existingLinks = new LinkedList(getDiagram().getEdges());
		for (Iterator linksIterator = existingLinks.iterator(); linksIterator
				.hasNext();) {
			boolean dLinksRemove = false;
			Edge nextDiagramLink = (Edge) linksIterator.next();
			EObject diagramLinkObject = nextDiagramLink.getElement();
			if (diagramLinkObject != null
					&& nextDiagramLink.getSource() != null
					&& nextDiagramLink.getTarget() != null) {
				EObject diagramLinkSrc = nextDiagramLink.getSource()
						.getElement();
				EObject diagramLinkDst = nextDiagramLink.getTarget()
						.getElement();
				int diagramLinkVisualID = SqlmodelVisualIDRegistry
						.getVisualID(nextDiagramLink);
				for (Iterator LinkDescriptorsIterator = linkDescriptors
						.iterator(); LinkDescriptorsIterator.hasNext();) {
					SqlmodelLinkDescriptor nextLinkDescriptor = (SqlmodelLinkDescriptor) LinkDescriptorsIterator
							.next();
					if (diagramLinkObject == nextLinkDescriptor
							.getModelElement()
							&& diagramLinkSrc == nextLinkDescriptor.getSource()
							&& diagramLinkDst == nextLinkDescriptor
									.getDestination()
							&& diagramLinkVisualID == nextLinkDescriptor
									.getVisualID()) {
						if (!dLinksRemove) {
							linksIterator.remove();
							dLinksRemove = true;
						}
						LinkDescriptorsIterator.remove();
					}
				}
			}
		}
		deleteViews(existingLinks.iterator());
		return createConnections(linkDescriptors, domain2NotationMap);
	}

	/**
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	private Collection collectAllLinks(View view, Map domain2NotationMap) {
		if (!SchemaEditPart.MODEL_ID.equals(SqlmodelVisualIDRegistry
				.getModelID(view))) {
			return Collections.EMPTY_LIST;
		}
		List result = new LinkedList();
		switch (SqlmodelVisualIDRegistry.getVisualID(view)) {
		case SchemaEditPart.VISUAL_ID: {
			if (!domain2NotationMap.containsKey(view.getElement())) {
				result.addAll(SqlmodelDiagramUpdater
						.getSchema_1000ContainedLinks(view));
			}
			if (!domain2NotationMap.containsKey(view.getElement())
					|| view.getEAnnotation("Shortcut") == null) { //$NON-NLS-1$
				domain2NotationMap.put(view.getElement(), view);
			}
			break;
		}
		case PersistentTableEditPart.VISUAL_ID: {
			if (!domain2NotationMap.containsKey(view.getElement())) {
				result.addAll(SqlmodelDiagramUpdater
						.getPersistentTable_1001ContainedLinks(view));
			}
			if (!domain2NotationMap.containsKey(view.getElement())
					|| view.getEAnnotation("Shortcut") == null) { //$NON-NLS-1$
				domain2NotationMap.put(view.getElement(), view);
			}
			break;
		}
		case ViewTableEditPart.VISUAL_ID: {
			if (!domain2NotationMap.containsKey(view.getElement())) {
				result.addAll(SqlmodelDiagramUpdater
						.getViewTable_1002ContainedLinks(view));
			}
			if (!domain2NotationMap.containsKey(view.getElement())
					|| view.getEAnnotation("Shortcut") == null) { //$NON-NLS-1$
				domain2NotationMap.put(view.getElement(), view);
			}
			break;
		}
		case ForeignKeyEditPart.VISUAL_ID: {
			if (!domain2NotationMap.containsKey(view.getElement())) {
				result.addAll(SqlmodelDiagramUpdater
						.getForeignKey_3001ContainedLinks(view));
			}
			if (!domain2NotationMap.containsKey(view.getElement())
					|| view.getEAnnotation("Shortcut") == null) { //$NON-NLS-1$
				domain2NotationMap.put(view.getElement(), view);
			}
			break;
		}
		}
		for (Iterator children = view.getChildren().iterator(); children
				.hasNext();) {
			result.addAll(collectAllLinks((View) children.next(),
					domain2NotationMap));
		}
		for (Iterator edges = view.getSourceEdges().iterator(); edges.hasNext();) {
			result.addAll(collectAllLinks((View) edges.next(),
					domain2NotationMap));
		}
		removeDuplicatedLinks(result);
		purgeCollection(view.getDiagram(), result);
		return result;
	}

	/**
	 * @generated
	 */
	private void removeDuplicatedLinks(List linkDescriptors) {
		List toDelete = new ArrayList();
		for (int i = 0; i < linkDescriptors.size(); i++) {
			Object object1 = linkDescriptors.get(i);
			if (object1 instanceof SqlmodelLinkDescriptor) {
				SqlmodelLinkDescriptor linkDescriptor1 = (SqlmodelLinkDescriptor) object1;
				for (int j = i + 1; j < linkDescriptors.size(); j++) {
					Object object2 = linkDescriptors.get(j);
					if (object2 instanceof SqlmodelLinkDescriptor) {
						SqlmodelLinkDescriptor linkDescriptor2 = (SqlmodelLinkDescriptor) object2;

						if (checkSameLinkDescriptor(linkDescriptor1,
								linkDescriptor2)) {
							if (toDelete.contains(linkDescriptor2) == false) {
								toDelete.add(linkDescriptor2);
							}
						}
					}
				}
			}
		}
		for (Object object : toDelete) {
			linkDescriptors.remove(object);
		}
	}

	/**
	 * @generated
	 */
	private void purgeCollection(Diagram diagram, Collection list) {
		for (Iterator i = list.iterator(); i.hasNext();) {
			Object obj = i.next();
			if (obj instanceof Collection) {
				purgeCollection(diagram, (Collection) obj);
			} else {
				if (obj instanceof SqlmodelLinkDescriptor) {
					SqlmodelLinkDescriptor lDes = (SqlmodelLinkDescriptor) obj;
					if (lDes.getModelElement() != null
							&& !MultiDiagramUtil
									.findEObjectReferencedInEAnnotation(
											diagram, lDes.getModelElement())) {
						i.remove();
					}
				}
			}
		}
	}

	/**
	 * @generated
	 */
	private boolean checkSameLinkDescriptor(SqlmodelLinkDescriptor link1,
			SqlmodelLinkDescriptor link2) {
		return MDTUtil.checkSameLinkDescriptor(link1, link2);
	}

	/**
	 * @generated
	 */
	private Collection createConnections(Collection linkDescriptors,
			Map domain2NotationMap) {
		List adapters = new LinkedList();
		for (Iterator linkDescriptorsIterator = linkDescriptors.iterator(); linkDescriptorsIterator
				.hasNext();) {
			final SqlmodelLinkDescriptor nextLinkDescriptor = (SqlmodelLinkDescriptor) linkDescriptorsIterator
					.next();
			EditPart sourceEditPart = getEditPart(nextLinkDescriptor
					.getSource(), domain2NotationMap);
			EditPart targetEditPart = getEditPart(nextLinkDescriptor
					.getDestination(), domain2NotationMap);
			if (sourceEditPart == null || targetEditPart == null) {
				continue;
			}
			CreateConnectionViewRequest.ConnectionViewDescriptor descriptor = new CreateConnectionViewRequest.ConnectionViewDescriptor(
					nextLinkDescriptor.getSemanticAdapter(), null,
					ViewUtil.APPEND, true, ((IGraphicalEditPart) getHost())
							.getDiagramPreferencesHint());
			CreateConnectionViewRequest ccr = new CreateConnectionViewRequest(
					descriptor);
			ccr.setType(RequestConstants.REQ_CONNECTION_START);
			ccr.setSourceEditPart(sourceEditPart);
			sourceEditPart.getCommand(ccr);
			ccr.setTargetEditPart(targetEditPart);
			ccr.setType(RequestConstants.REQ_CONNECTION_END);
			Command cmd = targetEditPart.getCommand(ccr);
			if (cmd != null && cmd.canExecute()) {
				executeCommand(cmd);
				IAdaptable viewAdapter = (IAdaptable) ccr.getNewObject();
				if (viewAdapter != null) {
					adapters.add(viewAdapter);
				}
			}
		}
		return adapters;
	}

	/**
	 * @generated NOT ; modified to return the correct edit part when the
	 *            element is a PersistentTable (it returned a
	 *            PersistentTable2EdirPart when the right one is
	 *            PersistentTableEditPart)
	 */
	private EditPart getEditPart(EObject domainModelElement,
			Map domain2NotationMap) {

		View view = null;

		if (domainModelElement instanceof PersistentTable) {
			view = getView(domainModelElement);
		} else {
			view = (View) domain2NotationMap.get(domainModelElement);
		}

		if (view != null) {
			return (EditPart) getHost().getViewer().getEditPartRegistry().get(
					view);
		}
		return null;
	}

	/**
	 * @NOT-generated
	 */
	private View getView(EObject eobj) {

		List views = DiagramEditPartsUtil.getEObjectViews(eobj);
		for (Object o : views) {
			if (o instanceof View) {
				View view = (View) o;
				if (view.eContainer() instanceof Diagram
						&& view.eContainer().equals(getDiagram())) {
					return view;
				}
			}
		}

		return null;
	}

	/**
	 * Allows dropping of ForeignKey elements
	 * 
	 * @NOT-generated
	 */
	@Override
	protected boolean allowDropElement(Object dropElement) {

		boolean allowDrop = super.allowDropElement(dropElement);

		if (!allowDrop) {
			if (dropElement instanceof ForeignKey)
				allowDrop = true;
		}

		return allowDrop;
	}
}
