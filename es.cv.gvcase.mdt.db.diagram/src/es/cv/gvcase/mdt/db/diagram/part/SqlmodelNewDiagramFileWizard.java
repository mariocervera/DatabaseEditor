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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import es.cv.gvcase.mdt.common.part.DiagramElementsSelectionPage;
import es.cv.gvcase.mdt.common.part.INewDiagramFileWizard;
import es.cv.gvcase.mdt.common.part.ModelElementSelectionPage;
import es.cv.gvcase.mdt.common.part.SelectModelElementsForDiagramDialog;
import es.cv.gvcase.mdt.common.util.MDTUtil;
import es.cv.gvcase.mdt.common.util.MultiDiagramUtil;
import es.cv.gvcase.mdt.db.diagram.edit.parts.SchemaEditPart;

/**
 * @generated
 */
public class SqlmodelNewDiagramFileWizard extends Wizard implements
		INewDiagramFileWizard {

	/**
	 * @generated
	 */
	private WizardPage diagramElementsSelectionPage;

	/**
	 * @generated
	 */
	private WizardNewFileCreationPage myFileCreationPage;

	/**
	 * @generated
	 */
	private ModelElementSelectionPage modelElementSelectionPage;

	/**
	 * @generated
	 */
	private TransactionalEditingDomain myEditingDomain;

	/**
	 * @generated
	 */
	private EObject selectedRootElement = null;

	/**
	 * @generated
	 */
	public EObject getRootModelElement() {
		return selectedRootElement;
	}

	/**
	 * @generated
	 */
	public void setRootModelElement(EObject rootElement) {
		this.selectedRootElement = rootElement;
	}

	/**
	 * @generated
	 */
	private String diagramKind = "Sqlmodel";

	/**
	 * @generated
	 */
	public String getDiagramKind() {
		return diagramKind;
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
	private PreferencesHint preferencesHint = new PreferencesHint(
			"es.cv.gvcase.mdt.db.diagram");

	/**
	 * @generated
	 */
	public PreferencesHint getPreferencesHint() {
		return this.preferencesHint;
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
	private Collection<EObject> selectedElements = null;

	/**
	 * @generated
	 */
	public Collection<EObject> getSelectedElementForDiagram() {
		return selectedElements;
	}

	/**
	 * @generated
	 */
	public void setSelectedElementForDiagram(
			Collection<EObject> selectedEObjects) {
		this.selectedElements = selectedEObjects;
	}

	/**
	 * @generated NOT
	 */
	public SqlmodelNewDiagramFileWizard(String actionId, URI domainModelURI,
			EObject diagramRoot, TransactionalEditingDomain editingDomain) {
		assert domainModelURI != null : "Domain model uri must be specified"; //$NON-NLS-1$
		assert diagramRoot != null : "Doagram root element must be specified"; //$NON-NLS-1$
		assert editingDomain != null : "Editing domain must be specified"; //$NON-NLS-1$

		myFileCreationPage = new WizardNewFileCreationPage(
				Messages.SqlmodelNewDiagramFileWizard_CreationPageName,
				StructuredSelection.EMPTY);
		myFileCreationPage
				.setTitle(Messages.SqlmodelNewDiagramFileWizard_CreationPageTitle);
		myFileCreationPage.setDescription(NLS.bind(
				Messages.SqlmodelNewDiagramFileWizard_CreationPageDescription,
				SchemaEditPart.MODEL_ID));
		IPath filePath;
		String fileName = domainModelURI.trimFileExtension().lastSegment();
		if (domainModelURI.isPlatformResource()) {
			filePath = new Path(domainModelURI.trimSegments(1)
					.toPlatformString(true));
		} else if (domainModelURI.isFile()) {
			filePath = new Path(domainModelURI.trimSegments(1).toFileString());
		} else {
			// TODO : use some default path
			throw new IllegalArgumentException(
					"Unsupported URI: " + domainModelURI); //$NON-NLS-1$
		}
		myFileCreationPage.setContainerFullPath(filePath);
		myFileCreationPage.setFileName(SqlmodelDiagramEditorUtil
				.getUniqueFileName(filePath, fileName, "sqlschema_diagram")); //$NON-NLS-1$

		modelElementSelectionPage = new ModelElementSelectionPage(
				Messages.SqlmodelNewDiagramFileWizard_RootSelectionPageName);
		modelElementSelectionPage
				.setTitle(Messages.SqlmodelNewDiagramFileWizard_RootSelectionPageTitle);
		modelElementSelectionPage
				.setDescription(Messages.SqlmodelNewDiagramFileWizard_RootSelectionPageDescription);
		modelElementSelectionPage.setModelElement(diagramRoot);

		if (actionId
				.equals("es.cv.gvcase.mdt.db.diagram.CustomizeDiagramAction")) {
			diagramElementsSelectionPage = new SqlDiagramElementsSelectionPage(
					"Select model elements for diagram", "Model objects",
					"Select model objects that must appear in the diagram");
		} else {
			// fjcano :: adding a page to select which elements are to appear in
			// the new diagram
			IBaseLabelProvider labelProvider = new AdapterFactoryLabelProvider(
					new ComposedAdapterFactory(
							ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
			IContentProvider contentProvider = new AdapterFactoryContentProvider(
					new ComposedAdapterFactory(
							ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
			diagramElementsSelectionPage = new SelectModelElementsForDiagramDialog(
					contentProvider, labelProvider);
		}

		myEditingDomain = editingDomain;
	}

	/**
	 * @generated
	 */
	public void addPages() {
		addPage(myFileCreationPage);
		addPage(modelElementSelectionPage);
		addPage(diagramElementsSelectionPage);
	}

	/**
	 * @generated NOT
	 */
	public boolean performFinish() {
		List affectedFiles = new LinkedList();
		IFile diagramFile = myFileCreationPage.createNewFile();
		SqlmodelDiagramEditorUtil.setCharset(diagramFile);
		affectedFiles.add(diagramFile);
		URI diagramModelURI = URI.createPlatformResourceURI(diagramFile
				.getFullPath().toString(), true);
		ResourceSet resourceSet = myEditingDomain.getResourceSet();
		final Resource diagramResource = resourceSet
				.createResource(diagramModelURI);
		AbstractTransactionalCommand command = new AbstractTransactionalCommand(
				myEditingDomain,
				Messages.SqlmodelNewDiagramFileWizard_InitDiagramCommand,
				affectedFiles) {

			protected CommandResult doExecuteWithResult(
					IProgressMonitor monitor, IAdaptable info)
					throws ExecutionException {
				int diagramVID = SqlmodelVisualIDRegistry
						.getDiagramVisualID(modelElementSelectionPage
								.getModelElement());
				if (diagramVID != SchemaEditPart.VISUAL_ID) {
					return CommandResult
							.newErrorCommandResult(Messages.SqlmodelNewDiagramFileWizard_IncorrectRootError);
				}
				Diagram diagram = ViewService.createDiagram(
						modelElementSelectionPage.getModelElement(),
						SchemaEditPart.MODEL_ID,
						SqlmodelDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
				diagramResource.getContents().add(diagram);
				addReferencedEObjectsToDiagram(diagram);
				associateDiagramToView(diagram, modelElementSelectionPage
						.getModelElement());
				// set the diagram version
				MDTUtil.addDiagramVersion(diagram, MDTUtil
						.getBundleVersion(SqlmodelDiagramEditorPlugin.ID));
				return CommandResult.newOKCommandResult();
			}

			private void addReferencedEObjectsToDiagram(Diagram diagram) {
				if (diagramElementsSelectionPage instanceof DiagramElementsSelectionPage) {
					for (EObject eObject : ((DiagramElementsSelectionPage) diagramElementsSelectionPage)
							.getSelectedEObjects()) {
						MultiDiagramUtil.AddEAnnotationReferenceToDiagram(
								diagram, eObject);
					}
				} else if (diagramElementsSelectionPage instanceof SelectModelElementsForDiagramDialog) {
					for (EObject eObject : ((SelectModelElementsForDiagramDialog) diagramElementsSelectionPage)
							.getSelectedEObjects()) {
						MultiDiagramUtil.AddEAnnotationReferenceToDiagram(
								diagram, eObject);
					}
				}
			}
		};
		try {
			OperationHistoryFactory.getOperationHistory().execute(command,
					new NullProgressMonitor(), null);
			diagramResource.save(SqlmodelDiagramEditorUtil.getSaveOptions());
			SqlmodelDiagramEditorUtil.openDiagram(diagramResource);
		} catch (ExecutionException e) {
			SqlmodelDiagramEditorPlugin.getInstance().logError(
					"Unable to create model and diagram", e); //$NON-NLS-1$
		} catch (IOException ex) {
			SqlmodelDiagramEditorPlugin.getInstance().logError(
					"Save operation failed for: " + diagramModelURI, ex); //$NON-NLS-1$
		} catch (PartInitException ex) {
			SqlmodelDiagramEditorPlugin.getInstance().logError(
					"Unable to open editor", ex); //$NON-NLS-1$
		}
		return true;
	}

	private static void associateDiagramToView(Diagram diagram, EObject element) {
		EAnnotation eAnnotation = diagram
				.getEAnnotation(MultiDiagramUtil.DiagramsRelatedToElement);
		if (eAnnotation == null) {
			eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
			eAnnotation.setSource(MultiDiagramUtil.DiagramsRelatedToElement);
			diagram.getEAnnotations().add(eAnnotation);
		}
		eAnnotation.getReferences().add(element);
	}
}
