
package ch.droptilllate.application.handlers;

import java.io.File;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;

import ch.droptilllate.application.controller.ViewController;
import ch.droptilllate.application.os.OSValidator;
import ch.droptilllate.application.properties.Messages;

public class DecryptHandler {
private String path;
	
	@Execute
	public void execute(Shell shell) {
		chooseDestionation(shell);
		if(path != null){
			ViewController.getInstance().decryptFiles(path);
	
		}
	}
	
	private void chooseDestionation(Shell shell){
		DirectoryDialog dirDialog = new DirectoryDialog(shell);
	    dirDialog.setText("Select your home directory");   	
	    try {
	    	path = dirDialog.open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
