/***************************************************************************
 * Copyright (c) 2008 Conselleria de Infraestructuras y Transporte,
 * Generalitat de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Mario Cervera Ubeda (Integranova) - initial API and implementation
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.diagram.sheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.datatools.modelbase.sql.accesscontrol.AuthorizationIdentifier;
import org.eclipse.datatools.modelbase.sql.accesscontrol.Group;
import org.eclipse.datatools.modelbase.sql.accesscontrol.Privilege;
import org.eclipse.datatools.modelbase.sql.accesscontrol.Role;
import org.eclipse.datatools.modelbase.sql.accesscontrol.SQLAccessControlFactory;
import org.eclipse.datatools.modelbase.sql.accesscontrol.SQLAccessControlPackage;
import org.eclipse.datatools.modelbase.sql.accesscontrol.User;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class SqlmodelAbstractPrivilegesSection extends
		SqlmodelAbstractPropertySection {

	/** Controls ; widgets. */
	/**
	 * Combo containing the subject
	 */
	protected CCombo comboSubjects = null;

	/** Subjects mapping. */
	protected Map<String, AuthorizationIdentifier> mapSubjects = new HashMap<String, AuthorizationIdentifier>();

	/**
	 * Combo containing privileges
	 */
	protected CCombo comboPrivileges = null;

	/**
	 * Table containing subjects - privileges
	 */
	Table tableSubjectsPrivileges = null;

	/** Add / Remove buttons (subject - privilege). */
	Button buttonAddSubjectPrivilege = null;
	Button buttonRemoveSubjectPrivilege = null;

	protected Object transformSelection(Object selected) {
		return super.transformSelection(selected);
	}

	public void setInput(IWorkbenchPart part, ISelection selection) {

		super.setInput(part, selection);

		// getSubjects();
		// fillRoleAndPrivilegeCombos();
		// fillRolePrivilegeTable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seees.cv.gvcase.mdt.db.diagram.sheet.SqlmodelAbstractPropertySection#
	 * createControls(org.eclipse.swt.widgets.Composite,
	 * org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {

		super.createControls(parent, tabbedPropertySheetPage);
		canvasComposite.setLayout(new FormLayout());

		// createSubjectPrivilegeInput();
	}

	/**
	 * Creates the combos and button to add a new subject - privilege to the
	 * table
	 */
	protected void createSubjectPrivilegeInput(String subject) {

		Label label = SqlmodelPropertySectionControlsFactory.createLabel(
				subject + ": ", getWidgetFactory(), canvasComposite);
		SqlmodelPropertySectionControlsFactory.setFormLayoutData(label,
				new Point(0, 0), new Point(10, 8), 2);

		comboSubjects = getWidgetFactory().createCCombo(canvasComposite,
				SWT.BORDER | SWT.READ_ONLY);
		SqlmodelPropertySectionControlsFactory.setFormLayoutData(comboSubjects,
				new Point(10, 0), new Point(90, 8), 2);

		Label label2 = SqlmodelPropertySectionControlsFactory.createLabel(
				"Privilege: ", getWidgetFactory(), canvasComposite);
		SqlmodelPropertySectionControlsFactory.setFormLayoutData(label2,
				new Point(0, 8), new Point(10, 16), 2);

		comboPrivileges = getWidgetFactory().createCCombo(canvasComposite,
				SWT.BORDER | SWT.READ_ONLY);
		SqlmodelPropertySectionControlsFactory.setFormLayoutData(
				comboPrivileges, new Point(10, 8), new Point(90, 16), 2);

		buttonAddSubjectPrivilege = SqlmodelPropertySectionControlsFactory
				.createButton("Add", getWidgetFactory(), canvasComposite);
		SqlmodelPropertySectionControlsFactory.setFormLayoutData(
				buttonAddSubjectPrivilege, new Point(90, 8),
				new Point(100, 16), 2);
		buttonAddSubjectPrivilege.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// XXX: should never be invoked
				return;
			}

			public void widgetSelected(SelectionEvent e) {
				handleAddSubjectPrivilegeButtonSelected();
			}
		});

		tableSubjectsPrivileges = SqlmodelPropertySectionControlsFactory
				.createTable(getWidgetFactory(), canvasComposite);
		SqlmodelPropertySectionControlsFactory
				.setFormLayoutData(tableSubjectsPrivileges, new Point(0, 20),
						new Point(90, 68), 2);
		tableSubjectsPrivileges.setHeaderVisible(true);
		tableSubjectsPrivileges.setLinesVisible(true);
		TableColumn column1 = new TableColumn(tableSubjectsPrivileges, SWT.LEFT);
		column1.setText(subject);
		column1.setWidth(250);
		TableColumn column2 = new TableColumn(tableSubjectsPrivileges, SWT.LEFT);
		column2.setText("Privilege");
		column2.setWidth(250);

		final String subject2 = subject;
		buttonRemoveSubjectPrivilege = SqlmodelPropertySectionControlsFactory
				.createButton("Remove", getWidgetFactory(), canvasComposite);
		SqlmodelPropertySectionControlsFactory.setFormLayoutData(
				buttonRemoveSubjectPrivilege, new Point(90, 60), new Point(100,
						68), 2);
		buttonRemoveSubjectPrivilege
				.addSelectionListener(new SelectionListener() {
					public void widgetDefaultSelected(SelectionEvent e) {
						// XXX: should never be invoked
						return;
					}

					public void widgetSelected(SelectionEvent e) {
						handleRemoveSubjectPrivilegeButtonSelected(subject2);
					}
				});

	}

	/**
	 * Gets the subjects.
	 */
	protected void getSubjects(String subject) {

		mapSubjects.clear();

		org.eclipse.datatools.modelbase.sql.tables.Table table = getSelectedTable();
		if (table == null) {
			return;
		}

		java.util.List authorizationsIds = table.getSchema().getDatabase()
				.getAuthorizationIds();
		if (authorizationsIds.size() <= 0) {
			return;
		}

		for (Object o : authorizationsIds) {

			AuthorizationIdentifier aid = null;

			if (subject.equals("Role") && o instanceof Role)
				aid = (Role) o;
			else if (subject.equals("User") && o instanceof User)
				aid = (User) o;
			else if (subject.equals("Group") && o instanceof Group)
				aid = (Group) o;

			if (aid != null) {
				String name = aid.getName();
				name = (name != null ? name : "");
				mapSubjects.put(name, aid);
			}
		}
	}

	/**
	 * Gets the selected table.
	 * 
	 * @return the selected table
	 */
	protected org.eclipse.datatools.modelbase.sql.tables.Table getSelectedTable() {

		ISelection selection = getSelection();
		if (selection != null && !selection.isEmpty()) {
			if (selection instanceof StructuredSelection) {
				Object first = ((StructuredSelection) selection)
						.getFirstElement();
				if (first instanceof org.eclipse.datatools.modelbase.sql.tables.Table) {
					return (org.eclipse.datatools.modelbase.sql.tables.Table) first;
				}
			}
		}
		return null;
	}

	/**
	 * Fill the subject and privilege combos with the proper values
	 */
	protected void fillSubjectAndPrivilegeCombos() {

		comboSubjects.removeAll();
		for (String name : mapSubjects.keySet()) {
			comboSubjects.add(name);
		}
		if (comboSubjects.getItemCount() > 0) {
			comboSubjects.select(0);
		}

		comboPrivileges.removeAll();
		comboPrivileges.add("SELECT");
		comboPrivileges.add("INSERT");
		comboPrivileges.add("DELETE");
		comboPrivileges.add("UPDATE");
		comboPrivileges.add("ALL");
		// comboPrivileges.add("ALTER");
		// comboPrivileges.add("REFERENCES");
		// comboPrivileges.add("INDEX");
		comboPrivileges.select(0);

		return;
	}

	/**
	 * Fill the subject - privilege table
	 */
	protected void fillSubjectPrivilegeTable(String subject) {

		tableSubjectsPrivileges.removeAll();

		org.eclipse.datatools.modelbase.sql.tables.Table table = getSelectedTable();
		if (table == null) {
			return;
		}

		java.util.List authorizationsIds = table.getSchema().getDatabase()
				.getAuthorizationIds();
		if (authorizationsIds.size() <= 0) {
			return;
		}

		for (Object o : authorizationsIds) {
			if ((subject.equals("Role") && o instanceof Role)
					|| (subject.equals("User") && o instanceof User)
					|| (subject.equals("Group") && o instanceof Group)) {
				AuthorizationIdentifier aid = (AuthorizationIdentifier) o;
				for (Object ob : aid.getReceivedPrivilege()) {
					if (ob instanceof Privilege) {
						Privilege p = (Privilege) ob;
						if (p.getObject() != null
								&& p.getObject().equals(table)) {
							// Add the item to the table
							TableItem tableItem = new TableItem(
									tableSubjectsPrivileges, 0);
							tableItem.setText(new String[] { aid.getName(),
									p.getName() });
						}
					}
				}
			}
		}

	}

	/**
	 * This method is invoked when the add button is pressed (subject -
	 * privilege)
	 */
	protected void handleAddSubjectPrivilegeButtonSelected() {

		String selected_subject = comboSubjects.getText();
		AuthorizationIdentifier aid = mapSubjects.get(selected_subject);

		String selected_privilege = comboPrivileges.getText();

		// The table item can't be repeated
		TableItem[] items = tableSubjectsPrivileges.getItems();
		boolean repeated = false;
		for (int i = 0; i < items.length; i++) {
			if (items[i].getText(0).equals(selected_subject)
					&& items[i].getText(1).equals(selected_privilege)) {
				repeated = true;
			}
		}
		org.eclipse.datatools.modelbase.sql.tables.Table selected_table = getSelectedTable();
		boolean contains_all_privilege = contains_all_privilege(aid,
				selected_table);

		if (!repeated && aid != null && !contains_all_privilege) {

			// Update the model
			addNewPrivilegeToSubjectOnTable(selected_table, selected_privilege,
					aid);

			// Add the item to the table
			TableItem tableItem = new TableItem(tableSubjectsPrivileges, 0);
			tableItem.setText(new String[] { selected_subject,
					selected_privilege });
		} else if (repeated) {
			String aid_string = "";
			if (aid instanceof Group)
				aid_string = "group";
			else if (aid instanceof User)
				aid_string = "user";
			else if (aid instanceof Role)
				aid_string = "role";

			writeErrorMessageOnStatusBar("The privilege has already been granted to the selected "
					+ aid_string);
		} else if (aid == null) {
			writeErrorMessageOnStatusBar("No grantee of the privilege selected");
		} else if (contains_all_privilege) {
			writeErrorMessageOnStatusBar("The selected grantee has already been granted with all the privileges on the selected element");
		}

	}

	protected void addNewPrivilegeToSubjectOnTable(
			org.eclipse.datatools.modelbase.sql.tables.Table selected_table,
			String privilege_name, AuthorizationIdentifier aid) {

		EditingDomain domain = getEditingDomain();
		if (domain == null)
			return;

		Privilege privilege = SQLAccessControlFactory.eINSTANCE
				.createPrivilege();
		if (privilege == null)
			return;

		// Remove all the authorization identifier's privileges on the selected
		// table as I am granting the privilege ALL
		if (privilege_name.equals("ALL")) {
			remove_all_privileges(selected_table, aid);
		}

		CompoundCommand cc = new CompoundCommand("Add new privilege");

		Command command = SetCommand.create(domain, privilege,
				EcorePackage.eINSTANCE.getENamedElement_Name(), privilege_name);
		cc.append(command);

		command = SetCommand.create(domain, privilege,
				SQLAccessControlPackage.eINSTANCE.getPrivilege_Object(),
				selected_table);
		cc.append(command);

		command = AddCommand.create(domain, aid,
				SQLAccessControlPackage.eINSTANCE
						.getAuthorizationIdentifier_ReceivedPrivilege(),
				privilege);
		cc.append(command);

		domain.getCommandStack().execute(cc);

		return;
	}

	/**
	 * This method is invoked when the remove button is pressed (subject -
	 * privilege)
	 */
	protected void handleRemoveSubjectPrivilegeButtonSelected(String subject) {

		TableItem[] selected_items = tableSubjectsPrivileges.getSelection();
		int[] indices = tableSubjectsPrivileges.getSelectionIndices();

		if (indices.length > 0) {
			// Remove the selected items from the model
			removePrivilegeSubject(selected_items, subject);

			// Remove the items
			tableSubjectsPrivileges.remove(indices);
		} else {
			writeErrorMessageOnStatusBar("No items selected");
		}
	}

	protected void removePrivilegeSubject(TableItem[] selected_items,
			String subject) {

		org.eclipse.datatools.modelbase.sql.tables.Table table = getSelectedTable();
		if (table == null) {
			return;
		}

		java.util.List authorizationsIds = table.getSchema().getDatabase()
				.getAuthorizationIds();
		if (authorizationsIds.size() <= 0) {
			return;
		}

		EditingDomain domain = getEditingDomain();
		if (domain == null)
			return;

		for (int i = 0; i < selected_items.length; i++) {

			String subject_name = selected_items[i].getText(0);
			String privilege_name = selected_items[i].getText(1);

			AuthorizationIdentifier aid = null;
			Privilege privilege = null;

			CompoundCommand cc = new CompoundCommand("Remove privileges");

			for (Object o : authorizationsIds) {
				if ((subject.equals("Role") && o instanceof Role && ((Role) o)
						.getName().equals(subject_name))
						|| (subject.equals("User") && o instanceof User && ((User) o)
								.getName().equals(subject_name))
						|| (subject.equals("Group") && o instanceof Group && ((Group) o)
								.getName().equals(subject_name))) {
					for (Object ob : ((AuthorizationIdentifier) o)
							.getReceivedPrivilege()) {
						if (ob instanceof Privilege
								&& ((Privilege) ob).getName().equals(
										privilege_name)) {
							aid = (AuthorizationIdentifier) o;
							privilege = (Privilege) ob;

							Command command = DeleteCommand.create(domain,
									privilege);
							cc.append(command);
						}
					}
				}
			}

			if (!cc.isEmpty()) {
				domain.getCommandStack().execute(cc);
			}

		}

		return;
	}

	/*
	 * This method is used to check if the authorization identifier has been
	 * granted with the privilege ALL on the selected table
	 */
	protected boolean contains_all_privilege(AuthorizationIdentifier ai,
			org.eclipse.datatools.modelbase.sql.tables.Table selected_table) {

		if (ai == null) {
			return false;
		}

		for (Object o : ai.getReceivedPrivilege()) {
			if (o instanceof Privilege) {
				Privilege privilege = (Privilege) o;
				if (privilege.getName().equals("ALL")
						&& privilege.getObject().equals(selected_table))
					return true;
			}
		}

		return false;
	}

	protected void remove_all_privileges(
			org.eclipse.datatools.modelbase.sql.tables.Table selected_table,
			AuthorizationIdentifier aid) {

		EditingDomain domain = getEditingDomain();
		if (domain == null)
			return;

		// Remove the items from the table

		TableItem[] items = tableSubjectsPrivileges.getItems();
		ArrayList<Integer> indices_aux = new ArrayList<Integer>();

		for (int i = 0; i < items.length; i++) {
			if (items[i].getText(0).equals(aid.getName()))
				indices_aux.add(i);
		}

		int[] indices = new int[indices_aux.size()];
		for (int i = 0; i < indices_aux.size(); i++)
			indices[i] = indices_aux.get(i).intValue();

		tableSubjectsPrivileges.remove(indices);

		// Remove the privileges from the model

		ArrayList<Privilege> privileges_to_delete = new ArrayList<Privilege>();

		for (Object o : aid.getReceivedPrivilege()) {
			if (o instanceof Privilege) {
				Privilege privilege = (Privilege) o;

				if (privilege.getObject().equals(selected_table)) {
					privileges_to_delete.add(privilege);
				}
			}
		}

		CompoundCommand cc = new CompoundCommand("Remove privileges");

		for (Privilege p : privileges_to_delete) {
			Command command = RemoveCommand.create(domain, aid,
					SQLAccessControlPackage.eINSTANCE
							.getAuthorizationIdentifier_ReceivedPrivilege(), p);
			cc.append(command);
		}

		if (!cc.isEmpty()) {
			domain.getCommandStack().execute(cc);
		}

		return;
	}

}
