package ch.droptilllate.application.views;


import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.workbench.UIEvents.Part;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import ch.droptilllate.application.com.CloudDropboxCom;
import ch.droptilllate.application.controller.ViewController;
import ch.droptilllate.application.dao.CloudAccountDao;
import ch.droptilllate.application.dnb.CloudAccount;
import ch.droptilllate.application.info.ErrorMessage;
import ch.droptilllate.application.info.SuccessMessage;
import ch.droptilllate.application.key.KeyManager;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.cloudprovider.error.CloudError;
import ch.droptilllate.couldprovider.api.ICloudProviderCom;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Group;

public class InitialView implements SelectionListener {

	@Inject EPartService partService;
	@Inject EModelService modelService;
	@Inject MApplication application;
	private Text text_tempPath;
	private Text text_dropboxPath;
	private Text text_password;
	private  Button btnSearchDropFolder;
	private Button btnSearchTmpFolder;
	private Button btnLogin;
	private Label lblPassword;
	private Boolean newUser = false;
	private Boolean newConfigFile = false;
	private Shell shell;
	private Text text_DropboxLoginName;
	private Text text_DropboxPassword;
	private Button btnTestDropbox;
	private Label lblPassword_1;
	private Label lblDropboxLoginname;
	private Label lblDropboxFolder ;
	private Label lblTempFolder;
	private CloudAccount cloudaccount = null;
	private String password;
	private String cloudusername;
	private String cloudpassword;
	private String status;
	private Button btnLogin_1;
	private Label lbldroptilllate;
	private Label lblDroptilllateFoldername;
	private Text txtDroptilllate;
	private String dropboxPath ="";
	private String tmpPath="";
	private Boolean dropboxaccountvalidation;
	private Group grpDroptilllateSettings;
	private Group grpDropboxSettings;
	@PostConstruct
	public void createControls(Composite parent, Shell shell, EPartService partService, EModelService modelService, MApplication application) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		this.shell = shell;
		Bundle bundle = FrameworkUtil.getBundle(TreeView.class);
		//URL url = FileLocator.find(bundle, new Path("icons/icon_128x128.png"), null);
		URL url = FileLocator.find(bundle, new Path("icons/LOGO_BIG_V1.png"), null);
		ImageDescriptor image = ImageDescriptor.createFromURL(url);
    	
    	SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
    	sashForm.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    	sashForm.setLocation(0, 0);
		
		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite_1.setLayout(new GridLayout(1, false));
		
		CLabel lblDroptilllate = new CLabel(composite_1, SWT.NONE);
		GridData gd_lblDroptilllate = new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1);
		gd_lblDroptilllate.widthHint = 686;
		gd_lblDroptilllate.heightHint = 104;
		lblDroptilllate.setLayoutData(gd_lblDroptilllate);
		lblDroptilllate.setAlignment(SWT.CENTER);
		lblDroptilllate.setText("");
		lblDroptilllate.setImage(image.createImage());
		
		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD | SWT.ITALIC));
		composite.setLayout(new GridLayout(4, false));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		grpDroptilllateSettings = new Group(composite, SWT.NONE);
		grpDroptilllateSettings.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		grpDroptilllateSettings.setFont(SWTResourceManager.getFont("Arial", 18, SWT.NORMAL));
		GridLayout gl_grpDroptilllateSettings = new GridLayout(4, false);
		gl_grpDroptilllateSettings.marginTop = 5;
		grpDroptilllateSettings.setLayout(gl_grpDroptilllateSettings);
		GridData gd_grpDroptilllateSettings = new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1);
		gd_grpDroptilllateSettings.widthHint = 393;
		grpDroptilllateSettings.setLayoutData(gd_grpDroptilllateSettings);
		grpDroptilllateSettings.setText("DropTillLate Settings");
		new Label(grpDroptilllateSettings, SWT.NONE);
		
		lblDroptilllateFoldername = new Label(grpDroptilllateSettings, SWT.NONE);
		lblDroptilllateFoldername.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		lblDroptilllateFoldername.setText("Foldername");
		
		txtDroptilllate = new Text(grpDroptilllateSettings, SWT.BORDER);
		txtDroptilllate.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		GridData gd_txtDroptilllate = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtDroptilllate.widthHint = 185;
		txtDroptilllate.setLayoutData(gd_txtDroptilllate);
		txtDroptilllate.setText("DropTillLate");
		new Label(grpDroptilllateSettings, SWT.NONE);
		new Label(grpDroptilllateSettings, SWT.NONE);
		
		lblPassword = new Label(grpDroptilllateSettings, SWT.NONE);
		lblPassword.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		lblPassword.setText("Password");
		
		text_password = new Text(grpDroptilllateSettings,SWT.PASSWORD| SWT.BORDER);
		GridData gd_text_password = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_text_password.widthHint = 164;
		text_password.setLayoutData(gd_text_password);
		text_password.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		
		btnLogin_1 = new Button(grpDroptilllateSettings, SWT.NONE);
		GridData gd_btnLogin_1 = new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1);
		gd_btnLogin_1.widthHint = 74;
		btnLogin_1.setLayoutData(gd_btnLogin_1);
		btnLogin_1.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
		btnLogin_1.setText("login");
		btnLogin_1.addSelectionListener(this);
		new Label(grpDroptilllateSettings, SWT.NONE);
		
		lblDropboxFolder = new Label(grpDroptilllateSettings, SWT.NONE);
		lblDropboxFolder.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		lblDropboxFolder.setText("Dropbox path");
		
		text_dropboxPath = new Text(grpDroptilllateSettings, SWT.BORDER);
		text_dropboxPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		text_dropboxPath.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		btnSearchDropFolder = new Button(grpDroptilllateSettings, SWT.NONE);
		GridData gd_btnSearchDropFolder = new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1);
		gd_btnSearchDropFolder.widthHint = 74;
		btnSearchDropFolder.setLayoutData(gd_btnSearchDropFolder);
		btnSearchDropFolder.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
		btnSearchDropFolder.setText("search ...");
		btnSearchDropFolder.addSelectionListener(this);
		new Label(grpDroptilllateSettings, SWT.NONE);
		
		lblTempFolder = new Label(grpDroptilllateSettings, SWT.NONE);
		lblTempFolder.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		lblTempFolder.setText("Temp path");
		
		text_tempPath = new Text(grpDroptilllateSettings, SWT.BORDER);
		text_tempPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		text_tempPath.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		
		btnSearchTmpFolder = new Button(grpDroptilllateSettings, SWT.NONE);
		GridData gd_btnSearchTmpFolder = new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1);
		gd_btnSearchTmpFolder.widthHint = 75;
		btnSearchTmpFolder.setLayoutData(gd_btnSearchTmpFolder);
		btnSearchTmpFolder.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
		btnSearchTmpFolder.setText("search...");
		btnSearchTmpFolder.addSelectionListener(this);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		grpDropboxSettings = new Group(composite, SWT.NONE);
		grpDropboxSettings.setFont(SWTResourceManager.getFont("Arial", 18, SWT.NORMAL));
		grpDropboxSettings.setText("Dropbox Settings");
		GridLayout gl_grpDropboxSettings = new GridLayout(3, false);
		gl_grpDropboxSettings.marginTop = 5;
		grpDropboxSettings.setLayout(gl_grpDropboxSettings);
		GridData gd_grpDropboxSettings = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd_grpDropboxSettings.widthHint = 397;
		grpDropboxSettings.setLayoutData(gd_grpDropboxSettings);
		
		 lblDropboxLoginname = new Label(grpDropboxSettings, SWT.NONE);
		 lblDropboxLoginname.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		 lblDropboxLoginname.setText("Dropbox Login");
		 
		 text_DropboxLoginName = new Text(grpDropboxSettings, SWT.BORDER);
		 GridData gd_text_DropboxLoginName = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		 gd_text_DropboxLoginName.widthHint = 187;
		 text_DropboxLoginName.setLayoutData(gd_text_DropboxLoginName);
		 text_DropboxLoginName.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		 new Label(grpDropboxSettings, SWT.NONE);
		 
		  lblPassword_1 = new Label(grpDropboxSettings, SWT.NONE);
		  lblPassword_1.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		  lblPassword_1.setText("Password");
		  
		  text_DropboxPassword = new Text(grpDropboxSettings, SWT.PASSWORD|SWT.BORDER);
		  text_DropboxPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		  text_DropboxPassword.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		  
		  btnTestDropbox= new Button(grpDropboxSettings, SWT.NONE);
		  GridData gd_btnTestDropbox = new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1);
		  gd_btnTestDropbox.widthHint = 78;
		  btnTestDropbox.setLayoutData(gd_btnTestDropbox);
		  btnTestDropbox.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
		  btnTestDropbox.setText("Test");
		  btnTestDropbox.addSelectionListener(this);
		
		
		btnLogin = new Button(composite, SWT.NONE);
		GridData gd_btnLogin = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1);
		gd_btnLogin.widthHint = 97;
		btnLogin.setLayoutData(gd_btnLogin);
		btnLogin.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
		btnLogin.setText("login");
		new Label(composite, SWT.NONE);
		
		lbldroptilllate = new Label(composite, SWT.RIGHT);
		lbldroptilllate.setFont(SWTResourceManager.getFont("Arial", 14, SWT.BOLD));
		lbldroptilllate.setText("\u00A9DropTillLate");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		sashForm.setWeights(new int[] {115, 385});
		
		btnLogin.addSelectionListener(this);
		   //Visible config
//			btnSearchDropFolder.setVisible(false);
//	    	btnSearchTmpFolder.setVisible(false);
//	    	 text_dropboxPath.setVisible(false);
//	    	 text_tempPath.setVisible(false);
//	    	 lblDropboxFolder.setVisible(false);
//	    	 lblTempFolder.setVisible(false);
//	    	btnLogin.setVisible(false);
//	    	btnLogin_1.setVisible(true);
//	    	txtDroptilllate.setVisible(false);
//			lblDroptilllateFoldername.setVisible(false);
//			
//			 text_DropboxPassword.setVisible(false);
//	    	 btnTestDropbox.setVisible(false);
//	    	 text_DropboxLoginName.setVisible(false);
//	    	 lblPassword_1.setVisible(false);
//	    	 lblDropboxLoginname.setVisible(false); 
//	    		btnLogin.setVisible(false);
//		    	btnLogin_1.setVisible(true);
//		    	grpDropboxSettings.setVisible(false);
			//CHECK properties
		checkPropertiesExists();
	  }
	

	/**
	 * Set newUser true if properties not existing
	 */
	private void checkPropertiesExists() {
	    KeyManager km = new KeyManager();
	    if((Configuration.getPropertieDropBoxPath(true) == null)){
	    	//If path not defined
	    	newConfigFile = true;
	    	btnSearchDropFolder.setVisible(true);
	    	btnSearchTmpFolder.setVisible(true);
	    	 text_dropboxPath.setVisible(true);
	    	 text_tempPath.setVisible(true);
	    	 lblDropboxFolder.setVisible(true);
	    	 lblTempFolder.setVisible(true);
	    	btnLogin.setVisible(true);
	    	btnLogin_1.setVisible(false);
	    	txtDroptilllate.setVisible(true);
			lblDroptilllateFoldername.setVisible(true);
			grpDropboxSettings.setVisible(true);
	    	
	  	  	}
	    if (!km.checkIfStructureFileExist()){
	    		  //FileStructure and Masterpassword missing
	    			lblPassword.setText("Create Password");   	    	  
	    	    	this.newUser = true;	  
	    	    	text_DropboxPassword.setVisible(true);
	    	    	btnTestDropbox.setVisible(true);
	    	    	text_DropboxLoginName.setVisible(true);
	    	    	lblPassword_1.setVisible(true);
	    	    	lblDropboxLoginname.setVisible(true); 
	    	    	btnLogin.setVisible(true);
	    		    btnLogin_1.setVisible(false);
	    		    grpDropboxSettings.setVisible(true);
	    	    }
	}


	@Focus
	public void setFocus() {
	}	
    //********************** OPEN Folder Diaolg "*****************************************
		private void openFolderDialog(boolean dropbox) {
			   DirectoryDialog dialog = new DirectoryDialog(shell);
			   if(dropbox == true){
				   dialog.setText("Choose Dropbox Directory");
				   dropboxPath = dialog.open();
				   dropboxPath = dropboxPath + "/";
				   text_dropboxPath.setText(dropboxPath);
				   
			   }
			   else{
				   dialog.setText("Choose Local Temp Directory");
				   tmpPath = dialog.open();
				   text_tempPath.setText(tmpPath);
			   }				   
		}
	

	/**
	 * set Properties
	 * @return true if it worked
	 */
	private boolean setProperties(){
		if(!dropboxPath.isEmpty() && !tmpPath.isEmpty() && !txtDroptilllate.getText().isEmpty() ){
			try {
				dropboxPath = dropboxPath + txtDroptilllate.getText();
				Configuration.setPropertieDropBoxPath(dropboxPath);
				Configuration.setPropertieTempPath(tmpPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();				
				return false;				
			}
			return true;			
		}
		else{
			return false;
		}

	}

	/**
	 * Check Path
	 * @return return true if tmp-path and dropbox-path not equal
	 */
	private boolean checkPath() {
			if(tmpPath.equals(dropboxPath)){
				return false;
			}
			return true;
	}


	public void loginPressed(){
//		if(text_DropboxPassword.getVisible()){
//			dropboxaccountvalidation = testDropboxLogin();
//		}
		//IF Configfile not exist, insert tmp and dropbox path	
		if(newConfigFile){
				if(checkPath()){
					if(!setProperties()){
						new ErrorMessage(shell,"Error","Propertie Error! Check Attributes" );
						return;						
					}
				}
				else{
					new ErrorMessage(shell,"Error","Propertie Error! Check if Temp path and Dropbox path are equals" );
					return;	
				}
			}
			password = text_password.getText();
			KeyManager km = new KeyManager();
			if(!password.isEmpty()){
				if(newUser){	
					dropboxaccountvalidation = testDropboxLogin();
					//Check if all properties are set
					if((Configuration.getPropertieDropBoxPath(true) != null)&&
							(Configuration.getPropertieTempPath(true) !=null)&& 
							dropboxaccountvalidation){
						//CreateFolder
						createFolder();
						km.initPassword(password);
						//Initi CloudAccount
						CloudAccountDao dao = new CloudAccountDao();
						dao.newElement(cloudaccount, null);
						startApplication();
					}
					else{
						new ErrorMessage(shell,"Error","Missing argument" );		
					}				
				}
				else checkPassword();
			}
			else{
				new ErrorMessage(shell,"Error","Missing argument" );	
			}
			
	}
	
	 
	private void createFolder() {  
		  File dir = new File(Configuration.getPropertieDropBoxPath(true)+ Messages.getIdSize());
		  dir.mkdir();	
	}
	
	 /**
	   * Check if Password Exist
	   */
	  public void checkPassword(){ 
		  KeyManager km = new KeyManager();
			if(!km.checkPassword(password, Messages.SaltMasterPassword, Messages.getIdSize())){
				new ErrorMessage(shell,"Error","Wrong Password" );	
			}
			else{
				//Password OK
				startApplication();
			}  
	  
	  }
	  private void startApplication(){
		  MHandledToolItem newFolderHandler = (MHandledToolItem) modelService.find("ch.droptilllate.application.handledtoolitem.newFolder", application);
			newFolderHandler.setVisible(true);
			MHandledToolItem importhandler = (MHandledToolItem) modelService.find("ch.droptilllate.application.handledtoolitem.import", application);
			importhandler.setVisible(true);
			MHandledToolItem logviewhandler = (MHandledToolItem) modelService.find("ch.droptilllate.application.handledtoolitem.logview", application);
			logviewhandler.setVisible(true);
		  MPart part = partService.findPart("ch.droptilllate.application.part.decryptedview");
			part.setVisible(true);
			ViewController.getInstance().initController();
			MPart ownpart = partService.findPart("ch.droptilllate.application.part.InitialView");
			ownpart.setVisible(false);	
	  }
	  
/**
 * Test Dropbox Loing
 * @return true if correct
 */
		private boolean testDropboxLogin() {
			cloudusername=text_DropboxLoginName.getText();
			cloudpassword =text_DropboxPassword.getText();
			if(cloudusername.isEmpty() || cloudpassword.isEmpty()){
				new ErrorMessage(shell, "Error", "Missing Parameters");
				return false;
			}
			else{
				//TODO Test if account correct
				ICloudProviderCom com = new CloudDropboxCom();
				CloudError status = com.testCloudAccount(cloudusername, cloudpassword);
				if(status != CloudError.NONE){
					new ErrorMessage(shell, "Error", status.getMessage());
					return false;
				};
				//NO ERROR
				//new SuccessMessage(shell,"Information", "Dropbox-Login Correct");
				cloudaccount = new CloudAccount(cloudusername, cloudpassword);
				return true; 
			}
		}


	@Override
	public void widgetSelected(SelectionEvent e) {
		if(e.getSource() == btnSearchDropFolder){ 
			openFolderDialog(true);
	        } 	
		if(e.getSource() == btnSearchTmpFolder){ 
			openFolderDialog(false);
        } 
		if(e.getSource() == btnLogin){ 
			loginPressed();
        } 
		if(e.getSource() == btnTestDropbox){ 
			testDropboxLogin();
        }
		if(e.getSource() == btnLogin_1){ 
			loginPressed();
        } 	
		
		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
