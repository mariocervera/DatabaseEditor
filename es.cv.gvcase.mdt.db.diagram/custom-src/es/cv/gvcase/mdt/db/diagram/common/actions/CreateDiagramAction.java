/***************************************************************************
* Copyright (c) 2008 Conselleria de Infraestructuras y Transporte,
* Generalitat de la Comunitat Valenciana . All rights reserved. This program
* and the accompanying materials are made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Francisco Javier Cano Muñoz (Prodevelop) – initial API and implementation
* Mario Cervera Ubeda (Integranova)
*
******************************************************************************/

package es.cv.gvcase.mdt.db.diagram.common.actions;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPage;

import es.cv.gvcase.mdt.common.commands.OpenAsDiagramCommand;
import es.cv.gvcase.mdt.common.ids.MOSKittEditorIDs;

// TODO: Auto-generated Javadoc
/**
 * "Create diagram" context menu action.
 * 
 * @author <a href="mailto:fjcano@prodeelop.es">Francisco Javier Cano Muñoz</a>
 */
public class CreateDiagramAction extends DiagramAction {

//	/** The Constant UMLDiagramFileExtension. */
//	public static final String UMLDiagramFileExtension = "_diagram";
//
//	/** The Constant EMPTY_NAME. */
//	private static final String EMPTY_NAME = "No_diagram";

	/** The diagram kind. */
	private String diagramKind = null;

	/**
	 * Instantiates a new creates the diagram action.
	 * 
	 * @param workbenchPage the workbench page
	 * @param diagramKind the diagram kind
	 */
	public CreateDiagramAction(IWorkbenchPage workbenchPage, String diagramKind) {
		super(workbenchPage);
		this.diagramKind = diagramKind;
		setText(diagramKind);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createTargetRequest()
	 */
	@Override
	protected Request createTargetRequest() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#isSelectionListener()
	 */
	@Override
	protected boolean isSelectionListener() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#getCommand()
	 */
	@Override
	protected Command getCommand() {
		// IGraphicalEditPart editPart = getSelectedEditPart();
		// if (editPart == null) {
		// return null;
		// }
		// View view = editPart.getNotationView();
		// Command command = new ICommandProxy(new OpenAsUMLDiagramCommand(view
		// .eResource(), view.getElement(), diagramKind));
		Resource resource = getDiagramResource();
		EObject selectedEObject = getSelectedEObject();
		if (resource != null && selectedEObject != null) {
			Command command = new ICommandProxy(new OpenAsDiagramCommand(
					getDiagramResource(), getSelectedEObject(), diagramKind));
			return command;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
//		return MOSKittEditorIDs.MapModelToEditor.keySet().contains(
//				diagramKind);
		return MOSKittEditorIDs.getAllExtensionModelToEditor().keySet().contains(
				diagramKind);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#refresh()
	 */
	@Override
	public void refresh() {
		super.refresh();
		setText(diagramKind);
	}

	/**
	 * Gets the selected e object.
	 * 
	 * @return the selected e object
	 */
	private EObject getSelectedEObject() {
		IGraphicalEditPart editPart = getSelectedEditPart();
		if (editPart != null) {
			return editPart.resolveSemanticElement();
		}
		// no GraphicalEditPart selected
		ISelection selection = getWorkbenchPage().getSelection();
		if (selection instanceof StructuredSelection) {
			StructuredSelection ss = (StructuredSelection) selection;
			Object object = ss.getFirstElement();
			if (object instanceof EObject) {
				return (EObject) object;
			}
		}
		// no element selected
		return null;
	}

	/**
	 * Gets the selected edit part.
	 * 
	 * @return the selected edit part
	 */
	private IGraphicalEditPart getSelectedEditPart() {
		for (Object next : getSelectedObjects()) {
			if (next instanceof IGraphicalEditPart) {
				IGraphicalEditPart editPart = (IGraphicalEditPart) next;
				return editPart;
			}
		}
		return null;
	}

	/**
	 * Gets the diagram resource.
	 * 
	 * @return the diagram resource
	 */
	private Resource getDiagramResource() {
		IGraphicalEditPart editPart = getSelectedEditPart();
		if (editPart != null) {
			return editPart.getNotationView().eResource();
		}
//		// no IgraphicalEditPart -> no diagram selection
//		EObject eObject = getSelectedEObject();
//		if (eObject != null) {
//			if (eObject instanceof View) {
//				return eObject.eResource();
//			}
//			String uriName = eObject.eResource().getURI().toString()
//					+ UMLDiagramFileExtension;
//			URI uri = URI.createURI(uriName);
//			return eObject.eResource().getResourceSet().getResource(uri, true);
//		}
//		// no selection -> no resource
		return null;
	}
}
