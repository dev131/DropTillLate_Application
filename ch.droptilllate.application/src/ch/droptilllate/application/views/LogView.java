package ch.droptilllate.application.views;


import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;



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
				this.textBox = new Text(parent, SWT.WRAP | SWT.BORDER);
				this.textBox.setBounds(0, 200, 450, 100);
				this.textBox.setTextLimit(100);
				this.textBox.setText(message);
				Status status = Status.getInstance();
				status.addObserver(this);
			
				
	}

	@Override
	public synchronized void update(Observable o, Object arg) {
		this.message = (String) arg;
		this.message = "\n" + this.message;
		this.textBox.append(this.message);
		
	}



}
