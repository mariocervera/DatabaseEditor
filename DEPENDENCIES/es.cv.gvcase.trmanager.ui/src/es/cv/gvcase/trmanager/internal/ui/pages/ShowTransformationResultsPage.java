/*******************************************************************************
 * Copyright (c) 2007-2010 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Javier Mu�oz (Integranova) � Initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager.internal.ui.pages;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;
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

import es.cv.gvcase.trmanager.TransformationExtraOperations;
import es.cv.gvcase.trmanager.TransformationInformation;
import es.cv.gvcase.trmanager.TransformedResource;
import es.cv.gvcase.trmanager.internal.ui.Messages;
import es.cv.gvcase.trmanager.registry.TransformationDesc;
import es.cv.gvcase.trmanager.registry.TransformationsRegistry;
import es.cv.gvcase.trmanager.ui.TrManagerPlugin;
import es.cv.gvcase.trmanager.ui.pages.IWizardPageParametersProvider;

/**
 * A {@link WizardPage} that shows the results of s transformation executed vis
 * the transformation wizard.
 * 
 * @author jmunoz
 * 
 */
public class ShowTransformationResultsPage extends WizardPage {

	private TransformationDesc transformation;
	private boolean correctExecution = false;
	private Composite messagesComposite;
	private ArrayList<String> resultMessages = new ArrayList<String>();
	private Label resultLabel;
	private Button exportErrorsButton;

	public ShowTransformationResultsPage(String pageName,
			TransformationDesc transfDesc) {
		super(pageName);
		setTitle(pageName);
		setDescription(Messages
				.getString("ShowTransformationResultsPage.TransforamtionHasBeenExecuted")); //$NON-NLS-1$
		this.transformation = transfDesc;
		super.setPageComplete(false);

	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout());

		// Transformation Result Group
		Group resultGroup = new Group(composite, SWT.NONE);
		resultGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		resultGroup.setLayout(new GridLayout(2, false));
		resultGroup
				.setText(Messages
						.getString("ShowTransformationResultsPage.TransformationResults")); //$NON-NLS-1$

		// label showing the information coming from the validation results
		resultLabel = new Label(resultGroup, SWT.NONE);
		resultLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
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
		renderTransformationResult();

		// Transformation results Messages Group
		Group messagesGroup = new Group(composite, SWT.NONE);
		messagesGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		messagesGroup.setLayout(new GridLayout());
		messagesGroup
				.setText(Messages
						.getString("ShowTransformationResultsPage.TransformationResults2")); //$NON-NLS-1$

		final Table table = new Table(messagesGroup, SWT.BORDER);
		GridData tableGridData = new GridData(GridData.FILL_BOTH);
		table.setLayoutData(tableGridData);

		messagesComposite = table;

		renderResultsMessages();

		setControl(composite);
	}

	private void renderTransformationResult() {
		if (this.correctExecution) {
			this.resultLabel
					.setText(Messages
							.getString("ShowTransformationResultsPage.ExecutedWithoutProblems")); //$NON-NLS-1$
		} else { // problems when executing the transformation
			this.resultLabel.setText(Messages
					.getString("ShowTransformationResultsPage.SomethingWrong")); //$NON-NLS-1$
		}
		if (exportErrorsButton != null) {
			//exportErrorsButton.setEnabled(!correctExecution);
			exportErrorsButton.setEnabled(!resultMessages.isEmpty());
		}	
	}

	public boolean canFlipToNextPage() {
		Object o = getWizard().getNextPage(this);
		return o != null;
	}

	private void renderResultsMessages() {
		Table table = (Table) this.messagesComposite;
		table.removeAll();
		for (String message : this.resultMessages) {
			TableItem t = new TableItem(table, SWT.BORDER | SWT.WRAP);
			t.setText(" - " + message);
		}

	}

	private void refreshPageInfo() {
		this.renderTransformationResult();
		this.renderResultsMessages();
	}

	public void performTransformation() {
		this.performTransformation(null);
	}

	public void performTransformation(
			TransformationExtraOperations transformationExtOperations) {

		this.resultMessages.clear();
		final RequestTransformationParametersPage parametersPage = (RequestTransformationParametersPage) this
				.getWizard()
				.getPage(
						Messages
								.getString("ShowTransformationResultsPage.EnterTransformationParametersPageName"));
		final RequestConfigurationPage configurationPage = (RequestConfigurationPage) this
				.getWizard()
				.getPage(
						Messages
								.getString("TransformationExecutionWizardDialog.SelectConfigurationModelPageName"));

		try {
			new ProgressMonitorDialog(this.getShell()).run(false, false,
					new ExecuteTransformationJob(parametersPage,
							configurationPage, transformationExtOperations));
		} catch (InvocationTargetException e) {
		} catch (InterruptedException e) {
		}

		this.setPageComplete(true);
		this.refreshPageInfo();
	}

	private class ExecuteTransformationJob implements IRunnableWithProgress {

		RequestTransformationParametersPage parametersPage = null;
		RequestConfigurationPage configurationPage = null;
		TransformationExtraOperations transformationExtOperations = null;

		public ExecuteTransformationJob(
				RequestTransformationParametersPage parametersPage,
				RequestConfigurationPage configurationPage,
				TransformationExtraOperations transformationExtOperations) {
			this.parametersPage = parametersPage;
			this.configurationPage = configurationPage;
			this.transformationExtOperations = transformationExtOperations;
		}

		public void run(IProgressMonitor monitor)
				throws InvocationTargetException, InterruptedException {
			monitor.beginTask("Proceeding to execute the transformation", 100);
			Map<String, Object> parameters = getPagesContributedParameters();
			RequestTransformationParametersPage page = (RequestTransformationParametersPage) ShowTransformationResultsPage.this
					.getWizard()
					.getPage(
							Messages
									.getString("TransformationExecutionWizard.EnterTransformationParameters"));
			boolean synchronize = page.shouldSynchronizeOutputModel();
			TransformedResource configuration = null;

			if (configurationPage != null
					&& !configurationPage.isDefaultConfiguration()) {
				configuration = new TransformedResource("CONFIGURATION",
						configurationPage.getConfigurationModel());
			}

			// Fill TransformationInformation
			TransformationInformation transformationInfo = new TransformationInformation(
					transformation.getId(), parametersPage.getInputParams(),
					parametersPage.getOutputParams(), resultMessages, monitor,
					configuration, parameters, synchronize);

			// Do previous operations before launching transformation
			doPreOperations(transformationInfo);

			doRun(transformationInfo);

			// Do post operations before launching transformation
			doPostOperations(transformationInfo);

			monitor.done();
		}

		protected void doPreOperations(TransformationInformation transfInfo) {
			if (transformationExtOperations != null) {
				transformationExtOperations.executePreOperations(transfInfo);
			}
		}

		protected void doRun(TransformationInformation transfInfo) {
			correctExecution = TransformationsRegistry.runTransformation(
					transfInfo.getTransformationId(), transfInfo
							.getInputParams(), transfInfo.getOutputParams(),
					transfInfo.getMessagesList(), transfInfo.getMonitor(),
					transfInfo.getConfiguration(), transfInfo.getParameters(),
					transfInfo.isSynchronize());
		}

		protected void doPostOperations(TransformationInformation transfInfo) {
			if (transformationExtOperations != null) {
				transformationExtOperations.executePostOperations(transfInfo);
			}
		}

	}

	protected Map<String, Object> getPagesContributedParameters() {
		Map<String, Object> mapContributedParameters = new HashMap<String, Object>();
		if (getWizard() != null && getWizard().getPages() != null) {
			for (IWizardPage page : getWizard().getPages()) {
				if (page instanceof IWizardPageParametersProvider) {
					IWizardPageParametersProvider provider = (IWizardPageParametersProvider) page;
					mapContributedParameters.putAll(provider.getParameters());
				}
			}
		}
		return mapContributedParameters;
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
							exportValidationErrors(resultMessages, printStream);
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
				.getString("ShowTransformationResultsPage.2"), //$NON-NLS-1$
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
				.getString("ShowTransformationResultsPage.3")); //$NON-NLS-1$
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
