package ch.droptilllate.application.views;

import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import ch.droptilllate.application.controller.ViewController;

public class StatusLine implements Observer {
	private Text textBox;
	private String message;

	@PostConstruct
	public void createControls(Composite parent) {
		// TODO evt. Statusline
		// statusLine.setMessage("gagag");
		String message = "Applicationn is ready";
		textBox = new Text(parent, SWT.WRAP | SWT.BORDER);
		textBox.setTextLimit(100);
		textBox.setText(message);
		Status status = Status.getInstance();
		status.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		message = (String) arg;
		textBox.setText(message);
	}
}
