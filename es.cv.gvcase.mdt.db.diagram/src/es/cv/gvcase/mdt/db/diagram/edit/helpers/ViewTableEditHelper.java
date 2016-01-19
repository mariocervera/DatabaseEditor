/***************************************************************************
 * Copyright (c) 2007, 2009 Conselleria de Infraestructuras y Transporte,
 * Generalitat de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Mario Cervera Ubeda (Integranova)
 * 		Francisco Javier Cano Muñoz (Prodevelop) - created configure command
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.diagram.edit.helpers;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.datatools.modelbase.sql.tables.PersistentTable;
import org.eclipse.datatools.modelbase.sql.tables.ViewTable;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;

/**
 * Will configure a newly created {@link PersistentTable} to have its Generated
 * AdditionalAttribute to false.
 * 
 * @generated
 */
public class ViewTableEditHelper extends SqlmodelBaseEditHelper {

	/**
	 * @NOT-generated
	 */
	@Override
	protected ICommand getConfigureCommand(ConfigureRequest req) {
		EObject elementToConfigure = req.getElementToConfigure();
		TransactionalEditingDomain domain = req.getEditingDomain();
		if (elementToConfigure instanceof ViewTable && domain != null) {
			// every newly created ViewTable must have its Generated
			// Property from AdditionalAttributes set to false.
			final ViewTable table = (ViewTable) elementToConfigure;
			AbstractTransactionalCommand command = new AbstractTransactionalCommand(
					domain, "Configure ViewTable", null) {

				@Override
				protected CommandResult doExecuteWithResult(
						IProgressMonitor monitor, IAdaptable info)
						throws ExecutionException {
					// set the Generated attribute from the AdditionalAttributes
					// to false.
					EAnnotation annotation = EcoreFactory.eINSTANCE
							.createEAnnotation();
					annotation.setSource("AdditionalAttributes");
					annotation.getDetails().put("Generated", "false");
					table.getEAnnotations().add(annotation);
					return null;
				}
			};
			return command;
		}
		// default configure handling
		return super.getConfigureCommand(req);
	}

}
