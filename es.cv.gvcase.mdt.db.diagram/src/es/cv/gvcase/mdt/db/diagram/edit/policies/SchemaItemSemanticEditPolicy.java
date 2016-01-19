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

import org.eclipse.datatools.modelbase.sql.schema.SQLSchemaPackage;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.schema.util.SQLSchemaSwitch;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;
import org.eclipse.gmf.runtime.notation.Diagram;

import es.cv.gvcase.mdt.common.commands.DuplicateAnythingCommand;
import es.cv.gvcase.mdt.common.part.DiagramCanvasSwitch;
import es.cv.gvcase.mdt.common.util.BasicEcoreSwitch;
import es.cv.gvcase.mdt.common.util.DiagramEditPartsUtil;
import es.cv.gvcase.mdt.common.util.MultiDiagramUtil;
import es.cv.gvcase.mdt.db.diagram.edit.commands.PersistentTableCreateCommand;
import es.cv.gvcase.mdt.db.diagram.edit.commands.ViewTableCreateCommand;
import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelElementTypes;

/**
 * @generated
 */
public class SchemaItemSemanticEditPolicy extends
		SqlmodelBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	public SchemaItemSemanticEditPolicy() {
		super(SqlmodelElementTypes.Schema_1000);
	}

	/**
	 * @generated
	 */
	static class EReferenceDiagramSwitch extends
			BasicEcoreSwitch<EReference, IElementType> {
		@Override
		public EReference doSwitch(EObject modelElement) {

			final IElementType type = getInfo();
			if (type == null) {
				return null;
			}
			SQLSchemaSwitch<EReference> aSwitch = new SQLSchemaSwitch<EReference>() {
				@Override
				public EReference caseSchema(Schema modelElement) {
					EReference reference = null;
					if (type.equals(SqlmodelElementTypes.PersistentTable_1001)) {
						reference = SQLSchemaPackage.eINSTANCE
								.getSchema_Tables();
					}
					if (type.equals(SqlmodelElementTypes.ViewTable_1002)) {
						reference = SQLSchemaPackage.eINSTANCE
								.getSchema_Tables();
					}
					return reference;
				}
			};

			return aSwitch.doSwitch(modelElement);
		}
	}

	/**
	 * @generated
	 */
	private EReferenceDiagramSwitch aSwitch = new EReferenceDiagramSwitch();

	/**
	 * @generated
	 */
	protected Command getCreateCommand(CreateElementRequest req) {

		Diagram diagram = DiagramEditPartsUtil
				.findDiagramFromEditPart(getHost());
		if (diagram != null) {
			req.getParameters().put(MultiDiagramUtil.BelongToDiagramSource,
					diagram);
		}

		EObject canvasElement = DiagramCanvasSwitch.getCanvasElement(this);
		if (canvasElement == null) {
			return null;
		}
		aSwitch.setInfo(req.getElementType());
		req.setContainmentFeature(aSwitch.doSwitch(canvasElement));
		if (SqlmodelElementTypes.PersistentTable_1001 == req.getElementType()) {
			return getGEFWrapper(PersistentTableCreateCommand.create(req,
					canvasElement));
		}
		if (SqlmodelElementTypes.ViewTable_1002 == req.getElementType()) {
			return getGEFWrapper(ViewTableCreateCommand.create(req,
					canvasElement));
		}
		return super.getCreateCommand(req);
	}

	/**
	 * @generated
	 */
	protected Command getDuplicateCommand(DuplicateElementsRequest req) {
		TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
				.getEditingDomain();
		Diagram currentDiagram = null;
		if (getHost() instanceof IGraphicalEditPart) {
			currentDiagram = ((IGraphicalEditPart) getHost()).getNotationView()
					.getDiagram();
		}
		return getGEFWrapper(new DuplicateAnythingCommand(editingDomain, req,
				currentDiagram));
	}

}
