/***************************************************************************
 * Copyright (c) 2008 Conselleria de Infraestructuras y Transporte,
 * Generalitat de la Comunitat Valenciana . All rights reserved. This program
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

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * The Class CreateSiblingActions.
 */
public class CreateSiblingActions extends MOSKittSqlSubmenuActionProvider {

	/** The create sibling actions. */
	protected Collection<IAction> createSiblingActions;
	
	/** The create sibling submenu actions. */
	protected Map<String, Collection<IAction>> createSiblingSubmenuActions;
	
	/** The create sibling menu manager. */
	protected IMenuManager createSiblingMenuManager;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.actions.ActionGroup#fillContextMenu(org.eclipse.jface.action.IMenuManager)
	 */
	@Override
	public void fillContextMenu(IMenuManager menu) {
		super.fillContextMenu(menu);
		// //
		ISelection selection = getContext().getSelection();
		Collection<?> newSiblingDescriptors = null;
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
			newSiblingDescriptors = domain.getNewChildDescriptors(null,
					selectedElement);
		}

		createSiblingActions = generateCreateSiblingActions(
				newSiblingDescriptors, selection);
		createSiblingSubmenuActions = extractSubmenuActions(
				createSiblingActions, "|");
		MenuManager submenuManager = new MenuManager("New sibling");
		populateManager(submenuManager, createSiblingSubmenuActions, null);
		populateManager(submenuManager, createSiblingActions, null);
		menu.add(submenuManager);
	}

	/**
	 * Generate create sibling actions.
	 * 
	 * @param descriptors the descriptors
	 * @param selection the selection
	 * 
	 * @return the collection< i action>
	 */
	protected Collection<IAction> generateCreateSiblingActions(
			Collection<?> descriptors, ISelection selection) {
		List<IAction> createSiblingActions = (List<IAction>) generateCreateActionsGen(
				descriptors, selection);

		Collections.<IAction> sort(createSiblingActions,
				new Comparator<IAction>() {

					public int compare(IAction a1, IAction a2) {
						return a1.getText().compareTo(a2.getText());
					}
				});

		return createSiblingActions;
	}

	/* (non-Javadoc)
	 * @see es.cv.gvcase.mdt.db.navigator.actions.model.MOSKittSqlSubmenuActionProvider#isCreateChild()
	 */
	@Override
	protected boolean isCreateChild() {
		return false;
	}

}
