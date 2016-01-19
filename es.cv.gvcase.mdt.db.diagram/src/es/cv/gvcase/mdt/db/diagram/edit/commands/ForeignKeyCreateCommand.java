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
import org.eclipse.datatools.modelbase.sql.constraints.PrimaryKey;
import org.eclipse.datatools.modelbase.sql.constraints.SQLConstraintsFactory;
import org.eclipse.datatools.modelbase.sql.tables.BaseTable;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Diagram;

import es.cv.gvcase.mdt.common.util.MDTUtil;
import es.cv.gvcase.mdt.common.util.MultiDiagramUtil;
import es.cv.gvcase.mdt.db.diagram.edit.policies.SqlmodelBaseItemSemanticEditPolicy;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelDiagramEditorPlugin;
import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelElementTypes;

/**
 * @generated
 */
public class ForeignKeyCreateCommand extends EditElementCommand {

	/**
	 * @generated
	 */
	private final EObject source;

	/**
	 * @generated
	 */
	private final EObject target;

	/**
	 * @generated
	 */
	private final BaseTable container;

	/**
	 * @generated
	 */
	public ForeignKeyCreateCommand(CreateRelationshipRequest request,
			EObject source, EObject target) {
		super(request.getLabel(), null, request);
		this.source = source;
		this.target = target;
		container = deduceContainer(source, target);
	}

	/**
	 * @generated
	 */
	public boolean canExecute() {
		if (source == null && target == null) {
			return false;
		}
		if (source != null && false == source instanceof BaseTable) {
			return false;
		}
		if (target != null && false == target instanceof BaseTable) {
			return false;
		}
		if (getSource() == null) {
			return true; // link creation is in progress; source is not defined
			// yet
		}
		// target may be null here but it's possible to check constraint
		if (getContainer() == null) {
			return false;
		}
		return SqlmodelBaseItemSemanticEditPolicy.LinkConstraints
				.canCreateForeignKey_3001(getContainer(), getSource(),
						getTarget());
	}

	/**
	 * @generated
	 */
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
			IAdaptable info) throws ExecutionException {
		if (!canExecute()) {
			throw new ExecutionException(
					"Invalid arguments in create link command"); //$NON-NLS-1$
		}

		ForeignKey newElement = SQLConstraintsFactory.eINSTANCE
				.createForeignKey();
		getContainer().getConstraints().add(newElement);
		newElement.setBaseTable(getSource());
		newElement.setReferencedTable(getTarget());
		SqlmodelElementTypes.init_ForeignKey_3001(newElement);
		doConfigure(newElement, monitor, info);
		((CreateElementRequest) getRequest()).setNewElement(newElement);
		return CommandResult.newOKCommandResult(newElement);

	}

	/**
	 * @generated NOT
	 */
	protected void doConfigure(ForeignKey newElement, IProgressMonitor monitor,
			IAdaptable info) throws ExecutionException {
		// Add the Foreign Key Columns of the Target Table, as member elements
		// into the Referenced Columns association of the ForeignKey element
		BaseTable bt = newElement.getReferencedTable();
		PrimaryKey pk = bt.getPrimaryKey();
		if (pk != null) {
			for (Column c : pk.getMembers()) {
				newElement.getReferencedMembers().add(c);
			}
		}

		// add eObject eAnnotation reference to the diagram

		Diagram diagram = MDTUtil.getDiagramFromRequest(getRequest());
		if (diagram != null) {
			MultiDiagramUtil.AddEAnnotationReferenceToDiagram(diagram,
					newElement);
		} else {
			MultiDiagramUtil.addEAnnotationReferenceToDiagram(
					SqlmodelDiagramEditorPlugin.getInstance(), newElement);
		}

		IElementType elementType = ((CreateElementRequest) getRequest())
				.getElementType();
		ConfigureRequest configureRequest = new ConfigureRequest(
				getEditingDomain(), newElement, elementType);
		configureRequest.setClientContext(((CreateElementRequest) getRequest())
				.getClientContext());
		configureRequest.addParameters(getRequest().getParameters());
		configureRequest.setParameter(CreateRelationshipRequest.SOURCE,
				getSource());
		configureRequest.setParameter(CreateRelationshipRequest.TARGET,
				getTarget());
		ICommand configureCommand = elementType
				.getEditCommand(configureRequest);
		if (configureCommand != null && configureCommand.canExecute()) {
			configureCommand.execute(monitor, info);
		}
	}

	/**
	 * @generated
	 */
	protected void setElementToEdit(EObject element) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @generated
	 */
	protected BaseTable getSource() {
		return (BaseTable) source;
	}

	/**
	 * @generated
	 */
	protected BaseTable getTarget() {
		return (BaseTable) target;
	}

	/**
	 * @generated
	 */
	public BaseTable getContainer() {
		return container;
	}

	/**
	 * Default approach is to traverse ancestors of the source to find instance
	 * of container. Modify with appropriate logic.
	 * 
	 * @generated
	 */
	private static BaseTable deduceContainer(EObject source, EObject target) {
		// Find container element for the new link.
		// Climb up by containment hierarchy starting from the source
		// and return the first element that is instance of the container class.
		for (EObject element = source; element != null; element = element
				.eContainer()) {
			if (element instanceof BaseTable) {
				return (BaseTable) element;
			}
		}
		return null;
	}
}
