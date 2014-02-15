package ch.droptilllate.application.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import ch.droptilllate.application.controller.ViewController;

public class ShareHandler {
	@Execute
	public void execute(Shell shell) {

		 ViewController viewcontroller = ViewController.getInstance();
		 viewcontroller.shareFiles();
	}
	
	
}
