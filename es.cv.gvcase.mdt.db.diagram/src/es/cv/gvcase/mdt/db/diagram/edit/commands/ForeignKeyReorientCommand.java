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
package es.cv.gvcase.mdt.db.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.datatools.modelbase.sql.constraints.ForeignKey;
import org.eclipse.datatools.modelbase.sql.tables.BaseTable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

import es.cv.gvcase.mdt.common.ids.ReorientLinkIDs;
import es.cv.gvcase.mdt.db.diagram.edit.policies.SqlmodelBaseItemSemanticEditPolicy;

/**
 * @generated
 */
public class ForeignKeyReorientCommand extends EditElementCommand {

	/**
	 * @generated
	 */
	private final int reorientDirection;

	/**
	 * @generated
	 */
	private final EObject oldEnd;

	/**
	 * @generated
	 */
	private final EObject newEnd;

	/**
	 * @generated
	 */
	private View node;

	/**
	 * @generated
	 */
	private View link;

	/**
	 * @generated
	 */
	public ForeignKeyReorientCommand(ReorientRelationshipRequest request) {
		super(request.getLabel(), request.getRelationship(), request);
		reorientDirection = request.getDirection();
		oldEnd = request.getOldRelationshipEnd();
		newEnd = request.getNewRelationshipEnd();

		if (request.getParameter(ReorientLinkIDs.nodeEditPart) != null) {
			node = (View) request.getParameter(ReorientLinkIDs.nodeEditPart);
		}
		if (request.getParameter(ReorientLinkIDs.linkEditPart) != null) {
			link = (View) request.getParameter(ReorientLinkIDs.linkEditPart);
		}
	}

	/**
	 * @generated
	 */
	public boolean canExecute() {
		if (false == getElementToEdit() instanceof ForeignKey) {
			return false;
		}
		if (reorientDirection == ReorientRelationshipRequest.REORIENT_SOURCE) {
			return canReorientSource();
		}
		if (reorientDirection == ReorientRelationshipRequest.REORIENT_TARGET) {
			return canReorientTarget();
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected boolean canReorientSource() {
		if (!(oldEnd instanceof BaseTable && newEnd instanceof BaseTable)) {
			return false;
		}
		BaseTable target = getLink().getReferencedTable();
		if (!(getLink().eContainer() instanceof BaseTable)) {
			return false;
		}
		BaseTable container = (BaseTable) getLink().eContainer();
		return SqlmodelBaseItemSemanticEditPolicy.LinkConstraints
				.canExistForeignKey_3001(container, getNewSource(), target);
	}

	/**
	 * @generated
	 */
	protected boolean canReorientTarget() {
		if (!(oldEnd instanceof BaseTable && newEnd instanceof BaseTable)) {
			return false;
		}
		BaseTable source = getLink().getBaseTable();
		if (!(getLink().eContainer() instanceof BaseTable)) {
			return false;
		}
		BaseTable container = (BaseTable) getLink().eContainer();
		return SqlmodelBaseItemSemanticEditPolicy.LinkConstraints
				.canExistForeignKey_3001(container, source, getNewTarget());
	}

	/**
	 * @generated
	 */
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
			IAdaptable info) throws ExecutionException {
		if (!canExecute()) {
			throw new ExecutionException(
					"Invalid arguments in reorient link command"); //$NON-NLS-1$
		}
		if (reorientDirection == ReorientRelationshipRequest.REORIENT_SOURCE) {
			return reorientSource();
		}
		if (reorientDirection == ReorientRelationshipRequest.REORIENT_TARGET) {
			return reorientTarget();
		}
		throw new IllegalStateException();
	}

	/**
	 * @generated
	 */
	protected CommandResult reorientSource() throws ExecutionException {
		getLink().setBaseTable(getNewSource());
		if (link != null && node != null) {
			((Edge) link).setSource(node);
		}
		return CommandResult.newOKCommandResult(getLink());
	}

	/**
	 * @generated
	 */
	protected CommandResult reorientTarget() throws ExecutionException {
		getLink().setReferencedTable(getNewTarget());
		if (link != null && node != null) {
			((Edge) link).setTarget(node);
		}
		return CommandResult.newOKCommandResult(getLink());
	}

	/**
	 * @generated
	 */
	protected ForeignKey getLink() {
		return (ForeignKey) getElementToEdit();
	}

	/**
	 * @generated
	 */
	protected BaseTable getOldSource() {
		return (BaseTable) oldEnd;
	}

	/**
	 * @generated
	 */
	protected BaseTable getNewSource() {
		return (BaseTable) newEnd;
	}

	/**
	 * @generated
	 */
	protected BaseTable getOldTarget() {
		return (BaseTable) oldEnd;
	}

	/**
	 * @generated
	 */
	protected BaseTable getNewTarget() {
		return (BaseTable) newEnd;
	}
}
