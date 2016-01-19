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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import es.cv.gvcase.mdt.common.commands.AbstractCommonTransactionalCommmand;
import es.cv.gvcase.mdt.common.model.ITypesFactory;
import es.cv.gvcase.mdt.common.model.TypesFactoryRegistry;
import es.cv.gvcase.mdt.common.model.TypesGroup;
import es.cv.gvcase.mdt.common.part.INewModelDiagramWizard;
import es.cv.gvcase.mdt.common.part.SelectTemplateWizardPage;

/**
 * @generated
 */
public class SqlmodelCreationWizard extends Wizard implements
		INewModelDiagramWizard {

	/**
	 * @generated
	 */
	private IWorkbench workbench;

	/**
	 * @generated
	 */
	protected IStructuredSelection selection;

	/**
	 * @generated
	 */
	protected SqlmodelCreationWizardPage diagramModelFilePage;

	/**
	 * @generated
	 */
	protected SqlmodelCreationWizardPage domainModelFilePage;

	/**
	 * @generated
	 */
	private SelectTemplateWizardPage selectTemplateWizardPage;

	/**
	 * @generated
	 */
	protected Resource diagram;

	/**
	 * @generated
	 */
	private boolean openNewlyCreatedDiagramEditor = true;

	/**
	 * @generated
	 */
	public IWorkbench getWorkbench() {
		return workbench;
	}

	/**
	 * @generated
	 */
	public IStructuredSelection getSelection() {
		return selection;
	}

	/**
	 * @generated
	 */
	public final Resource getDiagram() {
		return diagram;
	}

	/**
	 * @generated
	 */
	public final boolean isOpenNewlyCreatedDiagramEditor() {
		return openNewlyCreatedDiagramEditor;
	}

	/**
	 * @generated
	 */
	public void setOpenNewlyCreatedDiagramEditor(
			boolean openNewlyCreatedDiagramEditor) {
		this.openNewlyCreatedDiagramEditor = openNewlyCreatedDiagramEditor;
	}

	/**
	 * @generated
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		setWindowTitle(Messages.SqlmodelCreationWizardTitle);
		setDefaultPageImageDescriptor(SqlmodelDiagramEditorPlugin
				.getBundledImageDescriptor("icons/wizban/NewSQLSchemaWizard.gif")); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}

	/**
	 * @generated
	 */
	public void addPages() {
		diagramModelFilePage = new SqlmodelCreationWizardPage(
				"DiagramModelFile", getSelection(), "sqlschema_diagram"); //$NON-NLS-1$ //$NON-NLS-2$
		diagramModelFilePage
				.setTitle(Messages.SqlmodelCreationWizard_DiagramModelFilePageTitle);
		diagramModelFilePage
				.setDescription(Messages.SqlmodelCreationWizard_DiagramModelFilePageDescription);
		

	
		domainModelFilePage = new SqlmodelCreationWizardPage(
				"DomainModelFile", getSelection(), "sqlschema"); /* { //$NON-NLS-1$ //$NON-NLS-2$

			public void setVisible(boolean visible) {
				if (visible) {
					String fileName = diagramModelFilePage.getFileName();
					fileName = fileName.substring(0, fileName.length()
							- ".sqlschema_diagram".length()); //$NON-NLS-1$
					setFileName(SqlmodelDiagramEditorUtil.getUniqueFileName(
							getContainerFullPath(), fileName, "sqlschema")); //$NON-NLS-1$
				}
				super.setVisible(visible);
			}
		};*/
		domainModelFilePage
				.setTitle(Messages.SqlmodelCreationWizard_DomainModelFilePageTitle);
		domainModelFilePage
				.setDescription(Messages.SqlmodelCreationWizard_DomainModelFilePageDescription);

		
		selectTemplateWizardPage = new SelectTemplateWizardPage(
				SqlmodelDiagramEditor.ID, domainModelFilePage, diagramModelFilePage);
		
		addPage(diagramModelFilePage);
		addPage(domainModelFilePage);
		addPage(selectTemplateWizardPage);

	}

	/**
	 * @generated
	 */
	public boolean performFinish() {
		IRunnableWithProgress op = new WorkspaceModifyOperation(null) {

			protected void execute(IProgressMonitor monitor)
					throws CoreException, InterruptedException {
				String templatePath = selectTemplateWizardPage
						.getTemplatePath();

				Resource modelResource = null;

				if (templatePath != null) { // Copy the template file into the domain file
					String pluginId = selectTemplateWizardPage
							.getTemplatePluginId();

					modelResource = SqlmodelDiagramEditorUtil
							.initializeFromTemplate(domainModelFilePage
									.getURI(), pluginId, templatePath);
				} else {
					modelResource = SqlmodelDiagramEditorUtil.createModel(
							domainModelFilePage.getURI(), monitor);
				}

				// create the selected primitive types hierarchy
				createAbstractBasicTypes(modelResource);

				diagram = SqlmodelDiagramEditorUtil.createDiagram(
						diagramModelFilePage.getURI(), modelResource
								.getContents().get(0), monitor);

				if (isOpenNewlyCreatedDiagramEditor() && diagram != null) {
					try {
						SqlmodelDiagramEditorUtil.openDiagram(diagram);
					} catch (PartInitException e) {
						ErrorDialog.openError(getContainer().getShell(),
								Messages.SqlmodelCreationWizardOpenEditorError,
								null, e.getStatus());
					}
				}
			}
		};
		try {
			getContainer().run(false, true, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof CoreException) {
				ErrorDialog.openError(getContainer().getShell(),
						Messages.SqlmodelCreationWizardCreationError, null,
						((CoreException) e.getTargetException()).getStatus());
			} else {
				SqlmodelDiagramEditorPlugin.getInstance().logError(
						"Error creating diagram", e.getTargetException()); //$NON-NLS-1$
			}
			return false;
		}
		return diagram != null;
	}

	/**
	 * @generated
	 */
	private EObject modelRootElement = null;

	/**
	 * @generated
	 */
	private Collection<EObject> selectedElementsForDiagram = null;

	/**
	 * @generated
	 */
	private String diagramKind = null;

	/**
	 * @generated
	 */
	private PreferencesHint preferencesHint = null;

	/**
	 * @generated
	 */
	private Collection<TypesGroup> selectedBasicTypes = null;

	/**
	 * @generated
	 */
	public EObject getRootModelElement() {
		return this.modelRootElement;
	}

	/**
	 * @generated
	 */
	public Collection<EObject> getSelectedElementForDiagram() {
		return this.selectedElementsForDiagram;
	}

	/**
	 * @generated
	 */
	public void setRootModelElement(EObject rootElement) {
		this.modelRootElement = rootElement;
	}

	/**
	 * @generated
	 */
	public void setSelectedElementForDiagram(
			Collection<EObject> selectedEObjects) {
		this.selectedElementsForDiagram = selectedEObjects;
	}

	/**
	 * @generated
	 */
	public String getDiagramKind() {
		return this.diagramKind;
	}

	/**
	 * @generated
	 */
	public PreferencesHint getPreferencesHint() {
		return this.preferencesHint;
	}

	/**
	 * @generated
	 */
	public void setDiagramKind(String diagramKind) {
		this.diagramKind = diagramKind;
	}

	/**
	 * @generated
	 */
	public void setPreferencesHint(PreferencesHint preferencesHint) {
		this.preferencesHint = preferencesHint;
	}

	/**
	 * @generated
	 */
	public Collection<TypesGroup> getSelectedBasicTypes() {
		return this.selectedBasicTypes;
	}

	/**
	 * @generated
	 */
	public void setSelectedBasicTypes(Collection<TypesGroup> basicTypes) {
		this.selectedBasicTypes = basicTypes;
	}

	/**
	 * @generated NOT
	 */
	protected void createAbstractBasicTypes(final Resource modelResource) {
		if (modelResource == null || modelResource.getContents().isEmpty()) {
			return;
		}
		final EObject modelRoot = modelResource.getContents().get(0);
		TransactionalEditingDomain domain = TransactionUtil
				.getEditingDomain(modelRoot);
		if (domain != null) {
			AbstractCommonTransactionalCommmand command = new AbstractCommonTransactionalCommmand(
					domain, "Create Abstract Types", null) {
				@Override
				protected CommandResult doExecuteWithResult(
						IProgressMonitor monitor, IAdaptable info)
						throws ExecutionException {
					createAbstractDataTypesInModel(modelRoot);
					// save the resource
					try {
						modelResource.save(SqlmodelDiagramEditorUtil
								.getSaveOptions());
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					return null;
				}
			};
			command.executeInTransaction();
		} else {
			createAbstractDataTypesInModel(modelRoot);
		}
	}

	/**
	 * @generated
	 */
	protected void createAbstractDataTypesInModel(EObject modelRoot) {
		if (modelRoot == null) {
			return;
		}
		String editorID = selectTemplateWizardPage.getEditorId();
		ITypesFactory typesFactory = TypesFactoryRegistry.getInstance()
				.getITypesFactoryForEditor(editorID);
		if (typesFactory != null) {
			Collection<TypesGroup> typesGroups = getSelectedBasicTypes();
			if (typesGroups == null || typesGroups.isEmpty()) {
				return;
			}
			// get selected group types and create them
			for (TypesGroup group : typesGroups) {
				typesFactory.createTypeGroupInModel(group, modelRoot);
			}
		}
	}
}
