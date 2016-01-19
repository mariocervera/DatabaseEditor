/***************************************************************************
 * Copyright (c) 2007 Conselleria de Infraestructuras y Transporte,
 * Generalitat de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Mario Cervera Ubeda (Integranova)
 *
 ******************************************************************************/
package es.cv.gvcase.mdt.db.diagram.providers;

import org.eclipse.datatools.modelbase.sql.constraints.ForeignKey;
import org.eclipse.datatools.modelbase.sql.expressions.QueryExpressionDefault;
import org.eclipse.datatools.modelbase.sql.expressions.SQLExpressionsFactory;
import org.eclipse.datatools.modelbase.sql.expressions.SQLExpressionsPackage;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.PersistentTable;
import org.eclipse.datatools.modelbase.sql.tables.ViewTable;

import es.cv.gvcase.mdt.common.actions.LabelHelper;
import es.cv.gvcase.mdt.db.diagram.expressions.SqlmodelOCLFactory;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelDiagramEditorPlugin;

/**
 * @generated
 */
public class ElementInitializers {

	/**
	 * @generated
	 */
	public static void init_PersistentTable_1001(PersistentTable instance) {
		try {
			Object value_0 = name_PersistentTable_1001(instance);
			instance.setName((String) value_0);
		} catch (RuntimeException e) {
			SqlmodelDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public static void init_ViewTable_1002(ViewTable instance) {
		try {
			QueryExpressionDefault newInstance_0_0 = SQLExpressionsFactory.eINSTANCE
					.createQueryExpressionDefault();
			instance.setQueryExpression(newInstance_0_0);
			Object value_0_0_0 = SqlmodelOCLFactory
					.getExpression(
							"\'SELECT * FROM ...\'",
							SQLExpressionsPackage.eINSTANCE
									.getQueryExpressionDefault()).evaluate(
							newInstance_0_0);
			newInstance_0_0.setSQL((String) value_0_0_0);

			Object value_1 = name_ViewTable_1002(instance);
			instance.setName((String) value_1);
		} catch (RuntimeException e) {
			SqlmodelDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public static void init_Column_2001(Column instance) {
		try {
			Object value_0 = name_Column_2001(instance);
			instance.setName((String) value_0);
		} catch (RuntimeException e) {
			SqlmodelDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public static void init_ForeignKey_3001(ForeignKey instance) {
		try {
			Object value_0 = name_ForeignKey_3001(instance);
			instance.setName((String) value_0);
		} catch (RuntimeException e) {
			SqlmodelDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated NOT ; modified to provide implementation
	 */
	private static String name_PersistentTable_1001(PersistentTable self) {
		return LabelHelper.INSTANCE.findName(self.eContainer(), self);
	}

	/**
	 * @generated NOT ; modified to provide implementation
	 */
	private static String name_ViewTable_1002(ViewTable self) {
		return LabelHelper.INSTANCE.findName(self.eContainer(), self);
	}

	/**
	 * @generated NOT ; modified to provide implementation
	 */
	private static String name_Column_2001(Column self) {
		return LabelHelper.INSTANCE.findName(self.eContainer(), self);
	}

	/**
	 * @generated NOT ; modified to provide implementation
	 */
	private static String name_ForeignKey_3001(ForeignKey self) {
		return LabelHelper.INSTANCE.findName(self.eContainer(), self);
	}
}
