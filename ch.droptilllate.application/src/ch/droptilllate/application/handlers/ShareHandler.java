package ch.droptilllate.application.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.widgets.Shell;

import ch.droptilllate.application.controller.ViewController;
import ch.droptilllate.application.views.ShareView;

public class ShareHandler {
	@Inject EPartService partService;
	@Execute
	public void execute(Shell shell,EPartService partService) {		
		MPart ownpart = partService.findPart("ch.droptilllate.application.part.decryptedview");
		ownpart.setVisible(false);
		MPart mPart = partService.findPart("ch.droptilllate.application.part.sharepart");
		mPart.setVisible(true);		
		ViewController viewcontroller = ViewController.getInstance();
		 viewcontroller.openShareContext();
			MPart importpart = partService.findPart("ch.droptilllate.application.part.Import");
			importpart.setVisible(false);
	}
	@CanExecute
	public boolean canExecute() {
		boolean status = ViewController.getInstance().isSharefunction();
	 return  status;
	}
	
	
}
