package es.cv.gvcase.mdt.db.diagram.action.handlers;

import org.eclipse.emf.ecore.EObject;

import es.cv.gvcase.mdt.common.actions.handlers.ClipboardActionHandler;

public class DBClipboardActionHandler extends ClipboardActionHandler {

	@Override
	protected void prepareEObject(EObject object) {
		// fjcano :: keep the same name
		// if (object instanceof ENamedElement) {
		// ENamedElement namedElement = (ENamedElement) object;
		// namedElement.setName(LabelHelper.INSTANCE.findName(object
		// .eContainer(), namedElement));
		// }
	}
}
