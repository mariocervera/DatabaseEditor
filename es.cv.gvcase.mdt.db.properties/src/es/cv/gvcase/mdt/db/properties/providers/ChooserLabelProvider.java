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

import org.eclipse.datatools.modelbase.sql.schema.provider.SqlmodelEditPlugin;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.emf.ecoretools.tabbedproperties.providers.TabbedPropertiesLabelProvider;
import org.eclipse.swt.graphics.Image;

public class ChooserLabelProvider extends  TabbedPropertiesLabelProvider {
	
	public ChooserLabelProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}
	
	@Override
	public String getText(Object element) {
		
		String elementName = "";
		if(element instanceof ENamedElement) {
			if(((ENamedElement)element).getName() != null) elementName = ((ENamedElement)element).getName();
		}
		
		String elementClass = element.getClass().getSimpleName();
		if(elementClass != null && elementClass.endsWith("Impl")) {
			elementClass = elementClass.substring(0, elementClass.length() - 4);
		}
		
		if(elementClass == null) elementClass = "";
		
		if(!elementClass.equals("NullObject")) {
			return "<" + elementClass + "> " + elementName;
		}
		else {
			return elementName;
		}
		
	}
	
	@Override
	public Image getImage(Object object) {
		
		String imageName = object.getClass().getSimpleName();
		if(imageName != null && imageName.endsWith("Impl")) {
			imageName = imageName.substring(0, imageName.length() - 4);
		}
		
		if(imageName == null) imageName = "";
		
		try {
			return ExtendedImageRegistry.getInstance().getImage(
					SqlmodelEditPlugin.INSTANCE.getImage("full/obj16/" + imageName));
		}
		catch(Exception e) {
			return super.getImage(object);
		}
	}

}
