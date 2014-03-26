package ch.droptilllate.application.info;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class SuccessMessage {

	public SuccessMessage(Shell shell, String title, String message){
		MessageDialog dialog = new MessageDialog(shell, title, null,
				message, MessageDialog.INFORMATION, new String[] { "OK" }, 0);
		dialog.open();
	}
}
