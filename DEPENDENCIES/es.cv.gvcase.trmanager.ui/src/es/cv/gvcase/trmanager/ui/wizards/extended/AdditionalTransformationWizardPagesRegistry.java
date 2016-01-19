/*******************************************************************************
 * Copyright (c) 2010 Conselleria de Infraestructuras y Transporte, Generalitat 
 * de la Comunitat Valenciana . All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Francisco Javier Cano Muñoz (Prodevelop S.L.) - initial implementation
 *
 ******************************************************************************/
package es.cv.gvcase.trmanager.ui.wizards.extended;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardPage;

import es.cv.gvcase.trmanager.ui.pages.IWizardPageHandlesNext;

/**
 * A registry holding and serving the additional {@link WizardPage}s that are to
 * be added to certain transformations. <br>
 * Additional pages are added to the wizard via contribution to the
 * additionalTransformationWizardPages extension point. <br>
 * For each page, the point in the wizard where it is to be added can be
 * specified. <br>
 * For each page, the transformation to which that page is added can be
 * specified.
 * 
 * @author <a href="mailto:fjcano@prodevelop.es">Francisco Javier Cano Muñoz</a>
 * 
 * @see IWizardPageHandlesNext
 */
public class AdditionalTransformationWizardPagesRegistry {

	// //
	// Singleton
	// //

	protected static AdditionalTransformationWizardPagesRegistry INSTANCE = null;

	public static AdditionalTransformationWizardPagesRegistry getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AdditionalTransformationWizardPagesRegistry();
		}
		return INSTANCE;
	}

	private AdditionalTransformationWizardPagesRegistry() {
		return;
	}

	// //
	// Helper class
	// //

	public class AdditionalPage {
		public String pageID;
		public String location;
		public Object wizardPage;
		public String transformationID;
	}

	// //
	// read extension point
	// //

	public static final String ExtensionPointID = "es.cv.gvcase.trmanager.ui.additionalTransformationWizardPages";

	protected List<AdditionalPage> readExtensionPoint() {
		List<AdditionalPage> pages = new ArrayList<AdditionalPage>();

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtension[] extensions = registry.getExtensionPoint(ExtensionPointID)
				.getExtensions();

		for (IExtension extension : extensions) {
			pages.addAll(processExtension(extension));
		}

		return pages;
	}

	private static final String PageIDName = "pageID";
	private static final String locationName = "location";
	private static final String wizardPageName = "wizardPage";
	private static final String transformationIDName = "transformationID";

	private List<AdditionalPage> processExtension(IExtension extension) {
		ArrayList<AdditionalPage> pages = new ArrayList<AdditionalPage>();
		AdditionalPage additionalPage = new AdditionalPage();

		for (IConfigurationElement configElement : extension
				.getConfigurationElements()) {
			additionalPage = new AdditionalPage();
			additionalPage.pageID = configElement.getAttribute(PageIDName);
			additionalPage.location = configElement.getAttribute(locationName);
			additionalPage.transformationID = configElement
					.getAttribute(transformationIDName);
			try {
				additionalPage.wizardPage = configElement
						.createExecutableExtension(wizardPageName);
				if (additionalPage.pageID != null
						&& additionalPage.location != null
						&& additionalPage.transformationID != null
						&& additionalPage.wizardPage instanceof WizardPage) {
					pages.add(additionalPage);
				}
			} catch (CoreException ex) {
				// ignore
			}
		}
		return pages;
	}

	// //
	// utility methods
	// //

	protected List<AdditionalPage> additionalPages = null;

	public List<WizardPage> getAllWizardPages() {
		if (additionalPages == null) {
			additionalPages = readExtensionPoint();
		}
		//
		List<WizardPage> pages = new ArrayList<WizardPage>();
		for (AdditionalPage page : additionalPages) {
			if (page.wizardPage instanceof WizardPage) {
				pages.add((WizardPage) page.wizardPage);
			}
		}
		return pages;
	}

	public List<WizardPage> getAllStartWizardPages(String transformationID) {
		if (additionalPages == null) {
			additionalPages = readExtensionPoint();
		}
		//
		List<WizardPage> pages = new ArrayList<WizardPage>();
		for (AdditionalPage page : additionalPages) {
			if (page.wizardPage instanceof WizardPage
					&& page.location.equals("Start")
					&& page.transformationID.equals(transformationID)) {
				pages.add((WizardPage) page.wizardPage);
			}
		}
		return pages;
	}

	public List<WizardPage> getAllAfterParametersWizardPages(
			String transformationID) {
		if (additionalPages == null) {
			additionalPages = readExtensionPoint();
		}
		//
		List<WizardPage> pages = new ArrayList<WizardPage>();
		for (AdditionalPage page : additionalPages) {
			if (page.wizardPage instanceof WizardPage
					&& page.location.equals("AfterParameters")
					&& page.transformationID.equals(transformationID)) {
				pages.add((WizardPage) page.wizardPage);
			}
		}
		return pages;
	}

	public List<WizardPage> getAllAfterConfigurationWizardPages(
			String transformationID) {
		if (additionalPages == null) {
			additionalPages = readExtensionPoint();
		}
		//
		List<WizardPage> pages = new ArrayList<WizardPage>();
		for (AdditionalPage page : additionalPages) {
			if (page.wizardPage instanceof WizardPage
					&& page.location.equals("AfterConfiguration")
					&& page.transformationID.equals(transformationID)) {
				pages.add((WizardPage) page.wizardPage);
			}
		}
		return pages;
	}

	public List<WizardPage> getAllAfterValidationWizardPages(
			String transformationID) {
		if (additionalPages == null) {
			additionalPages = readExtensionPoint();
		}
		//
		List<WizardPage> pages = new ArrayList<WizardPage>();
		for (AdditionalPage page : additionalPages) {
			if (page.wizardPage instanceof WizardPage
					&& page.location.equals("AfterValidation")
					&& page.transformationID.equals(transformationID)) {
				pages.add((WizardPage) page.wizardPage);
			}
		}
		return pages;
	}

	public List<WizardPage> getAllBeforeEndWizardPages(String transformationID) {
		if (additionalPages == null) {
			additionalPages = readExtensionPoint();
		}
		//
		List<WizardPage> pages = new ArrayList<WizardPage>();
		for (AdditionalPage page : additionalPages) {
			if (page.wizardPage instanceof WizardPage
					&& page.location.equals("BeforeEnd")
					&& page.transformationID.equals(transformationID)) {
				pages.add((WizardPage) page.wizardPage);
			}
		}
		return pages;
	}
}
