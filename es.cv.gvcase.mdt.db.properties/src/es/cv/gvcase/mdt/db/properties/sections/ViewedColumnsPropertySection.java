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
package es.cv.gvcase.mdt.db.properties.sections;

import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.PersistentTable;
import org.eclipse.datatools.modelbase.sql.tables.SQLTablesPackage;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.datatools.modelbase.sql.tables.ViewTable;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import es.cv.gvcase.mdt.common.sections.AbstractCollectionPropertySection;
import es.cv.gvcase.mdt.db.properties.providers.SQLObjectLabelProvider;

public class ViewedColumnsPropertySection extends AbstractCollectionPropertySection {

	@Override
	protected EStructuralFeature getFeature() {

		return SQLTablesPackage.eINSTANCE.getViewTable_ViewedColumns();
	}

	@Override
	protected String getLabelText() {
		
		return "Viewed Columns";
	}
	
	@Override
	protected EList<EObject> getCandidateElements() {
		
		SQLObject selectedObject = (SQLObject)getEObject();
		EList<EObject> columns = new BasicEList<EObject>();
		
		if(selectedObject instanceof ViewTable) {
			ViewTable viewTable = (ViewTable) selectedObject;
			for(Table table: viewTable.getViewedTables()) {
				if(table instanceof PersistentTable) {
					PersistentTable pTable = (PersistentTable) table;
					columns.addAll(pTable.getColumns());
				}
			}
		}
		
		return columns;
	}
	
	@Override
	protected String getMembersText() {
		return "";
	}
	
	private IBaseLabelProvider labelProvider = new ViewedColumnLabelProvider();
	
	@Override
	protected IBaseLabelProvider getLabelProvider() {
		return labelProvider;
	}
	
	public class ViewedColumnLabelProvider extends LabelProvider {
		ILabelProvider wrappedProvider = new SQLObjectLabelProvider();
		
		
		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
		 */
		@Override
		public Image getImage(Object element) {
			
			return wrappedProvider.getImage(element);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
		 */
		@Override
		public String getText(Object element) {
			if (element instanceof Column){
				Column column = (Column) element;
				return column.getTable().getName()+" :: "+wrappedProvider.getText(element);
			}
			return wrappedProvider.getText(element);
		}
		
	}

}
