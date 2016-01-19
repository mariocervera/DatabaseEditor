package es.cv.gvcase.mdt.db.properties.sections;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.datatools.modelbase.sql.tables.CheckType;
import org.eclipse.datatools.modelbase.sql.tables.SQLTablesPackage;
import org.eclipse.datatools.modelbase.sql.tables.ViewTable;
import org.eclipse.emf.ecore.EStructuralFeature;
import es.cv.gvcase.mdt.common.sections.AbstractEnumerationPropertySection;

public class ViewCheckTypeSection extends AbstractEnumerationPropertySection {

	@Override
	protected String[] getEnumerationFeatureValues() {
		List<String> values = new ArrayList<String>();
		for (CheckType type : CheckType.values()){
			values.add(type.getLiteral());
		}
		return values.toArray(new String [0]);
	}

	@Override
	protected String getFeatureAsText() {
		return ((ViewTable)this.getEObject()).getCheckType().getLiteral();
	}

	@Override
	protected Object getFeatureValue(int index) {		
		return CheckType.get(index);
	}

	@Override
	protected Object getOldFeatureValue() {
		return ((ViewTable)this.getEObject()).getCheckType();
	}

	@Override
	protected EStructuralFeature getFeature() {
		return SQLTablesPackage.eINSTANCE.getViewTable_CheckType();
	}

	@Override
	protected String getLabelText() {
		return "Check Type: ";
	}

}
