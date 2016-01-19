package es.cv.gvcase.mdt.db.properties.sections;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.datatools.modelbase.sql.tables.ReferenceType;
import org.eclipse.datatools.modelbase.sql.tables.SQLTablesPackage;
import org.eclipse.datatools.modelbase.sql.tables.ViewTable;
import org.eclipse.emf.ecore.EStructuralFeature;
import es.cv.gvcase.mdt.common.sections.AbstractEnumerationPropertySection;

public class ViewSelfRefColumnGenerationSection extends AbstractEnumerationPropertySection {

	@Override
	protected String[] getEnumerationFeatureValues() {
		List<String> values = new ArrayList<String>();
		for (ReferenceType type : ReferenceType.values()){
			values.add(type.getLiteral());
		}
		return values.toArray(new String [0]);
	}

	@Override
	protected String getFeatureAsText() {
		return ((ViewTable)this.getEObject()).getSelfRefColumnGeneration().getLiteral();
	}

	@Override
	protected Object getFeatureValue(int index) {		
		return ReferenceType.get(index);
	}

	@Override
	protected Object getOldFeatureValue() {
		return ((ViewTable)this.getEObject()).getSelfRefColumnGeneration();
	}

	@Override
	protected EStructuralFeature getFeature() {
		return SQLTablesPackage.eINSTANCE.getTable_SelfRefColumnGeneration();
	}

	@Override
	protected String getLabelText() {
		return "Self ref Column Generation: ";
	}

}
