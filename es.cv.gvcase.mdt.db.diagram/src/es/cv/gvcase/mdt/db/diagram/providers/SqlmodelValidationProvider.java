/*******************************************************************************
 * Copyright (c) 2011 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 			Francisco Javier Cano Muñoz (Prodevelop) - factory adaptation
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.diagram.providers;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.datatools.modelbase.sql.constraints.ForeignKey;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.PersistentTable;
import org.eclipse.datatools.modelbase.sql.tables.ViewTable;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IClientSelector;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.action.IStatusLineManager;

import es.cv.gvcase.mdt.common.util.MDTUtil;
import es.cv.gvcase.mdt.db.diagram.edit.parts.SchemaEditPart;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelDiagramEditorPlugin;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry;

/**
 * @generated
 */
public class SqlmodelValidationProvider {

	/**
	 * @generated
	 */
	private static boolean constraintsActive = false;

	/**
	 * @generated
	 */
	public static boolean shouldConstraintsBePrivate() {
		return false;
	}

	/**
	 * @generated
	 */
	public static void runWithConstraints(
			TransactionalEditingDomain editingDomain, Runnable operation) {
		final Runnable op = operation;
		Runnable task = new Runnable() {
			public void run() {
				try {
					constraintsActive = true;
					op.run();
				} finally {
					constraintsActive = false;
				}
			}
		};
		if (editingDomain != null) {
			try {
				editingDomain.runExclusive(task);
			} catch (Exception e) {
				SqlmodelDiagramEditorPlugin.getInstance().logError(
						"Validation failed", e); //$NON-NLS-1$
			}
		} else {
			task.run();
		}
	}

	/**
	 * @generated
	 */
	static boolean isInDefaultEditorContext(Object object) {
		if (shouldConstraintsBePrivate() && !constraintsActive) {
			return false;
		}
		if (object instanceof View) {
			return constraintsActive
					&& SchemaEditPart.MODEL_ID.equals(SqlmodelVisualIDRegistry
							.getModelID((View) object));
		}
		return true;
	}

	/**
	 * @generated
	 */
	public static class DefaultCtx implements IClientSelector {

		/**
		 * @generated
		 */
		public boolean selects(Object object) {
			return isInDefaultEditorContext(object);
		}
	}

	/**
	 * @generated
	 */
	public static class Adapter1 extends AbstractModelConstraint {

		/**
		 * @generated NOT ; implementation provided
		 */
		public IStatus validate(IValidationContext ctx) {
			ENamedElement context = (ENamedElement) ctx.getTarget();
			if (context instanceof PersistentTable || context instanceof Column
					|| context instanceof ViewTable || context instanceof ForeignKey) {
				// only PersistentTables, Columns, ForeignKey and ViewTables are checked for a valid
				// name.
				IStatus status = ValidationProvider.validateUniqueName(context);
				if (status.getSeverity() != IStatus.OK) {
					// UMLDiagramEditorPlugin.getInstance().getLog().log(status);
					IStatusLineManager statusLine = MDTUtil
							.getStatusLineManager();
					if (statusLine != null) {
						statusLine.setMessage(status.getMessage());
					}
					return ctx.createFailureStatus(null);
				}
				return ctx.createSuccessStatus();
			}
			return ctx.createSuccessStatus();
		}
	}

	/**
	 * @generated
	 */
	static String formatElement(EObject object) {
		return EMFCoreUtil.getQualifiedName(object, true);
	}

}
