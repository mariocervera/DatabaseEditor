/*******************************************************************************
 * Copyright (c) 2008 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Francisco Javier Cano Muñoz (Prodevelop) - Initial API and implementation.
 * Mario Cervera Ubeda - Additional functionality
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.diagram.parsers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.datatools.modelbase.sql.constraints.ForeignKey;
import org.eclipse.datatools.modelbase.sql.constraints.Index;
import org.eclipse.datatools.modelbase.sql.constraints.IndexMember;
import org.eclipse.datatools.modelbase.sql.constraints.PrimaryKey;
import org.eclipse.datatools.modelbase.sql.constraints.TableConstraint;
import org.eclipse.datatools.modelbase.sql.constraints.UniqueConstraint;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.tables.BaseTable;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;

import es.cv.gvcase.mdt.common.runnable.HookRunner;
import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelStructuralFeatureParser;

public class ColumnWithConstraintsParser extends SqlmodelStructuralFeatureParser {

	/**
	 * Hook identifier for the after run_re method.
	 */
	public static final String AFTER_GETCOLUMNCONSTRAINTS_HOOKID = "es.cv.gvcase.mdt.db.diagram.parsers.after_getcolumnconstraints";
	
	public ColumnWithConstraintsParser(EStructuralFeature feature) {
		super(feature);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.cv.gvcase.mdt.db.diagram.providers.SqlmodelAbstractParser#getPrintString(org.eclipse.core.runtime.IAdaptable,
	 *      int)
	 */
	@Override
	public String getPrintString(IAdaptable adapter, int flags) {
		
		Column column = (Column) adapter.getAdapter(Column.class);
		if (column == null) {
			return super.getPrintString(adapter, flags);
		}
		
		String constraints = getColumnConstraints(column);
		String printingString = "";

		printingString += (constraints.length() <= 0 ? ("")
				: ("  " + constraints));

		return printingString;
	}


//	/**
//	 * Gets the column constraints.
//	 * 
//	 * @param column
//	 *            the column
//	 * 
//	 * @return the column constraints
//	 */
//	/*
//	 * This method adds to the names of the columns the following information:
//	 * - PK if the column is part of the primary key
//	 * - the names of the UKs that have the column as member
//	 * - the names of the FKs that have the column as member or referenced member
//	 * - the name of the indexes that have the column as member
//	 * 
//	 * This method is replaced provisionally by the other method which just adds acronyms
//	 * instead of names (the PK is always an acronym in both methods). 
//	 * The future application will have both functionalities
//	 * The methods used by the commented method have not been removed from this class
//	 */
//	private String getColumnConstraints(Column column) {
//		String constraintsString = "";
//
//		if (isColumnInPrimaryKey(column)) {
//			constraintsString += "PK";
//		}
//
//		String inUniques = getInUniqueConstraintsNames(column);
//		if (inUniques != null && inUniques.length() > 0) {
//			constraintsString += (constraintsString.length() <= 0 ? ("")
//					: (", "));
//			constraintsString += inUniques;
//		}
//
//		String inIndeces = getInIndecesConstraintsNames(column);
//		if (inIndeces != null && inIndeces.length() > 0) {
//			constraintsString += (constraintsString.length() <= 0 ? ("")
//					: (", "));
//			constraintsString += inIndeces;
//		}
//
//		String inForeignKeys = getColumnForeignKeysConstraintsNames(column);
//		if (inForeignKeys != null && inForeignKeys.length() > 0) {
//			constraintsString += (constraintsString.length() <= 0 ? ("")
//					: (", "));
//			constraintsString += inForeignKeys;
//		}
//
//		return constraintsString;
//	}
	
	private String getColumnConstraints(Column column) {
		String constraintsString = "";

		if (isColumnInPrimaryKey(column)) {
			constraintsString += "PK";
		}

		String inUniques = getInUniqueConstraintsAcronyms(column);
		if (inUniques != null && inUniques.length() > 0) {
			constraintsString += (constraintsString.length() <= 0 ? ("")
					: (", "));
			constraintsString += inUniques;
		}
		
		String inIndeces = getInIndecesConstraintsAcronyms(column);
		if (inIndeces != null && inIndeces.length() > 0) {
			constraintsString += (constraintsString.length() <= 0 ? ("")
					: (", "));
			constraintsString += inIndeces;
		}

		String inForeignKeys = getColumnForeignKeysConstraintsAcronyms(column);
		if (inForeignKeys != null && inForeignKeys.length() > 0) {
			constraintsString += (constraintsString.length() <= 0 ? ("")
					: (", "));
			constraintsString += inForeignKeys;
		}

		// New Column and String constraint Object Structure to parameters pass.
		// pos 0 - Column inter
		// pos 1 - String Returned
		ArrayList<Object> info=null;
		info = new ArrayList<Object>();
		info.add(0,column);
		info.add(1,new String());
				
		// Hook to perform additional constraints to the string.
		HookRunner.getInstance().runRunnablesWithInfoForHook(
				AFTER_GETCOLUMNCONSTRAINTS_HOOKID, info);
		
		if (info.get(1)!=null && info.get(1) instanceof String){
			constraintsString += (constraintsString.length() <= 0 ? ("")
					: (", "));
			constraintsString += info.get(1);
		}
		
		return constraintsString;
	}
	
	/**
	 * Checks if is column in primary key.
	 * 
	 * @param column
	 *            the column
	 * 
	 * @return true, if is column in primary key
	 */
	private boolean isColumnInPrimaryKey(Column column) {
		PrimaryKey primaryKey = getTablePrimaryKey(column);
		if (primaryKey == null) {
			return false;
		}

		EList<?> members = primaryKey.getMembers();
		if (members.contains(column)) {
			return true;
		}

		return false;
	}

	/**
	 * Gets the table primary key.
	 * 
	 * @param column
	 *            the column
	 * 
	 * @return the table primary key
	 */
	private PrimaryKey getTablePrimaryKey(Column column) {
		if (column == null) {
			return null;
		}
		BaseTable table = (BaseTable) column.getTable();
		if (table == null) {
			return null;
		}
		EList<?> constraints = table.getConstraints();
		for (Object o : constraints) {
			if (o instanceof PrimaryKey) {
				return (PrimaryKey) o;
			}
		}
		return null;
	}

	
	/**
	 * BELOW: Names
	 */
	
	
//	/**
//	 * Gets the in unique constraints names.
//	 * 
//	 * @param column
//	 *            the column
//	 * 
//	 * @return the in unique constraints names
//	 */
//	private String getInUniqueConstraintsNames(Column column) {
//		List<String> names = new ArrayList<String>();
//		List<UniqueConstraint> uniques = getTableUniqueConstraints(column);
//		for (UniqueConstraint u : uniques) {
//			EList members = u.getMembers();
//			if (members != null && members.contains(column)) {
//				names.add(u.getName());
//			}
//		}
//		if (names.size() <= 0) {
//			return "";
//		}
//
//		String inUniqueKeysNames = "";
//		inUniqueKeysNames += names.get(0);
//		for (int i = 1; i < names.size(); i++) {
//			inUniqueKeysNames += ", " + names.get(i);
//		}
//
//		return inUniqueKeysNames;
//	}

	/**
	 * Gets the table unique constraints.
	 * 
	 * @param column
	 *            the column
	 * 
	 * @return the table unique constraints
	 */
	private List<UniqueConstraint> getTableUniqueConstraints(Column column) {
		if (column == null) {
			return Collections.EMPTY_LIST;
		}

		BaseTable table = (BaseTable) column.getTable();
		if (table == null) {
			return Collections.EMPTY_LIST;
		}

		EList<?> constraints = table.getConstraints();
		List<UniqueConstraint> uniques = new ArrayList<UniqueConstraint>();
		for (Object o : constraints) {
			if (o instanceof UniqueConstraint && !(o instanceof PrimaryKey)) {
				uniques.add((UniqueConstraint) o);
			}
		}
		return uniques;
	}

//	/**
//	 * Gets the in indeces constraints names.
//	 * 
//	 * @param column
//	 *            the column
//	 * 
//	 * @return the in indeces constraints names
//	 */
//	private String getInIndecesConstraintsNames(Column column) {
//		List<Index> indeces = getInIndices(column);
//
//		if (indeces.size() <= 0) {
//			return "";
//		}
//
//		String inIndecesNames = "";
//		inIndecesNames += indeces.get(0).getName();
//		for (int i = 1; i < indeces.size(); i++) {
//			inIndecesNames += ", " + indeces.get(i).getName();
//		}
//
//		return inIndecesNames;
//	}

//	/**
//	 * Gets the in indices.
//	 * 
//	 * @param column
//	 *            the column
//	 * 
//	 * @return the in indices
//	 */
//	private List<Index> getInIndices(Column column) {
//		if (column == null) {
//			return Collections.EMPTY_LIST;
//		}
//
//		BaseTable table = (BaseTable) column.getTable();
//		if (table == null) {
//			return Collections.EMPTY_LIST;
//		}
//		Schema schema = table.getSchema();
//
//		EList indeces = null;
//		List<Index> inIndices = new ArrayList<Index>();
//		
//		if(schema != null) {
//			indeces = schema.getIndices();
//			for (Object o : indeces) {
//				if (o instanceof Index) {
//					Index index = (Index) o;
//					if (isColumnInIndex(column, index)) {
//						inIndices.add(index);
//					}
//				}
//			}
//		}
//		
//		return inIndices;
//	}

	/**
	 * Checks if is column in index.
	 * 
	 * @param column
	 *            the column
	 * @param index
	 *            the index
	 * 
	 * @return true, if is column in index
	 */
	private boolean isColumnInIndex(Column column, Index index) {
		if (column == null || index == null) {
			return false;
		}

		EList<?> members = index.getMembers();
		for (Object o : members) {
			if (o instanceof IndexMember) {
				IndexMember im = (IndexMember) o;
				if (im.getColumn() != null && im.getColumn().equals(column)) {
					return true;
				}
			}
		}

		return false;
	}

//	private String getColumnForeignKeysConstraintsNames(Column column) {
//		EReference[] featuresToInspect = new EReference[1];
//		featuresToInspect[0] = SQLConstraintsPackage.eINSTANCE
//				.getForeignKey_ReferencedMembers();
//
//		Collection<Object> referencers = EMFCoreUtil.getReferencers(column, null);
//				//featuresToInspect);
//
//		String foreignKeysConstraintsNames = "";
//		for (Object o : referencers) {
//			if (o instanceof ForeignKey) {
//				ForeignKey foreignKey = (ForeignKey) o;
//				String foreignKeyName = foreignKey.getName();
//				if (foreignKeyName != null && foreignKeyName.length() > 0) {
//					foreignKeysConstraintsNames += foreignKeysConstraintsNames
//							.length() > 0 ? ", " + foreignKeyName
//							: foreignKeyName;
//				}
//			}
//		}
//
//		return foreignKeysConstraintsNames;
//	}
	
	/**
	 * BELOW: Acronyms
	 */
	
	/**
	 * Gets the acronyms for unique constraints.
	 * 
	 * @param column
	 *            the column
	 * 
	 * @return the acronyms
	 */
	private String getInUniqueConstraintsAcronyms(Column column) {
		List<String> acronyms = new ArrayList<String>();
		List<UniqueConstraint> uniques = getTableUniqueConstraints(column);
		int cont = 1;
		for (UniqueConstraint u : uniques) {
			EList<?> members = u.getMembers();
			if (members != null && members.contains(column)) {
				acronyms.add("UK" + cont);
			}
			cont++;
		}
		if (acronyms.size() <= 0) {
			return "";
		}

		String inUniqueKeysAcronyms = "";
		inUniqueKeysAcronyms += acronyms.get(0);
		for (int i = 1; i < acronyms.size(); i++) {
			inUniqueKeysAcronyms += ", " + acronyms.get(i);
		}

		return inUniqueKeysAcronyms;
	}
	
	//Gives the indexes of the table of the column
	private List<Index> getTableIndices(Column column) {
		
		List<Index> indeces = new ArrayList<Index>();
		
		BaseTable table = (BaseTable)column.eContainer();
		if(table == null) return indeces;
		
		Schema schema = (Schema)table.eContainer();
		if(schema == null) return indeces;
		
		for(Index i: schema.getIndices()) {
			if(table.equals(i.getTable())) {
				indeces.add(i);
			}
		}
		
		return indeces;
	}
	
	/**
	 * Gets the acronyms for indexes.
	 * 
	 * @param column
	 *            the column
	 * 
	 * @return the acronyms
	 */
	private String getInIndecesConstraintsAcronyms(Column column) {
		List<String> acronyms = new ArrayList<String>();
		List<Index> indeces = getTableIndices(column);
		int cont = 1;
		for (Index i : indeces) {
			if (i.getMembers() != null && isColumnInIndex(column, i)) {
				acronyms.add("I" + cont);
			}
			cont++;
		}
		if (acronyms.size() <= 0) {
			return "";
		}

		String inIndecesAcronyms = "";
		inIndecesAcronyms += acronyms.get(0);
		for (int i = 1; i < acronyms.size(); i++) {
			inIndecesAcronyms += ", " + acronyms.get(i);
		}

		return inIndecesAcronyms;
	}
	
	
	private String getColumnForeignKeysConstraintsAcronyms(Column column) {

		Collection<Object> referencers = EMFCoreUtil.getReferencers(column, null);
				//featuresToInspect);

		String foreignKeysConstraintsNames = "";
		for (Object o : referencers) {
			if (o instanceof ForeignKey && column.eContainer() != null &&
					column.eContainer().equals(((ForeignKey)o).eContainer())) {
				ForeignKey foreignKey = (ForeignKey) o;
				String foreignKeyAcronym = getAcronymForFK(foreignKey);
				if (foreignKeyAcronym.length() > 0) {
					foreignKeysConstraintsNames += foreignKeysConstraintsNames
							.length() > 0 ? ", " + foreignKeyAcronym
							: foreignKeyAcronym;
				}
			}
		}

		return foreignKeysConstraintsNames;
	}
	
	
	private String getAcronymForFK(ForeignKey fk) {
		
		BaseTable table = fk.getBaseTable();
		if(table == null) return "";
		
		int cont = 1;
		for(TableConstraint tc : table.getConstraints()) {
			if(tc instanceof ForeignKey) {
				ForeignKey fkey = (ForeignKey) tc;
				if(fkey.equals(fk)) {
					return ("FK" + cont);
				}
				cont++;
			}
		}
		
		return "";
	}
	
}
