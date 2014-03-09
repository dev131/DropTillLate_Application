package ch.droptilllate.application.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import ch.droptilllate.application.controller.ViewController;
import ch.droptilllate.application.views.InputFolderDialog;

public class NewFolderHandler {

	@Execute
	public void execute(Shell shell) {
		InputFolderDialog dialog = new InputFolderDialog(shell);
		dialog.create();
		if (dialog.open() == Window.OK) {
		  System.out.println(dialog.getLastName());
		  ViewController viewcontroller = ViewController.getInstance();
		  viewcontroller.newFolder(dialog.getLastName());		  
		} 
	}
	
	

}

