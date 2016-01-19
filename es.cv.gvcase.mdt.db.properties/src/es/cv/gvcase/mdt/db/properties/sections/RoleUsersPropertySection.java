/*******************************************************************************
 * Copyright (c) 2008 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Mario Cervera Ubeda (Prodevelop) - initial API and implementation
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.properties.sections;

import java.util.Collection;

import org.eclipse.datatools.modelbase.sql.accesscontrol.AuthorizationIdentifier;
import org.eclipse.datatools.modelbase.sql.accesscontrol.Role;
import org.eclipse.datatools.modelbase.sql.accesscontrol.RoleAuthorization;
import org.eclipse.datatools.modelbase.sql.accesscontrol.SQLAccessControlFactory;
import org.eclipse.datatools.modelbase.sql.accesscontrol.SQLAccessControlPackage;
import org.eclipse.datatools.modelbase.sql.accesscontrol.User;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;

import es.cv.gvcase.mdt.common.composites.MembersComposite;
import es.cv.gvcase.mdt.common.composites.TableMembersComposite;
import es.cv.gvcase.mdt.common.sections.AbstractCollectionPropertySection;

public class RoleUsersPropertySection extends AbstractCollectionPropertySection {

	@Override
	public void refresh() {
		getMembersComposite().setCandidateElements(getCandidateElements());
		super.refresh();
	}

	@Override
	protected MembersComposite createMemberComposite() {
		MembersComposite member = new TableMembersComposite(groupMembers,
				groupMembers.getStyle(), getWidgetFactory(), getFeature(),
				getMembersText()) {
			@Override
			protected void executeAddCommand(Collection<?> objectsToAdd) {
				if (objectsToAdd.isEmpty()) {
					return;
				}
				if (getEditingDomain() == null) {
					return;
				}
				CompoundCommand cc = new CompoundCommand(
						"Add role authorization");
				for (Object objectToAdd : objectsToAdd) {

					if (!isAlreadyAdded(objectToAdd)) {

						if (objectToAdd instanceof User) {
							User user = (User) objectToAdd;
							EObject eobject = getEObject();
							if (eobject instanceof Role) {
								Role role = (Role) eobject;
								RoleAuthorization ra = SQLAccessControlFactory.eINSTANCE
										.createRoleAuthorization();

								cc.append(SetCommand.create(getEditingDomain(),
										ra, membersFeature(), role));
								cc.append(AddCommand.create(getEditingDomain(),
										user, getFeature(), ra));
							}
						}
					}
					getEditingDomain().getCommandStack().execute(cc);
				}
			}

			@Override
			protected void executeRemoveCommand(Collection<?> objectsToRemove) {
				if (objectsToRemove.isEmpty()) {
					return;
				}

				EditingDomain domain = getEditingDomain();
				if (domain == null)
					return;

				CompoundCommand command = new CompoundCommand();

				for (Object objectToRemove : objectsToRemove) {
					if (objectToRemove instanceof User) {
						User user = (User) objectToRemove;
						EObject eobject = getEObject();
						if (eobject instanceof Role) {
							Role role = (Role) eobject;
							RoleAuthorization ra = getRoleAuthorization(user,
									role);
							command.append(DeleteCommand.create(domain, ra));
						}
					}
				}
				domain.getCommandStack().execute(command);
			}

			@Override
			protected EList<EObject> getMemberElements() {
				return getMembersFeatureAsList();
			}
		};

		member.setEnableOrdering(enableOrdering());
		member.setLabelProvider(getLabelProvider());
		member.createWidgets(groupMembers, getWidgetFactory());

		return member;
	}

	protected boolean isAlreadyAdded(Object objectToAdd) {
		return getMembersFeatureAsList().contains(objectToAdd);
	}

	@Override
	protected IBaseLabelProvider getLabelProvider() {
		return new AdapterFactoryLabelProvider(new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
	}

	protected EList<EObject> getMembersFeatureAsList() {
		EList<EObject> users = new BasicEList<EObject>();

		if (getEObject() != null) {
			SQLObject selectedObject = (SQLObject) getEObject();

			SQLObject rootElement = (SQLObject) selectedObject.eContainer();
			while (!(rootElement instanceof Database)) {
				rootElement = (SQLObject) rootElement.eContainer();
			}

			Database db = (Database) rootElement;

			for (AuthorizationIdentifier aid : db.getAuthorizationIds()) {
				if (aid instanceof User) {
					User user = (User) aid;
					for (RoleAuthorization ra : user
							.getReceivedRoleAuthorization()) {
						if (ra.getRole().equals(selectedObject)) {
							users.add(user);
						}
					}
				}
			}
		}
		return users;
	}

	@Override
	protected EList<EObject> getCandidateElements() {
		EList<EObject> users = new BasicEList<EObject>();

		if (getEObject() != null) {
			SQLObject selectedObject = (SQLObject) getEObject();
			SQLObject rootElement = (SQLObject) selectedObject.eContainer();
			while (!(rootElement instanceof Database)) {
				rootElement = (SQLObject) rootElement.eContainer();
			}

			Database db = (Database) rootElement;

			for (AuthorizationIdentifier aid : db.getAuthorizationIds()) {
				if (aid instanceof User)
					users.add((User) aid);
			}
		}
		return users;
	}

	@Override
	protected EStructuralFeature getFeature() {
		return SQLAccessControlPackage.eINSTANCE
				.getAuthorizationIdentifier_ReceivedRoleAuthorization();
	}

	@Override
	protected String getLabelText() {
		return "Users";
	}

	protected EStructuralFeature membersFeature() {
		return SQLAccessControlPackage.eINSTANCE.getRoleAuthorization_Role();
	}

	private RoleAuthorization getRoleAuthorization(User user, Role role) {
		for (RoleAuthorization ra : user.getReceivedRoleAuthorization()) {
			if (ra.getRole().equals(role))
				return ra;
		}
		return null;
	}

}
