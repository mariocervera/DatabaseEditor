package es.cv.gvcase.mdt.db.properties.sections;

import org.eclipse.datatools.modelbase.sql.schema.Sequence;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecoretools.tabbedproperties.sections.AbstractTabbedPropertySection;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import es.cv.gvcase.mdt.db.properties.composites.IdentitySpecifierDetailsComposite;

public class SequenceDetailsPropertySection extends AbstractTabbedPropertySection {
	
	private Group groupDetails;
	
	IdentitySpecifierDetailsComposite isdcomposite;

	@Override
	protected EditingDomain getEditingDomain() {
		try {
			return super.getEditingDomain();
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}
	
	@Override
	protected EStructuralFeature getFeature() {
		return null;
	}

	@Override
	protected String getLabelText() {
		return null;
	}
	
	protected void createWidgets(Composite composite) {
		
		super.createWidgets(composite);
		
		groupDetails = getWidgetFactory().createGroup(composite, "Identity details");
		GridLayout gl = new GridLayout();
		groupDetails.setLayout(gl);
		FormData fd = new FormData();
		fd.left = new FormAttachment(0, 0);
		fd.right = new FormAttachment(100, 0);
		groupDetails.setLayoutData(fd);
		
		isdcomposite = new IdentitySpecifierDetailsComposite(groupDetails, groupDetails.getStyle());
		isdcomposite.createWidgets(groupDetails, getWidgetFactory());
		GridData gd = new GridData(GridData.FILL_BOTH);
		isdcomposite.setLayoutData(gd);
	}
	
	@Override
	protected void setSectionData(Composite composite) {
		
		super.setSectionData(composite);
		
		if(isdcomposite != null) {
			isdcomposite.setSectionData(composite);
		}
	}
	
	@Override
	protected void hookListeners() {
		
		super.hookListeners();
		
		if(isdcomposite != null) {
			isdcomposite.hookListeners();
		}
	}

	@Override
	public void refresh() {
		
		super.refresh();
		
		if(isdcomposite != null) {
			if(this.getEObject() instanceof Sequence) {
				if(isdcomposite != null) {
					isdcomposite.setElement(((Sequence)this.getEObject()).getIdentity());
				}
			}
		
			isdcomposite.setEMFEditDomain(getEditingDomain());
			isdcomposite.refresh();
		}
	}
}
