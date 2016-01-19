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

import java.util.Collections;
import java.util.List;

import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.PersistentTable;
import org.eclipse.datatools.modelbase.sql.tables.SQLTablesPackage;
import org.eclipse.datatools.modelbase.sql.tables.ViewTable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.diagram.ui.editparts.CompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.LabelDirectEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramColorRegistry;
import org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper;
import org.eclipse.gmf.runtime.diagram.ui.requests.GroupRequestViaKeyboard;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.tools.DragEditPartsTrackerEx;
import org.eclipse.gmf.runtime.diagram.ui.tools.TextDirectEditManager;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
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

import es.cv.gvcase.mdt.common.commands.wrappers.EMFtoGEFCommandWrapper;
import es.cv.gvcase.mdt.common.edit.policies.DeleteOnlyViewListComponentEditPolicy;
import es.cv.gvcase.mdt.common.util.DiagramEditPartsUtil;
import es.cv.gvcase.mdt.db.diagram.edit.policies.PersistentTable2ItemSemanticEditPolicy;
import es.cv.gvcase.mdt.db.diagram.edit.policies.SqlmodelTextNonResizableEditPolicy;
import es.cv.gvcase.mdt.db.diagram.edit.policies.SqlmodelTextSelectionEditPolicy;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry;
import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelElementTypes;
import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelParserProvider;

/**
 * @generated
 */
public class PersistentTable2EditPart extends
		es.cv.gvcase.mdt.common.diagram.editparts.CompartmentEditPart implements
		ITextAwareEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 2003;

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
	 * @generated
	 */
	public PersistentTable2EditPart(View view) {
		super(view);
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
				new PersistentTable2ItemSemanticEditPolicy());
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
		return false;
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
							SqlmodelElementTypes.PersistentTable_2003,
							getParserElement(),
							SqlmodelVisualIDRegistry
									.getType(es.cv.gvcase.mdt.db.diagram.edit.parts.PersistentTable2EditPart.VISUAL_ID));

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
	 * @generated
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
		ViewedTableNameLabel label = new ViewedTableNameLabel();

		if (label instanceof WrappingLabel) {
			label.setTextWrap(true);
		}
		return label;
	}

	/**
	 * @generated
	 */
	public class ViewedTableNameLabel extends WrappingLabel {

		/**
		 * @generated
		 */
		public ViewedTableNameLabel() {
			this.setText("<Table>");

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
	 * The elements represented by this editpart are contained in another
	 * element, so the 'delete' requests have to be treated specially.
	 * 
	 * @NOT-generated
	 */
	@Override
	public Command getCommand(Request _request) {

		EditingDomain domain = getEditingDomain();
		if (domain == null)
			return super.getCommand(_request);

		if (_request instanceof GroupRequestViaKeyboard) {
			GroupRequestViaKeyboard gReq = (GroupRequestViaKeyboard) _request;
			if (gReq.getType().equals("delete")) {
				if (this.getParent().getParent() instanceof ViewTableEditPart) {
					// We don't want to be able to delete the viewed table from
					// the view

					return UnexecutableCommand.INSTANCE;
				}
			}

		} else if (_request instanceof GroupRequest) {
			GroupRequest req = (GroupRequest) _request;
			if (req.getType().toString().equals("delete")) {
				if (req.getExtendedData() != null) {
					if (req.getExtendedData().size() > 0) {
						if (req.getExtendedData().get("Delete").equals(
								"Delete from diagram")) {
							return UnexecutableCommand.INSTANCE;
						}
					}
				}
			}
		}
		if (_request instanceof EditCommandRequestWrapper) {
			EditCommandRequestWrapper ecReq = (EditCommandRequestWrapper) _request;
			if (ecReq.getEditCommandRequest() instanceof DestroyElementRequest) {
				// Remove just the Viewed Table, not the column itself
				ViewTable viewtable = (ViewTable) ((IGraphicalEditPart) this
						.getParent().getParent()).resolveSemanticElement();
				PersistentTable tableToRemove = (PersistentTable) this
						.resolveSemanticElement();
				CompoundCommand cc = new CompoundCommand("Remove viewed table");

				// Remove the viewed columns corresponding to the viewed table
				// to be deleted
				for (Column column : viewtable.getViewedColumns()) {
					if (column.getTable().equals(tableToRemove)) {
						RemoveCommand removeCommand = (RemoveCommand) RemoveCommand
								.create(domain, viewtable,
										SQLTablesPackage.eINSTANCE
												.getViewTable_ViewedColumns(),
										column);
						cc.append(removeCommand);
					}
				}

				RemoveCommand command = (RemoveCommand) RemoveCommand.create(
						domain, viewtable, SQLTablesPackage.eINSTANCE
								.getViewTable_ViewedTables(), tableToRemove);
				cc.append(command);

				if (!cc.isEmpty()) {
					return (new EMFtoGEFCommandWrapper(cc));
				}
			}
		}

		return super.getCommand(_request);
	}

}
