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

public class InputView extends TitleAreaDialog {

	private String title;
	
	 public InputView(Shell parentShell, String title) {	
		 super(parentShell);
		 this.title = title;
	
}

	  private Text PasswordText;
	  private String passwordString;
	  private Text EmailText;
	  private String emailString;
	  private Label lblEmail;
	  private Text foldernameText;
	  private  Label lblFolderName;
	  private String foldernameString;

	  @Override
	  public void create() {
	    super.create();
	    setTitle(title);
	  }

	  @Override
	  protected Control createDialogArea(Composite parent) {
	    Composite area = (Composite) super.createDialogArea(parent);
	    Composite container = new Composite(area, SWT.NONE);
	    container.setLayoutData(new GridData(GridData.FILL_BOTH));
	    GridLayout layout = new GridLayout(2, false);
	    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    container.setLayout(layout);


	    createLastName(container);
	    
	    lblEmail = new Label(container, SWT.NONE);
	    lblEmail.setText("E-mail");
	    
	    EmailText = new Text(container, SWT.BORDER);
	    EmailText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	    
	    lblFolderName = new Label(container, SWT.NONE);
	    lblFolderName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	    lblFolderName.setText("Folder Name");
	    
	    foldernameText = new Text(container, SWT.BORDER);
	    foldernameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	    foldernameText.setVisible(false);
	    lblFolderName.setVisible(false);
	    return area;
	  }
	  public void setFolderNameVisible(){
		  foldernameText.setVisible(true);
		    lblFolderName.setVisible(true);
		    EmailText.setVisible(false);
		    lblEmail.setVisible(false);
	  }

	  
	  private void createLastName(Composite container) {
	    Label lbtLastName = new Label(container, SWT.NONE);
	    lbtLastName.setText("Password");
	    
	    GridData dataLastName = new GridData();
	    dataLastName.grabExcessHorizontalSpace = true;
	    dataLastName.horizontalAlignment = GridData.FILL;
	    PasswordText = new Text(container, SWT.BORDER);
	    PasswordText.setLayoutData(dataLastName);
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
		  foldernameString = foldernameText.getText();
	  }

	  @Override
	  protected void okPressed() {
	    saveInput();
	    super.okPressed();
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
