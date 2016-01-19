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

import java.util.Iterator;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.ICompositeCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyReferenceCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

import es.cv.gvcase.mdt.common.commands.wrappers.GEFtoGMFCommandWrapper;
import es.cv.gvcase.mdt.common.util.DiagramEditPartsUtil;
import es.cv.gvcase.mdt.common.util.MultiDiagramUtil;
import es.cv.gvcase.mdt.db.diagram.edit.commands.ViewTableViewedTablesCreateCommand;
import es.cv.gvcase.mdt.db.diagram.edit.commands.ViewTableViewedTablesReorientCommand;
import es.cv.gvcase.mdt.db.diagram.edit.parts.Column2EditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.PersistentTable2EditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableViewedColumnsEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableViewedTables2EditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableViewedTablesEditPart;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry;
import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelElementTypes;

/**
 * @generated
 */
public class ViewTableItemSemanticEditPolicy extends
		SqlmodelBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	public ViewTableItemSemanticEditPolicy() {
		super(SqlmodelElementTypes.ViewTable_1002);
	}

	/**
	 * @generated NOT ; modified to prevent the removal of child nodes
	 */
	protected Command getDestroyElementCommand(DestroyElementRequest req) {
		View view = (View) getHost().getModel();
		CompositeTransactionalCommand cmd = new CompositeTransactionalCommand(
				getEditingDomain(), null);
		cmd.setTransactionNestingEnabled(false);
		for (Iterator it = view.getTargetEdges().iterator(); it.hasNext();) {
			Edge incomingLink = (Edge) it.next();
			if (SqlmodelVisualIDRegistry.getVisualID(incomingLink) == ViewTableViewedTables2EditPart.VISUAL_ID) {
				DestroyReferenceRequest r = new DestroyReferenceRequest(
						incomingLink.getSource().getElement(), null,
						incomingLink.getTarget().getElement(), false);
				cmd.add(new DestroyReferenceCommand(r));
				cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
				continue;
			}
		}
		for (Iterator it = view.getSourceEdges().iterator(); it.hasNext();) {
			Edge outgoingLink = (Edge) it.next();
			if (SqlmodelVisualIDRegistry.getVisualID(outgoingLink) == ViewTableViewedTables2EditPart.VISUAL_ID) {
				DestroyReferenceRequest r = new DestroyReferenceRequest(
						outgoingLink.getSource().getElement(), null,
						outgoingLink.getTarget().getElement(), false);
				cmd.add(new DestroyReferenceCommand(r));
				cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
				continue;
			}
		}
		EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
		if (annotation == null) {
			// there are indirectly referenced children, need extra commands:
			// true
			// fjcano :: we don't want to remove the children elements
			// addDestroyChildNodesCommand(cmd);
			addDestroyShortcutsCommand(cmd, view);
			// delete host element
			cmd.add(new DestroyElementCommand(req));
		} else {
			cmd.add(new DeleteCommand(getEditingDomain(), view));
		}
		return getGEFWrapper(cmd.reduce());
	}

	/**
	 * @generated
	 */
	private void addDestroyChildNodesCommand(ICompositeCommand cmd) {
		View view = (View) getHost().getModel();
		for (Iterator nit = view.getChildren().iterator(); nit.hasNext();) {
			Node node = (Node) nit.next();
			switch (SqlmodelVisualIDRegistry.getVisualID(node)) {
			case ViewTableViewedColumnsEditPart.VISUAL_ID:
				for (Iterator cit = node.getChildren().iterator(); cit
						.hasNext();) {
					Node cnode = (Node) cit.next();
					switch (SqlmodelVisualIDRegistry.getVisualID(cnode)) {
					case Column2EditPart.VISUAL_ID:
						cmd.add(new GEFtoGMFCommandWrapper(
								getDestroyElementCommand(cnode)));
						break;
					}
				}
				break;
			case ViewTableViewedTablesEditPart.VISUAL_ID:
				for (Iterator cit = node.getChildren().iterator(); cit
						.hasNext();) {
					Node cnode = (Node) cit.next();
					switch (SqlmodelVisualIDRegistry.getVisualID(cnode)) {
					case PersistentTable2EditPart.VISUAL_ID:
						cmd.add(new GEFtoGMFCommandWrapper(
								getDestroyElementCommand(cnode)));
						break;
					}
				}
				break;
			}
		}
	}

	/**
	 * @generated
	 */
	protected Command getCreateRelationshipCommand(CreateRelationshipRequest req) {
		Command command = req.getTarget() == null ? getStartCreateRelationshipCommand(req)
				: getCompleteCreateRelationshipCommand(req);
		return command != null ? command : super
				.getCreateRelationshipCommand(req);
	}

	/**
	 * @generated
	 */
	protected Command getStartCreateRelationshipCommand(
			CreateRelationshipRequest req) {
		if (SqlmodelElementTypes.ViewTableViewedTables_3002 == req
				.getElementType()) {
			return getGEFWrapper(new ViewTableViewedTablesCreateCommand(req,
					req.getSource(), req.getTarget()));
		}
		return null;
	}

	/**
	 * @generated
	 */
	protected Command getCompleteCreateRelationshipCommand(
			CreateRelationshipRequest req) {

		Diagram diagram = DiagramEditPartsUtil
				.findDiagramFromEditPart(getHost());
		if (diagram != null) {
			req.getParameters().put(MultiDiagramUtil.BelongToDiagramSource,
					diagram);
		}
		if (SqlmodelElementTypes.ViewTableViewedTables_3002 == req
				.getElementType()) {
			return getGEFWrapper(new ViewTableViewedTablesCreateCommand(req,
					req.getSource(), req.getTarget()));
		}
		return null;
	}

	/**
	 * Returns command to reorient EReference based link. New link target or
	 * source should be the domain model element associated with this node.
	 * 
	 * @generated
	 */
	protected Command getReorientReferenceRelationshipCommand(
			ReorientReferenceRelationshipRequest req) {
		switch (getVisualID(req)) {
		case ViewTableViewedTables2EditPart.VISUAL_ID:
			return getGEFWrapper(new ViewTableViewedTablesReorientCommand(req));
		}
		return super.getReorientReferenceRelationshipCommand(req);
	}

}
