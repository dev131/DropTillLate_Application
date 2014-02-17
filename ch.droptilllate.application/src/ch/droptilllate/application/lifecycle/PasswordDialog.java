package ch.droptilllate.application.lifecycle;
import java.awt.Color;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

import ch.droptilllate.application.core.KeyManager;
import ch.droptilllate.application.views.Messages;

import org.eclipse.wb.swt.SWTResourceManager;

public class PasswordDialog extends Dialog {
  private String password = "";
  private Text loginPassword;
  private Boolean newUser = false;
  private Label lblResult;
  
  public PasswordDialog(Shell parentShell) {
    super(parentShell);
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    Composite container = (Composite) super.createDialogArea(parent);
    GridLayout layout = new GridLayout(3, false);
    layout.marginRight = 5;
    layout.marginLeft = 10;
    container.setLayout(layout);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    
    Label lblLoginPassword = new Label(container, SWT.NONE);
    lblLoginPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblLoginPassword.setText("Login Password");
    loginPassword = new Text(container, SWT.BORDER);
    loginPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
      
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    
    lblResult = new Label(container, SWT.NONE);
    lblResult.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
    lblResult.setEnabled(false);
    lblResult.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
    lblResult.setText("Wrong Password! Try Again");
    lblResult.setToolTipText("");
    lblResult.setVisible(false);
    
    KeyManager km = new KeyManager();
    if(!km.checkMasterPasswordExisting()){
    	lblLoginPassword.setText("Create Password");
    	newUser = true;
    }
    return container;
  }

  public void checkPassword(){ 
	  KeyManager km = new KeyManager();
		if(!km.checkPassword(getPassword(), Messages.getSaltMasterPassword(),Integer.parseInt(Messages.getShareFolder0name()))){
			lblResult.setVisible(true);
		}
		else{
			 super.okPressed();
		}  
  
  }
  // override method to use "Login" as label for the OK button
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.OK_ID, "Login", true);
    createButton(parent, IDialogConstants.CANCEL_ID,
        IDialogConstants.CANCEL_LABEL, false);
  }

  @Override
  protected Point getInitialSize() {
    return new Point(450, 300);
  }

  @Override
  protected void okPressed() {
    // Copy data from SWT widgets into fields on button press.
    // Reading data from the widgets later will cause an SWT
    // widget diposed exception.
    password = loginPassword.getText();
	KeyManager km = new KeyManager();
	if(newUser){
		km.initPassword(getPassword(), Messages.getSaltMasterPassword());
	    super.okPressed();
	}
	else checkPassword();
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

} 