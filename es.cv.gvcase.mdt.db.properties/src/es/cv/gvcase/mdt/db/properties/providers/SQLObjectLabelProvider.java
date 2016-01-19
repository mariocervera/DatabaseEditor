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
package es.cv.gvcase.mdt.db.properties.providers;

import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class SQLObjectLabelProvider implements ILabelProvider{
	
	
	public String getText(Object object) {
		
		if(object instanceof SQLObject) {
			SQLObject sqlobject = (SQLObject) object;
			String name = "";
			if(sqlobject.getName() != null) name = sqlobject.getName();
			String className = sqlobject.getClass().getSimpleName();
			//Remove the "Impl" part of the name
			className = className.substring(0, className.length() - 4);
			return "<" + className + "> " + name;
		}
		
		return "";
	}
	
	
	public Image getImage(Object object) {

		if(object instanceof SQLObject) {
			SQLObject sqlobject = (SQLObject) object;
			String imagePath = "";
			try {
				imagePath = FileLocator.toFileURL(Platform.getBundle("org.eclipse.datatools.modelbase.sql.edit").getResource("icons/full/obj16")).getPath();
			}
			catch(IOException e) {return null;}
			
			String className = sqlobject.getClass().getSimpleName();
			//Remove the "Impl" part of the name
			className = className.substring(0, className.length() - 4);
			imagePath += className + ".gif";
			Image image = new Image(Display.getCurrent(), imagePath);
			return image;
		}
		
		return null;
	}
	
	
	public void addListener(ILabelProviderListener listener) {}

	public void dispose() {}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {}

}
