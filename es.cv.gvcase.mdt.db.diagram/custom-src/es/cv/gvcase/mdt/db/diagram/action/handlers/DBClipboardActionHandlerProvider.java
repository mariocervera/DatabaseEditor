package es.cv.gvcase.mdt.db.diagram.action.handlers;

import es.cv.gvcase.mdt.common.actions.handlers.ClipboardActionHandler;
import es.cv.gvcase.mdt.common.actions.handlers.ClipboardActionHandlerProvider;

public class DBClipboardActionHandlerProvider extends ClipboardActionHandlerProvider {

	@Override
	protected ClipboardActionHandler getClipboardActionHandler() {
		return new DBClipboardActionHandler();
	}
}
