package es.cv.gvcase.launcher.xpand;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.jar.Manifest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.mwe.core.ConfigurationException;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.WorkflowContextDefaultImpl;
import org.eclipse.emf.mwe.core.WorkflowRunner;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.issues.IssuesImpl;
import org.eclipse.emf.mwe.core.monitor.NullProgressMonitor;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;
import org.eclipse.emf.mwe.core.resources.ResourceLoaderFactory;
import org.eclipse.emf.mwe.internal.core.Workflow;
import org.eclipse.emf.mwe.internal.core.ast.util.WorkflowFactory;
import org.eclipse.emf.mwe.internal.core.ast.util.converter.Converter;

public class WorkflowRunnerWithMessages extends WorkflowRunner{

	private WorkflowContextDefaultImpl wfContext = new WorkflowContextDefaultImpl();

	private ProgressMonitor monitor;

	private static final Log logger = LogFactory.getLog(WorkflowRunner.class);
	
	private Map<String, String> params;

	private Workflow workflow;
	
	//Added
	private List<String> messages = null;
	//End Added
	
	/**
	 * @param workFlowFile
	 * @param monitor
	 * @param logger
	 * @param params
	 */
	public boolean run(final String workFlowFile, final ProgressMonitor theMonitor,
			final Map<String, String> theParams, final Map<String, ?> externalSlotContents) {
		//Modified
		return run(workFlowFile, theMonitor, theParams, externalSlotContents, null);
		//End Modified
	}
	
	/**
	 * @param workFlowFile
	 * @param monitor
	 * @param logger
	 * @param params
	 * @param messages
	 */
	public boolean run(final String workFlowFile, final ProgressMonitor theMonitor,
			final Map<String, String> theParams, final Map<String, ?> externalSlotContents,
			List<String> messages) {
		//Added
		this.messages = messages;
		//End Added
		final boolean configOK = prepare(workFlowFile, theMonitor, theParams);
		final Issues issues = new IssuesImpl();
		if (configOK) {
			return executeWorkflow(externalSlotContents, issues);
		}
		return false;
	}

	public boolean prepare(final String workFlowFile, final ProgressMonitor theMonitor,
			final Map<String, String> theParams) {
		if (workFlowFile == null) {
			throw new NullPointerException("workflowFile is null");
		}

		if (theMonitor == null) {
			monitor = new NullProgressMonitor();
		}
		else {
			monitor = theMonitor;
		}
		params = theParams;

		logger.info("--------------------------------------------------------------------------------------");
		logger.info("EMF Modeling Workflow Engine " + getVersion());
		logger.info("(c) 2005-2009 openarchitectureware.org and contributors");
		logger.info("--------------------------------------------------------------------------------------");
		logger.info("running workflow: " + workFlowFile);
		logger.info("");

		if (logger.isDebugEnabled() && !params.isEmpty()) {
			logger.debug("Params:" + params.toString());
		}
		final Issues issues = new IssuesImpl();

		try {
			final WorkflowFactory factory = new WorkflowFactory();

			// see Bug#155854: WorkflowFactory will throw an
			// IllegalArgumentException which indicates
			// a configuration problem.
			// Detect this very special case
			try {
				workflow = factory.parseInitAndCreate(workFlowFile, params, getConverters(), issues);
			}
			catch (final IllegalArgumentException illegalArg) {
				if (illegalArg.getMessage().startsWith("Couldn't load file")) {
					throw new ConfigurationException(illegalArg.getMessage());
				}
				throw illegalArg;
			}
			logIssues(logger, issues);
			if (issues.hasErrors()) {
				//Modified
				logData("error", "Workflow interrupted because of configuration errors.");
				//End Modified
				return false;
			}
			if (workflow != null) {
				workflow.checkConfiguration(issues);
			}
			logIssues(logger, issues);
			if (issues.hasErrors()) {
				//Modified
				logData("error", "Workflow interrupted because of configuration errors.");
				//End Modified
				return false;
			}
		}
		catch (final ConfigurationException ex) {
			logData("fatal", ex.getMessage(), ex);
			logger.fatal(ex.getMessage(), ex);
			logIssues(logger, issues);
			return false;
		}

		return true;

	}

	/**
	 * This method delivers the Converter implementations currently used to
	 * inject values into the workflow components.
	 * 
	 * @return A map between injection types and converter implementations. Not
	 *         <code>null</code>.
	 */
	@SuppressWarnings("unchecked")
	private Map<Class<?>, Converter> getConverters() {
		Map<Class<?>, Converter> result = getCustomConverters();
		final Map<Class<?>, Converter> defaults = WorkflowFactory.getDefaultConverter();
		if (result == null) {
			// go with the default values
			result = defaults;
		}
		else {
			// add default Converter implementations if we don't have a custom
			// implementation yet
			for (final Map.Entry<Class<?>, Converter> record : defaults.entrySet()) {
				if (!result.containsKey(record.getKey())) {
					result.put(record.getKey(), record.getValue());
				}
			}
		}
		return result;
	}

	public boolean executeWorkflow(final Map<?, ?> externalSlotContents, final Issues issues) {
		try {
			wfContext = new WorkflowContextDefaultImpl();
			addExternalSlotContents(externalSlotContents);
			final long time = System.currentTimeMillis();
			monitor.started(workflow, wfContext);
			workflow.invoke(wfContext, monitor, issues);
			monitor.finished(workflow, wfContext);
			final long duration = System.currentTimeMillis() - time;
			//Modified
			logData("info", "workflow completed in " + duration + "ms!");
			//End Modified
			if (issues.getErrors().length > 0) {
				return false;
			}
			return true;
		}
		catch (final Exception e) {
			if (e.getClass().getName().indexOf("Interrupt") > -1) {
				//Modified
				logData("error", "Workflow interrupted. Reason: " + e.getMessage());
				//End Modified
			}
			else {
				//Modified
				logData("error", e.getMessage(), e);
				//End Modified
			}
		}
		finally {
			logIssues(logger, issues);
		}
		return false;
	}

	private void addExternalSlotContents(final Map<?, ?> slotContents) {
		if (slotContents == null) {
			return;
		}
		for (final Object name : slotContents.keySet()) {
			final String key = (String) name;
			wfContext.set(key, slotContents.get(key));
		}
	}

	private void logIssues(final Log logger, final Issues issues) {
		// log any configuration warning messages
		final Diagnostic[] issueArray = issues.getIssues();
		for (final Diagnostic issue : issueArray) {
			if (issue.getSeverity() == Diagnostic.ERROR) {
				//Modified
				logData("error", issue.toString());
			}
			if (issue.getSeverity() == Diagnostic.WARNING) {
				logData("warning", issue.toString());
			}
			if (issue.getSeverity() == Diagnostic.INFO) {
				logData("info", issue.toString());
				//End Modified
			}
		}
	}

	/**
	 * Tries to read the exact build version from the Manifest of the
	 * core.workflow plugin. Therefore the Manifest file is located and the
	 * version is read from the attribute 'Bundle-Version'.
	 * 
	 * @return The build version string, format "4.1.1, Build 200609291913"
	 */
	private String getVersion() {

		String version = null;

		// FIXME find a proper way to resolve the version
		// get all META-INF/MANIFEST.MF found in the classpath
		try {
			final Manifest manifest = new Manifest(ResourceLoaderFactory.createResourceLoader().getResource(
					"META-INF/MANIFEST.MF").openStream());
			// identify the manifest from core.workflow plugin
			final String bundleName = manifest.getMainAttributes().getValue("Bundle-SymbolicName");
			if (bundleName != null) {
				if (bundleName.startsWith("org.eclipse.emf.mwe.core")) {

					// Read bundle version an split it.
					// Original value : "4.1.1.200609291913"
					// Resulting value : "4.1.1, Build 200609291913"
					version = manifest.getMainAttributes().getValue("Bundle-Version");
					final int lastPoint = version.lastIndexOf('.');
					version = version.substring(0, lastPoint) + ", Build " + version.substring(lastPoint + 1);
					return version;
				}
			}
			else {
				logger.warn("Manifest does not contains 'Bundle-SymbolicName' attribute");
			}
		}
		catch (final IOException e) {
			logger.debug("Failed to read Manifest file. Unable to retrieve version");
		}
		// build version not detected from manifest, fallback
		// this will only occur in developer's workbench
		return "Development-Snapshot";
	}
	
	//Added
	private void logData(String severity, String m){
		logData(severity, m, null);
	}
	
	private void logData(String severity, String m, Exception e){
		if(e != null){
			if (messages != null) messages.add(m + ", " + e.toString());
			if(severity.equals("error")){
				logger.error(m, e);
			}
			else if(severity.equals("fatal")){
				logger.fatal(m, e);
			}
			else if(severity.equals("warning")){
				logger.warn(m, e);
			}
			else{
				logger.info(m, e);
			}
		}
		else{
			if (messages != null) messages.add(m);
			if(severity.equals("error")){
				logger.error(m);
			}
			else if(severity.equals("fatal")){
				logger.fatal(m);
			}
			else if(severity.equals("warning")){
				logger.warn(m);
			}
			else{
				logger.info(m);
			}
		}
	}
	//End Added

}
