package ch.droptilllate.application.views;


import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;


public class LogView implements Observer{
	private Text textBox;
	private String message;
	
	@PostConstruct
	public void createControls(Composite parent) {
		// TODO evt. Statusline
				// statusLine.setMessage("gagag");
				String message = "Applicationn is ready";
				textBox = new Text(parent, SWT.WRAP | SWT.BORDER);
				textBox.setBounds(0, 200, 450, 100);
				textBox.setTextLimit(100);
				textBox.setText(message);
				Status status = Status.getInstance();
				status.addObserver(this);
			
				
	}

	@Override
	public synchronized void update(Observable o, Object arg) {
		message = (String) arg;
		message = "\n" + message;
		textBox.append(message);
		
	}



}
