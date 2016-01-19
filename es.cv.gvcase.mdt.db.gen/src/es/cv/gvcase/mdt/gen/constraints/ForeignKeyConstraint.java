package es.cv.gvcase.mdt.gen.constraints;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.datatools.modelbase.sql.constraints.Constraint;
import org.eclipse.datatools.modelbase.sql.constraints.ForeignKey;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.tables.BaseTable;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.datatools.modelbase.sql.tables.ViewTable;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;

public class ForeignKeyConstraint extends AbstractModelConstraint {

	public ForeignKeyConstraint() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IStatus validate(IValidationContext ctx) {

		EObject eObj = ctx.getTarget();
		EMFEventType eType = ctx.getEventType();
		
		// In the case of batch mode.
		if (eType == EMFEventType.NULL) {
			
			if(eObj instanceof Database) {
				
				Database db = (Database) eObj;
				
				List<ForeignKey> fks = getForeignKeys(db);
				
				String fkList = "";
				int contWrongFKs = 0;
				
				for(ForeignKey fk : fks) {
					if(fk.getMembers().size() <= 0 || fk.getReferencedMembers().size() <=0) {

						String fkName ="";
						if(fk.getName() != null) fkName = fk.getName();
						fkList += fkName + ", ";
						contWrongFKs++;
						
					}
				}
				
				if(contWrongFKs > 0) {
					fkList = fkList.substring(0, fkList.length() - 2);
					return ctx.createFailureStatus(new Object[] {fkList});
				}
			}
			
		}
		
		return ctx.createSuccessStatus();
	}
	
	
	private List<ForeignKey> getForeignKeys(Database db) {
		
		java.util.List<ForeignKey> fks = new ArrayList<ForeignKey>();
		
		EList schemas = db.getSchemas();
		Iterator schemasIter = schemas.iterator();
		while(schemasIter.hasNext())
		{
			Schema schema = (Schema)schemasIter.next();
			EList tables = schema.getTables();
			Iterator tableIter = tables.iterator();
			while(tableIter.hasNext())
			{		
				Table table = (Table)tableIter.next();
				if(!(table instanceof ViewTable)) {
					Iterator constraintsIter = ((BaseTable)table).getConstraints().iterator();
					while(constraintsIter.hasNext())
					{
						Constraint constraint = (Constraint)constraintsIter.next();
						if(constraint instanceof ForeignKey) {
							fks.add((ForeignKey)constraint);
						}
					}
				}
			}
		}
		
		return fks;
	}

}
