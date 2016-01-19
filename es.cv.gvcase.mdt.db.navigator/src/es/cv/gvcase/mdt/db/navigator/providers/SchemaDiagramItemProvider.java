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
import java.util.List;

import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.schema.provider.SchemaItemProvider;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResource;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import es.cv.gvcase.mdt.common.util.MultiDiagramUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class SchemaDiagramItemProvider.
 */
public class SchemaDiagramItemProvider extends SchemaItemProvider {

	/**
	 * Instantiates a new schema diagram item provider.
	 * 
	 * @param adapterFactory
	 *            the adapter factory
	 */
	public SchemaDiagramItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.uml2.uml.edit.providers.ElementItemProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Collection<?> getChildren(Object object) {
		Collection<?> children = super.getChildren(object);
		List<EObject> childrens = new ArrayList<EObject>();
		for (Object child : children) {
			if (child instanceof EObject) {
				childrens.add((EObject) child);
			}
		}
		if (object instanceof Schema) {
			childrens.addAll(getDiagramsAssociatedToSchema((Schema) object));
		}
		return childrens;
	}

	/**
	 * Gets the diagrams associated to schema.
	 * 
	 * @param schema
	 *            the schema
	 * 
	 * @return the diagrams associated to schema
	 */
	private Collection<Diagram> getDiagramsAssociatedToSchema(Schema schema) {
		Collection<Diagram> diagrams = new ArrayList<Diagram>();
		IEditorPart activeEditor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (activeEditor != null && activeEditor instanceof DiagramEditor) {
			TransactionalEditingDomain domain = ((DiagramEditor) activeEditor)
					.getEditingDomain();
			if (domain == null) {
				return diagrams;
			}
			for (Resource resource : domain.getResourceSet().getResources()) {
				if (resource instanceof GMFResource) {
					for (EObject eObject : resource.getContents()) {
						if (eObject instanceof Diagram) {
							if (isDiagramAssociatedToSchema((Diagram) eObject,
									schema)) {
								diagrams.add((Diagram) eObject);
							}
						}
					}
				}
			}
		}
		return diagrams;
	}

	/**
	 * Checks if is diagram associated to schema.
	 * 
	 * @param diagram
	 *            the diagram
	 * @param schema
	 *            the schema
	 * 
	 * @return true, if is diagram associated to schema
	 */
	private boolean isDiagramAssociatedToSchema(Diagram diagram, Schema schema) {
		EAnnotation eAnnotation = diagram
				.getEAnnotation(MultiDiagramUtil.DiagramsRelatedToElement);
		if (eAnnotation != null) {
			if (eAnnotation.getReferences().contains(schema)) {
				return true;
			}
		}
		return false;
	}

}
