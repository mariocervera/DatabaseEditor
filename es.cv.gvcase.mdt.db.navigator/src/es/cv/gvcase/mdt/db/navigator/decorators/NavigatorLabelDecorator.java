/***************************************************************************
* Copyright (c) 2007 Conselleria de Infraestructuras y Transporte,
* Generalitat de la Comunitat Valenciana . All rights reserved. This program
* and the accompanying materials are made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors: Mario Cervera Ubeda (Integranova)
*
******************************************************************************/
package es.cv.gvcase.mdt.db.navigator.decorators;

import org.eclipse.datatools.modelbase.sql.accesscontrol.Privilege;
import org.eclipse.datatools.modelbase.sql.accesscontrol.Role;
import org.eclipse.datatools.modelbase.sql.accesscontrol.RoleAuthorization;
import org.eclipse.datatools.modelbase.sql.constraints.Index;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * The Class NavigatorLabelDecorator.
 */
public class NavigatorLabelDecorator implements ILabelDecorator {


	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelDecorator#decorateImage(org.eclipse.swt.graphics.Image, java.lang.Object)
	 */
	public Image decorateImage(Image image, Object element) {
		return image;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelDecorator#decorateText(java.lang.String, java.lang.Object)
	 */
	public String decorateText(String text, Object element) {
		
		if(element instanceof Index) {
			
			Index index = (Index) element;
			return getIndexLabel(index);
		}
		else if(element instanceof Privilege) {
			Privilege privilege = (Privilege) element;
			return getPrivilegeLabel(privilege);
		}
		else if(element instanceof RoleAuthorization) {
			RoleAuthorization ra = (RoleAuthorization) element;
			return getRoleAuthorizationLabel(ra);
		}
		else if(element instanceof SQLObject) {
			SQLObject sqlobject = (SQLObject) element;
			String className = sqlobject.getClass().getSimpleName();
			// Remove the "Impl" part of the name
			className = className.substring(0, className.length() - 4);
			String name = "";
			if(sqlobject.getName() != null) {
				name = sqlobject.getName();
			}
			return "<" + className + "> " + name;
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
	 */
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Gets the index label.
	 * 
	 * @param index the Index
	 * 
	 * @return the index label
	 */
	private String getIndexLabel(Index index) {
		
		Table table = index.getTable(); 
		if(table != null) {
			String tableName = "";
			if(table.getName() != null) {
				tableName = table.getName();
			}
			String indexName = "";
			if(index.getName() != null) {
				indexName = index.getName();
			}
			return "<Index> " + tableName + ":" + indexName;
		}
		
		return null;
	}
	
	/**
	 * Gets the privilege label.
	 * 
	 * @param privilege the Privilege
	 * 
	 * @return the privilege label
	 */
	private String getPrivilegeLabel(Privilege privilege) {
		
		if(privilege == null) return null;
		
		SQLObject object = privilege.getObject();
		String objectName = "";
		if(object != null && object.getName() != null) objectName = object.getName();
		String privilegeName = "";
		if(privilege.getName() != null) privilegeName = privilege.getName();
		
		String privilegeLabel = "<Privilege> " + privilegeName;
		if(object != null) {
			privilegeLabel += " on " + objectName;
		}
		
		return privilegeLabel;
	}
	
	/**
	 * Gets the role authorization label.
	 * 
	 * @param ra the RoleAuthorization
	 * 
	 * @return the role authorization label
	 */
	private String getRoleAuthorizationLabel(RoleAuthorization ra) {
		
		if(ra == null) return null;
		
		Role role = ra.getRole();
		String raName = "";
		if(role != null && role.getName() != null) raName = role.getName();
		
		String raLabel = "<Role Authorization> " + raName;
		
		return raLabel;
	}

}
