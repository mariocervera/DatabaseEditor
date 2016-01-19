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
import java.util.List;

import org.eclipse.datatools.modelbase.sql.accesscontrol.AuthorizationIdentifier;
import org.eclipse.datatools.modelbase.sql.accesscontrol.Privilege;
import org.eclipse.datatools.modelbase.sql.constraints.Index;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.tables.PersistentTable;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.ICompositeCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyReferenceCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

import es.cv.gvcase.mdt.common.commands.wrappers.EMFtoGMFCommandWrapper;
import es.cv.gvcase.mdt.common.commands.wrappers.GEFtoGMFCommandWrapper;
import es.cv.gvcase.mdt.common.ids.ReorientLinkIDs;
import es.cv.gvcase.mdt.common.util.DiagramEditPartsUtil;
import es.cv.gvcase.mdt.common.util.MultiDiagramUtil;
import es.cv.gvcase.mdt.db.diagram.edit.commands.ForeignKeyCreateCommand;
import es.cv.gvcase.mdt.db.diagram.edit.commands.ForeignKeyReorientCommand;
import es.cv.gvcase.mdt.db.diagram.edit.commands.ViewTableViewedTablesCreateCommand;
import es.cv.gvcase.mdt.db.diagram.edit.commands.ViewTableViewedTablesReorientCommand;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ColumnEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ForeignKeyEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.PersistentTablePersistentColumnTableCompartmentEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableViewedTables2EditPart;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry;
import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelElementTypes;

/**
 * @generated
 */
public class PersistentTableItemSemanticEditPolicy extends
		SqlmodelBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	public PersistentTableItemSemanticEditPolicy() {
		super(SqlmodelElementTypes.PersistentTable_1001);
	}

	/**
	 * @generated NOT ; modified to add the removal of indices and privileges
	 */
	protected Command getDestroyElementCommand(DestroyElementRequest req) {
		View view = (View) getHost().getModel();
		CompositeTransactionalCommand cmd = new CompositeTransactionalCommand(
				getEditingDomain(), null);
		cmd.setTransactionNestingEnabled(false);
		for (Iterator it = view.getTargetEdges().iterator(); it.hasNext();) {
			Edge incomingLink = (Edge) it.next();
			if (SqlmodelVisualIDRegistry.getVisualID(incomingLink) == ForeignKeyEditPart.VISUAL_ID) {
				DestroyElementRequest r = new DestroyElementRequest(
						incomingLink.getElement(), false);
				cmd.add(new DestroyElementCommand(r));
				cmd
						.add(new org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand(
								getEditingDomain(), incomingLink));
				continue;
			}
			if (SqlmodelVisualIDRegistry.getVisualID(incomingLink) == ViewTableViewedTables2EditPart.VISUAL_ID) {
				DestroyReferenceRequest r = new DestroyReferenceRequest(
						incomingLink.getSource().getElement(), null,
						incomingLink.getTarget().getElement(), false);
				cmd.add(new DestroyReferenceCommand(r));
				cmd
						.add(new org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand(
								getEditingDomain(), incomingLink));
				continue;
			}
		}
		for (Iterator it = view.getSourceEdges().iterator(); it.hasNext();) {
			Edge outgoingLink = (Edge) it.next();
			if (SqlmodelVisualIDRegistry.getVisualID(outgoingLink) == ForeignKeyEditPart.VISUAL_ID) {
				DestroyElementRequest r = new DestroyElementRequest(
						outgoingLink.getElement(), false);
				cmd.add(new DestroyElementCommand(r));
				cmd
						.add(new org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand(
								getEditingDomain(), outgoingLink));
				continue;
			}
		}

		// fjcano :: remove indices and privileges associated to this table
		addDestroyIndexesCommand(cmd);
		addDestroyPrivilegesCommand(cmd);

		EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
		if (annotation == null) {
			// there are indirectly referenced children, need extra commands:
			// false
			addDestroyChildNodesCommand(cmd);
			addDestroyShortcutsCommand(cmd, view);
			// delete host element
			cmd.add(new DestroyElementCommand(req));
		} else {
			cmd
					.add(new org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand(
							getEditingDomain(), view));
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
			case PersistentTablePersistentColumnTableCompartmentEditPart.VISUAL_ID:
				for (Iterator cit = node.getChildren().iterator(); cit
						.hasNext();) {
					Node cnode = (Node) cit.next();
					switch (SqlmodelVisualIDRegistry.getVisualID(cnode)) {
					case ColumnEditPart.VISUAL_ID:
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
		if (SqlmodelElementTypes.ForeignKey_3001 == req.getElementType()) {
			return getGEFWrapper(new ForeignKeyCreateCommand(req, req
					.getSource(), req.getTarget()));
		}
		if (SqlmodelElementTypes.ViewTableViewedTables_3002 == req
				.getElementType()) {
			return null;
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
		if (SqlmodelElementTypes.ForeignKey_3001 == req.getElementType()) {
			return getGEFWrapper(new ForeignKeyCreateCommand(req, req
					.getSource(), req.getTarget()));
		}
		if (SqlmodelElementTypes.ViewTableViewedTables_3002 == req
				.getElementType()) {
			return getGEFWrapper(new ViewTableViewedTablesCreateCommand(req,
					req.getSource(), req.getTarget()));
		}
		return null;
	}

	/**
	 * Returns command to reorient EClass based link. New link target or source
	 * should be the domain model element associated with this node.
	 * 
	 * @generated
	 */
	protected Command getReorientRelationshipCommand(
			ReorientRelationshipRequest req) {

		// add the view element
		req.setParameter(ReorientLinkIDs.nodeEditPart,
				((IGraphicalEditPart) this.getHost()).getNotationView());
		// add the view link
		List list = DiagramEditPartsUtil.getEObjectViews(req.getRelationship());
		if (list.size() > 0) {
			req.setParameter(ReorientLinkIDs.linkEditPart, list.get(0));
		}

		switch (getVisualID(req)) {
		case ForeignKeyEditPart.VISUAL_ID:
			return getGEFWrapper(new ForeignKeyReorientCommand(req));
		}
		return super.getReorientRelationshipCommand(req);
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

	/**
	 * Adds a Command to the CompositeTransactionalCommand that removes the
	 * indices associated to the host PersistentTable
	 * 
	 * @NOT-generated
	 */
	private void addDestroyIndexesCommand(CompositeTransactionalCommand cmd) {

		// This method adds the commands needed to remove the indexes associated
		// to the table

		EObject eobject = ((View) getHost().getModel()).getElement();
		if (eobject instanceof PersistentTable) {
			PersistentTable table = (PersistentTable) eobject;
			TransactionalEditingDomain domain = TransactionUtil
					.getEditingDomain(table);
			Schema schema = (Schema) table.eContainer();
			for (Index index : schema.getIndices()) {
				if (index.getTable() != null && index.getTable().equals(table)) {
					cmd.add(new EMFtoGMFCommandWrapper(DeleteCommand.create(
							domain, index)));
				}
			}
		}

	}

	/**
	 * Adds a Command to the ComopsiteTransactionalCommand that removes the
	 * Privileges associated to the host PersistentTable
	 * 
	 * @NOT-generated
	 */
	private void addDestroyPrivilegesCommand(CompositeTransactionalCommand cmd) {

		// This method adds the commands needed to remove the privileges
		// associated to the table

		EObject eobject = ((View) getHost().getModel()).getElement();
		if (eobject instanceof PersistentTable) {
			PersistentTable table = (PersistentTable) eobject;
			TransactionalEditingDomain domain = TransactionUtil
					.getEditingDomain(table);
			Schema schema = (Schema) table.eContainer();
			Database db = (Database) schema.eContainer();
			for (AuthorizationIdentifier aid : db.getAuthorizationIds()) {
				for (Privilege p : aid.getReceivedPrivilege()) {
					if (p.getObject() != null && p.getObject().equals(table)) {
						cmd.add(new EMFtoGMFCommandWrapper(DeleteCommand
								.create(domain, p)));
					}
				}
			}
		}

	}

}
