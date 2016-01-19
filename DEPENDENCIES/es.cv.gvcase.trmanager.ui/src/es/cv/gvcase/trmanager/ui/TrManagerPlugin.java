package es.cv.gvcase.trmanager.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class TrManagerPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "es.cv.gvcase.trmanager.ui";
	
	protected static TrManagerPlugin INSTANCE = null;

	public static TrManagerPlugin getInstance() {
		return INSTANCE;
	}

	public TrManagerPlugin() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
	}

}
