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
import org.eclipse.datatools.modelbase.sql.accesscontrol.SQLAccessControlPackage;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
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

public class RolesPropertySection extends AbstractCollectionPropertySection {

	@Override
	protected EditingDomain getEditingDomain() {
		try {
			return super.getEditingDomain();
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}
	
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
				CompoundCommand cc = new CompoundCommand("Add new element");
				for (Object objectToAdd : objectsToAdd) {
					if (!isAlreadyAdded(objectToAdd)) {
						// **** Now we create an object of the type referenced
						// (or contained) by the feature ****
						EObject newEObject = null;
						EClass eclass = null;
						if (((EStructuralFeature) getFeature()).getEType() instanceof EClass) {
							eclass = (EClass) ((EStructuralFeature) getFeature())
									.getEType();
						}
						EObject container = eclass.eContainer();
						if (container instanceof EPackage) {
							EPackage epackage = (EPackage) container;
							EFactory factory = epackage.getEFactoryInstance();
							if (factory != null) {
								newEObject = factory.create(eclass);
							}
						}

						if (newEObject != null) {

							Command command1 = SetCommand.create(
									getEditingDomain(), newEObject,
									membersFeature(), objectToAdd);
							Command command2 = AddCommand.create(
									getEditingDomain(), getEObject(),
									getFeature(), newEObject);
							cc.append(command1);
							cc.append(command2);

						}
					}
				}
				getEditingDomain().getCommandStack().execute(cc);
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
					Object objectToRemoveFromModel = null;
					EList<EObject> objects = getFeatureAsList();
					EStructuralFeature feature = membersFeature();
					for (EObject o : objects) {
						if (o.equals(objectToRemove)) {
							objectToRemoveFromModel = o;
						}
					}

					if (objectToRemoveFromModel != null) {
						command.append(DeleteCommand.create(domain,
								objectToRemoveFromModel));

					}
				}
				domain.getCommandStack().execute(command);
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
		EObject eobject = getEObject();
		if (eobject == null)
			return null;

		Object featureValue = eobject.eGet(getFeature());

		EList<EObject> result = new BasicEList<EObject>();

		if (featureValue instanceof EList) {
			EList<EObject> list = (EList<EObject>) featureValue;
			EStructuralFeature destFeature = membersFeature();
			for (EObject o : list) {
				result.add((EObject) o.eGet(destFeature));
			}
		}

		return result;
	}

	@Override
	protected EList<EObject> getCandidateElements() {
		EList<EObject> roles = new BasicEList<EObject>();

		if (getEObject() != null) {
			SQLObject selectedObject = (SQLObject) getEObject();

			SQLObject rootElement = (SQLObject) selectedObject.eContainer();
			while (!(rootElement instanceof Database)) {
				rootElement = (SQLObject) rootElement.eContainer();
			}

			Database db = (Database) rootElement;

			for (AuthorizationIdentifier aid : db.getAuthorizationIds()) {
				if (aid instanceof Role)
					roles.add((Role) aid);
			}
		}
		return roles;
	}

	@Override
	protected EStructuralFeature getFeature() {
		return SQLAccessControlPackage.eINSTANCE
				.getAuthorizationIdentifier_ReceivedRoleAuthorization();
	}

	@Override
	protected String getLabelText() {
		return "Roles";
	}

	protected EStructuralFeature membersFeature() {
		return SQLAccessControlPackage.eINSTANCE.getRoleAuthorization_Role();
	}

}
