/*******************************************************************************
 * Copyright (c) 2007 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Javier Mu�oz (Integranova) � Initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager.internal.ui.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.PlatformUI;

import es.cv.gvcase.trmanager.internal.ui.Messages;
import es.cv.gvcase.trmanager.internal.ui.dialogs.LoadEMFModelDialog;
import es.cv.gvcase.trmanager.internal.ui.dialogs.LoadFolderDialog;
import es.cv.gvcase.trmanager.registry.TransformationDesc;
import es.cv.gvcase.trmanager.registry.TransformationsRegistry;
import es.cv.gvcase.trmanager.ui.TransformationUIManager;
import es.cv.gvcase.trmanager.ui.wizards.TransformationExecutionWizardDialog;

public class RequestConfigurationPage extends WizardPage {

	private TransformationDesc transformation;
	private Button defaultConfigButton;
	private Button selectConfigButton;
	private Button newConfigButton;
	private Group actionsGroup;
	private ControlDecoration selectFileDecoration;
	private ControlDecoration selectFolderDecoration;
	private ControlDecoration newFileDecoration;

	private IResource sourceResource = null;

	private String configModelPath = null;

	public RequestConfigurationPage(String pageName,
			TransformationDesc transfDesc, IResource source) {
		super(pageName);
		setTitle(pageName);
		setDescription(Messages
				.getString("RequestConfigurationPage.TransformationCanBeConfigured")); //$NON-NLS-1$
		this.transformation = transfDesc;
		this.sourceResource = source;
		super.setPageComplete(false);

	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout compositeLayout = new GridLayout();
		composite.setLayout(compositeLayout);

		GridLayout groupLayout = new GridLayout();
		groupLayout.numColumns = 3;

		// Validation Result Group
		Group modeGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		modeGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		modeGroup.setLayout(groupLayout);
		modeGroup.setText(Messages
				.getString("RequestConfigurationPage.ConfigurationSelection")); //$NON-NLS-1$

		// this.validationResultLabel = new Label(modeGroup, SWT.LEFT );

		defaultConfigButton = new Button(modeGroup, SWT.RADIO);
		defaultConfigButton.setText(Messages
				.getString("RequestConfigurationPage.DefaultConfiguraiton")); //$NON-NLS-1$

		selectConfigButton = new Button(modeGroup, SWT.RADIO);
		selectConfigButton.setText(Messages
				.getString("RequestConfigurationPage.SelectConfiguration")); //$NON-NLS-1$

		newConfigButton = new Button(modeGroup, SWT.RADIO);
		newConfigButton.setText(Messages
				.getString("RequestConfigurationPage.NewConfiguration")); //$NON-NLS-1$

		if (!transformation.hasDefaultConfig()) {
			defaultConfigButton.setEnabled(false);
			selectConfigButton.setSelection(true);
		} else {
			defaultConfigButton.setSelection(true);
		}

		actionsGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		actionsGroup.setLayoutData(new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL));
		actionsGroup.setLayout(groupLayout);
		actionsGroup.setText(Messages
				.getString("RequestConfigurationPage.ConfigurationActions")); //$NON-NLS-1$

		initConfigurationPage();

		updateActionsGroup();

		hookButtonListeners();

		setControl(composite);
	}

	private void hookButtonListeners() {
		this.selectConfigButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (selectConfigButton.getSelection()) {
					updateActionsGroup();
				}
			}

		});

		this.defaultConfigButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (defaultConfigButton.getSelection()) {
					setConfigurationModel(null);
					updateActionsGroup();
				}
			}

		});

		this.newConfigButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (newConfigButton.getSelection()) {
					setConfigurationModel(null);
					updateActionsGroup();
				}
			}

		});
	}

	private void updateActionsGroup() {
		// dispose decorations
		disposeDecorations();
		// dispose the rest of widgets
		for (int i = 0; i < actionsGroup.getChildren().length; i++) {
			actionsGroup.getChildren()[i].dispose();
		}

		Composite actionComposite = new Composite(actionsGroup, SWT.FILL);
		GridData actionCompositeData = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL);
		actionComposite.setLayoutData(actionCompositeData);

		GridLayout actionCompositeLayout = new GridLayout();
		actionCompositeLayout.numColumns = 3;
		actionCompositeLayout.makeColumnsEqualWidth = false;
		actionCompositeLayout.horizontalSpacing = 10;
		actionComposite.setLayout(actionCompositeLayout);

		if (selectConfigButton.getSelection()) {

			Label selectModelLabel = new Label(actionComposite, SWT.NONE);
			selectModelLabel
					.setText(Messages
							.getString("RequestConfigurationPage.ConfigurationFile") + ":"); //$NON-NLS-1$
			selectModelLabel.setLayoutData(new GridData());

			final Text selectModelText = new Text(actionComposite, SWT.SINGLE
					| SWT.BORDER);
			selectModelText.setEditable(false);
			GridData data = new GridData(GridData.FILL_HORIZONTAL
					| GridData.GRAB_HORIZONTAL);
			data.widthHint = IDialogConstants.ENTRY_FIELD_WIDTH;
			selectModelText.setLayoutData(data);
			if (getConfigurationModel() != null) {
				selectModelText.setText(getConfigurationModel());
			}

			// Decoration to validate the configuration model resource
			selectFileDecoration = new ControlDecoration(selectModelText,
					SWT.LEFT);
			selectFileDecoration.setMarginWidth(3);
			updateFieldDecorators(selectModelText);

			// Listener to validate the configuration model resource
			selectModelText.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					updateFieldDecorators(selectModelText);
				}
			});

			Button selectModelButton = new Button(actionComposite, SWT.PUSH);
			selectModelButton.setText(Messages
					.getString("RequestConfigurationPage.Select") + "..."); //$NON-NLS-1$
			selectModelButton.setLayoutData(new GridData(
					GridData.HORIZONTAL_ALIGN_END));

			selectModelButton.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {

					LoadEMFModelDialog dialog = new LoadEMFModelDialog(
							getShell(),
							"http:///es.cv.gvcase.transformationConfiguration.ecore",
							sourceResource);

					dialog.open();
					String configModel = dialog.getURIText() == null ? ""
							: dialog.getURIText();
					setConfigurationModel(configModel);
					selectModelText.setText(configModel);
					setPageComplete(canFlipToNextPage());
				}
			});

			Button editConfigButton = new Button(actionComposite, SWT.PUSH);
			editConfigButton.setText(Messages
					.getString("RequestConfigurationPage.EditConfiguration")); //$NON-NLS-1$
			GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
			gd.horizontalSpan = 3;
			editConfigButton.setLayoutData(gd);

			editConfigButton.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					if (getConfigurationModel() != null) {
						// open transformation configuration editor
						editConfiguration();
					}
				}
			});

		} else if (newConfigButton.getSelection()) {

			Label folderSelectionLabel = new Label(actionComposite, SWT.LEFT);
			folderSelectionLabel.setText(Messages
					.getString("RequestConfigurationPage.Folder") + " :"); //$NON-NLS-1$

			final Text folderValueText = new Text(actionComposite, SWT.SINGLE
					| SWT.BORDER);
			GridData folderValueTextData = new GridData(
					GridData.FILL_HORIZONTAL);
			folderValueTextData.verticalAlignment = SWT.CENTER;// GridData.CENTER;
			folderValueTextData.grabExcessVerticalSpace = false;
			folderValueTextData.grabExcessHorizontalSpace = true;
			folderValueText.setLayoutData(folderValueTextData);
			folderValueText.setEditable(false);

			// Decoration to validate the configuration folder
			selectFolderDecoration = new ControlDecoration(folderValueText,
					SWT.LEFT);
			selectFolderDecoration.setMarginWidth(3);
			updateFieldDecorators(folderValueText);

			Button browseButton = new Button(actionComposite, SWT.PUSH);
			GridData browseButtonData = new GridData();
			browseButtonData.horizontalAlignment = SWT.RIGHT;
			browseButton.setLayoutData(browseButtonData);
			browseButton
					.setText(Messages
							.getString("RequestConfigurationPage.SelectFolder") + "..."); //$NON-NLS-1$

			Label fileNameLabel = new Label(actionComposite, SWT.LEFT);
			fileNameLabel.setText(Messages
					.getString("RequestConfigurationPage.FileName") + " :"); //$NON-NLS-1$

			final Text fileNameText = new Text(actionComposite, SWT.SINGLE
					| SWT.BORDER);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.verticalAlignment = SWT.CENTER;// GridData.CENTER;
			data.grabExcessVerticalSpace = false;
			data.grabExcessHorizontalSpace = true;
			data.horizontalSpan = 2;
			fileNameText.setLayoutData(data);

			// Decoration to validate the configuration file name
			newFileDecoration = new ControlDecoration(fileNameText, SWT.LEFT);
			newFileDecoration.setMarginWidth(3);
			updateFieldDecorators(fileNameText);

			fileNameText.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					String folder = folderValueText.getText();
					String fileName = fileNameText.getText();

					if ("".equals(folder) || "".equals(fileName)) {
						setConfigurationModel(null);
					} else {
						setConfigurationModel(folder + "/" + fileName);
					}
					updateFieldDecorators(fileNameText);
					setPageComplete(canFlipToNextPage());
				}

			});

			browseButton.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					LoadFolderDialog dialog = new LoadFolderDialog(getShell(),
							sourceResource, false);
					dialog.open();
					String folder = dialog.getURIText() == null ? "" : dialog
							.getURIText();
					folderValueText.setText(folder);
					String fileName = fileNameText.getText();

					if ("".equals(folder) || "".equals(fileName)) {
						setConfigurationModel(null);
					} else {
						setConfigurationModel(folder + "/" + fileName);
					}
					updateFieldDecorators(folderValueText);
					setPageComplete(canFlipToNextPage());
				}

			});

		}

		actionsGroup.layout(true);
		Composite c = actionsGroup;
		while (c.getParent() != null) {
			// this redraw is necessary because decoration is shown twice in the
			// page
			c.getParent().redraw();
			c = c.getParent();
		}
		setPageComplete(canFlipToNextPage());
	}

	private void initConfigurationPage() {
		if (hasPreviousConfiguration(this.sourceResource)) {
			String baseQualifier = TransformationUIManager.TRANSFORMATION_INFO
					+ "/" + this.transformation.getId() + "/";
			String baseConfigQualifier = baseQualifier
					+ TransformationUIManager.CONFIG_MODEL;
			try {

				QualifiedName qname = new QualifiedName(baseConfigQualifier,
						TransformationUIManager.CONFIG_MODEL);
				this.setConfigurationModel(this.sourceResource
						.getPersistentProperty(qname));
				this.selectConfigButton.setSelection(true);
				this.defaultConfigButton.setSelection(false);

			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private boolean hasPreviousConfiguration(IResource resource) {
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
									.getId())
							&& qualifierInfo[2]
									.equals(TransformationUIManager.CONFIG_MODEL)) {
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

	public void rememberSelectedConfiguration() {
		if (this.sourceResource == null) {
			return;
		}

		String baseQualifier = TransformationUIManager.TRANSFORMATION_INFO
				+ "/" + this.transformation.getId() + "/";
		String baseConfigQualifier = baseQualifier
				+ TransformationUIManager.CONFIG_MODEL;
		try {

			QualifiedName qname = new QualifiedName(baseConfigQualifier,
					TransformationUIManager.CONFIG_MODEL);
			this.sourceResource.setPersistentProperty(qname, this
					.getConfigurationModel());

		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setConfigurationModel(String configModel) {
		this.configModelPath = configModel;
	}

	public String getConfigurationModel() {
		String configurationModel = this.configModelPath;
		if (configurationModel != null
				&& !configurationModel.endsWith(".transformationconfiguration")) {
			configurationModel += ".transformationconfiguration";
		}
		return configurationModel;
	}

	public boolean canFlipToNextPage() {

		if (defaultConfigButton.getSelection()) {
			return true;
		} else if (selectConfigButton.getSelection()) {
			if (this.getConfigurationModel() == null
					|| !resourceIsValid(this.getConfigurationModel(),
							new ArrayList<String>())) {
				return false;
			}
		}
		return this.getConfigurationModel() != null;
	}

	public boolean isDefaultConfiguration() {
		return this.defaultConfigButton.getSelection();
	}

	public boolean isSelectedConfiguration() {
		return this.selectConfigButton.getSelection();
	}

	public boolean isNewConfiguration() {
		return this.newConfigButton.getSelection();
	}

	public boolean createNewConfiguration(List<String> messagesList) {
		return TransformationsRegistry
				.generateConfiguration(
						this.transformation.getId(),
						((RequestTransformationParametersPage) this
								.getWizard()
								.getPage(
										Messages
												.getString("RequestConfigurationPage.EnterTransformationParametersPageName"))).getInputParams(), //$NON-NLS-1$
						this.getConfigurationModel(), messagesList);

	}

	private void updateFieldDecorators(Text text) {
		String textValue = text.getText();
		String description = "";
		Image image = null;
		ControlDecoration decoration = null;

		if (selectFileDecoration != null
				&& text.equals(selectFileDecoration.getControl())) {

			selectFileDecoration.hide();
			decoration = selectFileDecoration;

			if ("".equals(textValue)) {
				description = "Resource path can not be empty";
				image = FieldDecorationRegistry.getDefault()
						.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR)
						.getImage();
				selectFileDecoration.show();
			} else {
				Collection<String> messages = new ArrayList<String>();
				boolean isValid = resourceIsValid(textValue, messages);
				if (!isValid) {
					if (!messages.isEmpty()) {
						description = (String) messages.toArray()[0];
					} else {
						description = "'" + textValue
								+ "' resource is not valid";
					}
					image = FieldDecorationRegistry.getDefault()
							.getFieldDecoration(
									FieldDecorationRegistry.DEC_ERROR)
							.getImage();
					selectFileDecoration.show();
				}
			}
		}

		if (selectFolderDecoration != null
				&& text.equals(selectFolderDecoration.getControl())) {

			selectFolderDecoration.hide();
			decoration = selectFolderDecoration;

			if ("".equals(textValue)) {
				description = "Folder path can not be empty";
				image = FieldDecorationRegistry.getDefault()
						.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR)
						.getImage();
				selectFolderDecoration.show();
			} else if (textValue.startsWith("platform:/resource/")) {
				IPath myPath = new Path(textValue.replaceAll(
						"platform:/resource/", ""));
				myPath.addTrailingSeparator();
				try {
					if (myPath.segmentCount() == 1) {
						myPath.lastSegment();
						IProject project = ResourcesPlugin.getWorkspace()
								.getRoot().getProject(myPath.lastSegment());
						if (!project.isAccessible()) {
							description = "Unable to read the folder";
							image = FieldDecorationRegistry.getDefault()
									.getFieldDecoration(
											FieldDecorationRegistry.DEC_ERROR)
									.getImage();
							selectFolderDecoration.show();
						}
					} else {

						IFolder folder = ResourcesPlugin.getWorkspace()
								.getRoot().getFolder(myPath);
						if (!folder.isAccessible()) {
							description = "Unable to read the folder";
							image = FieldDecorationRegistry.getDefault()
									.getFieldDecoration(
											FieldDecorationRegistry.DEC_ERROR)
									.getImage();
							selectFolderDecoration.show();
						}
					}
				} catch (IllegalArgumentException e) {
					description = "Unable to read the folder";
					image = FieldDecorationRegistry.getDefault()
							.getFieldDecoration(
									FieldDecorationRegistry.DEC_ERROR)
							.getImage();
					selectFolderDecoration.show();
				}
			} else if (textValue.startsWith("file:")) {
				IPath myPath = new Path(textValue.replace("file:/", ""));
				if (!Platform.getOS().equals(Platform.OS_WIN32)) {
					myPath = new Path(textValue.replace("file:", ""));
				}
				if (!myPath.toFile().canRead()) {
					description = "Unable to read the folder";
					image = FieldDecorationRegistry.getDefault()
							.getFieldDecoration(
									FieldDecorationRegistry.DEC_ERROR)
							.getImage();
					selectFolderDecoration.show();
				}
			}
		}

		if (newFileDecoration != null
				&& text.equals(newFileDecoration.getControl())) {

			newFileDecoration.hide();
			decoration = newFileDecoration;

			if ("".equals(textValue)) {
				description = "File name can not be empty";
				image = FieldDecorationRegistry.getDefault()
						.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR)
						.getImage();
				newFileDecoration.show();
			} else if (selectFolderDecoration != null
					&& selectFolderDecoration.getControl() instanceof Text) {
				Text folderText = (Text) selectFolderDecoration.getControl();
				String folderTextValue = folderText.getText();
				if (fileExists(folderTextValue + "/" + textValue)) {
					description = "This file already exists";
					image = FieldDecorationRegistry.getDefault()
							.getFieldDecoration(
									FieldDecorationRegistry.DEC_WARNING)
							.getImage();
					newFileDecoration.show();
				}
			}

		}

		if (decoration != null) {
			decoration.setDescriptionText(description);
			decoration.setImage(image);
		}

	}

	private boolean resourceIsValid(String resourcePath,
			Collection<String> errorMessages) {

		// Trying to load the file as an EMF/XMI file
		ResourceSet rset = new ResourceSetImpl();
		Resource.Factory rfact = new XMIResourceFactoryImpl();
		rset.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*",
				rfact);

		Resource resource = null;

		try {
			resource = rset.getResource(URI.createURI(resourcePath), true);
		} catch (Exception e) {
			errorMessages.add("'" + resourcePath + "' does not exist");
			return false;
		}

		if (resource == null) {
			errorMessages.add("Unable to read the configuration model");
			return false;
		}

		// Check that the model conforms to the required NsURI
		// metamodel
		EObject obj = resource.getContents().get(0);

		String configNsURI = "http:///es.cv.gvcase.transformationConfiguration.ecore";
		String modelNsURI = obj.eClass().getEPackage().getNsURI();

		if (!modelNsURI.equals(configNsURI)) {
			errorMessages
					.add("Model do not conforms to the required metamodel: "
							+ configNsURI);
			return false;
		}

		return true;

	}

	private void editConfiguration() {
		IEditorDescriptor defaultEditor = PlatformUI.getWorkbench()
				.getEditorRegistry().getDefaultEditor(getConfigurationModel());
		if (defaultEditor == null) {
			return;
		}

		try {
			URI uri = URI.createURI(getConfigurationModel());
			if (uri.isPlatform()) {
				String platformStringURI = uri.isPlatform() ? uri
						.toPlatformString(false) : uri.toFileString();
				Path path = new Path(platformStringURI);
				IFile newConfigFile = ResourcesPlugin.getWorkspace().getRoot()
						.getFile(path);

				if (!newConfigFile.exists()) {
					return;
				}
				org.eclipse.ui.ide.IDE.openEditor(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage(),
						newConfigFile, PlatformUI.getWorkbench()
								.getEditorRegistry().getDefaultEditor(
										getConfigurationModel()).getId(), true);
			} else {
				java.net.URI javaURI = java.net.URI
						.create(getConfigurationModel());
				org.eclipse.ui.ide.IDE.openEditor(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage(), javaURI,
						PlatformUI.getWorkbench().getEditorRegistry()
								.getDefaultEditor(getConfigurationModel())
								.getId(), true);
			}

			if (getContainer() instanceof TransformationExecutionWizardDialog) {
				((TransformationExecutionWizardDialog) getContainer()).close();
			}

		} catch (CoreException exception) {
			exception.printStackTrace();
		}
	}

	private void disposeDecorations() {
		if (selectFileDecoration != null) {
			selectFileDecoration.dispose();
		}
		if (selectFolderDecoration != null) {
			selectFolderDecoration.dispose();
		}
		if (newFileDecoration != null) {
			newFileDecoration.dispose();
		}
	}

	protected boolean fileExists(String filePath) {
		if (!filePath.endsWith(".transformationconfiguration")) {
			filePath += ".transformationconfiguration";
		}
		if (filePath.startsWith("platform:/resource")) {
			IPath path = new Path(filePath.replace("platform:/resource", ""));
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			return file != null && file.exists();

		}
		return false;
	}

}
