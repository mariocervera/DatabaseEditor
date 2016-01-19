package es.cv.gvcase.mdt.db.properties.sections;

import org.eclipse.datatools.modelbase.sql.schema.IdentitySpecifier;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.SQLTablesPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

import es.cv.gvcase.mdt.common.composites.DetailComposite;
import es.cv.gvcase.mdt.db.properties.composites.IdentitySpecifierDetailsComposite;

public class ColumnSequencePropertySection extends
		AbstractDetailedBooleanPropertySection {

	@Override
	protected String getLabelText() {
		return "Sequence";
	}
	
	@Override
	protected EStructuralFeature getFeature() {
		return SQLTablesPackage.eINSTANCE.getColumn_IdentitySpecifier();
	}
	
	@Override
	protected String getDetailsLabelText() {
		return "Sequence details";
	}
	
	@Override
	protected DetailComposite getComposite(Composite parent) {
		IdentitySpecifierDetailsComposite c =
			new IdentitySpecifierDetailsComposite(parent, parent.getStyle());
		if(getEObject() instanceof Column) {
			IdentitySpecifier is = ((Column)getEObject()).getIdentitySpecifier();
			if(is != null) c.setElement(is);
		}
		return c;
	}

}
