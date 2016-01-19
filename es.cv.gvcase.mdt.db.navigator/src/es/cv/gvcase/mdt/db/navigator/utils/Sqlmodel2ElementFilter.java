/*******************************************************************************
 * Copyright (c) 2007 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   Mario Cervera Ubeda - Initial API and implementation
 *
 ******************************************************************************/

package es.cv.gvcase.mdt.db.navigator.utils;

import org.eclipse.datatools.modelbase.sql.accesscontrol.AuthorizationIdentifier;
import org.eclipse.datatools.modelbase.sql.accesscontrol.Privilege;
import org.eclipse.datatools.modelbase.sql.accesscontrol.RoleAuthorization;
import org.eclipse.datatools.modelbase.sql.accesscontrol.util.SQLAccessControlSwitch;
import org.eclipse.datatools.modelbase.sql.constraints.Constraint;
import org.eclipse.datatools.modelbase.sql.constraints.Index;
import org.eclipse.datatools.modelbase.sql.constraints.IndexMember;
import org.eclipse.datatools.modelbase.sql.constraints.util.SQLConstraintsSwitch;
import org.eclipse.datatools.modelbase.sql.datatypes.DataType;
import org.eclipse.datatools.modelbase.sql.datatypes.util.SQLDataTypesSwitch;
import org.eclipse.datatools.modelbase.sql.schema.Sequence;
import org.eclipse.datatools.modelbase.sql.schema.util.SQLSchemaSwitch;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.PersistentTable;
import org.eclipse.datatools.modelbase.sql.tables.ViewTable;
import org.eclipse.datatools.modelbase.sql.tables.util.SQLTablesSwitch;
import org.eclipse.emf.ecore.EObject;


/**
 * The Class Sqlmodel2ElementFilter.
 */
public class Sqlmodel2ElementFilter {

	/**
	 * The Class ConstraintsFilterSwitch.
	 */
	public class ConstraintsFilterSwitch extends SQLConstraintsSwitch<Object> {		
		
		/* (non-Javadoc)
		 * @see org.eclipse.datatools.modelbase.sql.constraints.util.SQLConstraintsSwitch#caseConstraint(org.eclipse.datatools.modelbase.sql.constraints.Constraint)
		 */
		@Override
		public Object caseConstraint(Constraint object) {	
			return true;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.datatools.modelbase.sql.constraints.util.SQLConstraintsSwitch#caseIndex(org.eclipse.datatools.modelbase.sql.constraints.Index)
		 */
		@Override
		public Object caseIndex(Index object) {
			return true;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.datatools.modelbase.sql.constraints.util.SQLConstraintsSwitch#caseIndexMember(org.eclipse.datatools.modelbase.sql.constraints.IndexMember)
		 */
		@Override
		public Object caseIndexMember(IndexMember object) {
			return true;
		}	
		
	}
	
	/**
	 * The Class DataTypesFilterSwitch.
	 */
	public class DataTypesFilterSwitch extends SQLDataTypesSwitch<Object> {
		
		/* (non-Javadoc)
		 * @see org.eclipse.datatools.modelbase.sql.datatypes.util.SQLDataTypesSwitch#caseDataType(org.eclipse.datatools.modelbase.sql.datatypes.DataType)
		 */
		@Override
		public Object caseDataType(DataType object) {
			return true;
		}
	}
	
	/**
	 * The Class TablesFilterSwitch.
	 */
	public class TablesFilterSwitch extends SQLTablesSwitch<Object> {
		
		/* (non-Javadoc)
		 * @see org.eclipse.datatools.modelbase.sql.tables.util.SQLTablesSwitch#casePersistentTable(org.eclipse.datatools.modelbase.sql.tables.PersistentTable)
		 */
		@Override
		public Object casePersistentTable(PersistentTable object) {
			return true;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.datatools.modelbase.sql.tables.util.SQLTablesSwitch#caseColumn(org.eclipse.datatools.modelbase.sql.tables.Column)
		 */
		@Override
		public Object caseColumn(Column object) {
			return true;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.datatools.modelbase.sql.tables.util.SQLTablesSwitch#caseViewTable(org.eclipse.datatools.modelbase.sql.tables.ViewTable)
		 */
		@Override
		public Object caseViewTable(ViewTable object) {
			return true;
		}
	}
	
	/**
	 * The Class AccessControlFilterSwitch.
	 */
	public class AccessControlFilterSwitch extends SQLAccessControlSwitch<Object> {
		
		/* (non-Javadoc)
		 * @see org.eclipse.datatools.modelbase.sql.accesscontrol.util.SQLAccessControlSwitch#caseAuthorizationIdentifier(org.eclipse.datatools.modelbase.sql.accesscontrol.AuthorizationIdentifier)
		 */
		@Override
		public Object caseAuthorizationIdentifier(AuthorizationIdentifier object) {
			return true;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.datatools.modelbase.sql.accesscontrol.util.SQLAccessControlSwitch#caseRoleAuthorization(org.eclipse.datatools.modelbase.sql.accesscontrol.RoleAuthorization)
		 */
		@Override
		public Object caseRoleAuthorization(RoleAuthorization object) {
			return true;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.datatools.modelbase.sql.accesscontrol.util.SQLAccessControlSwitch#casePrivilege(org.eclipse.datatools.modelbase.sql.accesscontrol.Privilege)
		 */
		@Override
		public Object casePrivilege(Privilege object) {
			return true;
		}
	}
	
	/**
	 * The Class SchemaFilterSwitch.
	 */
	public class SchemaFilterSwitch extends SQLSchemaSwitch<Object> {
		
		/* (non-Javadoc)
		 * @see org.eclipse.datatools.modelbase.sql.schema.util.SQLSchemaSwitch#caseSequence(org.eclipse.datatools.modelbase.sql.schema.Sequence)
		 */
		@Override
		public Object caseSequence(Sequence object) {
			return true;
		}
	}
	
	/**
	 * Checks if is visible.
	 * 
	 * @param obj the obj
	 * 
	 * @return true, if is visible
	 */
	public boolean isVisible(Object obj) {
		if (obj instanceof EObject) {
			
			Object result = (new ConstraintsFilterSwitch()).doSwitch((EObject) obj);
			
			if(result == null || (result!=null && !((Boolean)result).booleanValue())) {
				result = (new DataTypesFilterSwitch()).doSwitch((EObject) obj);
			}
			if(result == null || (result!=null && !((Boolean)result).booleanValue())) {
				result = (new TablesFilterSwitch()).doSwitch((EObject) obj);
			}
			if(result == null || (result!=null && !((Boolean)result).booleanValue())) {
				result = (new AccessControlFilterSwitch()).doSwitch((EObject) obj);
			}
			if(result == null || (result!=null && !((Boolean)result).booleanValue())) {
				result = (new SchemaFilterSwitch()).doSwitch((EObject) obj);
			}
			
			if (result != null && result instanceof Boolean) {
				return (Boolean) result;
			}
		}
		return false;
	}
}
