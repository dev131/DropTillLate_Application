package ch.droptilllate.application.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;

import ch.droptilllate.application.controller.ViewController;
import ch.droptilllate.application.views.ShareView;

public class ImportHandler {
	@Inject EPartService partService;
	@Execute
	public void execute(Shell shell, EPartService partService) {
		MPart ownpart = partService.findPart("ch.droptilllate.application.part.decryptedview");
		ownpart.setVisible(false);
		MPart sharePart = partService.findPart("ch.droptilllate.application.part.sharepart");
		sharePart.setVisible(false);
		MPart mPart = partService.findPart("ch.droptilllate.application.part.Import");
		mPart.setVisible(true);
		ShareView.getInstance().deleteUnSuccessShareFolder();
	}
}
