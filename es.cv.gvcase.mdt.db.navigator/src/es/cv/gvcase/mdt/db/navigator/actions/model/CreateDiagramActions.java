/*******************************************************************************
 * Copyright (c) 2008 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Francisco Javier Cano Muñoz (Prodevelop) - initial API implementation
 * Mario Cervera Ubeda (Integranova)
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.navigator.actions.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;

import es.cv.gvcase.ide.navigator.provider.MOSKittSubmenuActionProvider;
import es.cv.gvcase.ide.navigator.util.NavigatorUtil;
import es.cv.gvcase.mdt.common.ids.MOSKittEditorIDs;
import es.cv.gvcase.mdt.db.diagram.edit.parts.SchemaEditPart;

// TODO: Auto-generated Javadoc
/**
 * The Class CreateDiagramActions.
 */
public class CreateDiagramActions extends MOSKittSubmenuActionProvider {

	/** The create diagram actions. */
	protected Collection<IAction> createDiagramActions;
	
	/** The create diagram menu manager. */
	protected IMenuManager createDiagramMenuManager;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.actions.ActionGroup#fillContextMenu(org.eclipse.jface.action.IMenuManager)
	 */
	@Override
	public void fillContextMenu(IMenuManager menu) {
		super.fillContextMenu(menu);
		// //
		ISelection selection = getContext().getSelection();
		Object selectedElement = null;
		if (selection instanceof IStructuredSelection
				&& ((IStructuredSelection) selection).size() == 1) {
			selectedElement = ((IStructuredSelection) selection)
					.getFirstElement();
			EditingDomain domain = getCommonNavigator().getEditingDomain();
			// End if no domain
			if (domain == null) {
				return;
			}
			if (getDiagramResource() != null) {
				createDiagramActions = generateCreateDiagramActions(selectedElement);
			} else {
				return;
			}
		}
		MenuManager submenuManager = new MenuManager("New diagram");
		populateManager(submenuManager, createDiagramActions, null);
		menu.add(submenuManager);
	}

	/**
	 * Generate create diagram actions.
	 * 
	 * @param selectedObject the selected object
	 * 
	 * @return the collection< i action>
	 */
	protected Collection<IAction> generateCreateDiagramActions(
			Object selectedObject) {
		if (!(selectedObject instanceof Schema)) {
			return Collections.EMPTY_LIST;
		}
		Schema schema = (Schema) selectedObject;
		Resource resource = getDiagramResource();
		if (resource == null) {
			return Collections.EMPTY_LIST;
		}
		List<IAction> actions = new ArrayList<IAction>();
		String kind = SchemaEditPart.MODEL_ID;
		String label = MOSKittEditorIDs.getExtensionsMapModelToLabel().get(kind);
		label = label != null ? label : kind;
		IAction action = new CreateSqlDiagramAction("Create " + label
				+ " diagram", schema, kind, resource);
		if (action != null) {
			actions.add(action);
		}
		return actions;
	}

	/**
	 * Gets the diagram resource.
	 * 
	 * @return the diagram resource
	 */
	private GMFResource getDiagramResource() {
		IEditorPart activeEditor = NavigatorUtil.getActiveEditor();
		if (activeEditor != null && activeEditor instanceof DiagramEditor) {
			TransactionalEditingDomain domain = ((DiagramEditor) activeEditor)
					.getEditingDomain();
			if (domain == null) {
				return null;
			}
			for (Resource resource : domain.getResourceSet().getResources()) {
				if (resource instanceof GMFResource) {
					return (GMFResource) resource;
				}
			}
		}
		return null;
	}

}
