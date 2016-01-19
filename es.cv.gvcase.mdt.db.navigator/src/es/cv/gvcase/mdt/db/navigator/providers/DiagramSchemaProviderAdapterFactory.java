/***************************************************************************
 * Copyright (c) 2008 Conselleria de Infraestructuras y Transporte,
 * Generalitat de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Francisco Javier Cano Muñoz (Prodevelop) – initial api implementation
 * Mario Cervera Ubeda (Integranova)
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.navigator.providers;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.schema.provider.SQLSchemaItemProviderAdapterFactory;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITableItemLabelProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.gmf.runtime.notation.Diagram;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating DiagramPackageProviderAdapter objects.
 */
public class DiagramSchemaProviderAdapterFactory extends SQLSchemaItemProviderAdapterFactory {

	/** The supported types. */
	protected Collection<Object> supportedTypes = new ArrayList<Object>();

	/**
	 * Instantiates a new diagram package provider adapter factory.
	 */
	public DiagramSchemaProviderAdapterFactory() {
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemPropertySource.class);
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(ITableItemLabelProvider.class);
	}
	
	/** The diagram adapter. */
	Adapter diagramAdapter = null;
	
	/** The schema adapter. */
	Adapter schemaAdapter = null;
	
	/**
	 * Creates a new DiagramSchemaItemProvider object.
	 * 
	 * @return the adapter
	 */
	public Adapter createDiagramAdapter() {
		if (diagramAdapter == null) {
			diagramAdapter = new DiagramSchemaItemProvider(this);
		}
		return diagramAdapter;
	}
	
	@Override
	public Adapter createSchemaAdapter() {
		
		if (diagramAdapter == null) {
			diagramAdapter = new SchemaDiagramItemProvider(this);
		}
		return diagramAdapter;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.uml2.uml.edit.providers.UMLItemProviderAdapterFactory#createAdapter(org.eclipse.emf.common.notify.Notifier)
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		if (target instanceof Diagram) {
			return createDiagramAdapter();
		}
		else if (target instanceof Schema) {
			return createSchemaAdapter();
		}
		
		return super.createAdapter(target);
	}

}
