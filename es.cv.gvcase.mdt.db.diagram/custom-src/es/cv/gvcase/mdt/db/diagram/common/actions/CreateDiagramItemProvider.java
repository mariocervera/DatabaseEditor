/***************************************************************************
* Copyright (c) 2008 Conselleria de Infraestructuras y Transporte,
* Generalitat de la Comunitat Valenciana . All rights reserved. This program
* and the accompanying materials are made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors: Francisco Javier Cano Muñoz (Prodevelop) – initial API and implementation
*
******************************************************************************/

package es.cv.gvcase.mdt.db.diagram.common.actions;

import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchPage;

import es.cv.gvcase.mdt.common.ids.MOSKittModelIDs;


// TODO: Auto-generated Javadoc
/**
 * Contributes the "Create diagram" to the context menu.
 * 
 * @author <a href="mailto:fjcano@prodeelop.es">Francisco Javier Cano Muñoz</a>
 */
public class CreateDiagramItemProvider extends AbstractContributionItemProvider implements IProvider {

	/** The Constant MENU_CREATE_DIAGRAM. */
	public static final String MENU_CREATE_DIAGRAM = "menu_create_diagram"; //$NON-NLS-1$
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider#createMenuManager(java.lang.String, org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor)
	 */
	@Override
	protected IMenuManager createMenuManager(String menuId, IWorkbenchPartDescriptor partDescriptor) {
		if (!MENU_CREATE_DIAGRAM.equals(menuId)) {
			return super.createMenuManager(menuId, partDescriptor);
		}
		MenuManager menuManager = new MenuManager("Create diagram");
		MenuBuilder builder = new MenuBuilder(partDescriptor);
		// XXX: build initial content -- otherwise menu is never shown
		builder.buildMenu(menuManager);

		menuManager.addMenuListener(builder);
		return menuManager;
	}
	
	/**
	 * The Class MenuBuilder.
	 */
	private class MenuBuilder implements IMenuListener {

		/** The my workbench part. */
		private final IWorkbenchPartDescriptor myWorkbenchPart;

		/**
		 * Instantiates a new menu builder.
		 * 
		 * @param workbenchPart the workbench part
		 */
		public MenuBuilder(IWorkbenchPartDescriptor workbenchPart) {
			myWorkbenchPart = workbenchPart;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
		 */
		public void menuAboutToShow(IMenuManager manager) {
			buildMenu(manager);
		}

		/**
		 * Builds the menu.
		 * 
		 * @param manager the manager
		 */
		public void buildMenu(IMenuManager manager) {
			manager.removeAll();
			
			CreateDiagramAction action = new CreateDiagramAction(getWorkbenchPage(),
					MOSKittModelIDs.SQLModelID);
			action.init();
			manager.add(action);
		}

		/**
		 * Gets the workbench page.
		 * 
		 * @return the workbench page
		 */
		private IWorkbenchPage getWorkbenchPage() {
			return myWorkbenchPart.getPartPage();
		}
	}
	
}
