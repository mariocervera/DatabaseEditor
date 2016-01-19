package es.cv.gvcase.mdt.gen.constraints;

import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.emf.validation.model.IClientSelector;

public class ValidationDelegateClientSelector implements IClientSelector{
	
	public boolean selects(Object object) {

		if(object instanceof Database) {
			return true;
		}
		else {
			return false;
		}
	}

}
