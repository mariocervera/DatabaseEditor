/***************************************************************************
 * Copyright (c) 2008 Conselleria de Infraestructuras y Transporte,
 * Generalitat de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Francisco Javier Cano Muñoz (Prodevelop) - initial API implementation
 * Mario Cervera Ubeda (Integranova)
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.navigator.providers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.datatools.modelbase.sql.accesscontrol.provider.SQLAccessControlItemProviderAdapterFactory;
import org.eclipse.datatools.modelbase.sql.constraints.provider.SQLConstraintsItemProviderAdapterFactory;
import org.eclipse.datatools.modelbase.sql.datatypes.provider.SQLDataTypesItemProviderAdapterFactory;
import org.eclipse.datatools.modelbase.sql.expressions.provider.SQLExpressionsItemProviderAdapterFactory;
import org.eclipse.datatools.modelbase.sql.routines.provider.SQLRoutinesItemProviderAdapterFactory;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.provider.SQLSchemaItemProviderAdapterFactory;
import org.eclipse.datatools.modelbase.sql.statements.provider.SQLStatementsItemProviderAdapterFactory;
import org.eclipse.datatools.modelbase.sql.tables.provider.SQLTablesItemProviderAdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResourceFactory;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;

import es.cv.gvcase.ide.navigator.provider.MOSKittCommonContentProvider;
import es.cv.gvcase.ide.navigator.util.NavigatorUtil;
import es.cv.gvcase.ide.navigator.view.MOSKittCommonNavigator;
import es.cv.gvcase.mdt.db.navigator.decorators.NavigatorLabelDecorator;

/**
 * The Class SQLContentProvider.
 */
public class SQLContentProvider extends MOSKittCommonContentProvider {

	/** The resource factories. */
	protected static Map<String, Object> resourceFactories = null;

	/** The Model id. */
	private final String ModelID = "Sqlmodel";
	
	/** The Editor id. */
	private final String EditorID = "es.cv.gvcase.mdt.db.diagram.part.SqlmodelDiagramEditorID";

	/**
	 * Instantiates a new sQL content provider.
	 */
	public SQLContentProvider() {
		// Common factories
		addFactory(new DiagramSchemaProviderAdapterFactory());
		addFactory(new SqlmodelItemProviderAdapterFactory());
		addFactory(new SQLAccessControlItemProviderAdapterFactory());
		addFactory(new SQLConstraintsItemProviderAdapterFactory());
		addFactory(new SQLDataTypesItemProviderAdapterFactory());
		addFactory(new SQLExpressionsItemProviderAdapterFactory());
		addFactory(new SQLRoutinesItemProviderAdapterFactory());
		addFactory(new SQLSchemaItemProviderAdapterFactory());
		addFactory(new SQLStatementsItemProviderAdapterFactory());
		addFactory(new SQLTablesItemProviderAdapterFactory(), true);
		
		// resource factories
		if (resourceFactories == null) {
			resourceFactories = new HashMap<String, Object>();
			resourceFactories.put("sqlschema", XMIResourceFactoryImpl.class);
			resourceFactories
					.put("sqlschema_diagram", GMFResourceFactory.class);
			resourceFactories.put("ecore", ResourceFactoryImpl.class);
		}

		// model to editor mapping
		NavigatorUtil.MOSKittModelIDs.getModelToEditorMap().put(ModelID,
				EditorID);
	}
	
	@Override
	protected ILabelDecorator getLabelDecorator() {
		
		return new NavigatorLabelDecorator();
	}

	/* (non-Javadoc)
	 * @see es.cv.gvcase.ide.navigator.provider.MOSKittCommonContentProvider#init(org.eclipse.ui.navigator.ICommonContentExtensionSite)
	 */
	@Override
	public void init(ICommonContentExtensionSite config) {
		super.init(config);
		MOSKittCommonNavigator.addPropertySheetContributor(this);
	}

	/* (non-Javadoc)
	 * @see es.cv.gvcase.ide.navigator.provider.MOSKittCommonContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		MOSKittCommonNavigator.removePropertySheetContributor(this);
	}

	/* (non-Javadoc)
	 * @see es.cv.gvcase.ide.navigator.provider.MOSKittCommonContentProvider#getContributorID()
	 */
	@Override
	public String getContributorID() {
//		return SqlmodelDiagramEditorPlugin.ID;
		return "es.cv.gvcase.mdt.db.properties";
	}

	/* (non-Javadoc)
	 * @see es.cv.gvcase.ide.navigator.provider.MOSKittCommonContentProvider#getResourceFactories()
	 */
	@Override
	protected Map<String, Object> getResourceFactories() {
		return resourceFactories;
	}

	/* (non-Javadoc)
	 * @see es.cv.gvcase.ide.navigator.provider.MOSKittCommonContentProvider#getViewerID()
	 */
	@Override
	protected String getViewerID() {
		return contentExtensionSite.getExtensionStateModel().getViewerId();
	}

	/* (non-Javadoc)
	 * @see es.cv.gvcase.ide.navigator.provider.MOSKittCommonContentProvider#hasPropertySheetPage()
	 */
	@Override
	public boolean hasPropertySheetPage() {
		Resource resource = getResourceFromEditor();
		return canHandleResource(resource);
	}

	/* (non-Javadoc)
	 * @see es.cv.gvcase.ide.navigator.provider.MOSKittCommonContentProvider#canHandleResource(org.eclipse.emf.ecore.resource.Resource)
	 */
	@Override
	protected boolean canHandleResource(Resource resource) {
		if (resource != null) {
			List<EObject> contents = resource.getContents();
			if (contents.size() > 0 && contents.get(0) instanceof Database) {
				return true;
			}
		}
		return false;
	}

}
