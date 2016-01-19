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
import java.util.List;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITreeBranchEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;

import es.cv.gvcase.mdt.common.edit.policies.DeleteOnlyViewComponentEditPolicy;
import es.cv.gvcase.mdt.common.edit.policies.DeleteReferencedConnectionEditPolicy;
import es.cv.gvcase.mdt.common.util.DiagramEditPartsUtil;
import es.cv.gvcase.mdt.db.diagram.edit.policies.ViewTableViewedTables2ItemSemanticEditPolicy;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry;

/**
 * @generated
 */
public class ViewTableViewedTables2EditPart extends
		es.cv.gvcase.mdt.common.diagram.editparts.ConnectionNodeEditPart
		implements ITreeBranchEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 3002;

	/**
	 * @generated
	 */
	public ViewTableViewedTables2EditPart(View view) {
		super(view);
	}

	/**
	 * @generated NOT
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new ViewTableViewedTables2ItemSemanticEditPolicy());
	}

	/**
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model
	 * so you may safely remove <i>generated</i> tag and modify it.
	 * 
	 * @generated
	 */

	protected Connection createConnectionFigure() {
		return new ViewedTableFigure();
	}

	/**
	 * @generated
	 */
	public ViewedTableFigure getPrimaryShape() {
		return (ViewedTableFigure) getFigure();
	}

	/**
	 * @generated
	 */
	public class ViewedTableFigure extends PolylineConnectionEx {

		/**
		 * @generated
		 */
		public ViewedTableFigure() {
			this.setLineWidth(2);
			this.setLineStyle(Graphics.LINE_DASH);
			this.setForegroundColor(THIS_FORE);

			setTargetDecoration(createTargetDecoration());
		}

		/**
		 * @generated
		 */
		private RotatableDecoration createTargetDecoration() {
			PolygonDecoration df = new PolygonDecoration();
			df.setFill(true);
			return df;
		}

	}

	/**
	 * @generated
	 */
	static final Color THIS_FORE = new Color(null, 0, 0, 0);

	/**
	 * @generated
	 */
	protected void handleNotificationEvent(Notification notification) {
		super.handleNotificationEvent(notification);

		// mgil:: if the notification is for ADD an element and the element is
		// an Edge, don't refresh the Diagram to avoid duplicated Links
		if (notification.getEventType() == Notification.ADD
				&& SqlmodelVisualIDRegistry
						.getLinkWithClassVisualID((EObject) notification
								.getNewValue()) > -1) {
			return;
		} else {
			List<EStructuralFeature> features = new ArrayList<EStructuralFeature>();

			// no target or source features found
			DiagramEditPartsUtil.handleNotificationForDiagram(this,
					notification, features);
		}
		// mgil:: end
	}

}
