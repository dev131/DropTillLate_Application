package ch.droptilllate.application.controller;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.widgets.Shell;

import ch.droptilllate.application.com.CloudDropboxCom;
import ch.droptilllate.application.dao.CloudAccountDao;
import ch.droptilllate.application.dnb.CloudAccount;
import ch.droptilllate.application.info.ErrorMessage;
import ch.droptilllate.application.info.SuccessMessage;
import ch.droptilllate.application.key.KeyManager;
import ch.droptilllate.application.lifecycle.OSValidator;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.cloudprovider.api.ICloudProviderCom;
import ch.droptilllate.cloudprovider.error.CloudError;
import ch.droptilllate.filesystem.preferences.Constants;
import ch.droptilllate.keyfile.api.KeyFileHandlingSummary;
import ch.droptilllate.keyfile.error.KeyFileError;

public class InitController {

	public Boolean newUser = false;;
	private CloudAccount cloudaccount;
	private KeyManager keyManager;
	private Shell shell;
	private static Boolean SUCCESS = true;
	private static Boolean CANCEL = false;
	
	public InitController(Shell shell){
		this.shell = shell;
		keyManager = KeyManager.getInstance();
	}
	/**
	 * Check if Properties are OK
	 * @return true if OK
	 */
	public boolean checkProperties(){
		if(Configuration.getPropertieDropBoxPath(true) == null || Configuration.getPropertieTempPath(false) == null ){
			this.newUser = true;
			return false;
		}
		return true;	
	}
	
	/**
	 * Return false if a failure occured
	 * @param password
	 * @return
	 */
	public boolean login(String password){
		//Checklogin
		KeyFileHandlingSummary sum = keyManager.loadKeyFile(Messages.getApplicationpath(), password);
		if(sum.wrongKey()){
			new ErrorMessage(shell, "error", sum.getKeyFileErrorList().get(0).getError());
			return false;
		};
		keyManager.setKeyrelation(sum.getKeyRelation());
		return true;
		
	}
	
	/**
	 * Return false if a failure occured
	 * @param dropTillLateName
	 * @param password
	 * @param dropboxpath
	 * @param temppath
	 * @return
	 */
	public boolean newUser(String dropTillLateName, String password, String dropboxpath, String temppath, String dropboxLogin, String dropboxpassword, boolean withSharing){
		if(!setProperties(dropboxpath,temppath,dropTillLateName)) return false;
		//Create 100000 folder on Dropbox
		createFolder();
		//init password
		int i = Messages.getIdSize();
		keyManager.addKeyRelation(Messages.getIdSize(), password);
		if(keyManager.saveKeyFile(Messages.getApplicationpath(), password) != KeyFileError.NONE){
			
			return CANCEL;
		}
		//WIthout CloudAccount
		if(!withSharing)return CANCEL;
		//Check Dropboxaccount
		if(!checkDropboxAccount( dropboxLogin, dropboxpassword)){
			return CANCEL;
		}
		//init cloudaccount
		CloudAccountDao dao = new CloudAccountDao();
		if(dao.newElement(cloudaccount, keyManager.getShareRelation(Messages.getIdSize()).getKey())== null){
			return CANCEL;
		}
		//Everything OK
		return SUCCESS;
		
	}
	
	public boolean checkExitError(){
		return SUCCESS;
	}

	/**
	 * Return true if Dropbox/100000.tilllate/100000.xml exist
	 * @return
	 */
	public boolean checkIfFileStructureAvailable() {
		File file = new File(Configuration.getPropertieDropBoxPath(true) + Messages.getIdSize()+ OSValidator.getSlash()+ XMLConstruct.IdXMLContainer+"."+ Constants.CONTAINER_EXTENTION);
		if(file.exists()){
			return true;
		}
		else{
	    	this.newUser = true;
	    	return false;
		}
	}
	
	public boolean setProperties(String dropboxPath, String tempPath, String dropTillLateFolderName){
			try {
				// TODO check for valid dropbox path (no eding slashes)
				dropboxPath = dropboxPath + OSValidator.getSlash() + dropTillLateFolderName;
				Configuration.setPropertieDropBoxPath(dropboxPath);
				Configuration.setPropertieTempPath(tempPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();				
				return false;				
			}
			return true;			
	}
	
	public boolean checkDropboxAccount(String username, String password){
			//TODO Test if account correct
			ICloudProviderCom com = new CloudDropboxCom();
			CloudError status = com.testCloudAccount(username, password);
			cloudaccount = new CloudAccount(username, password);			
			if(status != CloudError.NONE){
				new ErrorMessage(shell, "Error", status.getMessage());
				return false;
			}
				new SuccessMessage(shell, "Success", "Test Successfull");
			
			return true; 
		}
	
	public void insertCloudAccount(){
		CloudAccountDao dao = new CloudAccountDao();
		dao.newElement(cloudaccount, null);
	}
	
	
	private void createFolder() {  
		  File dir = new File(Configuration.getPropertieDropBoxPath(true)+ Messages.getIdSize());
		  dir.mkdir();	
	}
	
	private void openFileStructure(){
		 KeyManager km = new KeyManager();
		 
	}

	/**
	 * return true if the user use the applicatio for the first time
	 * @return
	 */
	public Boolean isNewUser() {
		return newUser;
	}

	
}
