/*******************************************************************************
 * Copyright (c) 2007 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Mario Cervera Ubeda (Integranova) � Initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.gen.launchers;

import java.io.File;
import java.io.FileOutputStream;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.datatools.connectivity.sqm.core.rte.DDLGenerator;
import org.eclipse.datatools.connectivity.sqm.core.rte.EngineeringOption;
import org.eclipse.datatools.modelbase.sql.accesscontrol.impl.SQLAccessControlPackageImpl;
import org.eclipse.datatools.modelbase.sql.constraints.impl.SQLConstraintsPackageImpl;
import org.eclipse.datatools.modelbase.sql.datatypes.impl.SQLDataTypesPackageImpl;
import org.eclipse.datatools.modelbase.sql.expressions.impl.SQLExpressionsPackageImpl;
import org.eclipse.datatools.modelbase.sql.routines.impl.SQLRoutinesPackageImpl;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.schema.impl.SQLSchemaPackageImpl;
import org.eclipse.datatools.modelbase.sql.statements.impl.SQLStatementsPackageImpl;
import org.eclipse.datatools.modelbase.sql.tables.impl.SQLTablesPackageImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class JavaLauncher
{
	public static boolean runTransformation(DDLGenerator generator,
			String destinationFile, String modelPath, String vendor, String version)
	{
		
		// Load source model
		
		SQLAccessControlPackageImpl.init();
		SQLConstraintsPackageImpl.init();
		SQLDataTypesPackageImpl.init();
		SQLExpressionsPackageImpl.init();
		SQLRoutinesPackageImpl.init();
		SQLSchemaPackageImpl.init();
		SQLStatementsPackageImpl.init();
		SQLTablesPackageImpl.init();
		
        XMIResourceFactoryImpl _xmiFac = new XMIResourceFactoryImpl();        
        Schema rootElement = null;
        EList rootAuthorization = null;
        
        File sourceModelFile = new File (modelPath); 
        ResourceSet rSet = new ResourceSetImpl ();
        
        rSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*",_xmiFac);
        URI uri = URI.createFileURI(sourceModelFile.getAbsolutePath());
        Resource resource = rSet.getResource(uri, true);
      
        if (resource != null)
        {
            if (resource.getContents().size() > 0)
            {
            	Database db = (Database)resource.getContents().get(0);
            	db.setVendor(vendor);
                db.setVersion(version);
            	
            	rootElement = (Schema) db.getSchemas().get(0);
            	rootAuthorization=db.getAuthorizationIds();
            }
        } 

        //DDL Generation
	
		String[] s;
		int size=rootAuthorization.size();
		SQLObject[] elements = new SQLObject[size+1];
		
		elements[0] = rootElement;
        for(int j = 0; j < size; j++)
		{
			elements[j+1] = (SQLObject) rootAuthorization.get(j);
		}
        
		EngineeringOption[] eo = generator.getOptions(elements);
		
		for(int i = 0; i < eo.length; i++)
		{
			if(eo[i].getId().equals("GENERATE_TABLES")) eo[i].setBoolean(true);
			if(eo[i].getId().equals("GENERATE_SCHEMAS")) eo[i].setBoolean(true);
			if(eo[i].getId().equals("GENERATE_FK_CONSTRAINTS")) eo[i].setBoolean(true);
			if(eo[i].getId().equals("GENERATE_PK_CONSTRAINTS")) eo[i].setBoolean(true);
			if(eo[i].getId().equals("GENERATE_CK_CONSTRAINTS")) eo[i].setBoolean(true);
			if(eo[i].getId().equals("GENERATE_CREATE_STATEMENTS")) eo[i].setBoolean(true);
			if(eo[i].getId().equals("GENERATE_SCHEMAS")) eo[i].setBoolean(true);
			if(eo[i].getId().equals("GENERATE_INDEX")) eo[i].setBoolean(true);
			if(eo[i].getId().equals("GENERATE_COMMENTS")) eo[i].setBoolean(true);
			if(eo[i].getId().equals("GENERATE_VIEWS")) eo[i].setBoolean(true);
			if(eo[i].getId().equals("GENERATE_SEQUENCES")) eo[i].setBoolean(true);
			if(eo[i].getId().equals("GENERATE_FULLY_QUALIFIED_NAME")) eo[i].setBoolean(true);
			if(eo[i].getId().equals("GENERATE_QUOTED_IDENTIFIER")) eo[i].setBoolean(true);
		}
		
		s = generator.generateDDL(elements, new NullProgressMonitor());
		
		//Generate destination folder
		
		try
		{
		
		File f = new File(destinationFile);
		
		FileOutputStream fop = new FileOutputStream(f);
		
		for(int i = 0; i< s.length; i++)
		{
			fop.write((s[i] + "\n").getBytes());
		}
		fop.flush();
		fop.close();
	      
		}
		catch(Exception e) {System.out.println(e.getMessage());}
		
		
		return (s.length > 0);
		
		
	}
}
