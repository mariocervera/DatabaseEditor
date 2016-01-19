/*******************************************************************************
 * Copyright (c) 2007 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Javier Muï¿½oz (Integranova) ï¿½ Initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager.internal.ui.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import es.cv.gvcase.trmanager.Transformation;
import es.cv.gvcase.trmanager.TransformedResource;
import es.cv.gvcase.trmanager.internal.ui.Messages;
import es.cv.gvcase.trmanager.internal.ui.composites.SelectFolderComposite;
import es.cv.gvcase.trmanager.internal.ui.composites.SelectInputFileComposite;
import es.cv.gvcase.trmanager.internal.ui.composites.SelectInputModelComposite;
import es.cv.gvcase.trmanager.internal.ui.composites.SelectOutputFileComposite;
import es.cv.gvcase.trmanager.internal.ui.composites.SelectProjectComposite;
import es.cv.gvcase.trmanager.internal.ui.composites.SelectTransfResourceComposite;
import es.cv.gvcase.trmanager.registry.TransformationDesc;
import es.cv.gvcase.trmanager.registry.TransformedRes;
import es.cv.gvcase.trmanager.ui.TransformationUIManager;

public class RequestTransformationParametersPage extends WizardPage {

	private TransformationDesc transformation;

	private List<SelectTransfResourceComposite> inputParamsWidgets = new ArrayList<SelectTransfResourceComposite>();
	private List<SelectTransfResourceComposite> outputParamsWidgets = new ArrayList<SelectTransfResourceComposite>();

	private HashMap<String, TransformedResource> initialInputParams = null;
	private HashMap<String, TransformedResource> initialOutputParams = null;

	private IResource sourceResource = null;

	private Button buttonSynchronize;
	private boolean firstSynchronizeValue = true;

	public RequestTransformationParametersPage(String pageName,
			TransformationDesc transfDesc) {
		super(pageName);
		constructorCommonActions(pageName, transfDesc);
	}

	public RequestTransformationParametersPage(String pageName,
			TransformationDesc transfDesc, IResource sourceResource) {
		super(pageName);
		constructorCommonActions(pageName, transfDesc);
		this.sourceResource = sourceResource;
		initParams(sourceResource);

	}

	private void initParams(IResource sourceResource) {

		if (sourceResource == null) {
			// Case 1: no input resource available
			sourceResource = lastSourceResource();
			if (sourceResource == null) {
				return;
			}
			this.sourceResource = sourceResource;
		}

		if (!hasPreviousParameters(sourceResource)) {
			// Case 2: first time the resource is used in the transformation
			this.initialInputParams = initialInputResourcesFromIResource(
					sourceResource, this.transformation);
			this.initialOutputParams = initialOutputResourcesFromIResource(
					sourceResource, this.transformation);

		} else {
			// Case 3: previous parameters are recorded in the resource
			reloadSavedParameters();
		}
	}

	private IResource lastSourceResource() {
		IResource lastResource = null;
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		try {
			String baseQualifier = TransformationUIManager.TRANSFORMATION_INFO
					+ "/" + this.transformation.getTrId() + "/";
			String lastResourceQualifier = baseQualifier
					+ TransformationUIManager.LAST_RESOURCE;
			QualifiedName qname = new QualifiedName(lastResourceQualifier,
					TransformationUIManager.LAST_RESOURCE);
			String lastResourcePath = root.getPersistentProperty(qname);
			if (lastResourcePath != null) {
				if (lastResourcePath.startsWith("platform:")) {
					lastResource = root.findMember(lastResourcePath.replace(
							"platform:/", ""));
				} else {
					lastResource = root.findMember(lastResourcePath);
				}
			}

		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lastResource;
	}

	private void reloadSavedParameters() {
		this.initialInputParams = new HashMap<String, TransformedResource>();
		this.initialOutputParams = new HashMap<String, TransformedResource>();
		try {
			for (Object obj : this.sourceResource.getPersistentProperties()
					.keySet()) {
				if (obj instanceof QualifiedName) {
					QualifiedName qname = (QualifiedName) obj;
					String[] qualifierInfo = qname.getQualifier().split("/");
					if (qualifierInfo[0]
							.equals(TransformationUIManager.TRANSFORMATION_INFO)
							&& qualifierInfo[1].equals(this.transformation
									.getTrId())) {
						String paramName = qname.getLocalName();
						TransformedResource trResource = new TransformedResource(
								paramName, this.sourceResource
										.getPersistentProperty(qname));
						if (qualifierInfo[2]
								.equals(TransformationUIManager.PARAMETER_INPUT)) {
							this.initialInputParams.put(paramName, trResource);
						} else if (qualifierInfo[2]
								.equals(TransformationUIManager.PARAMETER_OUTPUT)) {
							this.initialOutputParams.put(paramName, trResource);
						} else if (qualifierInfo[2]
								.equals(TransformationUIManager.OVERRIDE_OUTPUT)) {
							if (trResource.getPath().equals("false")) {
								firstSynchronizeValue = false;
							} else {
								firstSynchronizeValue = true;
							}
						}
					}
				}
			}
		} catch (CoreException e) {
		}
	}

	private boolean hasPreviousParameters(IResource resource) {

		if (resource == null) {
			return false;
		}

		boolean result = false;

		try {
			for (Object obj : resource.getPersistentProperties().keySet()) {
				if (obj instanceof QualifiedName) {
					QualifiedName qname = (QualifiedName) obj;
					String[] qualifierInfo = qname.getQualifier().split("/");
					if (qualifierInfo[0]
							.equals(TransformationUIManager.TRANSFORMATION_INFO)
							&& qualifierInfo[1].equals(this.transformation
									.getTrId())) {
						result = true;
						break;
					}
				}
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public void rememberSelectedParameters() {

		if (this.sourceResource == null) {
			return;
		}

		String baseQualifier = TransformationUIManager.TRANSFORMATION_INFO
				+ "/" + this.transformation.getTrId() + "/";

		// INPUT PARAMETERS
		String baseInputQualifier = baseQualifier
				+ TransformationUIManager.PARAMETER_INPUT;
		try {
			for (TransformedResource resource : this.getInputParams().values()) {
				QualifiedName qname = new QualifiedName(baseInputQualifier,
						resource.getName());
				if ((this.sourceResource != null) && (resource != null)) {
					this.sourceResource.setPersistentProperty(qname, resource
							.getPath());
				}
			}
		} catch (CoreException e) {
		}

		// OUTPUT PARAMETERS
		String baseOutputQualifier = baseQualifier
				+ TransformationUIManager.PARAMETER_OUTPUT + "/";
		try {
			for (TransformedResource resource : this.getOutputParams().values()) {
				QualifiedName qname = new QualifiedName(baseOutputQualifier,
						resource.getName());
				this.sourceResource.setPersistentProperty(qname, resource
						.getPath());
			}
		} catch (CoreException e) {
		}

		// OVERRIDE OUTPUT
		String baseOverrideQualifier = baseQualifier
				+ TransformationUIManager.OVERRIDE_OUTPUT + "/";
		try {
			QualifiedName qname = new QualifiedName(baseOverrideQualifier,
					"OverrideOutput");
			this.sourceResource.setPersistentProperty(qname, buttonSynchronize
					.getSelection() ? "true" : "false");
		} catch (CoreException e) {
		}

		if (this.sourceResource != null) {
			// Set the current resource as the last source resource used in this
			// kind of transformation.
			// It will be used if not source resource is provided.
			IResource root = ResourcesPlugin.getWorkspace().getRoot();
			String lastResourceQualifier = baseQualifier
					+ TransformationUIManager.LAST_RESOURCE;
			QualifiedName qname = new QualifiedName(lastResourceQualifier,
					TransformationUIManager.LAST_RESOURCE);
			try {
				root.setPersistentProperty(qname, this.sourceResource
						.getFullPath().toString());
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private HashMap<String, TransformedResource> initialOutputResourcesFromIResource(
			IResource sourceResource, TransformationDesc transformation) {
		HashMap<String, TransformedResource> outputParams = new HashMap<String, TransformedResource>();

		for (TransformedRes res : transformation.getOutputResources()) {
			if (res.getType().equals(TransformedRes.PROJECT)) {
				outputParams.put(res.getName(), new TransformedResource(res
						.getName(), sourceResource.getProject().getFullPath()
						.toString()));
			} else {
				outputParams.put(res.getName(), new TransformedResource(res
						.getName(), sourceResource.getParent().getFullPath()
						.toString()));
			}
		}

		return outputParams;
	}

	private HashMap<String, TransformedResource> initialInputResourcesFromIResource(
			IResource selectedResource, TransformationDesc transf) {
		HashMap<String, TransformedResource> inputParams = new HashMap<String, TransformedResource>();

		boolean isFile = selectedResource instanceof IFile;
		boolean isFolder = selectedResource instanceof IFolder;
		boolean isProject = selectedResource instanceof IProject;

		for (TransformedRes res : transf.getInputResources()) {
			if (isFile
					&& (res.getType().equals(TransformedRes.FILE) || res
							.getType().equals(TransformedRes.MODEL))
					|| isFolder && res.getType().equals(TransformedRes.FOLDER)
					|| isProject
					&& res.getType().equals(TransformedRes.PROJECT)) {
				inputParams.put(res.getName(), new TransformedResource(res
						.getName(), selectedResource.getFullPath().toString()));
				break;
			}
		}

		return inputParams;
	}

	private void constructorCommonActions(String pageName,
			TransformationDesc transfDesc) {
		this.transformation = transfDesc;
		setTitle(pageName);
		setDescription(Messages
				.getString("RequestTransformationParametersPage.EnterParametersToTransformation")); //$NON-NLS-1$
		super.setPageComplete(false);
	}

	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout compositeLayout = new GridLayout();
		composite.setLayout(compositeLayout);

		GridLayout groupLayout = new GridLayout();
		groupLayout.numColumns = 2;

		// Transformations Data Group
		Group transfDataGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		transfDataGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		transfDataGroup.setLayout(groupLayout);
		transfDataGroup
				.setText(Messages
						.getString("RequestTransformationParametersPage.TransformationData")); //$NON-NLS-1$

		Label nameLabel = new Label(transfDataGroup, SWT.LEFT);
		nameLabel.setText(Messages
				.getString("RequestTransformationParametersPage.Name") + " :"); //$NON-NLS-1$

		Label transformationNameLabel = new Label(transfDataGroup, SWT.LEFT);
		transformationNameLabel.setText(this.transformation.getName());
		transformationNameLabel.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));

		Label packageLabel = new Label(transfDataGroup, SWT.LEFT);
		packageLabel
				.setText(Messages
						.getString("RequestTransformationParametersPage.Package") + " :"); //$NON-NLS-1$

		Label transformationPackageLabel = new Label(transfDataGroup, SWT.LEFT);
		if (this.transformation.getPackage() != null)
			transformationPackageLabel
					.setText(this.transformation.getPackage());
		else
			transformationPackageLabel.setText(Messages
					.getString("RequestTransformationParametersPage.None")); //$NON-NLS-1$
		transformationPackageLabel.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));

		GridLayout parametersGroupLayout = new GridLayout();
		parametersGroupLayout.numColumns = 1;
		// Input Parameters Group
		Group inputParamsGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		inputParamsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		inputParamsGroup.setLayout(parametersGroupLayout);
		inputParamsGroup
				.setText(Messages
						.getString("RequestTransformationParametersPage.InputParameters")); //$NON-NLS-1$

		for (TransformedRes resource : this.transformation.getInputResources()) {
			this.inputParamsWidgets.add(createParamWidgets(resource,
					inputParamsGroup, true));
		}

		if (this.initialInputParams != null) {
			initializeInputParams();
		}

		// Output Parameters Group
		Group outputParamsGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		outputParamsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		outputParamsGroup.setLayout(parametersGroupLayout);
		outputParamsGroup
				.setText(Messages
						.getString("RequestTransformationParametersPage.OutputParameters")); //$NON-NLS-1$

		for (TransformedRes resource : this.transformation.getOutputResources()) {
			this.outputParamsWidgets.add(createParamWidgets(resource,
					outputParamsGroup, false));
		}

		if (this.initialOutputParams != null) {
			initializeOutputParams();
		}

		// Extra parameters Group
		Group extraParamsGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		extraParamsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		extraParamsGroup.setLayout(parametersGroupLayout);

		buttonSynchronize = new Button(extraParamsGroup, SWT.CHECK);

		Transformation transf = null;
		try {
			transf = (Transformation) this.transformation.getConfigElement()
					.createExecutableExtension("class");
		} catch (CoreException e1) {
		}

		Class c = transf.getClass();
		String text = null;
		String toolTip = null;
		boolean selection = false;
		boolean enabled = false;
		while (c != null) {
			String name = c.getName().toLowerCase();
			if (name.contains("atltransformation")) {
				text = "Synchronize output model if exists";
				toolTip = "Enabled due to the transformation is an ATL transformation.";
				selection = firstSynchronizeValue;
				enabled = true;
				break;
			} else if (name.contains("xpandtransformation")) {
				text = "Synchronize output files";
				toolTip = "Not enabled due to the transformation is an XPand transformation and will always override the output files.";
				selection = true;
				enabled = false;
				break;
			}
			c = c.getSuperclass();
		}

		// if transformation doesn't inherit from atl or xpand
		// transformation, we assume that it's a M2M transformation
		if (text == null) {
			text = "Synchronize output model if exists";
			toolTip = "Enabled due to the transformation is an ATL transformation.";
			selection = firstSynchronizeValue;
			enabled = true;
		}

		buttonSynchronize.setText(text);
		buttonSynchronize.setSelection(selection);
		buttonSynchronize.setEnabled(enabled);
		buttonSynchronize.setToolTipText(toolTip);

		setControl(composite);
	}

	private void initializeInputParams() {
		for (SelectTransfResourceComposite t : this.inputParamsWidgets) {
			TransformedRes res = (TransformedRes) t.getData();
			if (this.initialInputParams.containsKey(res.getName())) {
				String resourcePath = this.initialInputParams
						.get(res.getName()).getPath();
				if (resourcePath.startsWith("platform:/")
						|| resourcePath.startsWith("file:/")) {
					t.setInitialResourcePath(resourcePath);
				} else {

					t.setInitialResourcePath(org.eclipse.emf.common.util.URI
							.createPlatformResourceURI(resourcePath, true)
							.toString());
				}
			}
		}

	}

	private void initializeOutputParams() {
		for (SelectTransfResourceComposite t : this.outputParamsWidgets) {
			TransformedRes res = (TransformedRes) t.getData();
			if (this.initialOutputParams.containsKey(res.getName())) {
				String resourcePath = this.initialOutputParams.get(
						res.getName()).getPath();
				if (resourcePath.startsWith("platform:/")
						|| resourcePath.startsWith("file:/")) {
					t.setInitialResourcePath(resourcePath);
				} else {
					t.setInitialResourcePath(org.eclipse.emf.common.util.URI
							.createPlatformResourceURI(resourcePath, true)
							.toString());
				}
			}
		}
	}

	private SelectTransfResourceComposite createParamWidgets(
			TransformedRes resource, Composite parent, boolean isInputParam) {

		SelectTransfResourceComposite resourceSelectionComposite = null;

		if (resource.getType().equals(TransformedRes.FOLDER)) {
			resourceSelectionComposite = new SelectFolderComposite(parent,
					SWT.NONE, resource);
		} else if (resource.getType().equals(TransformedRes.PROJECT)) {
			resourceSelectionComposite = new SelectProjectComposite(parent,
					SWT.NONE, resource);
		} else if (resource.getType().equals(TransformedRes.FILE)
				&& isInputParam) {
			resourceSelectionComposite = new SelectInputFileComposite(parent,
					SWT.NONE, resource);
		} else if (resource.getType().equals(TransformedRes.MODEL)
				&& isInputParam) {
			resourceSelectionComposite = new SelectInputModelComposite(parent,
					SWT.NONE, resource) {
				@Override
				protected IResource getInputResource() {
					return sourceResource;
				}
			};
		} else if ((resource.getType().equals(TransformedRes.FILE))
				|| (resource.getType().equals(TransformedRes.MODEL))
				&& !isInputParam) {
			resourceSelectionComposite = new SelectOutputFileComposite(parent,
					SWT.NONE, resource) {
				@Override
				protected IResource getInputResource() {
					return sourceResource;
				}
			};
		}

		resourceSelectionComposite
				.addModifyListener(new TextFieldModifyListener(
						resourceSelectionComposite));
		if (resourceSelectionComposite != null) {
			resourceSelectionComposite.setLayoutData(new GridData(
					GridData.FILL_HORIZONTAL));
		}

		return resourceSelectionComposite;
	}

	private class TextFieldModifyListener implements ModifyListener {

		SelectTransfResourceComposite resourceSelectionComposite;

		public TextFieldModifyListener(
				SelectTransfResourceComposite resourceSelectionComposite) {
			this.resourceSelectionComposite = resourceSelectionComposite;
		}

		public void modifyText(ModifyEvent e) {
			updateEnablement();
			this.resourceSelectionComposite.updateFieldDecorators();
		}
	}

	private void updateEnablement() {
		//
		// TODO: Check if the page can be enabled
		if (this.isPageComplete()) {
			this.setPageComplete(true);
		} else {
			this.setPageComplete(false);
		}

	}

	public boolean isPageComplete() {
		for (SelectTransfResourceComposite t : this.inputParamsWidgets) {
			if (t == null || !t.isResourceSelected()) {
				return false;
			} else if (!((TransformedRes) t.getData()).resourceInputIsValid(t
					.getResourceSelected(), transformation.getFileExtensions())) {
				return false;
			}
		}

		for (SelectTransfResourceComposite t : this.outputParamsWidgets) {
			if (t == null || !t.isResourceSelected()) {
				return false;
			} else if (((TransformedRes) t.getData()).getType().equals(
					TransformedRes.FOLDER)) {
				if (!((TransformedRes) t.getData()).resourceIsValid(t
						.getResourceSelected())) {
					return false;
				}
			}
		}

		return true;
	}

	public boolean canFlipToNextPage() {
		return this.isPageComplete();
	}

	public HashMap<String, TransformedResource> getInputParams() {
		return this.parseParams(this.inputParamsWidgets);
	}

	public HashMap<String, TransformedResource> getOutputParams() {
		return this.parseParams(this.outputParamsWidgets);
	}

	private HashMap<String, TransformedResource> parseParams(
			List<SelectTransfResourceComposite> params) {
		// resources : Param Name, TransformedResource(name,path)
		HashMap<String, TransformedResource> resources = new HashMap<String, TransformedResource>();

		for (SelectTransfResourceComposite t : params) {
			TransformedRes res = (TransformedRes) t.getData();
			resources.put(res.getName(), new TransformedResource(res.getName(),
					t.getResourceSelected()));
		}

		return resources;
	}

	public boolean shouldSynchronizeOutputModel() {
		return buttonSynchronize.getSelection();
	}

}
