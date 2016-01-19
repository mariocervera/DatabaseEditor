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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.datatools.modelbase.sql.accesscontrol.AuthorizationIdentifier;
import org.eclipse.datatools.modelbase.sql.accesscontrol.Group;
import org.eclipse.datatools.modelbase.sql.accesscontrol.Privilege;
import org.eclipse.datatools.modelbase.sql.accesscontrol.Role;
import org.eclipse.datatools.modelbase.sql.accesscontrol.SQLAccessControlFactory;
import org.eclipse.datatools.modelbase.sql.accesscontrol.SQLAccessControlPackage;
import org.eclipse.datatools.modelbase.sql.accesscontrol.User;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.PropertySource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import es.cv.gvcase.mdt.common.composites.TableMembersComposite;
import es.cv.gvcase.mdt.db.diagram.sheet.SqlmodelAbstractPropertySection;
import es.cv.gvcase.mdt.db.diagram.sheet.SqlmodelPropertySectionControlsFactory;

public class GrantedPrivilegesPropertySection extends
		SqlmodelAbstractPropertySection implements IPropertySourceProvider {

	/** Controls ; widgets. */
	/**
	 * Combo containing the objects (Tables, Views ...)
	 */
	// CCombo comboObjects;
	TableMembersComposite memberComposite;

	/** Objects mapping. */
	Map<String, SQLObject> mapObjects;

	/**
	 * Combo containing privileges
	 */
	CCombo comboPrivileges;

	public IPropertySource getPropertySource(Object object) {
		if (object instanceof IPropertySource) {
			return (IPropertySource) object;
		}
		AdapterFactory af = getAdapterFactory(object);
		if (af != null) {
			IItemPropertySource ips = (IItemPropertySource) af.adapt(object,
					IItemPropertySource.class);
			if (ips != null) {
				return new PropertySource(object, ips);
			}
		}
		if (object instanceof IAdaptable) {
			return (IPropertySource) ((IAdaptable) object)
					.getAdapter(IPropertySource.class);
		}
		return null;
	}

	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);

		getObjects();
		fillPrivilegeCombo();
	}

	protected AdapterFactory getAdapterFactory(Object object) {
		if (getEditingDomain() instanceof AdapterFactoryEditingDomain) {
			return ((AdapterFactoryEditingDomain) getEditingDomain())
					.getAdapterFactory();
		}
		TransactionalEditingDomain editingDomain = TransactionUtil
				.getEditingDomain(object);
		if (editingDomain != null) {
			return ((AdapterFactoryEditingDomain) editingDomain)
					.getAdapterFactory();
		}
		return null;
	}

	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		mapObjects = new HashMap<String, SQLObject>();
		canvasComposite.setLayout(new FormLayout());

		createObjectPrivilegeInputAndTable();
	}

	/*
	 * Return the name of the SQLObject class and the name of the object
	 */
	private String getName(SQLObject object) {
		if (object == null)
			return "";

		String object_class = object.eClass().getName();
		String object_name = (object.getName() != null ? object.getName() : "");

		return ("<" + object_class + "> " + object_name);
	}

	/*
	 * Create the controls
	 */
	private void createObjectPrivilegeInputAndTable() {
		Label label2 = SqlmodelPropertySectionControlsFactory.createLabel(
				"Privilege: ", getWidgetFactory(), canvasComposite);
		SqlmodelPropertySectionControlsFactory.setFormLayoutData(label2,
				new Point(1, 0), new Point(10, 8), 5);

		comboPrivileges = getWidgetFactory().createCCombo(canvasComposite,
				SWT.BORDER | SWT.READ_ONLY);
		SqlmodelPropertySectionControlsFactory.setFormLayoutData(
				comboPrivileges, new Point(10, 0), new Point(100, 8), 5);

		comboPrivileges.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				refresh();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// nothing to do
			}
		});

		memberComposite = new TableMembersComposite(canvasComposite,
				SWT.BORDER, getWidgetFactory(),
				SQLAccessControlPackage.eINSTANCE
						.getAuthorizationIdentifier_ReceivedPrivilege(),
				"Granted Privileges") {
			@Override
			protected void executeAddCommand(Collection<?> objectsToAdd) {
				handleAddObjectPrivilegeButtonSelected((List<SQLObject>) objectsToAdd);
			}

			@Override
			protected void executeRemoveCommand(Collection<?> objectsToRemove) {
				handleRemoveObjectPrivilegeButtonSelected((List<SQLObject>) objectsToRemove);
			}

			@Override
			protected EList<EObject> getMemberElements() {
				return getMemberElementList();
			}
		};
		memberComposite.setLabelProvider(new AdapterFactoryLabelProvider(
				new ComposedAdapterFactory(
						ComposedAdapterFactory.Descriptor.Registry.INSTANCE)));
		memberComposite.createWidgets(canvasComposite, getWidgetFactory());

		FormData data = new FormData();
		data.top = new FormAttachment(comboPrivileges, 5);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		memberComposite.setLayoutData(data);

		memberComposite.setSectionData(canvasComposite);
		getWidgetFactory().adapt(memberComposite);

		memberComposite.hookListeners();
	}

	/**
	 * Gets the subjects.
	 */
	protected void getObjects() {
		mapObjects.clear();

		AuthorizationIdentifier aid = getSelectedAuthorizationIdentifier();
		if (aid == null) {
			return;
		}

		List<?> objects = ((Schema) aid.getDatabase().getSchemas().get(0))
				.getTables();
		if (objects.size() <= 0) {
			return;
		}

		for (Object o : objects) {
			SQLObject object = (SQLObject) o;
			mapObjects.put(getName(object), object);
		}
	}

	/**
	 * Gets the selected role/user/group.
	 * 
	 * @return the selected authorization identifier
	 */
	protected AuthorizationIdentifier getSelectedAuthorizationIdentifier() {
		ISelection selection = getSelection();
		if (selection != null && !selection.isEmpty()) {
			if (selection instanceof StructuredSelection) {
				Object first = ((StructuredSelection) selection)
						.getFirstElement();
				if (first instanceof AuthorizationIdentifier) {
					return (AuthorizationIdentifier) first;
				}
			}
		}
		return null;
	}

	/**
	 * Fill the object and privilege combos with the proper values
	 */
	private void fillPrivilegeCombo() {
		comboPrivileges.removeAll();
		comboPrivileges.add("SELECT");
		comboPrivileges.add("INSERT");
		comboPrivileges.add("DELETE");
		comboPrivileges.add("UPDATE");
		comboPrivileges.add("ALL");
		comboPrivileges.select(0);

		return;
	}

	@Override
	public void refresh() {
		if (memberComposite != null) {
			if (getCandidateElements() != null) {
				memberComposite.setCandidateElements(getCandidateElements());
			}

			memberComposite.setElement(getEObject());
			try {
				if (getEditingDomain() != null) {
					memberComposite.setEMFEditDomain(getEditingDomain());
				}
				memberComposite.refresh();
			} catch (IllegalArgumentException e) {
			}
		}
	}

	protected Collection<?> getCandidateElements() {
		List<SQLObject> list = new ArrayList<SQLObject>();

		for (Entry<String, SQLObject> entry : mapObjects.entrySet()) {
			list.add(entry.getValue());
		}

		AuthorizationIdentifier aid = getSelectedAuthorizationIdentifier();

		for (Privilege p : aid.getReceivedPrivilege()) {
			if (list.contains(p.getObject())
					&& (p.getName().contains(comboPrivileges.getText()) || contains_all_privilege(
							aid, p.getObject()))) {
				list.remove(p.getObject());
			}
		}
		return list;
	}

	protected EList<EObject> getMemberElementList() {
		EList<EObject> list = new BasicEList<EObject>();

		AuthorizationIdentifier aid = getSelectedAuthorizationIdentifier();

		for (Privilege p : aid.getReceivedPrivilege()) {
			if (p.getName().contains(comboPrivileges.getText())) {
				list.add(p.getObject());
			}
		}

		return list;
	}

	/**
	 * This method is invoked when the add button is pressed (subject -
	 * privilege)
	 */
	protected void handleAddObjectPrivilegeButtonSelected(
			List<SQLObject> objectsToAdd) {
		for (SQLObject object : objectsToAdd) {
			String selected_privilege = comboPrivileges.getText();

			AuthorizationIdentifier aid = getSelectedAuthorizationIdentifier();
			if (aid == null) {
				return;
			}

			boolean contains_all_privilege = contains_all_privilege(aid, object);

			if (!contains_all_privilege) {
				// Update the model
				addNewPrivilegeToObject(object, selected_privilege, aid);
			} else {
				String aid_string = "";
				if (aid instanceof Group)
					aid_string = "group";
				else if (aid instanceof User)
					aid_string = "user";
				else if (aid instanceof Role)
					aid_string = "role";

				writeErrorMessageOnStatusBar("The "
						+ aid_string
						+ " has already been granted with all the privileges on the selected object");
			}
		}

		refresh();
	}

	private void addNewPrivilegeToObject(SQLObject object,
			String privilege_name, AuthorizationIdentifier aid) {
		EditingDomain domain = getEditingDomain();
		if (domain == null)
			return;

		Privilege privilege = SQLAccessControlFactory.eINSTANCE
				.createPrivilege();

		// Remove all the authorization identifier's privileges on the selected
		// table as I am granting the privilege ALL
		if (privilege_name.equals("ALL")) {
			remove_all_privileges(object, aid);
		}

		CompoundCommand cc = new CompoundCommand("Add new privilege");

		Command command = SetCommand.create(domain, privilege,
				EcorePackage.eINSTANCE.getENamedElement_Name(), privilege_name);
		cc.append(command);

		command = SetCommand
				.create(domain, privilege, SQLAccessControlPackage.eINSTANCE
						.getPrivilege_Object(), object);
		cc.append(command);

		command = AddCommand.create(domain, aid,
				SQLAccessControlPackage.eINSTANCE
						.getAuthorizationIdentifier_ReceivedPrivilege(),
				privilege);
		cc.append(command);

		domain.getCommandStack().execute(cc);

		return;
	}

	private void remove_all_privileges(SQLObject object,
			AuthorizationIdentifier aid) {
		EditingDomain domain = getEditingDomain();
		if (domain == null)
			return;

		// Remove the privileges from the model
		ArrayList<Privilege> privileges_to_delete = new ArrayList<Privilege>();

		for (Privilege p : aid.getReceivedPrivilege()) {
			if (p.getObject().equals(object)) {
				privileges_to_delete.add(p);
			}
		}

		Command command = DeleteCommand.create(domain, privileges_to_delete);

		if (command != null && command.canExecute()) {
			domain.getCommandStack().execute(command);
		}
	}

	/*
	 * This method is used to check if the authorization identifier has been
	 * granted with the privilege ALL on the selected object
	 */
	protected boolean contains_all_privilege(AuthorizationIdentifier ai,
			SQLObject object) {
		for (Object o : ai.getReceivedPrivilege()) {
			if (o instanceof Privilege) {
				Privilege privilege = (Privilege) o;
				if (privilege.getName().equals("ALL")
						&& privilege.getObject().equals(object))
					return true;
			}
		}

		return false;
	}

	/**
	 * This method is invoked when the remove button is pressed (object -
	 * privilege)
	 */
	protected void handleRemoveObjectPrivilegeButtonSelected(
			List<SQLObject> objectsToRemove) {
		AuthorizationIdentifier aid = getSelectedAuthorizationIdentifier();
		List<Privilege> privileges = aid.getReceivedPrivilege();
		CompoundCommand cc = new CompoundCommand("Remove privileges");

		for (SQLObject object : objectsToRemove) {
			// Remove the selected items from the model
			for (Privilege p : privileges) {
				if (p.getObject().equals(object)) {
					if (p.getObject().equals(object)) {
						Command command = DeleteCommand.create(
								getEditingDomain(), p);
						cc.append(command);
					}
				}
			}
		}

		if (cc.canExecute()) {
			getEditingDomain().getCommandStack().execute(cc);
		}

		refresh();
	}
}
