
package es.cv.gvcase.mdt.gen.transf;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.datatools.modelbase.sql.accesscontrol.AuthorizationIdentifier;
import org.eclipse.datatools.modelbase.sql.constraints.Constraint;
import org.eclipse.datatools.modelbase.sql.constraints.ForeignKey;
import org.eclipse.datatools.modelbase.sql.constraints.PrimaryKey;
import org.eclipse.datatools.modelbase.sql.constraints.UniqueConstraint;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.IdentitySpecifier;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.schema.Sequence;
import org.eclipse.datatools.modelbase.sql.tables.BaseTable;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.PersistentTable;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.datatools.modelbase.sql.tables.ViewTable;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

import es.cv.gvcase.trmanager.Transformation;



public class Extensions {
	
	/**
	 * 
	 * @param cadena
	 *            A String to transform
	 * @return A valid String prepared to work fine in a gvHidra app
	 */
	public static String validStringDesc(String cadena) {
		if (cadena!=null)
		cadena = cadena.replaceAll("'", "");
		
		return cadena.toString();	
	}	
	
	
	/**
	 * 
	 * @param cadena
	 *            A String to transform
	 * @return A valid String prepared to work fine in a sql creation
	 */ 
		
		public static String validString(String cadena) {
			if (cadena == null)
				return null;
			cadena = cadena.replaceAll("-", "");
			cadena = cadena.replaceAll(" ", "");
			cadena = cadena.replaceAll("ª", "_");
			cadena = cadena.replaceAll("º", "_");
			cadena = cadena.replaceAll("\"", "");

			// remove strange letters
			cadena = cadena.replaceAll("[ñÑ]", "ny");
			cadena = cadena.replaceAll("[çÇ]", "c");

			// remove accents
			cadena = cadena.replaceAll("[àáâäÀÁÂÄ]", "a");
			cadena = cadena.replaceAll("[èéêëÈÉÊË]", "e");
			cadena = cadena.replaceAll("[ìíîïÌÍÎÏ]", "i");
			cadena = cadena.replaceAll("[òóôöÒÓÔÖ]", "o");
			
			cadena = cadena.replaceAll("[ŵĉŷŝĝĥĵẑŴĈŶŜĜĤĴŶ]", "");			
	
			//removeNumbers

			cadena = cadena.replaceAll("[!#$%&'()*+,-./:;<=>?¿@\\^`{|}~\"·~¬|]","");

			StringBuilder cadenaSB = new StringBuilder(cadena);

			while (cadenaSB.indexOf(":") != -1) {
				cadenaSB.delete(cadenaSB.indexOf(":"), cadenaSB.length());
			}
			while (cadenaSB.indexOf("/") != -1) {
				cadenaSB.deleteCharAt(cadenaSB.indexOf("/"));
			}
			while (cadenaSB.indexOf(".".toString()) != -1) {
				cadenaSB.deleteCharAt(cadenaSB.indexOf(".".toString()));
			}
			return cadenaSB.toString();
		}
	
		
	/*
	 * 	Actualizar Progress Monitor en Transformaciones
	 */
	public static void updateMonitor(String value) {
			if (Transformation.monitor != null) {
				Transformation.monitor.beginTask(value, IProgressMonitor.UNKNOWN); // a main task
				Transformation.monitor.subTask("Processing Panel "  ); // a subtask error: + cow.getId()
			}
		}

	/**
	 * 
	 * @param db
	 *            A DataBase
	 * @return if exist error into FKs: error if members size=0 or no equals into source and referenced
	 */	
	public static List<String>  ValidateForeignKeys(Database db) {				
		List<String> v = new ArrayList<String>();	
		
		List<ForeignKey> fks = getForeignKeys(db); //obtenemos las FKs de la base de datos y vemos si existe error	
		int contWrongFKs = 0;		
		for(ForeignKey fk : fks) {
			if(fk.getMembers().size() <= 0 || fk.getReferencedMembers().size() <=0) {
				v.add(((ForeignKey)fk).getName());	
			}
		}				
		return v;
	}
	
	/**
	 * 
	 * @param db
	 *            A DataBase
	 * @return if exist error into FKs: error if members size no equals referenced members size
	 */	
	public static List<String>  ValidateForeignKeysSize(Database db) {				
		List<String> v = new ArrayList<String>();	
		
		List<ForeignKey> fks = getForeignKeys(db); //obtenemos las FKs de la base de datos y vemos si existe error			
		int contWrongFKs = 0;		
		for(ForeignKey fk : fks) {
			if(fk.getMembers().size() != fk.getReferencedMembers().size()) {
				v.add(((ForeignKey)fk).getName());		
			}
		}		
		
		return v;
	}

	
	/**
	 * 
	 * @param db
	 *            A DataBase
	 * @return if exist error in fk names
	 */	
	public static List<String>  ValidateFKNames(Database db) {		
		List<String> v = new ArrayList<String>();	
		
		TreeIterator<EObject> it = db.getSchemas().get(0).eAllContents();             	             	
     	
     	List<ForeignKey> v1 = new ArrayList<ForeignKey>();
     	List<ForeignKey> v2 = new ArrayList<ForeignKey>();
     	
     	while (it.hasNext()) {
			Object e = it.next();
			if (e instanceof ForeignKey){
				v1.add((ForeignKey) e);
				v2.add((ForeignKey) e);
			}
     	}
     	
     	for (ForeignKey fk1 : v1) {
     		for (ForeignKey fk2 : v2) {
     			if (!fk1.equals(fk2)){ 
     				String n1=((ForeignKey)fk1).getName();
     				String n2=((ForeignKey)fk2).getName();
     				if (n1.equalsIgnoreCase(n2)){     					
     					v.add(fk1.getName()+" from "+fk1.getReferencedTable().getName()+" to "+fk1.getBaseTable().getName());		     					
     				}
     			}
     		}
			
		}
    	
		return v;
	}
	
	/**
	 * 
	 * @param db
	 *            A DataBase
	 * @return if exist error in names
	 */	
	public static boolean ValidateNames(Database db) {			
    	if(db.getName() == null)  		
			return false;
		
    	for(Schema schema: db.getSchemas()) {
    		if(schema.getName() == null)        			
    			return false;        	
    	}
    	
		return true;
	}
	
	/**
	 * 
	 * @param db
	 *          A DataBase
	 * @return  database FK list
	 */
	private static List<ForeignKey> getForeignKeys(Database db) {
		java.util.List<ForeignKey> fks = new ArrayList<ForeignKey>();

		EList schemas = db.getSchemas();
		Iterator schemasIter = schemas.iterator();
		while (schemasIter.hasNext()) {
			Schema schema = (Schema) schemasIter.next();
			EList tables = schema.getTables();
			Iterator tableIter = tables.iterator();
			while (tableIter.hasNext()) {
				Table table = (Table) tableIter.next();
				if (!(table instanceof ViewTable)) {
					Iterator constraintsIter = ((BaseTable) table)
							.getConstraints().iterator();
					while (constraintsIter.hasNext()) {
						Constraint constraint = (Constraint) constraintsIter
								.next();
						if (constraint instanceof ForeignKey) {
							fks.add((ForeignKey) constraint);
						}
					}
				}
			}
		}

		return fks;
	}
	
	/**
	 * 
	 * @param db
	 *            A DataBase
	 * @return if exist error in tables,views,columns or constrainst names 
	 */	
	public static List<String>  ValidateSQLObjectName(Database db) {	
		List<String> v = new ArrayList<String>();	
		TreeIterator<EObject> it = db.eAllContents();      	
     	while (it.hasNext()) {
			Object e = it.next();
			if ((e instanceof PersistentTable) || (e instanceof ViewTable) || (e instanceof Column) || (e instanceof Constraint)){
				if (((SQLObject)e).getName()==null)	v.add(((SQLObject)e).getName());	
			}
     	}     	    	
    	
		return v;
	}
	
	/**
	 * 
	 * @param db
	 *            A DataBase
	 * @return if datatype column is null 
	 */	
	public static List<String> ValidateColumnType(Database db) {				
		List<String> v = new ArrayList<String>();			
		TreeIterator<EObject> it = db.eAllContents();      	
     	while (it.hasNext()) {
			Object e = it.next();
			if ((e instanceof Column)){
				if (((Column)e).getDataType()==null) {
					String n=((Column)e).getName();
					v.add(n);				
				}
			}
     	}     	    	    	
		return v;
	}
	
	/**
	 * 
	 * @param db
	 *            A DataBase
	 * @return if datatype column is null 
	 */	
	public static List<String> ValidateColumnName(Database db) {				
		List<String> v = new ArrayList<String>();			
		TreeIterator<EObject> it = db.eAllContents();      	
     	while (it.hasNext()) {
			Object e = it.next();
			if ((e instanceof Column)){
				Column c = (Column)e;
				if (((Column)e).getName().equalsIgnoreCase("desc")) {
					String n="Column "+c.getName()+" from table "+ c.getTable().getName()+" has reserved name.";
					v.add(n);				
				}
			}
     	}     	    	    	
		return v;
	}
	
	/**
	 * 
	 * @param db
	 *            A DataBase
	 * @return if all tables has unique key
	 */	
	public static List<String>  ExistUK(Database db) {		
		List<String> v = new ArrayList<String>();	
		TreeIterator<EObject> it = db.eAllContents();      	
     	while (it.hasNext()) {
			Object e = it.next();
			if (e instanceof ForeignKey) {				
				ForeignKey fk=(ForeignKey)e;
				BaseTable target = fk.getReferencedTable();		
				if (target.getUniqueConstraints().isEmpty()) v.add(((ForeignKey)e).getName());		
			}				
     	}     	    	    	
		return v;
	}
	
	public static List<String>  ValidateUK(Database db) {	
		List<String> v = new ArrayList<String>();	
		boolean res=true;
		TreeIterator<EObject> it = db.eAllContents();      	
     	while (it.hasNext()) {
			Object e = it.next();
			if (e instanceof ForeignKey) {
				ForeignKey fk=(ForeignKey)e;
				BaseTable target = fk.getReferencedTable();			
				Iterator it2 = target.getUniqueConstraints().iterator();		
				List r = fk.getReferencedMembers();				
				while (it2.hasNext()) {
					UniqueConstraint uk = (UniqueConstraint) it2.next();
					EList l = uk.getMembers();
					// si dentro de las uk encontradas esta algunas de las refererenced columns
					if (!l.containsAll(r)){
						v.add(((ForeignKey)e).getName());	
					}	
				}								
			}				 
     	}     	    	    	
		return v;
	}
	
	/**
	 * 
	 * @param db
	 *            A DataBase
	 * @return if all tables has unique key
	 */	
	public static List<String>  ValidateSequeces(Database db) {
		List<String> v = new ArrayList<String>();	
		TreeIterator<EObject> it = db.eAllContents();      	
     	while (it.hasNext()) {
			Object e = it.next();
			if ((e instanceof Column) && ((Column)e).getIdentitySpecifier()!=null){				
				IdentitySpecifier id = ((Column)e).getIdentitySpecifier();
				if (id.getMinimum()==null || id.getMaximum()==null) v.add(((Column)e).getName());	
		
			}
			
     	}     	    	    	
		return v;
	}
	
		
	public static String formateo(List<String> v) {			
		String cad="";
		if (v.size()>0){
			cad=v.get(0);		
			for(int i=1;i<v.size();i++){
				cad=cad+","+v.get(i);
			}
		}		  	    	
		return cad;
	}

}
		