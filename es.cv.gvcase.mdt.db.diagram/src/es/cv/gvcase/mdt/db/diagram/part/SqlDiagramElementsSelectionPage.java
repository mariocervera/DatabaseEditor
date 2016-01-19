/***************************************************************************
 * Copyright (c) 2011 Conselleria de Infraestructuras y Transporte,
 * Generalitat de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Miguel Llacer San Fernanado (Prodevelop) – initial API and implementation
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.diagram.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.datatools.modelbase.sql.constraints.ForeignKey;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.PersistentTable;
import org.eclipse.datatools.modelbase.sql.tables.ViewTable;
import org.eclipse.emf.ecore.EObject;

import es.cv.gvcase.mdt.common.part.DiagramElementsSelectionPage;

/**
 * A wizard page that allows selecting elements from a tree. Used in a new
 * diagram wizard. Those elements will be displayed in the newly created
 * diagram.
 */
public class SqlDiagramElementsSelectionPage extends
		DiagramElementsSelectionPage {

	public SqlDiagramElementsSelectionPage(String name, String title,
			String description) {
		super(name, title, description);
	}

	@Override
	protected boolean hasVisualID(EObject eObject) {
		if (eObject instanceof PersistentTable || eObject instanceof ViewTable
				|| eObject instanceof Column || eObject instanceof ForeignKey) {
			return true;
		}
		return false;
	}

	@Override
	protected List<EObject> referencedElements(EObject eObject) {
		List<EObject> elements = new ArrayList<EObject>();
		if (eObject instanceof PersistentTable) {
			for (Object o : ((PersistentTable) eObject).getForeignKeys()) {
				if (!(o instanceof ForeignKey)) {
					continue;
				}
				if (((ForeignKey) o).getReferencedTable() != null) {
					elements.add(((ForeignKey) o).getReferencedTable());
				}
			}
		} else if (eObject instanceof ViewTable) {
			for (Column c : ((ViewTable) eObject).getViewedColumns()) {
				if (c.getTable() != null && !elements.contains(c.getTable())) {
					elements.add(c.getTable());
				}
			}
		}
		return elements;
	}

	@Override
	protected List<EObject> referencingElements(EObject eObject) {
		List<EObject> elements = new ArrayList<EObject>();
		if (eObject instanceof PersistentTable) {
			for (ForeignKey fk : ((PersistentTable) eObject)
					.getReferencingForeignKeys()) {
				if (fk.getBaseTable() != null) {
					elements.add(fk.getBaseTable());
				}
			}
			for (EObject vt : eObject.eContainer().eContents()) {
				if (!(vt instanceof ViewTable)) {
					continue;
				}
				for (Column c : ((ViewTable) vt).getViewedColumns()) {
					if (((PersistentTable) eObject).getColumns().contains(c)
							&& !elements.contains(vt)) {
						elements.add(vt);
					}
				}
			}
		} else if (eObject instanceof ViewTable) {
			// no elements references to ViewTable elements
		}
		return elements;
	}

	@Override
	protected List<EObject> allRelatedElements(EObject eObject) {
		List<EObject> elements = new ArrayList<EObject>();

		elements.addAll(referencedElements(eObject));
		elements.addAll(referencingElements(eObject));

		return elements;
	}

}
