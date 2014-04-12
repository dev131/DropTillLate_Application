package ch.droptilllate.application.views;


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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import ch.droptilllate.application.controller.InitController;
import ch.droptilllate.application.controller.ViewController;
import ch.droptilllate.application.info.ErrorMessage;
import ch.droptilllate.application.info.SuccessMessage;
import ch.droptilllate.cloudprovider.error.CloudError;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Group;

public class InitialView implements SelectionListener, ModifyListener {

	@Inject EPartService partService;
	@Inject EModelService modelService;
	@Inject MApplication application;
	private InitController controller;
	private Text text_tempPath;
	private Text text_dropboxPath;
	private Text text_password;
	private  Button btnSearchDropFolder;
	private Button btnSearchTmpFolder;
	private Button btnLogin;
	private Label lblPassword;
	private Shell shell;
	private Text text_DropboxLoginName;
	private Text text_DropboxPassword;
	private Button btnTestDropbox;
	private Label lblPassword_1;
	private Label lblDropboxLoginname;
	private Label lblDropboxFolder ;
	private Label lblTempFolder;
	private Button btnLogin_1;
	private Label lbldroptilllate;
	private Label lblDroptilllateFoldername;
	private Text txtDroptilllate;
	private Group grpDroptilllateSettings;
	private Group grpDropboxSettings;
	
	//STRINGS
	private String dropboxfoldername;
	private String password;
	private String dropboxLogin;
	private String dropboxPassword;
	private String dropboxPath;
	private String tempPath;
	
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
		
		CLabel lblDroptilllate = new CLabel(composite_1, SWT.CENTER);
		GridData gd_lblDroptilllate = new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1);
		gd_lblDroptilllate.widthHint = 686;
		gd_lblDroptilllate.heightHint = 104;
		lblDroptilllate.setLayoutData(gd_lblDroptilllate);
		lblDroptilllate.setAlignment(SWT.CENTER);
		lblDroptilllate.setText("");
		lblDroptilllate.setImage(image.createImage());
		
		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD | SWT.ITALIC));
		composite.setLayout(new GridLayout(3, false));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		grpDroptilllateSettings = new Group(composite, SWT.NONE);
		grpDroptilllateSettings.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		grpDroptilllateSettings.setFont(SWTResourceManager.getFont("Arial", 18, SWT.NORMAL));
		GridLayout gl_grpDroptilllateSettings = new GridLayout(3, false);
		gl_grpDroptilllateSettings.marginTop = 5;
		grpDroptilllateSettings.setLayout(gl_grpDroptilllateSettings);
		GridData gd_grpDroptilllateSettings = new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1);
		gd_grpDroptilllateSettings.widthHint = 393;
		grpDroptilllateSettings.setLayoutData(gd_grpDroptilllateSettings);
		grpDroptilllateSettings.setText("DropTillLate Settings");
		
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
		
		lblDropboxFolder = new Label(grpDroptilllateSettings, SWT.NONE);
		lblDropboxFolder.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		lblDropboxFolder.setText("Dropbox path ");
		
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
		
		lblTempFolder = new Label(grpDroptilllateSettings, SWT.NONE);
		lblTempFolder.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		lblTempFolder.setText("Temp path");
		
		text_tempPath = new Text(grpDroptilllateSettings, SWT.BORDER);
		text_tempPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		text_tempPath.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		
		btnSearchTmpFolder = new Button(grpDroptilllateSettings, SWT.NONE);
		GridData gd_btnSearchTmpFolder = new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1);
		gd_btnSearchTmpFolder.widthHint = 75;
		btnSearchTmpFolder.setLayoutData(gd_btnSearchTmpFolder);
		btnSearchTmpFolder.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
		btnSearchTmpFolder.setText("search...");
		btnSearchTmpFolder.addSelectionListener(this);
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
		 GridData gd_lblDropboxLoginname = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		 gd_lblDropboxLoginname.minimumWidth = 200;
		 lblDropboxLoginname.setLayoutData(gd_lblDropboxLoginname);
		 lblDropboxLoginname.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		 lblDropboxLoginname.setText("Dropbox Login ");
		 
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
		new Label(composite, SWT.NONE);
		
		lbldroptilllate = new Label(composite, SWT.RIGHT);
		lbldroptilllate.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
		
		
		btnLogin = new Button(composite, SWT.NONE);
		GridData gd_btnLogin = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1);
		gd_btnLogin.widthHint = 97;
		btnLogin.setLayoutData(gd_btnLogin);
		btnLogin.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
		btnLogin.setText("login");
		
		btnLogin.addSelectionListener(this);
		sashForm.setWeights(new int[] {115, 385});
		   //Visible config
			btnSearchDropFolder.setVisible(false);
	    	btnSearchTmpFolder.setVisible(false);
	    	 text_dropboxPath.setVisible(false);
	    	 text_tempPath.setVisible(false);
	    	 lblDropboxFolder.setVisible(false);
	    	 lblTempFolder.setVisible(false);
	    	btnLogin.setVisible(false);
	    	btnLogin_1.setVisible(true);
	    	txtDroptilllate.setVisible(false);
			lblDroptilllateFoldername.setVisible(false);
			
			 text_DropboxPassword.setVisible(false);
	    	 btnTestDropbox.setVisible(false);
	    	 text_DropboxLoginName.setVisible(false);
	    	 lblPassword_1.setVisible(false);
	    	 lblDropboxLoginname.setVisible(false); 
	    		btnLogin.setVisible(false);
		    	btnLogin_1.setVisible(true);
		    	grpDropboxSettings.setVisible(false);
			//CHECK properties
		init();
	  }
	
	
	private void init(){
		dropboxfoldername = "DropTillLate";
		controller = new InitController(shell);
		 //Check if config files and filestructure file are available
		 if(!controller.checkProperties() || !controller.checkIfFileStructureAvailable()){
			 makePathPropertiesVisible();
		 }
		//Check if an Exit error exist	 
		 controller.checkExitError();	
		 
			text_DropboxPassword.addModifyListener(this);
			text_DropboxLoginName.addModifyListener(this);
			text_dropboxPath.addModifyListener(this);
			text_tempPath.addModifyListener(this);
			txtDroptilllate.addModifyListener(this);
			text_password.addModifyListener(this);
	}


	private void makePathPropertiesVisible() {
		// TODO Auto-generated method stub
		//If path not defined
		lblPassword.setText("Create Password");   	    	    
    	text_DropboxPassword.setVisible(true);
    	btnTestDropbox.setVisible(true);
    	text_DropboxLoginName.setVisible(true);
    	lblPassword_1.setVisible(true);
    	lblDropboxLoginname.setVisible(true); 
    	btnLogin.setVisible(true);
	    btnLogin_1.setVisible(false);
    	btnSearchDropFolder.setVisible(true);
    	btnSearchTmpFolder.setVisible(true);
    	 text_dropboxPath.setVisible(true);
    	 text_tempPath.setVisible(true);
    	 lblDropboxFolder.setVisible(true);
    	 lblTempFolder.setVisible(true);
    	btnLogin.setVisible(true);
    	txtDroptilllate.setVisible(true);
		lblDroptilllateFoldername.setVisible(true);
		grpDropboxSettings.setVisible(true);
    	
	}
	
	@Focus
	public void setFocus() {
	}	
    //********************** OPEN Folder Diaolg "*****************************************
		private void openFolderDialog(boolean dropbox) {
			   DirectoryDialog dialog = new DirectoryDialog(shell);
			   if(dropbox == true){
				   dialog.setText("Choose Dropbox Directory");
				   text_dropboxPath.setText(dialog.open());
				   
			   }
			   else{
				   dialog.setText("Choose Local Temp Directory");
				   text_tempPath.setText(dialog.open());
			   }				   
		}
		
	/**
	 * Check Path
	 * @return return true if equals
	 */
	private boolean checkPathofEquals() {
			if(tempPath.equals(dropboxPath)){
				new ErrorMessage(shell, "Error", "Dropbox-Path and Temp-Path can not be equals");
				return true;
			}
			return false;
	}


	public void loginPressed(){
		
		if(controller.getNewUser()){
			//Check if all fields are not empty
			if(checkAllFields()){
				if(controller.newUser(dropboxfoldername,  password,dropboxPath, tempPath, dropboxLogin, dropboxPassword)){
					startApplication();
				}
			}	
		}
		else{
			if(!password.isEmpty()){
				//Check if Login successful
				if(controller.login(password)){
					startApplication();
				}
			}
			else{
				new ErrorMessage(shell, "Error", "No Password");
			}
			
		}
		
		
			
	}
	
	  private boolean checkAllFields() {

		 if(dropboxPassword.isEmpty() || 
				 dropboxfoldername.isEmpty() ||
				 dropboxPath.isEmpty() ||
				 tempPath.isEmpty() ||
				  dropboxLogin.isEmpty() ||
				  password.isEmpty() ||
				  checkPathofEquals()
				 ){
			 new ErrorMessage(shell, "Error", "Missing Argument");
			 return false;
		 }
		return true;
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
			controller.checkDropboxAccount(dropboxLogin, dropboxPassword);			
        }
		if(e.getSource() == btnLogin_1){ 
			loginPressed();
        } 	
		
		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void modifyText(ModifyEvent e) {

		if(e.getSource() == text_DropboxPassword){ 
			dropboxPassword = text_DropboxPassword.getText();
	        } 	
		if(e.getSource() == text_DropboxLoginName){ 
			dropboxLogin = text_DropboxLoginName.getText();
        } 
		if(e.getSource() == text_dropboxPath){ 
			dropboxPath = text_dropboxPath.getText();
        } 
		if(e.getSource() == text_tempPath){ 
			tempPath = text_tempPath.getText();
        }
		if(e.getSource() == txtDroptilllate){ 
			dropboxfoldername = txtDroptilllate.getText();
        } 	
		if(e.getSource() == text_password){ 
			password = text_password.getText();
        } 	
		
	}
}
