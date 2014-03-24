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
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.share.KeyManager;
import ch.droptilllate.couldprovider.api.ICloudProviderCom;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;

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
	private Label lblInformationForSharing;
	private Label lblDropboxFolder ;
	private Label lblTempFolder;
	private CloudAccount cloudaccount = null;
	private String password;
	private String cloudusername;
	private String cloudpassword;
	private Label lblWelcomeToDroptilllate;
	private Button btnLogin_1;
	private Label lbldroptilllate;
	private Label lblDroptilllateFoldername;
	private Text txtDroptilllate;
	private String dropboxPath ="";
	private String tmpPath="";
	@PostConstruct
	public void createControls(Composite parent, Shell shell, EPartService partService, EModelService modelService, MApplication application) {
		
		this.shell = shell;
		Bundle bundle = FrameworkUtil.getBundle(TreeView.class);
		URL url = FileLocator.find(bundle, new Path("icons/icon_128x128.png"), null);
		ImageDescriptor image = ImageDescriptor.createFromURL(url);
    	
    	SashForm sashForm = new SashForm(parent, SWT.NONE);
    	sashForm.setLocation(0, 0);
		
		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		
		CLabel lblDroptilllate = new CLabel(composite_1, SWT.NONE);
		lblDroptilllate.setAlignment(SWT.CENTER);
		lblDroptilllate.setBounds(39, 53, 194, 153);
		lblDroptilllate.setText("");
		lblDroptilllate.setImage(image.createImage());
		
		lbldroptilllate = new Label(composite_1, SWT.NONE);
		lbldroptilllate.setFont(SWTResourceManager.getFont("Lucida Grande", 16, SWT.NORMAL));
		lbldroptilllate.setBounds(82, 247, 120, 34);
		lbldroptilllate.setText("\u00A9DropTillLate");
		
		Composite composite = new Composite(sashForm, SWT.NONE);
		
		text_password = new Text(composite,SWT.PASSWORD| SWT.BORDER);
		text_password.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		text_password.setBounds(131, 56, 125, 19);
		
		lblPassword = new Label(composite, SWT.NONE);
		lblPassword.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.NORMAL));
		lblPassword.setBounds(10, 56, 129, 17);
		lblPassword.setText("Password");
		
		text_dropboxPath = new Text(composite, SWT.BORDER);
		text_dropboxPath.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		text_dropboxPath.setBounds(131, 124, 125, 19);
		
		lblDropboxFolder = new Label(composite, SWT.NONE);
		lblDropboxFolder.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.NORMAL));
		lblDropboxFolder.setBounds(10, 125, 115, 19);
		lblDropboxFolder.setText("Dropbox path");
		btnSearchDropFolder = new Button(composite, SWT.NONE);
		btnSearchDropFolder.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		btnSearchDropFolder.setBounds(264, 123, 78, 28);
		btnSearchDropFolder.setText("search ...");
		
		btnSearchTmpFolder = new Button(composite, SWT.NONE);
		btnSearchTmpFolder.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		btnSearchTmpFolder.setBounds(264, 160, 78, 28);
		btnSearchTmpFolder.setText("search...");
		
		text_tempPath = new Text(composite, SWT.BORDER);
		text_tempPath.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		text_tempPath.setBounds(131, 163, 125, 19);
		
		
		btnLogin = new Button(composite, SWT.NONE);
		btnLogin.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		btnLogin.setBounds(226, 371, 103, 28);
		btnLogin.setText("login");
		
		lblTempFolder = new Label(composite, SWT.NONE);
		lblTempFolder.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.NORMAL));
		lblTempFolder.setBounds(13, 162, 104, 19);
		lblTempFolder.setText("Temp path");
		
		text_DropboxLoginName = new Text(composite, SWT.BORDER);
		text_DropboxLoginName.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		text_DropboxLoginName.setBounds(120, 245, 125, 19);
		
		 lblDropboxLoginname = new Label(composite, SWT.NONE);
		 lblDropboxLoginname.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.NORMAL));
		lblDropboxLoginname.setBounds(10, 245, 115, 19);
		lblDropboxLoginname.setText("Dropbox Login");
		
		 lblInformationForSharing = new Label(composite, SWT.NONE);
		 lblInformationForSharing.setFont(SWTResourceManager.getFont("Lucida Grande", 16, SWT.BOLD));
		lblInformationForSharing.setBounds(10, 208, 200, 19);
		lblInformationForSharing.setText("Information for Sharing:");
		
		 lblPassword_1 = new Label(composite, SWT.NONE);
		 lblPassword_1.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.NORMAL));
		lblPassword_1.setBounds(10, 291, 78, 17);
		lblPassword_1.setText("Password");
		
		text_DropboxPassword = new Text(composite, SWT.PASSWORD|SWT.BORDER);
		text_DropboxPassword.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		text_DropboxPassword.setBounds(120, 291, 125, 19);
		
		btnTestDropbox= new Button(composite, SWT.NONE);
		btnTestDropbox.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		btnTestDropbox.setBounds(251, 287, 72, 28);
		btnTestDropbox.setText("Test");
		
		lblWelcomeToDroptilllate = new Label(composite, SWT.NONE);
		lblWelcomeToDroptilllate.setFont(SWTResourceManager.getFont("Lucida Grande", 20, SWT.BOLD));
		lblWelcomeToDroptilllate.setBounds(10, 10, 336, 23);
		lblWelcomeToDroptilllate.setText("Welcome to DropTillLate");
		
		btnLogin_1 = new Button(composite, SWT.NONE);
		btnLogin_1.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		btnLogin_1.setBounds(262, 52, 84, 28);
		btnLogin_1.setText("login");
		
		lblDroptilllateFoldername = new Label(composite, SWT.NONE);
		lblDroptilllateFoldername.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.NORMAL));
		lblDroptilllateFoldername.setBounds(10, 81, 184, 27);
		lblDroptilllateFoldername.setText("Foldername");
		
		txtDroptilllate = new Text(composite, SWT.BORDER);
		txtDroptilllate.setText("DropTillLate");
		txtDroptilllate.setBounds(130, 81, 126, 19);
		btnLogin_1.addSelectionListener(this);
		btnTestDropbox.addSelectionListener(this);
		
		btnLogin.addSelectionListener(this);
		btnSearchTmpFolder.addSelectionListener(this);
		btnSearchDropFolder.addSelectionListener(this);
		
    	//Invisible
    	btnSearchDropFolder.setVisible(false);
    	btnSearchTmpFolder.setVisible(false);
    	 text_dropboxPath.setVisible(false);
    	 text_tempPath.setVisible(false);
    	 text_DropboxPassword.setVisible(false);
    	 btnTestDropbox.setVisible(false);
    	 text_DropboxLoginName.setVisible(false);
    	 lblPassword_1.setVisible(false);
    	 lblDropboxLoginname.setVisible(false);
    	 lblInformationForSharing.setVisible(false);
    	 lblDropboxFolder.setVisible(false);
    	 lblTempFolder.setVisible(false);
		btnLogin.setVisible(false);
		txtDroptilllate.setVisible(false);
		lblDroptilllateFoldername.setVisible(false);
		sashForm.setWeights(new int[] {271, 414});
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
	    	    	 lblInformationForSharing.setVisible(true); 
	    	    		btnLogin.setVisible(true);
	    		    	btnLogin_1.setVisible(false);
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

	private boolean checkPath() {
			if(tmpPath.equals(dropboxPath)){
				return false;
			}
			return true;
	}


	public void loginPressed(){
		//IF Configfile not exist, insert tmp and dropbox path	
		if(newConfigFile){
				if(checkPath()){
					if(!setProperties()){
						new ErrorMessage(shell,"Error","Propertie Error! Check if Temp path and Dropbox path are equals" );
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
					testDropboxLogin();
					//Check if all properties are set
					if((Configuration.getPropertieDropBoxPath(true) != null)&&
							(Configuration.getPropertieTempPath(true) !=null)&& 
							cloudaccount !=null){
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
	  

		private void testDropboxLogin() {
			cloudusername=text_DropboxLoginName.getText();
			cloudpassword =text_DropboxPassword.getText();
			if(cloudusername.isEmpty() || cloudpassword.isEmpty()){
				new ErrorMessage(shell, "Error", "Missing Parameters");
			}
			else{
				//TODO Test if account correct
				ICloudProviderCom com = new CloudDropboxCom();
				com.testCloudAccount(cloudusername, cloudpassword);
				//ERROR
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
