package ch.droptilllate.application.views;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;


public class StatusLine {
	private Text textBox;
	private String message;

	@PostConstruct
	public void createControls(Composite parent) {
		// TODO evt. Statusline
		// statusLine.setMessage("gagag");
		String message = "Welcome to DropTillLate";
		textBox = new Text(parent, SWT.WRAP | SWT.BORDER);
		textBox.setTextLimit(100);
		textBox.setText(message);
	
	}

}
