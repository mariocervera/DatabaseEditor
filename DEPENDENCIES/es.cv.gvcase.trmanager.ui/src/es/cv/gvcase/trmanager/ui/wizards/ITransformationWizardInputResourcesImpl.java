package es.cv.gvcase.trmanager.ui.wizards;

import java.util.HashMap;
import java.util.Map;

import es.cv.gvcase.trmanager.TransformedResource;

public class ITransformationWizardInputResourcesImpl implements
		ITransformationWizardInputResources {

	private Map<String, TransformedResource> inputResources;

	public ITransformationWizardInputResourcesImpl(
			Map<String, TransformedResource> inputResources) {
		this.inputResources = inputResources;
	}

	public HashMap<String, TransformedResource> getInputResources() {
		return (HashMap<String, TransformedResource>) this.inputResources;
	}

}
