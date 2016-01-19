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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.GetParserOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserProvider;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ParserHintAdapter;
import org.eclipse.gmf.runtime.notation.View;

import es.cv.gvcase.mdt.db.diagram.edit.parts.Column2EditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ColumnEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ForeignKeyNameEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.PersistentTable2EditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.PersistentTableNameEditPart;
import es.cv.gvcase.mdt.db.diagram.edit.parts.ViewTableNameEditPart;
import es.cv.gvcase.mdt.db.diagram.parsers.ColumnWithTableNameParser;
import es.cv.gvcase.mdt.db.diagram.parsers.ColumnWithTypeParser;
import es.cv.gvcase.mdt.db.diagram.parsers.MessageFormatParser;
import es.cv.gvcase.mdt.db.diagram.part.SqlmodelVisualIDRegistry;

/**
 * @generated
 */
public class SqlmodelParserProvider extends AbstractProvider implements
		IParserProvider {

	/**
	 * @generated
	 */
	private IParser persistentTableName_4001Parser;

	/**
	 * @generated
	 */
	private IParser getPersistentTableName_4001Parser() {
		if (persistentTableName_4001Parser == null) {
			EAttribute[] features = new EAttribute[] { EcorePackage.eINSTANCE
					.getENamedElement_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			persistentTableName_4001Parser = parser;
		}
		return persistentTableName_4001Parser;
	}

	/**
	 * @generated
	 */
	private IParser viewTableName_4002Parser;

	/**
	 * @generated
	 */
	private IParser getViewTableName_4002Parser() {
		if (viewTableName_4002Parser == null) {
			EAttribute[] features = new EAttribute[] { EcorePackage.eINSTANCE
					.getENamedElement_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			viewTableName_4002Parser = parser;
		}
		return viewTableName_4002Parser;
	}

	/**
	 * @generated NOT ; modified to chenge type of parser to
	 *            ColumnWithTypeParser
	 */
	private ColumnWithTypeParser column_2001Parser;

	/**
	 * @generated NOT ; modified to use a parser that also shows the type of the
	 *            column
	 */
	protected IParser getColumn_2001Parser() {
		if (column_2001Parser == null) {
			column_2001Parser = new ColumnWithTypeParser(EcorePackage.eINSTANCE
					.getENamedElement_Name());
			column_2001Parser.setViewPattern("{0}");
			column_2001Parser.setEditPattern("{0}");
		}
		return column_2001Parser;
	}

	/**
	 * @generated NOT ; modified to change type to ColumnWithTableNameParser
	 */
	private ColumnWithTableNameParser column_2002Parser;

	/**
	 * @generated NOT ; modifies to use the ColumnWithTableNameParser
	 */
	protected IParser getColumn_2002Parser() {
		if (column_2002Parser == null) {
			column_2002Parser = new ColumnWithTableNameParser(
					EcorePackage.eINSTANCE.getENamedElement_Name());
			column_2002Parser.setViewPattern("{0}");
			column_2002Parser.setEditPattern("{0}");
		}
		return column_2002Parser;
	}

	/**
	 * @generated
	 */
	private IParser persistentTable_2003Parser;

	/**
	 * @generated
	 */
	private IParser getPersistentTable_2003Parser() {
		if (persistentTable_2003Parser == null) {
			EAttribute[] features = new EAttribute[] { EcorePackage.eINSTANCE
					.getENamedElement_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			persistentTable_2003Parser = parser;
		}
		return persistentTable_2003Parser;
	}

	/**
	 * @generated
	 */
	private IParser foreignKeyName_4003Parser;

	/**
	 * @generated
	 */
	private IParser getForeignKeyName_4003Parser() {
		if (foreignKeyName_4003Parser == null) {
			EAttribute[] features = new EAttribute[] { EcorePackage.eINSTANCE
					.getENamedElement_Name() };
			MessageFormatParser parser = new MessageFormatParser(features);
			foreignKeyName_4003Parser = parser;
		}
		return foreignKeyName_4003Parser;
	}

	/**
	 * @generated
	 */
	protected IParser getParser(int visualID) {
		switch (visualID) {
		case PersistentTableNameEditPart.VISUAL_ID:
			return getPersistentTableName_4001Parser();
		case ViewTableNameEditPart.VISUAL_ID:
			return getViewTableName_4002Parser();
		case ColumnEditPart.VISUAL_ID:
			return getColumn_2001Parser();
		case Column2EditPart.VISUAL_ID:
			return getColumn_2002Parser();
		case PersistentTable2EditPart.VISUAL_ID:
			return getPersistentTable_2003Parser();
		case ForeignKeyNameEditPart.VISUAL_ID:
			return getForeignKeyName_4003Parser();
		}
		return null;
	}

	/**
	 * Utility method that consults ParserService
	 * 
	 * @generated
	 */
	public static IParser getParser(IElementType type, EObject object,
			String parserHint) {
		return ParserService.getInstance().getParser(
				new HintAdapter(type, object, parserHint));
	}

	/**
	 * @generated
	 */
	public IParser getParser(IAdaptable hint) {
		String vid = (String) hint.getAdapter(String.class);
		if (vid != null) {
			return getParser(SqlmodelVisualIDRegistry.getVisualID(vid));
		}
		View view = (View) hint.getAdapter(View.class);
		if (view != null) {
			return getParser(SqlmodelVisualIDRegistry.getVisualID(view));
		}
		return null;
	}

	/**
	 * @generated
	 */
	public boolean provides(IOperation operation) {
		if (operation instanceof GetParserOperation) {
			IAdaptable hint = ((GetParserOperation) operation).getHint();
			if (SqlmodelElementTypes.getElement(hint) == null) {
				return false;
			}
			return getParser(hint) != null;
		}
		return false;
	}

	/**
	 * @generated
	 */
	private static class HintAdapter extends ParserHintAdapter {

		/**
		 * @generated
		 */
		private final IElementType elementType;

		/**
		 * @generated
		 */
		public HintAdapter(IElementType type, EObject object, String parserHint) {
			super(object, parserHint);
			assert type != null;
			elementType = type;
		}

		/**
		 * @generated
		 */
		public Object getAdapter(Class adapter) {
			if (IElementType.class.equals(adapter)) {
				return elementType;
			}
			return super.getAdapter(adapter);
		}
	}

}
