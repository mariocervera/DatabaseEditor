/***************************************************************************
* Copyright (c) 2007 Conselleria de Infraestructuras y Transporte,
* Generalitat de la Comunitat Valenciana . All rights reserved. This program
* and the accompanying materials are made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors: Mario Cervera Ubeda (Prodevelop) - Initial API and implementation
*
******************************************************************************/
package es.cv.gvcase.mdt.db.navigator.providers;

import java.util.Collections;

import org.eclipse.datatools.modelbase.sql.accesscontrol.AuthorizationIdentifier;
import org.eclipse.datatools.modelbase.sql.accesscontrol.Privilege;
import org.eclipse.datatools.modelbase.sql.constraints.Index;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.tables.PersistentTable;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;

import es.cv.gvcase.mdt.common.actions.registry.ICommandProvider;
import es.cv.gvcase.mdt.common.commands.DeleteCommand;
import es.cv.gvcase.mdt.common.commands.wrappers.EMFtoGMFCommandWrapper;
import es.cv.gvcase.mdt.common.commands.wrappers.GMFtoEMFCommandWrapper;

public class DbCommandProvider implements ICommandProvider {

	public Command getCommand(EObject eobj, String action) {
		
		if(action.equals(ICommandProvider.DELETE_ACTION)) {
			if(eobj instanceof PersistentTable) {
				CompositeCommand cc = new CompositeCommand("Delete PersistentTable");
				TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(eobj);
				if(domain != null) {
					cc.add(new DeleteCommand(domain, "Delete PersistentTable", null,
							Collections.singleton((Object)eobj)));
					addDestroyIndexesCommand(cc, (PersistentTable)eobj, domain);
					addDestroyPrivilegesCommand(cc, (PersistentTable)eobj, domain);
					return new GMFtoEMFCommandWrapper(cc);
				}
			}
		}
		
		return null;
	}
	
	
	
	private void addDestroyIndexesCommand(CompositeCommand cc,
			PersistentTable table, TransactionalEditingDomain domain) {

		//This method adds the commands needed to remove the indexes associated to the table

		Schema schema = (Schema) table.eContainer();
		for (Index index : schema.getIndices()) {
			if (index.getTable() != null && index.getTable().equals(table)) {
				cc.add(new EMFtoGMFCommandWrapper(org.eclipse.emf.edit.command.DeleteCommand.create(
						domain, index)));
			}
		}

	}

	
	private void addDestroyPrivilegesCommand(CompositeCommand cc,
			PersistentTable table, TransactionalEditingDomain domain) {

		Schema schema = (Schema) table.eContainer();
		Database db = (Database) schema.eContainer();
		for (AuthorizationIdentifier aid : db.getAuthorizationIds()) {
			for (Privilege p : aid.getReceivedPrivilege()) {
				if (p.getObject() != null && p.getObject().equals(table)) {
					cc.add(new EMFtoGMFCommandWrapper(org.eclipse.emf.edit.command.DeleteCommand
							.create(domain, p)));
				}
			}
		}

	}
	
}
