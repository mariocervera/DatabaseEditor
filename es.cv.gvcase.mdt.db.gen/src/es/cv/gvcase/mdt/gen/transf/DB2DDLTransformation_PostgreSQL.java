/*******************************************************************************
 * Copyright (c) 2007 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Javier Mu�oz (Integranova) � Initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.gen.transf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.datatools.modelbase.sql.constraints.ForeignKey;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import es.cv.gvcase.launcher.xpand.transformation.XPANDTransformation;
import es.cv.gvcase.trmanager.TransformedResource;


public class DB2DDLTransformation_PostgreSQL extends XPANDTransformation {

	@Override
	protected String getTransformationId() {
		return "es.cv.gvcase.mdt.db.gen.DB2DDL.Postgre";
	}

	@Override
	protected String getWorkflowName() {
		return "PostgreGenerator.mwe";
	}

	@Override
	protected boolean hasConfiguration() {
		return false;
	}

	@Override
	//Postgresql: You can repeat FK name in scheme, less from the same table
	public String getTransformationExtension() {
		return "postgre";
	}

	@Override
	public boolean inputsValid(HashMap<String, TransformedResource> inputs,
			List<String> errorList) {		
		String inputModelPath = ((TransformedResource) inputs.values()
				.toArray()[0]).getPath();

			
        XMIResourceFactoryImpl _xmiFac = new XMIResourceFactoryImpl();        
        
        ResourceSet rSet = new ResourceSetImpl ();    
        
        rSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*",_xmiFac);
        URI uri = URI.createURI(inputModelPath);
        Resource resource = rSet.getResource(uri, true);
        
        if (resource != null && resource.getContents().size() > 0) {        		
        	Database db = (Database) resource.getContents().get(0);       	
        	
        	List<String> dbFKSize=es.cv.gvcase.mdt.gen.transf.Extensions.ValidateForeignKeysSize(db);	
        	List<String> dbFKNumber=es.cv.gvcase.mdt.gen.transf.Extensions.ValidateForeignKeys(db);					
        	boolean dbNames=es.cv.gvcase.mdt.gen.transf.Extensions.ValidateNames(db);	
        	List<String> dbFKName=es.cv.gvcase.mdt.gen.transf.Extensions.ValidateFKNames(db);		
        	List<String> dbObjName=es.cv.gvcase.mdt.gen.transf.Extensions.ValidateSQLObjectName(db);		
			List<String> dbTypes=es.cv.gvcase.mdt.gen.transf.Extensions.ValidateColumnType(db);		
			List<String> dbName=es.cv.gvcase.mdt.gen.transf.Extensions.ValidateColumnName(db);	
			List<String> dbUK1=es.cv.gvcase.mdt.gen.transf.Extensions.ExistUK(db);
			List<String> dbUK2=es.cv.gvcase.mdt.gen.transf.Extensions.ValidateUK(db);	
			List<String> dbSeq=es.cv.gvcase.mdt.gen.transf.Extensions.ValidateSequeces(db);		
			
			if(dbFKSize.size()>0)
				errorList.add("ERROR: Revised Foreign Key : "+es.cv.gvcase.mdt.gen.transf.Extensions.formateo(dbFKSize)+" in the model. Number of members and referenced members must be equals");			
			
			if(dbFKNumber.size()>0)
				errorList.add("ERROR: Revised Foreign Key :"+es.cv.gvcase.mdt.gen.transf.Extensions.formateo(dbFKNumber)+" in the model. The Foreign key needed members and referenced members");
			
			if(dbFKName.size()>0)
				errorList.add("ERROR: ForeignKeys :"+es.cv.gvcase.mdt.gen.transf.Extensions.formateo(dbFKName)+" the name is duplicate. Revised Model.");
	
			if(dbNames!= true)
				errorList.add("ERROR: Database and schemas in the input model must have a name. Revised Model.");

			if(dbObjName.size()>0)
				errorList.add("ERROR: Some table, view, column or constrainst name :"+es.cv.gvcase.mdt.gen.transf.Extensions.formateo(dbObjName)+" is null. Revised Model.");
		
			if(dbTypes.size()>0)
				errorList.add("ERROR: : "+es.cv.gvcase.mdt.gen.transf.Extensions.formateo(dbTypes)+" datatype is null. Revised Model.");
		
			if(dbName.size()>0)
				errorList.add("ERROR: "+es.cv.gvcase.mdt.gen.transf.Extensions.formateo(dbName)+" Revised Model.");
			
			if(dbUK1.size()>0)
				errorList.add("ERROR: Tables : "+es.cv.gvcase.mdt.gen.transf.Extensions.formateo(dbUK1)+" which reference a Foreign Key hasn't unique key. Revised Model.");	
		
			if(dbUK2.size()>0)
				errorList.add("ERROR: Uniques constraints: "+es.cv.gvcase.mdt.gen.transf.Extensions.formateo(dbUK2)+ " is no matching given columns in the referenced table. Revised Model.");	
			
			if(dbSeq.size()>0)
				errorList.add("ERROR: Sequences : "+es.cv.gvcase.mdt.gen.transf.Extensions.formateo(dbSeq)+ " are incompletes. Revised Model.");
			
        	        		
		} else {
			errorList.add("Database Not support this transformation");			
		}	
        		
		return true;
		
	}
	

	
}
