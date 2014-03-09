package ch.droptilllate.application.views;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

import ch.droptilllate.application.core.KeyManager;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.application.properties.Messages;

import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.custom.CLabel;

public class PasswordDialog extends Dialog {
  private String password = "";
  private Text loginPassword;
  private Boolean newUser = false;
  private Label lblResult;
  private Shell shell;
  private Label lblMissingPasswordOr;
  
  public PasswordDialog(Shell parentShell) {
    super(parentShell);
    shell = parentShell;
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
    
    CLabel lblNewLabel = new CLabel(container, SWT.NONE);
    lblNewLabel.setTopMargin(4);
    lblNewLabel.setImage(ResourceManager.getPluginImage("ch.droptilllate.application", "logos/Logo64.png"));
    lblNewLabel.setText("\u00A9DropTillLate");
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
    //Listener DropboxFolder
        Button btnChooseDropboxFolder = new Button(container, SWT.NONE);
        btnChooseDropboxFolder.addSelectionListener(new SelectionAdapter() {
         	@Override
         	public void widgetSelected(SelectionEvent e) {
         		openFolderDialog();
         	}

		private void openFolderDialog() {
			    DirectoryDialog dialog = new DirectoryDialog(shell);
			    dialog.setText("Choose Dropbox Directory");
			    String dropboxPath = dialog.open();
			    if(dropboxPath != null)
					try {
						Configuration.setPropertieDropBoxPath(dropboxPath);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}
         });
        btnChooseDropboxFolder.setText("Choose Dropbox Folder ....");
        btnChooseDropboxFolder.setVisible(false);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    //Listener TempFolder
      Button btnChooseTempFolder = new Button(container, SWT.NONE);
      btnChooseTempFolder.addSelectionListener(new SelectionAdapter() {
      	@Override
      	public void widgetSelected(SelectionEvent e) {
      		openFolderDialog();
      	}
      	private void openFolderDialog() {
		    DirectoryDialog dialog = new DirectoryDialog(shell);
		    dialog.setText("Choose Tempfolder");
		    String tempPath = dialog.open();
		    if(tempPath != null)
				try {
					Configuration.setPropertieTempPath(tempPath+ Messages.getSlash());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
      });
      btnChooseTempFolder.setText("Choose Temp Folder ....");
      btnChooseTempFolder.setVisible(false);
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
    
    lblResult = new Label(container, SWT.NONE);
    lblResult.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
    lblResult.setEnabled(false);
    lblResult.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
    lblResult.setText("Wrong Password! Try Again");
    lblResult.setToolTipText("");
    lblResult.setVisible(false);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    new Label(container, SWT.NONE);
    
    lblMissingPasswordOr = new Label(container, SWT.NONE);
    lblMissingPasswordOr.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
    lblMissingPasswordOr.setEnabled(false);
    lblMissingPasswordOr.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
    lblMissingPasswordOr.setText("Missing password or folder definition!");
    lblMissingPasswordOr.setToolTipText("");
    lblMissingPasswordOr.setVisible(false);
 
    KeyManager km = new KeyManager();
    String k = Configuration.getPropertieDropBoxPath();
    if((Configuration.getPropertieDropBoxPath() == null)){
    	//If path not defined
    	btnChooseDropboxFolder.setVisible(true);
  	  	btnChooseTempFolder.setVisible(true);
  	  	}
    if (!km.checkIfStructureFileExist()){
    		  //FileStructure and Masterpassword missing
    	    	lblLoginPassword.setText("Create Password");   	    	  
    	    	newUser = true;
    	      
    	    }
    return container;
  }

  public void checkPassword(){ 
	  KeyManager km = new KeyManager();
		if(!km.checkPassword(getPassword(), Messages.SaltMasterPassword, Messages.getIdSize())){
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
		//Check if all properties are set
		if((Configuration.getPropertieDropBoxPath() != null)&&
				(Configuration.getPropertieTempPath() !=null)&&
				(password != null)){
			//CreateFolder
			createFolder();
			km.initPassword(getPassword(), Messages.SaltMasterPassword);
			super.okPressed();
		}
		lblMissingPasswordOr.setVisible(true);
	}
	else checkPassword();
	super.okPressed();
  }

  private void createFolder() {  
	  File dir = new File(Configuration.getPropertieDropBoxPath()+Messages.getSlash()+Messages.getIdSize());
	  dir.mkdir();	
}

public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
 
} 