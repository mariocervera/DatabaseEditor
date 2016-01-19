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

import org.eclipse.datatools.modelbase.sql.tables.SQLTablesPackage;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.RoundedRectangle;
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
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
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
import es.cv.gvcase.mdt.db.diagram.edit.policies.SqlmodelTextSelectionEditPolicy;
import es.cv.gvcase.mdt.db.diagram.edit.policies.ViewTableItemSemanticEditPolicy;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry;
import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelElementTypes;

/**
 * @generated
 */
public class ViewTableEditPart extends ShapeNodeEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 1002;

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
	public ViewTableEditPart(View view) {
		super(view);
	}

	/**
	 * @generated NOT ; modified to remove the popup bar policy for the
	 *            ViewTableEditPart
	 */
	protected void createDefaultEditPolicies() {
		installEditPolicy(EditPolicyRoles.CREATION_ROLE,
				new CreationEditPolicy() {
					public Command getCommand(Request request) {
						if (understandsRequest(request)) {
							if (request instanceof CreateViewAndElementRequest) {
								CreateElementRequestAdapter adapter = ((CreateViewAndElementRequest) request)
										.getViewAndElementDescriptor()
										.getCreateElementRequestAdapter();
								IElementType type = (IElementType) adapter
										.getAdapter(IElementType.class);
								if (type == SqlmodelElementTypes.Column_2002) {
									EditPart compartmentEditPart = getChildBySemanticHint(SqlmodelVisualIDRegistry
											.getType(ViewTableViewedColumnsEditPart.VISUAL_ID));
									return compartmentEditPart == null ? null
											: compartmentEditPart
													.getCommand(request);
								}
								if (type == SqlmodelElementTypes.PersistentTable_2003) {
									EditPart compartmentEditPart = getChildBySemanticHint(SqlmodelVisualIDRegistry
											.getType(ViewTableViewedTablesEditPart.VISUAL_ID));
									return compartmentEditPart == null ? null
											: compartmentEditPart
													.getCommand(request);
								}
							}
							return super.getCommand(request);
						}
						return null;
					}
				});

		super.createDefaultEditPolicies();

		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new DeleteOnlyViewComponentEditPolicy());

		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new ViewTableItemSemanticEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
		// remove popup bar
		if (getEditPolicy(EditPolicyRoles.POPUPBAR_ROLE) != null) {
			removeEditPolicy(EditPolicyRoles.POPUPBAR_ROLE);
		}
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
		ViewFigure figure = new ViewFigure();
		return primaryShape = figure;
	}

	/**
	 * @generated
	 */
	public ViewFigure getPrimaryShape() {
		return (ViewFigure) primaryShape;
	}

	/**
	 * @generated
	 */
	protected boolean addFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof ViewTableNameEditPart) {
			((ViewTableNameEditPart) childEditPart).setLabel(getPrimaryShape()
					.getFigureViewNameFigure());
			return true;
		}
		if (childEditPart instanceof ViewTableViewedColumnsEditPart) {
			IFigure pane = getPrimaryShape()
					.getFigureViewViewedColumnsContainer();
			setupContentPane(pane); // FIXME each comparment should handle his content pane in his own way 
			pane.add(((ViewTableViewedColumnsEditPart) childEditPart)
					.getFigure());
			return true;
		}
		if (childEditPart instanceof ViewTableViewedTablesEditPart) {
			IFigure pane = getPrimaryShape()
					.getFigureViewViewedTablesContainer();
			setupContentPane(pane); // FIXME each comparment should handle his content pane in his own way 
			pane.add(((ViewTableViewedTablesEditPart) childEditPart)
					.getFigure());
			return true;
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected boolean removeFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof ViewTableNameEditPart) {
			return true;
		}
		if (childEditPart instanceof ViewTableViewedColumnsEditPart) {
			IFigure pane = getPrimaryShape()
					.getFigureViewViewedColumnsContainer();
			setupContentPane(pane); // FIXME each comparment should handle his content pane in his own way 
			pane.remove(((ViewTableViewedColumnsEditPart) childEditPart)
					.getFigure());
			return true;
		}
		if (childEditPart instanceof ViewTableViewedTablesEditPart) {
			IFigure pane = getPrimaryShape()
					.getFigureViewViewedTablesContainer();
			setupContentPane(pane); // FIXME each comparment should handle his content pane in his own way 
			pane.remove(((ViewTableViewedTablesEditPart) childEditPart)
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
		if (editPart instanceof ViewTableViewedColumnsEditPart) {
			return getPrimaryShape().getFigureViewViewedColumnsContainer();
		}
		if (editPart instanceof ViewTableViewedTablesEditPart) {
			return getPrimaryShape().getFigureViewViewedTablesContainer();
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
				.getType(ViewTableNameEditPart.VISUAL_ID));
	}

	/**
	 * @generated
	 */
	public List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/getMARelTypesOnSource() {
		List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/types = new ArrayList/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/();
		types.add(SqlmodelElementTypes.ViewTableViewedTables_3002);
		return types;
	}

	/**
	 * @generated
	 */
	public List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/getMARelTypesOnSourceAndTarget(
			IGraphicalEditPart targetEditPart) {
		List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/types = new ArrayList/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/();
		if (targetEditPart instanceof PersistentTableEditPart) {
			types.add(SqlmodelElementTypes.ViewTableViewedTables_3002);
		}
		if (targetEditPart instanceof es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableEditPart) {
			types.add(SqlmodelElementTypes.ViewTableViewedTables_3002);
		}
		return types;
	}

	/**
	 * @generated
	 */
	public List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/getMATypesForTarget(
			IElementType relationshipType) {
		List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/types = new ArrayList/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/();
		if (relationshipType == SqlmodelElementTypes.ViewTableViewedTables_3002) {
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
	public List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/getMARelTypesOnTarget() {
		List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/types = new ArrayList/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/();
		types.add(SqlmodelElementTypes.ViewTableViewedTables_3002);
		return types;
	}

	/**
	 * @generated
	 */
	public List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/getMATypesForSource(
			IElementType relationshipType) {
		List/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/types = new ArrayList/*<org.eclipse.gmf.runtime.emf.type.core.IElementType>*/();
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
			if (type == SqlmodelElementTypes.Column_2002) {
				return getChildBySemanticHint(SqlmodelVisualIDRegistry
						.getType(ViewTableViewedColumnsEditPart.VISUAL_ID));
			}
			if (type == SqlmodelElementTypes.PersistentTable_2003) {
				return getChildBySemanticHint(SqlmodelVisualIDRegistry
						.getType(ViewTableViewedTablesEditPart.VISUAL_ID));
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
	protected void handleNotificationEvent(Notification notification) {
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
			features
					.add(SQLTablesPackage.eINSTANCE.getViewTable_ViewedTables());
			DiagramEditPartsUtil.handleNotificationForDiagram(this,
					notification, features);
		}
		// mgil:: end
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
	public class ViewFigure extends RoundedRectangle {

		/**
		 * @generated
		 */
		private WrappingLabel fFigureViewNameFigure;
		/**
		 * @generated
		 */
		private RectangleFigure fFigureViewViewedColumnsContainer;
		/**
		 * @generated
		 */
		private RectangleFigure fFigureViewViewedTablesContainer;

		/**
		 * @generated
		 */
		public ViewFigure() {

			ToolbarLayout layoutThis = new ToolbarLayout();
			layoutThis.setStretchMinorAxis(true);
			layoutThis.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);

			layoutThis.setSpacing(0);
			layoutThis.setVertical(true);

			this.setLayoutManager(layoutThis);

			this.setCornerDimensions(new Dimension(getMapMode().DPtoLP(8),
					getMapMode().DPtoLP(8)));
			this.setForegroundColor(ColorConstants.black);
			this.setBackgroundColor(THIS_BACK);
			this.setPreferredSize(new Dimension(getMapMode().DPtoLP(150),
					getMapMode().DPtoLP(70)));

			this.setBorder(new MarginBorder(getMapMode().DPtoLP(3),
					getMapMode().DPtoLP(3), getMapMode().DPtoLP(3),
					getMapMode().DPtoLP(3)));
			createContents();
		}

		/**
		 * @generated
		 */
		private void createContents() {

			RectangleFigure viewNameContainerFigure0 = new RectangleFigure();
			viewNameContainerFigure0.setOutline(false);

			viewNameContainerFigure0.setBorder(new MarginBorder(getMapMode()
					.DPtoLP(0), getMapMode().DPtoLP(0), getMapMode().DPtoLP(0),
					getMapMode().DPtoLP(0)));

			this.add(viewNameContainerFigure0);

			GridLayout layoutViewNameContainerFigure0 = new GridLayout();
			layoutViewNameContainerFigure0.numColumns = 1;
			layoutViewNameContainerFigure0.makeColumnsEqualWidth = true;
			layoutViewNameContainerFigure0.horizontalSpacing = 0;
			layoutViewNameContainerFigure0.verticalSpacing = 0;
			layoutViewNameContainerFigure0.marginWidth = 0;
			layoutViewNameContainerFigure0.marginHeight = 0;
			viewNameContainerFigure0
					.setLayoutManager(layoutViewNameContainerFigure0);

			fFigureViewNameFigure = new WrappingLabel();
			fFigureViewNameFigure.setText("<Vista>");

			fFigureViewNameFigure.setFont(FFIGUREVIEWNAMEFIGURE_FONT);

			fFigureViewNameFigure.setBorder(new MarginBorder(getMapMode()
					.DPtoLP(2), getMapMode().DPtoLP(5), getMapMode().DPtoLP(2),
					getMapMode().DPtoLP(5)));

			GridData constraintFFigureViewNameFigure = new GridData();
			constraintFFigureViewNameFigure.verticalAlignment = GridData.CENTER;
			constraintFFigureViewNameFigure.horizontalAlignment = GridData.CENTER;
			constraintFFigureViewNameFigure.horizontalIndent = 0;
			constraintFFigureViewNameFigure.horizontalSpan = 0;
			constraintFFigureViewNameFigure.verticalSpan = 0;
			constraintFFigureViewNameFigure.grabExcessHorizontalSpace = true;
			constraintFFigureViewNameFigure.grabExcessVerticalSpace = true;
			viewNameContainerFigure0.add(fFigureViewNameFigure,
					constraintFFigureViewNameFigure);

			RectangleFigure viewAuxCont0 = new RectangleFigure();
			viewAuxCont0.setOutline(false);

			this.add(viewAuxCont0);

			ToolbarLayout layoutViewAuxCont0 = new ToolbarLayout();
			layoutViewAuxCont0.setStretchMinorAxis(true);
			layoutViewAuxCont0.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);

			layoutViewAuxCont0.setSpacing(0);
			layoutViewAuxCont0.setVertical(true);

			viewAuxCont0.setLayoutManager(layoutViewAuxCont0);

			fFigureViewViewedColumnsContainer = new RectangleFigure();
			fFigureViewViewedColumnsContainer.setOutline(false);
			fFigureViewViewedColumnsContainer.setOutlineXOR(true);

			viewAuxCont0.add(fFigureViewViewedColumnsContainer);
			fFigureViewViewedColumnsContainer
					.setLayoutManager(new StackLayout());

			fFigureViewViewedTablesContainer = new RectangleFigure();
			fFigureViewViewedTablesContainer.setOutline(false);
			fFigureViewViewedTablesContainer.setOutlineXOR(true);

			viewAuxCont0.add(fFigureViewViewedTablesContainer);
			fFigureViewViewedTablesContainer
					.setLayoutManager(new StackLayout());

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
		public WrappingLabel getFigureViewNameFigure() {
			return fFigureViewNameFigure;
		}

		/**
		 * @generated
		 */
		public RectangleFigure getFigureViewViewedColumnsContainer() {
			return fFigureViewViewedColumnsContainer;
		}

		/**
		 * @generated
		 */
		public RectangleFigure getFigureViewViewedTablesContainer() {
			return fFigureViewViewedTablesContainer;
		}

	}

	/**
	 * @generated
	 */
	static final Color THIS_BACK = new Color(null, 255, 215, 174);

	/**
	 * @generated
	 */
	static final Font FFIGUREVIEWNAMEFIGURE_FONT = new Font(Display
			.getCurrent(), "SANS", 10, SWT.BOLD);

}
