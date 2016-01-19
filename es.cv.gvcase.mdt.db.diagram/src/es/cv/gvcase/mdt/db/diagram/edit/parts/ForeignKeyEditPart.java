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

import org.eclipse.datatools.modelbase.sql.constraints.SQLConstraintsPackage;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITreeBranchEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import es.cv.gvcase.mdt.common.edit.policies.DeleteOnlyViewComponentEditPolicy;
import es.cv.gvcase.mdt.common.edit.policies.DeleteReferencedConnectionEditPolicy;
import es.cv.gvcase.mdt.common.util.DiagramEditPartsUtil;
import es.cv.gvcase.mdt.db.diagram.edit.policies.ForeignKeyItemSemanticEditPolicy;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry;

/**
 * @generated
 */
public class ForeignKeyEditPart extends
		es.cv.gvcase.mdt.common.diagram.editparts.ConnectionNodeEditPart
		implements ITreeBranchEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 3001;

	/**
	 * @generated
	 */
	public ForeignKeyEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new ForeignKeyItemSemanticEditPolicy());
		// ** install new ComponentEditPolicy
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new DeleteOnlyViewComponentEditPolicy());
		// ** install new ConnectionEditPolicy
		installEditPolicy(EditPolicy.CONNECTION_ROLE,
				new DeleteReferencedConnectionEditPolicy());
	}

	/**
	 * @generated
	 */
	protected boolean addFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof ForeignKeyNameEditPart) {
			((ForeignKeyNameEditPart) childEditPart).setLabel(getPrimaryShape()
					.getFigureForeignKeyLabelFigure());
			return true;
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected void addChildVisual(EditPart childEditPart, int index) {
		if (addFixedChild(childEditPart)) {
			return;
		}
		super.addChildVisual(childEditPart, -1);
	}

	/**
	 * @generated
	 */
	protected boolean removeFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof ForeignKeyNameEditPart) {
			return true;
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected void removeChildVisual(EditPart childEditPart) {
		if (removeFixedChild(childEditPart)) {
			return;
		}
		super.removeChildVisual(childEditPart);
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
		return new ForeignKeyFigure();
	}

	/**
	 * @generated
	 */
	public ForeignKeyFigure getPrimaryShape() {
		return (ForeignKeyFigure) getFigure();
	}

	/**
	 * @generated
	 */
	public class ForeignKeyFigure extends PolylineConnectionEx {

		/**
		 * @generated
		 */
		private WrappingLabel fFigureForeignKeyLabelFigure;

		/**
		 * @generated
		 */
		public ForeignKeyFigure() {
			this.setLineWidth(2);
			this.setForegroundColor(THIS_FORE);

			createContents();
			setTargetDecoration(createTargetDecoration());
		}

		/**
		 * @generated
		 */
		private void createContents() {

			fFigureForeignKeyLabelFigure = new WrappingLabel();
			fFigureForeignKeyLabelFigure.setText("");

			fFigureForeignKeyLabelFigure
					.setFont(FFIGUREFOREIGNKEYLABELFIGURE_FONT);

			this.add(fFigureForeignKeyLabelFigure);

		}

		/**
		 * @generated
		 */
		private RotatableDecoration createTargetDecoration() {
			PolygonDecoration df = new PolygonDecoration();
			df.setFill(true);
			return df;
		}

		/**
		 * @generated
		 */
		public WrappingLabel getFigureForeignKeyLabelFigure() {
			return fFigureForeignKeyLabelFigure;
		}

	}

	/**
	 * @generated
	 */
	static final Color THIS_FORE = new Color(null, 0, 0, 0);
	/**
	 * @generated
	 */
	static final Font FFIGUREFOREIGNKEYLABELFIGURE_FONT = new Font(Display
			.getCurrent(),
			Display.getDefault().getSystemFont().getFontData()[0].getName(), 8,
			SWT.NORMAL);

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
			features.add(SQLConstraintsPackage.eINSTANCE
					.getTableConstraint_BaseTable());
			features.add(SQLConstraintsPackage.eINSTANCE
					.getForeignKey_ReferencedTable());
			DiagramEditPartsUtil.handleNotificationForDiagram(this,
					notification, features);
		}
		// mgil:: end
	}

}
