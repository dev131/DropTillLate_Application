package ch.droptilllate.application.views;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;


public class StatusLine {
	private String message;

	@PostConstruct
	public void createControls(Composite parent) {
		// TODO evt. Statusline
		// statusLine.setMessage("gagag");
		String message = "© DropTillLate";
		
		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblNewLabel.setBounds(0, 0, 59, 14);
		lblNewLabel.setText("© DropTillLate");
	
	}
}
