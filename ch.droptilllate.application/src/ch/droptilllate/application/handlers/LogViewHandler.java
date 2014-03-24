package ch.droptilllate.application.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;

public class LogViewHandler {
	@Execute
	public void execute(Shell shell, EPartService partService) {
	
		MPart mPart = partService.findPart("ch.droptilllate.application.part.logview");
		if(mPart.isVisible()){
			mPart.setVisible(false);
		}
		else{
			mPart.setVisible(true);
		}
	
	}
}
