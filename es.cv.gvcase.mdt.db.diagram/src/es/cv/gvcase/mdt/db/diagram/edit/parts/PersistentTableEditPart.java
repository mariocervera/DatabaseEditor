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

import org.eclipse.datatools.modelbase.sql.constraints.ForeignKey;
import org.eclipse.datatools.modelbase.sql.tables.SQLTablesPackage;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConstrainedToolbarLayoutEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.ShapeStyle;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import es.cv.gvcase.mdt.common.commands.AnnotateNodeStyleCommand;
import es.cv.gvcase.mdt.common.diagram.editparts.ShapeNodeEditPart;
import es.cv.gvcase.mdt.common.edit.policies.DeleteOnlyViewComponentEditPolicy;
import es.cv.gvcase.mdt.common.util.DiagramEditPartsUtil;
import es.cv.gvcase.mdt.db.diagram.edit.policies.PersistentTableItemSemanticEditPolicy;
import es.cv.gvcase.mdt.db.diagram.edit.policies.SqlmodelTextSelectionEditPolicy;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry;
import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelElementTypes;

/**
 * @generated
 */
public class PersistentTableEditPart extends ShapeNodeEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 1001;

	/**
	 * @generated
	 */
	protected IFigure contentPane;

	/**
	 * @generated
	 */
	protected IFigure primaryShape;

	/**
	 * @generated
	 */
	public PersistentTableEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies() {
		installEditPolicy(EditPolicyRoles.CREATION_ROLE,
				new CreationEditPolicy());
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new PersistentTableItemSemanticEditPolicy());
		// ** install new ComponentEditPolicy
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new DeleteOnlyViewComponentEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
		// XXX need an SCR to runtime to have another abstract superclass that would let children add reasonable editpolicies
		// removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.CONNECTION_HANDLES_ROLE);
	}

	/**
	 * @generated
	 */
	protected LayoutEditPolicy createLayoutEditPolicy() {

		ConstrainedToolbarLayoutEditPolicy lep = new ConstrainedToolbarLayoutEditPolicy() {

			protected EditPolicy createChildEditPolicy(EditPart child) {
				if (child.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE) == null) {
					if (child instanceof ITextAwareEditPart) {
						return new SqlmodelTextSelectionEditPolicy();
					}
				}
				return super.createChildEditPolicy(child);
			}
		};
		return lep;
	}

	/**
	 * @generated
	 */
	protected IFigure createNodeShape() {
		PersistentTableFigure figure = new PersistentTableFigure();
		return primaryShape = figure;
	}

	/**
	 * @generated
	 */
	public PersistentTableFigure getPrimaryShape() {
		return (PersistentTableFigure) primaryShape;
	}

	/**
	 * @generated
	 */
	protected boolean addFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof PersistentTableNameEditPart) {
			((PersistentTableNameEditPart) childEditPart)
					.setLabel(getPrimaryShape()
							.getFigurePersistentTableNameFigure());
			return true;
		}
		if (childEditPart instanceof PersistentTablePersistentColumnTableCompartmentEditPart) {
			IFigure pane = getPrimaryShape()
					.getFigurePersistentColumnTableFigure();
			setupContentPane(pane); // FIXME each comparment should handle his content pane in his own way 
			pane
					.add(((PersistentTablePersistentColumnTableCompartmentEditPart) childEditPart)
							.getFigure());
			return true;
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected boolean removeFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof PersistentTableNameEditPart) {
			return true;
		}
		if (childEditPart instanceof PersistentTablePersistentColumnTableCompartmentEditPart) {
			IFigure pane = getPrimaryShape()
					.getFigurePersistentColumnTableFigure();
			setupContentPane(pane); // FIXME each comparment should handle his content pane in his own way 
			pane
					.remove(((PersistentTablePersistentColumnTableCompartmentEditPart) childEditPart)
							.getFigure());
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
	protected void removeChildVisual(EditPart childEditPart) {
		if (removeFixedChild(childEditPart)) {
			return;
		}
		super.removeChildVisual(childEditPart);
	}

	/**
	 * @generated
	 */
	protected IFigure getContentPaneFor(IGraphicalEditPart editPart) {
		if (editPart instanceof PersistentTablePersistentColumnTableCompartmentEditPart) {
			return getPrimaryShape().getFigurePersistentColumnTableFigure();
		}
		return getContentPane();
	}

	/**
	 * @generated
	 */
	protected NodeFigure createNodePlate() {
		DefaultSizeNodeFigure result = new DefaultSizeNodeFigure(150, 70);
		return result;
	}

	/**
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model so
	 * you may safely remove <i>generated</i> tag and modify it.
	 * 
	 * @generated
	 */
	protected NodeFigure createNodeFigure() {
		NodeFigure figure = createNodePlate();
		figure.setLayoutManager(new StackLayout());
		IFigure shape = createNodeShape();
		figure.add(shape);
		contentPane = setupContentPane(shape);
		return figure;
	}

	/**
	 * Default implementation treats passed figure as content pane. Respects
	 * layout one may have set for generated figure.
	 * 
	 * @param nodeShape
	 *            instance of generated figure class
	 * @generated
	 */
	protected IFigure setupContentPane(IFigure nodeShape) {
		if (nodeShape.getLayoutManager() == null) {
			ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
			layout.setSpacing(5);
			nodeShape.setLayoutManager(layout);
		}
		return nodeShape; // use nodeShape itself as contentPane
	}

	/**
	 * @generated
	 */
	public IFigure getContentPane() {
		if (contentPane != null) {
			return contentPane;
		}
		return super.getContentPane();
	}

	/**
	 * @generated
	 */
	public EditPart getPrimaryChildEditPart() {
		return getChildBySemanticHint(SqlmodelVisualIDRegistry
				.getType(PersistentTableNameEditPart.VISUAL_ID));
	}

	/**
	 * @generated
	 */
	public List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/getMARelTypesOnSource() {
		List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/types = new ArrayList/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/();
		types.add(SqlmodelElementTypes.ForeignKey_3001);
		return types;
	}

	/**
	 * @generated
	 */
	public List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/getMARelTypesOnSourceAndTarget(
			IGraphicalEditPart targetEditPart) {
		List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/types = new ArrayList/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/();
		if (targetEditPart instanceof es.cv.gvcase.mdt.db.diagram.edit.parts.PersistentTableEditPart) {
			types.add(SqlmodelElementTypes.ForeignKey_3001);
		}
		return types;
	}

	/**
	 * @generated
	 */
	public List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/getMATypesForTarget(
			IElementType relationshipType) {
		List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/types = new ArrayList/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/();
		if (relationshipType == SqlmodelElementTypes.ForeignKey_3001) {
			types.add(SqlmodelElementTypes.PersistentTable_1001);
		}
		return types;
	}

	/**
	 * @generated
	 */
	public List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/getMARelTypesOnTarget() {
		List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/types = new ArrayList/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/();
		types.add(SqlmodelElementTypes.ForeignKey_3001);
		types.add(SqlmodelElementTypes.ViewTableViewedTables_3002);
		return types;
	}

	/**
	 * @generated
	 */
	public List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/getMATypesForSource(
			IElementType relationshipType) {
		List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/types = new ArrayList/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/();
		if (relationshipType == SqlmodelElementTypes.ForeignKey_3001) {
			types.add(SqlmodelElementTypes.PersistentTable_1001);
		}
		if (relationshipType == SqlmodelElementTypes.ViewTableViewedTables_3002) {
			types.add(SqlmodelElementTypes.ViewTable_1002);
		}
		return types;
	}

	/**
	 * @generated
	 */
	public EditPart getTargetEditPart(Request request) {
		if (request instanceof CreateViewAndElementRequest) {
			CreateElementRequestAdapter adapter = ((CreateViewAndElementRequest) request)
					.getViewAndElementDescriptor()
					.getCreateElementRequestAdapter();
			IElementType type = (IElementType) adapter
					.getAdapter(IElementType.class);
			if (type == SqlmodelElementTypes.Column_2001) {
				return getChildBySemanticHint(SqlmodelVisualIDRegistry
						.getType(PersistentTablePersistentColumnTableCompartmentEditPart.VISUAL_ID));
			}
		}
		return super.getTargetEditPart(request);
	}

	/**
	 * @generated
	 */
	protected EAnnotation getAppearenceEAnnotation() {
		EAnnotation eAnn = getPrimaryView().getEAnnotation(
				AnnotateNodeStyleCommand.APPEARANCE_EANNOTATION_NAME);
		return eAnn;
	}

	/**
	 * @generated
	 */
	private List<EObject> changesFromDefaultStyle() {
		EAnnotation eAnn = getAppearenceEAnnotation();
		if (eAnn == null)
			return new ArrayList<EObject>();
		else
			return eAnn.getReferences();
	}

	/**
	 * @generated
	 */
	protected void handleNotificationEventGen(Notification notification) {
		if (notification.getNotifier() == getModel()
				&& EcorePackage.eINSTANCE.getEModelElement_EAnnotations()
						.equals(notification.getFeature())) {
			handleMajorSemanticChange();
		} else if (notification.getNotifier() instanceof ShapeStyle) {
			super.handleNotificationEvent(notification);

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

		else {
			super.handleNotificationEvent(notification);
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
			features.add(SQLTablesPackage.eINSTANCE.getBaseTable_Constraints());
			DiagramEditPartsUtil.handleNotificationForDiagram(this,
					notification, features);
		}
		// mgil:: end
	}

	/**
	 * @generated NOT
	 */
	protected void handleNotificationEvent(Notification notification) {
		handleNotificationEventGen(notification);

		// Refresh the diagram if a property of a ForeignKey has been modified
		if (notification.getOldValue() instanceof ForeignKey
				|| notification.getNewValue() instanceof ForeignKey) {
			EditPolicy ep = this.getParent().getEditPolicy(
					EditPolicyRoles.CANONICAL_ROLE);
			if (ep != null && ep instanceof CanonicalEditPolicy) {
				((CanonicalEditPolicy) ep).refresh();
			}
		}
	}

	/**
	 * @generated
	 */
	public static boolean isLabel(IFigure figure) {
		if (figure instanceof Label) {
			return true;
		}
		if (figure instanceof WrappingLabel) {
			return true;
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected boolean isFigureFromChildEditPart(IFigure figure) {
		for (Iterator i = getChildren().iterator(); i.hasNext();) {
			java.lang.Object obj = i.next();
			if (obj instanceof AbstractGraphicalEditPart) {
				AbstractGraphicalEditPart gEP = (AbstractGraphicalEditPart) obj;
				if (gEP.getFigure() == figure) {
					// Check if semantic elements are different
					if (gEP instanceof GraphicalEditPart
							&& ((GraphicalEditPart) gEP)
									.resolveSemanticElement() == resolveSemanticElement()) {
						return false;
					}
					return true;
				} else {
					// Check if it is a child figure of the editpart
					for (java.lang.Object child : gEP.getChildren()) {
						if (child instanceof GraphicalEditPart) {
							GraphicalEditPart childEP = (GraphicalEditPart) child;
							if (childEP.getFigure() == figure) {
								if (childEP.resolveSemanticElement() != resolveSemanticElement())
									return true;
								else
									return false;
							}
						}
					}
				} // end else
			}
		}
		return false;
	}

	/**
	 * @generated
	 */
	@Override
	protected void setBackgroundColor(Color color) {
		// Only update if the Node doesn't have the default style
		if (changesFromDefaultStyle().contains(
				NotationPackage.Literals.FILL_STYLE__FILL_COLOR)) {
			setOwnedFiguresBackgroundColor(getFigure(), color);
		} else
			super.setBackgroundColor(color);

	}

	/**
	 * @generated
	 */
	public void setOwnedFiguresBackgroundColor(IFigure parent, Color color) {
		parent.setBackgroundColor(color);
		for (Iterator i = parent.getChildren().iterator(); i.hasNext();) {
			Object obj = i.next();
			if (obj instanceof IFigure
					&& !isFigureFromChildEditPart((IFigure) obj)) {
				setOwnedFiguresBackgroundColor((IFigure) obj, color);
			}
		}
	}

	/**
	 * @generated
	 */
	protected void setLineWidth(int width) {
		if (primaryShape instanceof Shape) {
			((Shape) primaryShape).setLineWidth(width);
		}
	}

	/**
	 * @generated
	 */
	protected void setLineType(int style) {
		if (primaryShape instanceof Shape) {
			((Shape) primaryShape).setLineStyle(style);
		}
	}

	/**
	 * @generated
	 */
	@Override
	protected void setForegroundColor(Color color) {
		// Only update if the Node doesn't have the default style
		if (changesFromDefaultStyle().contains(
				NotationPackage.Literals.LINE_STYLE__LINE_COLOR)) {
			setOwnedFiguresForegroundColor(getFigure(), color);
		} else
			super.setForegroundColor(color);

	}

	/**
	 * @generated
	 */
	public void setOwnedFiguresForegroundColor(IFigure parent, Color color) {
		if (!isLabel(parent))
			parent.setForegroundColor(color);
		for (Iterator i = parent.getChildren().iterator(); i.hasNext();) {
			java.lang.Object obj = i.next();
			if (obj instanceof IFigure && !isLabel((IFigure) obj)
					&& !isFigureFromChildEditPart((IFigure) obj)) {
				setOwnedFiguresForegroundColor((IFigure) obj, color);
			}
		}
	}

	/**
	 * @generated
	 */

	@Override
	protected void setFontColor(Color color) {
		// Only update if the Node doesn't have the default style
		if (changesFromDefaultStyle().contains(
				NotationPackage.Literals.LINE_STYLE__LINE_COLOR)) {
			setOwnedFiguresFontColor(getFigure(), color);
		} else
			super.setFontColor(color);

	}

	/**
	 * @generated
	 */
	public void setOwnedFiguresFontColor(IFigure parent, Color color) {
		if (isLabel(parent))
			parent.setForegroundColor(color);
		for (Iterator i = parent.getChildren().iterator(); i.hasNext();) {
			Object obj = i.next();
			if (obj instanceof IFigure && isLabel((IFigure) obj)
					&& !isFigureFromChildEditPart((IFigure) obj)) {
				setOwnedFiguresFontColor((IFigure) obj, color);
			}
		}
	}

	/**
	 * @generated
	 */
	@Override
	public EditPartViewer getViewer() {
		if (getRoot() != null) {
			return getRoot().getViewer();
		} else {
			return DiagramEditPartsUtil.getDiagramEditPart().getViewer();
		}
	}

	/**
	 * @generated
	 */
	public static Color getDeclaredBackgroundColor() {
		return THIS_BACK;
	}

	/**
	 * @generated
	 */
	public class PersistentTableFigure extends RectangleFigure {

		/**
		 * @generated
		 */
		private WrappingLabel fFigurePersistentTableNameFigure;
		/**
		 * @generated
		 */
		private RectangleFigure fFigurePersistentColumnTableFigure;

		/**
		 * @generated
		 */
		public PersistentTableFigure() {

			ToolbarLayout layoutThis = new ToolbarLayout();
			layoutThis.setStretchMinorAxis(true);
			layoutThis.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);

			layoutThis.setSpacing(0);
			layoutThis.setVertical(true);

			this.setLayoutManager(layoutThis);

			this.setForegroundColor(ColorConstants.black);
			this.setBackgroundColor(THIS_BACK);
			this.setMinimumSize(new Dimension(getMapMode().DPtoLP(160),
					getMapMode().DPtoLP(75)));
			this.setBorder(new LineBorder(null, getMapMode().DPtoLP(1)));
			createContents();
		}

		/**
		 * @generated
		 */
		private void createContents() {

			RectangleFigure persistentTableName0 = new RectangleFigure();
			persistentTableName0.setOutline(false);
			persistentTableName0.setOutlineXOR(true);

			this.add(persistentTableName0);

			GridLayout layoutPersistentTableName0 = new GridLayout();
			layoutPersistentTableName0.numColumns = 1;
			layoutPersistentTableName0.makeColumnsEqualWidth = true;
			persistentTableName0.setLayoutManager(layoutPersistentTableName0);

			fFigurePersistentTableNameFigure = new WrappingLabel();
			fFigurePersistentTableNameFigure.setText("<Tabla>");

			fFigurePersistentTableNameFigure
					.setFont(FFIGUREPERSISTENTTABLENAMEFIGURE_FONT);

			GridData constraintFFigurePersistentTableNameFigure = new GridData();
			constraintFFigurePersistentTableNameFigure.verticalAlignment = GridData.CENTER;
			constraintFFigurePersistentTableNameFigure.horizontalAlignment = GridData.CENTER;
			constraintFFigurePersistentTableNameFigure.horizontalIndent = 0;
			constraintFFigurePersistentTableNameFigure.horizontalSpan = 1;
			constraintFFigurePersistentTableNameFigure.verticalSpan = 1;
			constraintFFigurePersistentTableNameFigure.grabExcessHorizontalSpace = true;
			constraintFFigurePersistentTableNameFigure.grabExcessVerticalSpace = true;
			persistentTableName0.add(fFigurePersistentTableNameFigure,
					constraintFFigurePersistentTableNameFigure);

			fFigurePersistentColumnTableFigure = new RectangleFigure();
			fFigurePersistentColumnTableFigure.setOutline(false);
			fFigurePersistentColumnTableFigure.setOutlineXOR(true);

			this.add(fFigurePersistentColumnTableFigure);

		}

		/**
		 * @generated
		 */
		private boolean myUseLocalCoordinates = false;

		/**
		 * @generated
		 */
		protected boolean useLocalCoordinates() {
			return myUseLocalCoordinates;
		}

		/**
		 * @generated
		 */
		protected void setUseLocalCoordinates(boolean useLocalCoordinates) {
			myUseLocalCoordinates = useLocalCoordinates;
		}

		/**
		 * @generated
		 */
		public WrappingLabel getFigurePersistentTableNameFigure() {
			return fFigurePersistentTableNameFigure;
		}

		/**
		 * @generated
		 */
		public RectangleFigure getFigurePersistentColumnTableFigure() {
			return fFigurePersistentColumnTableFigure;
		}

	}

	/**
	 * @generated
	 */
	static final Color THIS_BACK = new Color(null, 253, 253, 221);

	/**
	 * @generated
	 */
	static final Font FFIGUREPERSISTENTTABLENAMEFIGURE_FONT = new Font(Display
			.getCurrent(), "SANS", 10, SWT.BOLD);

}
