/*******************************************************************************
 * Copyright (c) 2007 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Javier Mu�oz (Integranova) � Initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.expressions.Expression;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.AbstractContributionFactory;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.services.IServiceLocator;

import es.cv.gvcase.trmanager.internal.ui.Messages;
import es.cv.gvcase.trmanager.registry.TransformationDesc;
import es.cv.gvcase.trmanager.registry.TransformationsRegistry;

public class TrManagerUIStartup implements IStartup {

	private static final String navigatorId = "es.cv.gvcase.ide.navigator.resourceView";
	
	public void earlyStartup() {

		// Create Menu
		IMenuService menuService = (IMenuService) org.eclipse.ui.PlatformUI.getWorkbench().getService(
				IMenuService.class);

		AbstractContributionFactory transfMenu = new AbstractContributionFactory(
				"popup:"+navigatorId, "es.cv.gvcase.trmanager.ui") {

			@Override
			public void createContributionItems(IServiceLocator serviceLocator, IContributionRoot additions) {

				// Add a MenuManager
				IMenuManager transfMenuManager = new MenuManager(Messages.getString("TrManagerUIStartup.MOSKittTransformations")); //$NON-NLS-1$

				// Add the commandContribution inside the MenuManager

				IContributionItem dynamicItem = new CompoundContributionItem() {
					protected IContributionItem[] getContributionItems() {

						// Get current selection extensions
						String selectedFileExtension = "";

						ISelectionService selectionService = org.eclipse.ui.PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getSelectionService();

						IStructuredSelection selection = (IStructuredSelection) selectionService.getSelection();

						Object selectedObject = selection.getFirstElement();

						if (selectedObject instanceof IFile) {
							IFile selectedFile = (IFile) selectedObject;
							selectedFileExtension = selectedFile.getFileExtension();
						}

						// Get current applicable transformations
						List<TransformationDesc> transformations = TransformationsRegistry
								.getTransformationsByFileExtension(selectedFileExtension);

						// Create menu items
						IContributionItem[] list = new IContributionItem[transformations.size()];

						TransformationDesc transf;
						for (int i = 0; i < transformations.size(); i++) {

							transf = transformations.get(i);

							Map<String, String> params = new HashMap<String, String>();
							params.put("es.cv.gvcase.trmanager.transfId", transf.getId()); // TODO:
							// Use
							// transformation
							// kinds
							CommandContributionItem commandContribution = new CommandContributionItem(PlatformUI
									.getWorkbench(), null, "es.cv.gvcase.trmanager.transf", params, null, null, null,
									transf.getName(), String.valueOf(i), transf.getTrId(),
									CommandContributionItem.STYLE_PUSH);

							list[i] = commandContribution;

						}
						return list;
					} // getContributionItems
				}; // CompoundContributionItem

				transfMenuManager.add(dynamicItem);

				additions.addContributionItem(transfMenuManager, Expression.TRUE);

			}

		};
		menuService.addContributionFactory(transfMenu);

	}

}
