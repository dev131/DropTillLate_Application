package ch.droptilllate.application.views;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class ShareDiaolog extends TitleAreaDialog {

	 public ShareDiaolog(Shell parentShell) {	
		 super(parentShell);
	
}

	  private Text PasswordText;
	  private String passwordString;
	  private Text EmailText;
	  private String emailString;
	  private Label lblEmail;
	  private String foldernameString;

	  @Override
	  public void create() {
	    super.create();
	    setTitle("DroptTillLate");
	  }

	  @Override
	  protected Control createDialogArea(Composite parent) {
	  	setTitleImage(ResourceManager.getPluginImage("ch.droptilllate.application", "logos/Logo48.png"));
	    Composite area = (Composite) super.createDialogArea(parent);
	    Composite container = new Composite(area, SWT.NONE);
	    container.setLayoutData(new GridData(GridData.FILL_BOTH));
	    container.setLayout(new FormLayout());
	    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    Label lbtLastName = new Label(container, SWT.NONE);
	    FormData fd_lbtLastName = new FormData();
	    fd_lbtLastName.top = new FormAttachment(0, 7);
	    fd_lbtLastName.left = new FormAttachment(0, 5);
	    lbtLastName.setLayoutData(fd_lbtLastName);
	    lbtLastName.setText("Password");
	    PasswordText = new Text(container, SWT.BORDER);
	    FormData fd_PasswordText = new FormData();
	    fd_PasswordText.right = new FormAttachment(0, 445);
	    fd_PasswordText.top = new FormAttachment(0, 5);
	    fd_PasswordText.left = new FormAttachment(0, 65);
	    PasswordText.setLayoutData(fd_PasswordText);
	    
	    lblEmail = new Label(container, SWT.NONE);
	    FormData fd_lblEmail = new FormData();
	    fd_lblEmail.top = new FormAttachment(0, 31);
	    fd_lblEmail.left = new FormAttachment(0, 5);
	    lblEmail.setLayoutData(fd_lblEmail);
	    lblEmail.setText("E-mail");
	    EmailText = new Text(container, SWT.BORDER);
	    FormData fd_EmailText = new FormData();
	    fd_EmailText.right = new FormAttachment(0, 445);
	    fd_EmailText.top = new FormAttachment(0, 29);
	    fd_EmailText.left = new FormAttachment(0, 65);
	    EmailText.setLayoutData(fd_EmailText);
	    
	    Composite composite = new Composite(container, SWT.NONE);
	    FormData fd_composite = new FormData();
	    fd_composite.bottom = new FormAttachment(0, 170);
	    fd_composite.right = new FormAttachment(0, 440);
	    fd_composite.top = new FormAttachment(0, 51);
	    fd_composite.left = new FormAttachment(0, 5);
	    composite.setLayoutData(fd_composite);
	    composite.setLayout(new GridLayout(1, false));
	    
	    return area;
	  }
	  

	  @Override
	  protected boolean isResizable() {
	    return true;
	  }

	  // save content of the Text fields because they get disposed
	  // as soon as the Dialog closes
	  private void saveInput() {
		  passwordString = PasswordText.getText();
		  emailString = EmailText.getText();
	  }

	  @Override
	  protected void okPressed() {
	    saveInput();
	    if(passwordString == null || emailString == null){
	    	//TODO error messages
	    }
	    else{
		    super.okPressed();
	    }
	  }

	  public String getPassword() {
	    return passwordString;
	  }

	  public String getEmail(){
		  return emailString;
	  }

	public String getFoldernameString() {
		return foldernameString;
	}
	} 
