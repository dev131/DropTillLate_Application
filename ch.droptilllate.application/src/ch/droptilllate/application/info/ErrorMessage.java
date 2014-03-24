package ch.droptilllate.application.info;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class ErrorMessage {

	public ErrorMessage(Shell shell, String title, String message){
		MessageDialog dialog = new MessageDialog(shell, title, null,
				message, MessageDialog.ERROR, new String[] { "OK" }, 0);
		dialog.open();
	}
}
