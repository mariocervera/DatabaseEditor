/***************************************************************************
 * Copyright (c) 2007-2011 Conselleria de Infraestructuras y Transporte,
 * Generalitat de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Mario Cervera Ubeda (Integranova)
 * 				Francisco Javier Cano Muñoz (Prodevelop S.L.)
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.diagram.edit.parts;

import java.util.Collections;
import java.util.List;

import org.eclipse.datatools.modelbase.sql.constraints.ForeignKey;
import org.eclipse.datatools.modelbase.sql.constraints.Index;
import org.eclipse.datatools.modelbase.sql.constraints.UniqueConstraint;
import org.eclipse.datatools.modelbase.sql.datatypes.SQLDataType;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.LabelDirectEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramColorRegistry;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.tools.DragEditPartsTrackerEx;
import org.eclipse.gmf.runtime.diagram.ui.tools.TextDirectEditManager;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ISemanticParser;
import org.eclipse.gmf.runtime.gef.ui.internal.parts.WrapTextCellEditor;
import org.eclipse.gmf.runtime.notation.FontStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import es.cv.gvcase.mdt.common.diagram.editparts.CompartmentEditPart;
import es.cv.gvcase.mdt.common.edit.policies.DeleteOnlyViewListComponentEditPolicy;
import es.cv.gvcase.mdt.common.util.DiagramEditPartsUtil;
import es.cv.gvcase.mdt.db.diagram.edit.policies.ColumnItemSemanticEditPolicy;
import es.cv.gvcase.mdt.db.diagram.edit.policies.SqlmodelTextNonResizableEditPolicy;
import es.cv.gvcase.mdt.db.diagram.edit.policies.SqlmodelTextSelectionEditPolicy;
import es.cv.gvcase.mdt.db.diagram.parsers.ColumnWithConstraintsParser;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry;
import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelElementTypes;
import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelParserProvider;
import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelStructuralFeatureParser;

/**
 * @generated
 */
public class ColumnEditPart extends CompartmentEditPart implements
		ITextAwareEditPart {

	private ResourceListener resourceListener = new ResourceListener();

	private Label constraintsLabel;
	private SqlmodelStructuralFeatureParser constraintsLabelParser;

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 2001;

	/**
	 * @generated
	 */
	private DirectEditManager manager;

	/**
	 * @generated
	 */
	private IParser parser;

	/**
	 * @generated
	 */
	private List parserElements;

	/**
	 * @generated
	 */
	private String defaultText;

	/**
	 * @generated NOT ; modified to set a special label parser that adds
	 *            constrain information to the column label
	 */
	public ColumnEditPart(View view) {
		super(view);

		if (getConstraintsLabel() == null) {
			setConstraintsLabel(new Label());
			getConstraintsLabel().setText(" ");
			getConstraintsLabel().setForegroundColor(
					new Color(Display.getCurrent(), 83, 88, 85));
			getConstraintsLabel().setFont(
					new Font(Display.getCurrent(), "SANS", 10, SWT.ITALIC));
		}
		if (constraintsLabelParser == null) {
			constraintsLabelParser = new ColumnWithConstraintsParser(
					EcorePackage.eINSTANCE.getENamedElement_Name());
			constraintsLabelParser.setViewPattern("{0}");
			constraintsLabelParser.setEditPattern("{0}");
		}
	}

	public void setConstraintsLabel(Label constraintsLabel) {
		this.constraintsLabel = constraintsLabel;
	}

	public Label getConstraintsLabel() {
		return constraintsLabel;
	}

	/**
	 * @generated
	 */
	public DragTracker getDragTracker(Request request) {
		if (request instanceof SelectionRequest
				&& ((SelectionRequest) request).getLastButtonPressed() == 3) {
			return null;
		}
		return new DragEditPartsTrackerEx(this);
	}

	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new ColumnItemSemanticEditPolicy());
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE,
				new SqlmodelTextNonResizableEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new DeleteOnlyViewListComponentEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new LabelDirectEditPolicy());
	}

	/**
	 * @generated
	 */
	protected String getLabelTextHelper(IFigure figure) {
		if (figure instanceof WrappingLabel) {
			return ((WrappingLabel) figure).getText();
		} else {
			return ((Label) figure).getText();
		}
	}

	/**
	 * @generated
	 */
	protected void setLabelTextHelper(IFigure figure, String text) {
		if (figure instanceof WrappingLabel) {
			((WrappingLabel) figure).setText(text);
		} else {
			((Label) figure).setText(text);
		}
	}

	/**
	 * @generated
	 */
	protected Image getLabelIconHelper(IFigure figure) {
		if (figure instanceof WrappingLabel) {
			return ((WrappingLabel) figure).getIcon();
		} else {
			return ((Label) figure).getIcon();
		}
	}

	/**
	 * @generated
	 */
	protected void setLabelIconHelper(IFigure figure, Image icon) {
		if (figure instanceof WrappingLabel) {
			((WrappingLabel) figure).setIcon(icon);
		} else {
			((Label) figure).setIcon(icon);
		}
	}

	/**
	 * @generated
	 */
	public void setLabel(IFigure figure) {
		unregisterVisuals();
		setFigure(figure);
		defaultText = getLabelTextHelper(figure);
		if (figure instanceof WrappingLabel) {
			WrappingLabel wrappingLabel = (WrappingLabel) figure;
			wrappingLabel.setTextWrap(true);
			wrappingLabel.setAlignment(PositionConstants.CENTER);
			wrappingLabel.setTextJustification(PositionConstants.CENTER);
		}
		registerVisuals();
		refreshVisuals();
	}

	/**
	 * @generated
	 */
	protected List getModelChildren() {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public IGraphicalEditPart getChildBySemanticHint(String semanticHint) {
		return null;
	}

	/**
	 * @generated
	 */
	protected EObject getParserElement() {
		return resolveSemanticElement();
	}

	/**
	 * @generated
	 */
	protected Image getLabelIcon() {
		return null;
	}

	/**
	 * @generated
	 */
	protected String getLabelText() {
		String text = null;
		EObject parserElement = getParserElement();
		if (parserElement != null && getParser() != null) {
			text = getParser().getPrintString(
					new EObjectAdapter(parserElement),
					getParserOptions().intValue());
		}
		if (text == null || text.length() == 0) {
			text = defaultText;
		}
		return text;
	}

	/**
	 * @generated
	 */
	public void setLabelText(String text) {
		setLabelTextHelper(getFigure(), text);
		Object pdEditPolicy = getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
		if (pdEditPolicy instanceof SqlmodelTextSelectionEditPolicy) {
			((SqlmodelTextSelectionEditPolicy) pdEditPolicy).refreshFeedback();
		}
		Object sfEditPolicy = getEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE);
		if (sfEditPolicy instanceof SqlmodelTextSelectionEditPolicy) {
			((SqlmodelTextSelectionEditPolicy) sfEditPolicy).refreshFeedback();
		}
	}

	/**
	 * @generated
	 */
	public String getEditText() {
		if (getParserElement() == null || getParser() == null) {
			return ""; //$NON-NLS-1$
		}
		return getParser().getEditString(
				new EObjectAdapter(getParserElement()),
				getParserOptions().intValue());
	}

	/**
	 * @generated
	 */
	protected boolean isEditable() {
		return getParser() != null;
	}

	/**
	 * @generated
	 */
	public ICellEditorValidator getEditTextValidator() {
		return new ICellEditorValidator() {

			public String isValid(final Object value) {
				if (value instanceof String) {
					final EObject element = getParserElement();
					final IParser parser = getParser();
					try {
						IParserEditStatus valid = (IParserEditStatus) getEditingDomain()
								.runExclusive(new RunnableWithResult.Impl() {

									public void run() {
										setResult(parser.isValidEditString(
												new EObjectAdapter(element),
												(String) value));
									}
								});
						return valid.getCode() == ParserEditStatus.EDITABLE ? null
								: valid.getMessage();
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}
				}

				// shouldn't get here
				return null;
			}
		};
	}

	/**
	 * @generated
	 */
	public IContentAssistProcessor getCompletionProcessor() {
		if (getParserElement() == null || getParser() == null) {
			return null;
		}
		return getParser().getCompletionProcessor(
				new EObjectAdapter(getParserElement()));
	}

	/**
	 * @generated
	 */
	public ParserOptions getParserOptions() {
		return ParserOptions.NONE;
	}

	/**
	 * @generated
	 */
	public IParser getParser() {
		if (parser == null) {
			parser = SqlmodelParserProvider
					.getParser(
							SqlmodelElementTypes.Column_2001,
							getParserElement(),
							SqlmodelVisualIDRegistry
									.getType(es.cv.gvcase.mdt.db.diagram.edit.parts.ColumnEditPart.VISUAL_ID));

		}
		return parser;
	}

	/**
	 * @generated
	 */
	protected DirectEditManager getManager() {
		if (manager == null) {
			setManager(new TextDirectEditManager(this,
					WrapTextCellEditor.class, SqlmodelEditPartFactory
							.getTextCellEditorLocator(this)));
		}
		return manager;
	}

	/**
	 * @generated
	 */
	protected void setManager(DirectEditManager manager) {
		this.manager = manager;
	}

	/**
	 * @generated
	 */
	protected void performDirectEdit() {
		getManager().show();
	}

	/**
	 * @generated
	 */
	protected void performDirectEdit(Point eventLocation) {
		if (getManager().getClass() == TextDirectEditManager.class) {
			((TextDirectEditManager) getManager()).show(eventLocation
					.getSWTPoint());
		}
	}

	/**
	 * @generated
	 */
	private void performDirectEdit(char initialCharacter) {
		if (getManager() instanceof TextDirectEditManager) {
			((TextDirectEditManager) getManager()).show(initialCharacter);
		} else {
			performDirectEdit();
		}
	}

	/**
	 * @generated
	 */
	protected void performDirectEditRequest(Request request) {
		final Request theRequest = request;
		try {
			getEditingDomain().runExclusive(new Runnable() {

				public void run() {
					if (isActive() && isEditable()) {
						if (theRequest
								.getExtendedData()
								.get(
										RequestConstants.REQ_DIRECTEDIT_EXTENDEDDATA_INITIAL_CHAR) instanceof Character) {
							Character initialChar = (Character) theRequest
									.getExtendedData()
									.get(
											RequestConstants.REQ_DIRECTEDIT_EXTENDEDDATA_INITIAL_CHAR);
							performDirectEdit(initialChar.charValue());
						} else if ((theRequest instanceof DirectEditRequest)
								&& (getEditText().equals(getLabelText()))) {
							DirectEditRequest editRequest = (DirectEditRequest) theRequest;
							performDirectEdit(editRequest.getLocation());
						} else {
							performDirectEdit();
						}
					}
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @generated
	 */
	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshLabel();
		refreshFont();
		refreshFontColor();
		refreshUnderline();
		refreshStrikeThrough();
	}

	/**
	 * @generated
	 */
	protected void refreshLabel() {
		setLabelTextHelper(getFigure(), getLabelText());
		setLabelIconHelper(getFigure(), getLabelIcon());
		Object pdEditPolicy = getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
		if (pdEditPolicy instanceof SqlmodelTextSelectionEditPolicy) {
			((SqlmodelTextSelectionEditPolicy) pdEditPolicy).refreshFeedback();
		}
		Object sfEditPolicy = getEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE);
		if (sfEditPolicy instanceof SqlmodelTextSelectionEditPolicy) {
			((SqlmodelTextSelectionEditPolicy) sfEditPolicy).refreshFeedback();
		}
	}

	/**
	 * @generated
	 */
	protected void refreshUnderline() {
		FontStyle style = (FontStyle) getFontStyleOwnerView().getStyle(
				NotationPackage.eINSTANCE.getFontStyle());
		if (style != null && getFigure() instanceof WrappingLabel) {
			((WrappingLabel) getFigure()).setTextUnderline(style.isUnderline());
		}
	}

	/**
	 * @generated
	 */
	protected void refreshStrikeThrough() {
		FontStyle style = (FontStyle) getFontStyleOwnerView().getStyle(
				NotationPackage.eINSTANCE.getFontStyle());
		if (style != null && getFigure() instanceof WrappingLabel) {
			((WrappingLabel) getFigure()).setTextStrikeThrough(style
					.isStrikeThrough());
		}
	}

	/**
	 * @generated
	 */
	protected void refreshFont() {
		FontStyle style = (FontStyle) getFontStyleOwnerView().getStyle(
				NotationPackage.eINSTANCE.getFontStyle());
		if (style != null) {
			FontData fontData = new FontData(style.getFontName(), style
					.getFontHeight(), (style.isBold() ? SWT.BOLD : SWT.NORMAL)
					| (style.isItalic() ? SWT.ITALIC : SWT.NORMAL));
			setFont(fontData);
		}
	}

	/**
	 * @generated
	 */
	protected void setFontColor(Color color) {
		getFigure().setForegroundColor(color);
	}

	/**
	 * @generated
	 */
	protected void addSemanticListeners() {
		if (getParser() instanceof ISemanticParser) {
			EObject element = resolveSemanticElement();
			parserElements = ((ISemanticParser) getParser())
					.getSemanticElementsBeingParsed(element);
			for (int i = 0; i < parserElements.size(); i++) {
				addListenerFilter(
						"SemanticModel" + i, this, (EObject) parserElements.get(i)); //$NON-NLS-1$
			}
		} else {
			super.addSemanticListeners();
		}
	}

	/**
	 * @generated
	 */
	protected void removeSemanticListeners() {
		if (parserElements != null) {
			for (int i = 0; i < parserElements.size(); i++) {
				removeListenerFilter("SemanticModel" + i); //$NON-NLS-1$
			}
		} else {
			super.removeSemanticListeners();
		}
	}

	/**
	 * @generated
	 */
	protected AccessibleEditPart getAccessibleEditPart() {
		if (accessibleEP == null) {
			accessibleEP = new AccessibleGraphicalEditPart() {

				public void getName(AccessibleEvent e) {
					e.result = getLabelTextHelper(getFigure());
				}
			};
		}
		return accessibleEP;
	}

	/**
	 * @generated
	 */
	private View getFontStyleOwnerView() {
		return (View) getModel();
	}

	/**
	 * @generated
	 */
	protected void addNotationalListeners() {
		super.addNotationalListeners();
		addListenerFilter("PrimaryView", this, getPrimaryView()); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	protected void removeNotationalListeners() {
		super.removeNotationalListeners();
		removeListenerFilter("PrimaryView"); //$NON-NLS-1$
	}

	/**
	 * @generated NOT ; modified to refresh the label when a change that affects
	 *            this column or its constrains happens
	 */
	protected void handleNotificationEvent(Notification event) {
		Object feature = event.getFeature();
		if (NotationPackage.eINSTANCE.getFontStyle_FontColor().equals(feature)) {
			Integer c = (Integer) event.getNewValue();
			setFontColor(DiagramColorRegistry.getInstance().getColor(c));
		} else if (NotationPackage.eINSTANCE.getFontStyle_Underline().equals(
				feature)) {
			refreshUnderline();
		} else if (NotationPackage.eINSTANCE.getFontStyle_StrikeThrough()
				.equals(feature)) {
			refreshStrikeThrough();
		} else if (NotationPackage.eINSTANCE.getFontStyle_FontHeight().equals(
				feature)
				|| NotationPackage.eINSTANCE.getFontStyle_FontName().equals(
						feature)
				|| NotationPackage.eINSTANCE.getFontStyle_Bold()
						.equals(feature)
				|| NotationPackage.eINSTANCE.getFontStyle_Italic().equals(
						feature)) {
			refreshFont();
		} else {
			if (getParser() != null
					&& getParser().isAffectingEvent(event,
							getParserOptions().intValue())) {
				refreshLabel();
			}
			if (getParser() instanceof ISemanticParser) {
				ISemanticParser modelParser = (ISemanticParser) getParser();
				if (modelParser.areSemanticElementsAffected(null, event)) {
					removeSemanticListeners();
					if (resolveSemanticElement() != null) {
						addSemanticListeners();
					}
					refreshLabel();
				}
			}
			if (event.getEventType() == Notification.SET) {
				this.refresh();
			}
		}
		super.handleNotificationEvent(event);
	}

	/**
	 * @generated
	 */
	protected IFigure createFigure() {
		IFigure label = createFigurePrim();
		defaultText = getLabelTextHelper(label);
		return label;
	}

	/**
	 * @generated
	 */
	protected IFigure createFigurePrim() {
		ColumnNameLabelFigure label = new ColumnNameLabelFigure();

		if (label instanceof WrappingLabel) {
			label.setTextWrap(true);
		}
		return label;
	}

	/**
	 * @generated
	 */
	public class ColumnNameLabelFigure extends WrappingLabel {

		/**
		 * @generated
		 */
		public ColumnNameLabelFigure() {
			this.setText("{...}");

			this.setFont(THIS_FONT);

			this.setBorder(new MarginBorder(getMapMode().DPtoLP(2),
					getMapMode().DPtoLP(5), getMapMode().DPtoLP(2),
					getMapMode().DPtoLP(5)));
		}

	}

	/**
	 * @generated
	 */
	static final Font THIS_FONT = new Font(Display.getCurrent(), "SANS", 10,
			SWT.NORMAL);

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
	 * Performs a normal refresh and a refresh of the label showing the
	 * constrains of the column
	 * 
	 * @NOT-generated
	 */
	@Override
	public void refresh() {

		super.refresh();

		refreshConstraintsLabel();
	}

	/**
	 * activates the editpart and adds a listener to catch changes in the
	 * resource
	 * 
	 * @NOT-generated
	 */
	@Override
	public void activate() {
		super.activate();

		TransactionalEditingDomain domain = this.getEditingDomain();
		domain.addResourceSetListener(resourceListener);
	}

	/**
	 * Deactivates the editpart and removes the resource listener
	 * 
	 * @NOT-generated
	 */
	@Override
	public void deactivate() {
		super.deactivate();

		TransactionalEditingDomain domain = this.getEditingDomain();
		domain.removeResourceSetListener(resourceListener);
	}

	/**
	 * Perform a refresh of the label showing the constrains on the affected
	 * column
	 * 
	 * @NOT-generated
	 */
	public void refreshConstraintsLabel() {
		if (constraintsLabelParser != null && getConstraintsLabel() != null) {
			EObject parserElement = this.resolveSemanticElement();
			if (parserElement != null) {
				EObjectAdapter adapter = new EObjectAdapter(parserElement);
				getConstraintsLabel().setText(
						constraintsLabelParser.getPrintString(adapter,
								ParserOptions.NONE.intValue()));
			}
		}
	}

	/**
	 * Resource lisener that listens to changes in column constrains or column
	 * datatypes and performs a refresh when one of those changes happen.
	 * 
	 * @NOT-generated
	 */
	private class ResourceListener extends ResourceSetListenerImpl {

		@Override
		public boolean isPostcommitOnly() {
			return true;
		}

		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			boolean refreshNeeded = false;
			for (Object n : event.getNotifications()) {
				if (n instanceof Notification) {
					Notification notification = (Notification) n;
					// Check for changes in the constraints that affect the
					// Column
					if (notification.getNotifier() instanceof UniqueConstraint
							|| notification.getNotifier() instanceof Index
							|| notification.getNotifier() instanceof ForeignKey) {
						refreshNeeded = true;
					}
					// Check for changes in the Datatype or the attributes of
					// the DataType of the Column
					if (notification.getNotifier() instanceof SQLDataType) {
						if (((EObject) notification.getNotifier()).eContainer() instanceof Column) {
							refreshNeeded = true;
						}
					}
				}
			}
			// Refresh the labels if any of the changes need the labels to be
			// refreshed
			if (refreshNeeded) {
				refreshLabel();
				refreshConstraintsLabel();
			}
		}

	}

}
