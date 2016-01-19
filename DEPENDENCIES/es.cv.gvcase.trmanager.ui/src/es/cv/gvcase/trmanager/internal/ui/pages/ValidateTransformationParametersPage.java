/*******************************************************************************
 * Copyright (c) 2007-2011 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Javier Mu�oz (Integranova) � Initial implementation
 * 				Francisco Javier Cano Muñoz (Prodevelop S.L.) - added export capabilities for validation messages.
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager.internal.ui.pages;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.dialogs.SaveAsDialog;

import es.cv.gvcase.trmanager.TransformedResource;
import es.cv.gvcase.trmanager.internal.ui.Messages;
import es.cv.gvcase.trmanager.registry.TransformationDesc;
import es.cv.gvcase.trmanager.ui.TrManagerPlugin;

public class ValidateTransformationParametersPage extends WizardPage {

	private boolean parametersValid = false;
	private List<String> validationMessages = new ArrayList<String>();
	private Table tableComposite;
	private TransformationDesc transformation;
	private Label validationResultLabel;
	private Button exportErrorsButton = null;

	public ValidateTransformationParametersPage(String pageName,
			TransformationDesc transfDesc) {
		super(pageName);
		setTitle(pageName);
		setDescription(Messages
				.getString("ValidateTransformationParametersPage.InputsParametersValidated")); //$NON-NLS-1$
		transformation = transfDesc;
		super.setPageComplete(false);
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout());

		// Validation Result Group
		Group resultGroup = new Group(composite, SWT.NONE);
		resultGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		resultGroup.setLayout(new GridLayout(2, false));
		resultGroup
				.setText(Messages
						.getString("ValidateTransformationParametersPage.ValidationResults")); //$NON-NLS-1$
		// label showing the information coming from the validation results
		validationResultLabel = new Label(resultGroup, SWT.NONE);
		validationResultLabel.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		// button to export the log of warnings and errors coming from the
		// validation result
		exportErrorsButton = new Button(resultGroup, SWT.PUSH);
		exportErrorsButton.setLayoutData(new GridData(GridData.END
				| GridData.VERTICAL_ALIGN_CENTER));
		exportErrorsButton.setText(Messages
				.getString("ValidateTransformationParametersPage.0")); //$NON-NLS-1$
		exportErrorsButton
				.addSelectionListener(getExportErrorsButtonSelectionListener());
		// 
		renderValidationResult();

		// Validation Messages Group
		Group messagesGroup = new Group(composite, SWT.NONE);
		messagesGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		messagesGroup.setLayout(new GridLayout());
		messagesGroup
				.setText(Messages
						.getString("ValidateTransformationParametersPage.ValidationsMessages")); //$NON-NLS-1$

		tableComposite = new Table(messagesGroup, SWT.BORDER);
		GridData tableGridData = new GridData(GridData.FILL_BOTH);
		tableComposite.setLayoutData(tableGridData);

		renderValidationMessages();

		setControl(composite);
	}

	public boolean canFlipToNextPage() {
		return parametersValid;
	}
	
	public  List<String> getValidationMessages() {
		return validationMessages;
	}

	private void renderValidationResult() {
		if (this.parametersValid) {
			validationResultLabel.getParent().redraw();
			validationResultLabel
					.setText(Messages
							.getString("ValidateTransformationParametersPage.InputsValid")); //$NON-NLS-1$

		} else { // parameters are not valid
			validationResultLabel
					.setText(Messages
							.getString("ValidateTransformationParametersPage.InputsNotValid")); //$NON-NLS-1$
		}
		if (exportErrorsButton != null) {
			//exportErrorsButton.setEnabled(!parametersValid);
			exportErrorsButton.setEnabled(!validationMessages.isEmpty());
		}
		
	}

	private void renderValidationMessages() {
		tableComposite.removeAll();
		for (String message : validationMessages) {
			TableItem t = new TableItem(tableComposite, SWT.BORDER | SWT.WRAP);
			t.setText(" - " + message); //$NON-NLS-1$
		}
	}

	public void performValidation() {
		validationMessages.clear();
		parametersValid = es.cv.gvcase.trmanager.registry.TransformationsRegistry
				.validateInputParams(transformation.getId(),
						getTransformationInputResources(), //$NON-NLS-1$
						validationMessages);

		refreshPageInfo();
	}

	private void refreshPageInfo() {
		renderValidationResult();
		renderValidationMessages();
	}

	protected SelectionListener exportButtonSelectionListener = null;

	protected SelectionListener getExportErrorsButtonSelectionListener() {
		if (exportButtonSelectionListener == null) {
			exportButtonSelectionListener = new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					File errorLogFile = getExportingFile();
					if (errorLogFile != null) {
						try {
							PrintStream printStream = new PrintStream(
									errorLogFile);
							exportErrorLogHeader(printStream);
							exportValidationErrors(validationMessages,
									printStream);
							try {
								ResourcesPlugin.getWorkspace().getRoot()
										.refreshLocal(IResource.DEPTH_INFINITE,
												null);
							} catch (CoreException ex) {
								// no problem
							}
						} catch (FileNotFoundException ex) {
							TrManagerPlugin
									.getInstance()
									.getLog()
									.log(
											new Status(
													IStatus.ERROR,
													TrManagerPlugin.PLUGIN_ID,
													Messages
															.getString("ValidateTransformationParametersPage.1"))); //$NON-NLS-1$
							return;
						}
					}
				}
			};
		}
		return exportButtonSelectionListener;
	}

	protected File getExportingFile() {
		String suffix = "_validation_errors.txt"; //$NON-NLS-1$ 
		String fileName = transformation.getName() + suffix;
		IPath filePath = null;
		File errorLogFile = null;
		SaveAsDialog fileSelectDialog = new SaveAsDialog(getShell());
		fileSelectDialog.setOriginalName(fileName);
		if (fileSelectDialog.open() == SaveAsDialog.OK) {
			// a file was selected, find its true path
			filePath = fileSelectDialog.getResult();
			filePath = ResourcesPlugin.getWorkspace().getRoot().getLocation()
					.append(filePath);
			errorLogFile = filePath.toFile();
		} else {
			return null;
		}
		// return the selected error export file
		return errorLogFile;
	}

	protected void exportValidationErrors(List<String> errors,
			PrintStream printStream) {
		if (errors == null || errors.size() <= 0 || printStream == null) {
			return;
		}
		int i = 1;
		for (String errorString : errors) {
			printStream.println(buildErrorStringForExport(i++, errorString));
		}
	}

	protected void exportErrorLogHeader(PrintStream printStream) {
		printStream.println(MessageFormat.format(Messages
				.getString("ValidateTransformationParametersPage.2"), //$NON-NLS-1$
				transformation.getName()));
		printStream.println("   ------------------------------------"); //$NON-NLS-1$
		printStream.println(Messages
				.getString("ValidateTransformationParametersPage.4")); //$NON-NLS-1$
		printStream.println("   ------------------------------------"); //$NON-NLS-1$
		for (TransformedResource transformedResource : getTransformationInputResources()
				.values()) {
			printStream.println("  · " + transformedResource.getPath()); //$NON-NLS-1$
		}
		printStream.println("   ------------------------------------"); //$NON-NLS-1$
		printStream.println(Messages
				.getString("ValidateTransformationParametersPage.3")); //$NON-NLS-1$
		printStream.println("   ------------------------------------"); //$NON-NLS-1$
	}

	protected String buildErrorStringForExport(int i, String message) {
		return String.format("[%1$4d] %2$s", i, message); //$NON-NLS-1$
	}

	protected HashMap<String, TransformedResource> getTransformationInputResources() {
		return ((RequestTransformationParametersPage) this
				.getWizard()
				.getPage(
						Messages
								.getString("ValidateTransformationParametersPage.EnterTransformationParameters"))) //$NON-NLS-1$
				.getInputParams();
	}

}
