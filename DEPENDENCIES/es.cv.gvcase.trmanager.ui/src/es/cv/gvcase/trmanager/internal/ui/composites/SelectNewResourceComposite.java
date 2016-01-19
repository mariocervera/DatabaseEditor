/*******************************************************************************
 * Copyright (c) 2008 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Javier Mu�oz (Integranova) � Initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager.internal.ui.composites;

import java.io.File;
import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import es.cv.gvcase.trmanager.internal.ui.Messages;
import es.cv.gvcase.trmanager.registry.TransformedRes;

public abstract class  SelectNewResourceComposite extends SelectTransfResourceComposite {

	private Text folderValueText;
	private Label folderSelectionLabel;
	private Button browseButton;
	private Text fileNameText;
	private ControlDecoration folderDecoration;
	private ControlDecoration fileDecoration;
	
	public SelectNewResourceComposite(Composite parent, int style,
			TransformedRes resource) {
		super(parent, style, resource);
	}
	
	protected Group createGroupField(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.verticalAlignment = SWT.CENTER;// GridData.CENTER;
		data.grabExcessVerticalSpace = false;
		group.setLayoutData(data);
		return group;
	}
	
	protected void createControls(Composite parent, TransformedRes resource){
		GridLayout layout = new GridLayout(1,false);
		this.setLayout(layout);
		
		Group group = createGroupField(parent);
		group.setText(resource.getName());
		GridLayout groupLayout = new GridLayout(3,false);
		groupLayout.horizontalSpacing = 10;
		group.setLayout(groupLayout);
		
		folderSelectionLabel = new Label(group, SWT.LEFT);
		folderSelectionLabel.setText(Messages.getString("SelectNewResourceComposite.Folder")+" :"); //$NON-NLS-1$
		
		folderValueText = createTextField(group);
		folderValueText.setData(resource);
		
		folderDecoration = new ControlDecoration(folderValueText,SWT.LEFT);
		folderDecoration.setMarginWidth(3);
		
		browseButton = createButton(group);
		browseButton.setText(Messages.getString("SelectNewResourceComposite.SelectFolder")+"..."); //$NON-NLS-1$
		
		browseButton.addSelectionListener( getSelectionListener(browseButton,folderValueText,resource));
		
		Label fileNameLabel = new Label(group, SWT.LEFT);
		fileNameLabel.setText(Messages.getString("SelectNewResourceComposite.FileName")+" :"); //$NON-NLS-1$
		
		fileNameText = new Text(group, SWT.SINGLE | SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.verticalAlignment = SWT.CENTER;// GridData.CENTER;
		data.grabExcessVerticalSpace = false;
		data.grabExcessHorizontalSpace = true;
		data.horizontalSpan = 2;
		fileNameText.setLayoutData(data);
		
		fileNameText.setData(resource);
		
		fileDecoration = new ControlDecoration(fileNameText,SWT.LEFT);
		fileDecoration.setMarginWidth(3);
		
		updateFieldDecorators();
	
	}
	
	@Override
	public void addModifyListener(ModifyListener listener) {
		super.addModifyListener(listener);
		if (this.getModifyListener() != null){
			folderValueText.addModifyListener(this.getModifyListener());
			fileNameText.addModifyListener(this.getModifyListener());
		}
	}
	
	private boolean isContainer(String resourceString){
		URI resourceURI = URI.create(resourceString);
		
		if (resourceURI.getScheme().equals("platform")){
			IResource r = ResourcesPlugin.getWorkspace().getRoot().findMember(
					resourceString.replaceFirst("platform:/resource", ""));
			return r instanceof IContainer;
		} else if (resourceURI.getScheme().equals("file")) {
			return true;
		}
		return false;
	}
	
	@Override
	protected void setResourceSelected(String resourcePath) {		
		
		if (isContainer(resourcePath)){ // is a container (Project o Folder){
			this.folderValueText.setText(resourcePath);
		} else {
			IPath path = new Path (resourcePath);
			this.fileNameText.setText(path.lastSegment());
			this.folderValueText.setText(path.removeLastSegments(1).toString());
		}
	}

	@Override
	public String getResourceSelected() {
		if (!folderValueText.getText().equals("") &&
				!fileNameText.getText().equals("")){
			return folderValueText.getText().concat("/").concat(fileNameText.getText());
		}
		return "";
	}

	protected abstract SelectionListener getSelectionListener(Button button,  Text valueText, TransformedRes resource);

	
	@Override
	public void updateFieldDecorators() {
		// folderText decoration
		this.folderDecoration.hide();
		String folderPathString = folderValueText.getText();
		String decorationFolderDescription = "";
		Image decorationFolderImage = null;
		if (folderPathString.equals("")){
			decorationFolderDescription = "Folder path can not be empty";
			decorationFolderImage = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
			this.folderDecoration.show();
		} else {			
			if (folderValueText.getText().startsWith("platform:")){
				IPath myPath = new Path(folderValueText.getText().replaceAll("platform:/resource/", ""));
				myPath.addTrailingSeparator();				
				try {
					if (myPath.segmentCount() == 1){
						myPath.lastSegment();
						IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(myPath.lastSegment());
						if (!project.isAccessible()) {
							decorationFolderDescription = "Unable to read the folder";
							decorationFolderImage = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
							this.folderDecoration.show();					
						}
					} else {
						
						IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(myPath);
						if (!folder.isAccessible()) {
							decorationFolderDescription = "Unable to read the folder";
							decorationFolderImage = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
							this.folderDecoration.show();					
						}
					}
				} catch (IllegalArgumentException e) {
					decorationFolderDescription = "Unable to read the folder";
					decorationFolderImage = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
					this.folderDecoration.show();
				} 
			} else {
				if (folderValueText.getText().startsWith("file:")){
					File file = new File(folderValueText.getText().replaceAll("file:/", ""));
					if (!Platform.getOS().equals(Platform.OS_WIN32)) {
						file = new File(folderValueText.getText().replace("file:", ""));
					}
					if (!file.exists() || !file.isDirectory()) {
						decorationFolderDescription = "Unable to read the folder";
						decorationFolderImage = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
						this.folderDecoration.show();
					}
				}
			}
		}
		
		this.folderDecoration.setDescriptionText(decorationFolderDescription);
		this.folderDecoration.setImage(decorationFolderImage);
		
		// fileText decoration	
		this.fileDecoration.hide();
		String fileNameString = this.fileNameText.getText();
		String decorationFileDescription = "";
		Image decorationFileImage = null;
		if (fileNameString.equals("")){
			decorationFileDescription = "File name can not be empty";
			decorationFileImage = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
			this.fileDecoration.show();
		} 
		
		this.fileDecoration.setDescriptionText(decorationFileDescription);
		this.fileDecoration.setImage(decorationFileImage);
		
	}	


}
