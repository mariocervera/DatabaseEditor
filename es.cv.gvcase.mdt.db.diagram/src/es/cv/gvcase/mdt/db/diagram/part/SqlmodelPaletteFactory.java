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
package es.cv.gvcase.mdt.db.diagram.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeConnectionTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeCreationTool;

import es.cv.gvcase.mdt.common.palette.EditorPaletteRegistry;
import es.cv.gvcase.mdt.db.diagram.providers.SqlmodelElementTypes;

/**
 * @generated
 */
public class SqlmodelPaletteFactory {

	/**
	 * @generated
	 */
	public void fillPalette(PaletteRoot paletteRoot) {
		paletteRoot.add(createSchemaElements1Group());
		paletteRoot.add(createRelationships2Group());

		EditorPaletteRegistry.getInstance().customizePaletteForEditor(
				SqlmodelDiagramEditor.ID, paletteRoot);
	}

	/**
	 * Creates "Schema Elements" palette tool group
	 * @generated
	 */
	private PaletteContainer createSchemaElements1Group() {
		PaletteDrawer paletteContainer = new PaletteDrawer(
				Messages.SchemaElements1Group_title);

		paletteContainer.setId("Schema Elements");
		paletteContainer.add(createTable1CreationTool());
		paletteContainer.add(createView2CreationTool());
		paletteContainer.add(createColumn3CreationTool());
		return paletteContainer;
	}

	/**
	 * Creates "Relationships" palette tool group
	 * @generated
	 */
	private PaletteContainer createRelationships2Group() {
		PaletteDrawer paletteContainer = new PaletteDrawer(
				Messages.Relationships2Group_title);

		paletteContainer.setId("Relationships");
		paletteContainer.add(createForeignKey1CreationTool());
		paletteContainer.add(createViewedTable2CreationTool());
		return paletteContainer;
	}

	/**
	 * @generated
	 */
	private ToolEntry createTable1CreationTool() {
		List/*<IElementType>*/types = new ArrayList/*<IElementType>*/(1);
		types.add(SqlmodelElementTypes.PersistentTable_1001);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.Table1CreationTool_title,
				Messages.Table1CreationTool_desc, types);

		entry.setId("Table");
		entry.setSmallIcon(SqlmodelDiagramEditorPlugin
				.findImageDescriptor("icons/sqlicons/table.gif")); //$NON-NLS-1$
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createView2CreationTool() {
		List/*<IElementType>*/types = new ArrayList/*<IElementType>*/(1);
		types.add(SqlmodelElementTypes.ViewTable_1002);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.View2CreationTool_title,
				Messages.View2CreationTool_desc, types);

		entry.setId("View");
		entry.setSmallIcon(SqlmodelDiagramEditorPlugin
				.findImageDescriptor("icons/sqlicons/view.gif")); //$NON-NLS-1$
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createColumn3CreationTool() {
		List/*<IElementType>*/types = new ArrayList/*<IElementType>*/(1);
		types.add(SqlmodelElementTypes.Column_2001);
		NodeToolEntry entry = new NodeToolEntry(
				Messages.Column3CreationTool_title,
				Messages.Column3CreationTool_desc, types);

		entry.setId("Column");
		entry.setSmallIcon(SqlmodelDiagramEditorPlugin
				.findImageDescriptor("icons/sqlicons/column.gif")); //$NON-NLS-1$
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createForeignKey1CreationTool() {
		List/*<IElementType>*/types = new ArrayList/*<IElementType>*/(1);
		types.add(SqlmodelElementTypes.ForeignKey_3001);
		LinkToolEntry entry = new LinkToolEntry(
				Messages.ForeignKey1CreationTool_title,
				Messages.ForeignKey1CreationTool_desc, types);

		entry.setId("Foreign Key");
		entry.setSmallIcon(SqlmodelDiagramEditorPlugin
				.findImageDescriptor("icons/sqlicons/event.gif")); //$NON-NLS-1$
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createViewedTable2CreationTool() {
		List/*<IElementType>*/types = new ArrayList/*<IElementType>*/(1);
		types.add(SqlmodelElementTypes.ViewTableViewedTables_3002);
		LinkToolEntry entry = new LinkToolEntry(
				Messages.ViewedTable2CreationTool_title,
				Messages.ViewedTable2CreationTool_desc, types);

		entry.setId("Viewed Table");
		entry.setSmallIcon(SqlmodelDiagramEditorPlugin
				.findImageDescriptor("icons/sqlicons/viewedTable.gif")); //$NON-NLS-1$
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private static class NodeToolEntry extends ToolEntry {

		/**
		 * @generated
		 */
		private final List elementTypes;

		/**
		 * @generated
		 */
		private NodeToolEntry(String title, String description,
				List elementTypes) {
			super(title, description, null, null);
			this.elementTypes = elementTypes;
		}

		/**
		 * @generated
		 */
		public Tool createTool() {
			Tool tool = new UnspecifiedTypeCreationTool(elementTypes);
			tool.setProperties(getToolProperties());
			return tool;
		}
	}

	/**
	 * @generated
	 */
	private static class LinkToolEntry extends ToolEntry {

		/**
		 * @generated
		 */
		private final List relationshipTypes;

		/**
		 * @generated
		 */
		private LinkToolEntry(String title, String description,
				List relationshipTypes) {
			super(title, description, null, null);
			this.relationshipTypes = relationshipTypes;
		}

		/**
		 * @generated
		 */
		public Tool createTool() {
			Tool tool = new UnspecifiedTypeConnectionTool(relationshipTypes);
			tool.setProperties(getToolProperties());
			return tool;
		}
	}
}
