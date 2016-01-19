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
package es.cv.gvcase.mdt.db.diagram.edit.parts;

import org.eclipse.datatools.modelbase.sql.constraints.Index;
import org.eclipse.datatools.modelbase.sql.schema.SQLSchemaPackage;
import org.eclipse.datatools.modelbase.sql.tables.SQLTablesPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.View;

import es.cv.gvcase.mdt.common.edit.policies.DiagramDragDropEditPolicy;
import es.cv.gvcase.mdt.common.edit.policies.ViewAndFeatureResolver;
import es.cv.gvcase.mdt.common.provider.IDiagramLinksViewInfo;
import es.cv.gvcase.mdt.common.provider.ViewInfo;
import es.cv.gvcase.mdt.common.util.DiagramEditPartsUtil;
import es.cv.gvcase.mdt.common.util.MDTUtil;
import es.cv.gvcase.mdt.db.diagram.edit.policies.SchemaCanonicalEditPolicy;
import es.cv.gvcase.mdt.db.diagram.edit.policies.SchemaItemSemanticEditPolicy;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelDiagramUpdater;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry;

/**
 * @generated
 */
public class SchemaEditPart extends
		es.cv.gvcase.mdt.common.diagram.editparts.DiagramEditPart {

	/**
	 * @generated
	 */
	public final static String MODEL_ID = "Sqlmodel"; //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 1000;

	/**
	 * @generated
	 */
	private ViewAndFeatureResolver resolver = new ViewAndFeatureResolver() {

		public boolean isEObjectNode(EObject element) {
			if (SqlmodelVisualIDRegistry.getNodeVisualID(getNotationView(),
					element) > -1) {
				return true;
			}
			return false;
		}

		public boolean isEObjectLink(EObject element) {
			if (SqlmodelVisualIDRegistry.getLinkWithClassVisualID(element) > -1) {
				return true;
			}
			return false;
		}

		public int getEObjectSemanticHint(EObject element) {
			if (element != null) {
				return SqlmodelVisualIDRegistry.getNodeVisualID(
						getNotationView(), element);
			}
			return -1;
		}

		public EStructuralFeature getEStructuralFeatureForEClass(EClass class1) {
			if (SQLTablesPackage.eINSTANCE.getPersistentTable().equals(class1)) {
				return SQLSchemaPackage.eINSTANCE.getSchema_Tables();
			}
			if (SQLTablesPackage.eINSTANCE.getViewTable().equals(class1)) {
				return SQLSchemaPackage.eINSTANCE.getSchema_Tables();
			}
			return null;
		}
	};

	/**
	 * @generated
	 */
	public Object getAdapter(Class adapter) {
		if (adapter != null && adapter.equals(ViewAndFeatureResolver.class)) {
			return this.resolver;
		}

		if (adapter != null && adapter.equals(ViewInfo.class)) {
			return SqlmodelVisualIDRegistry.getDiagramViewInfo();
		}
		if (IDiagramLinksViewInfo.class.equals(adapter)) {
			return SqlmodelDiagramUpdater.getDiagramLinksViewInfo();
		}
		return super.getAdapter(adapter);
	}

	/**
	 * @generated
	 */
	public SchemaEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new SchemaItemSemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
				new SchemaCanonicalEditPolicy());

		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE,
				new DiagramDragDropEditPolicy(resolver));

		// removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.POPUPBAR_ROLE);
	}

	/**
	 * @generated NOT ; modified to react upon the addition of an Index to set
	 *            its initial attributes
	 */
	protected void handleNotificationEvent(Notification event) {

		super.handleNotificationEvent(event);
		if (event.getNotifier() instanceof EAnnotation) {
			EAnnotation eAnnotation = (EAnnotation) event.getNotifier();
			if (eAnnotation.getSource() != null
					&& eAnnotation.getSource().equals(
							MDTUtil.FilterViewAndLabelsSource)) {
				DiagramEditPartsUtil.updateDiagram(this);
			}
		}

		if (event.getEventType() == Notification.ADD) {

			// If a new index is being added ...

			if (event.getNewValue() instanceof Index) {
				Index index = (Index) event.getNewValue();
				EAnnotation annotation = EcoreFactory.eINSTANCE
						.createEAnnotation();
				annotation.setSource("AdditionalAttributes");
				annotation.getDetails().put("Expression", "");
				index.getEAnnotations().add(annotation);
			}
		}
	}

}
