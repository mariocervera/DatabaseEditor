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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.datatools.modelbase.sql.schema.SQLSchemaPackage;
import org.eclipse.datatools.modelbase.sql.tables.SQLTablesPackage;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.ShapeStyle;
import org.eclipse.gmf.runtime.notation.View;

import es.cv.gvcase.mdt.common.diagram.editparts.ListCompartmentEditPart;
import es.cv.gvcase.mdt.common.edit.policies.DragDropEditPolicy;
import es.cv.gvcase.mdt.common.edit.policies.DuplicateEditPolicy;
import es.cv.gvcase.mdt.common.edit.policies.ListDragDropEditPolicy;
import es.cv.gvcase.mdt.common.edit.policies.ReorderingCompartmentEditPolicy;
import es.cv.gvcase.mdt.common.edit.policies.ViewAndFeatureResolver;
import es.cv.gvcase.mdt.common.util.DiagramEditPartsUtil;
import es.cv.gvcase.mdt.db.diagram.edit.policies.ViewTableViewedTablesCanonicalEditPolicy;
import es.cv.gvcase.mdt.db.diagram.edit.policies.ViewTableViewedTablesItemSemanticEditPolicy;
import es.cv.gvcase.mdt.db.diagram.part.Messages;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry;

/**
 * @generated
 */
public class ViewTableViewedTablesEditPart extends ListCompartmentEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 5003;

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
			return null;
		}
	};

	/**
	 * @generated
	 */
	public ViewTableViewedTablesEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	protected boolean hasModelChildrenChanged(Notification evt) {
		return false;
	}

	/**
	 * @generated
	 */
	public String getCompartmentName() {
		return Messages.ViewTableViewedTablesEditPart_title;
	}

	/**
	 * @generated
	 */
	public IFigure createFigure() {
		ResizableCompartmentFigure result = (ResizableCompartmentFigure) super
				.createFigure();
		result.setTitleVisibility(false);
		return result;
	}

	/**
	 * @generated NOT ; modified to remove the Drag&Drop edit policy
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new ViewTableViewedTablesItemSemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.CREATION_ROLE,
				new CreationEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
				new ViewTableViewedTablesCanonicalEditPolicy());
	}

	/**
	 * @generated
	 */
	protected void handleNotificationEvent(Notification notification) {
		super.handleNotificationEvent(notification);
		if (notification.getNotifier() instanceof ShapeStyle) {
			// Propagate style
			for (Iterator i = getChildren().iterator(); i.hasNext();) {
				java.lang.Object obj = i.next();
				if (!(obj instanceof GraphicalEditPart))
					continue;
				GraphicalEditPart ep = (GraphicalEditPart) obj;

				if (ep.resolveSemanticElement() != resolveSemanticElement())
					continue;

				ShapeStyle style = (ShapeStyle) ((View) ep.getModel())
						.getStyle(NotationPackage.eINSTANCE.getShapeStyle());
				if (style != null) {
					style.eSet((EStructuralFeature) notification.getFeature(),
							notification.getNewValue());
					ep.refresh();
				}
			}

		}

		// mgil:: if the notification is for ADD an element and the element is
		// an Edge, don't refresh the Diagram to avoid duplicated Links
		if (notification.getEventType() == Notification.ADD
				&& SqlmodelVisualIDRegistry
						.getLinkWithClassVisualID((EObject) notification
								.getNewValue()) > -1) {
			return;
		} else {
			List<EStructuralFeature> features = new ArrayList<EStructuralFeature>();
			features.add(SQLSchemaPackage.eINSTANCE.getSchema_Tables());
			DiagramEditPartsUtil.handleNotificationForView(this, notification,
					features);
		}
		// mgil:: end
	}

	/**
	 * @generated
	 */
	protected void setRatio(Double ratio) {
		// nothing to do -- parent layout does not accept Double constraints as ratio
		// super.setRatio(ratio); 
	}

	/**
	 * @generated
	 */
	public Object getAdapter(Class adapter) {
		if (adapter != null && adapter.equals(ViewAndFeatureResolver.class)) {
			return this.resolver;
		}
		return super.getAdapter(adapter);
	}

}
