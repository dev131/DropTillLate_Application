package ch.droptilllate.application.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.widgets.Shell;

import ch.droptilllate.application.com.CloudDropboxCom;
import ch.droptilllate.application.dnb.CloudAccount;
import ch.droptilllate.application.error.ParamInitException;
import ch.droptilllate.application.exceptions.DatabaseStatus;
import ch.droptilllate.application.info.ErrorMessage;
import ch.droptilllate.application.info.SuccessMessage;
import ch.droptilllate.application.key.KeyManager;
import ch.droptilllate.application.os.OSValidator;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.cloudprovider.api.ICloudProviderCom;
import ch.droptilllate.cloudprovider.error.CloudError;
import ch.droptilllate.database.api.DBSituation;
import ch.droptilllate.database.api.IDatabase;
import ch.droptilllate.database.xml.XMLDatabase;
import ch.droptilllate.filesystem.preferences.Constants;
import ch.droptilllate.keyfile.api.KeyFileHandlingSummary;
import ch.droptilllate.keyfile.error.KeyFileError;

public class InitController
{

	public Boolean newUser = false;;
	private CloudAccount cloudaccount;
	private KeyManager keyManager;
	private Shell shell;
	private static Boolean SUCCESS = true;
	private static Boolean CANCEL = false;

	public InitController(Shell shell)
	{
		this.shell = shell;
		keyManager = KeyManager.getInstance();
	}

	/**
	 * Check if Properties are OK
	 * 
	 * @return true if OK
	 */
	public boolean checkProperties()
	{
		if (Configuration.getPropertieDropBoxPath("", true) == null || Configuration.getPropertieTempPath("", false) == null)
		{
			this.newUser = true;
			return false;
		}
		return true;
	}

	/**
	 * Return false if a failure occured
	 * 
	 * @param password
	 * @return
	 */
	public boolean login(String password) throws ParamInitException
	{
		// Checklogin
		KeyFileHandlingSummary sum = keyManager.loadKeyFile(Messages.getApplicationpath(), password);
		if (sum.wrongKey())
		{
			throw new ParamInitException("Invalid Parameter",
					"The Password is invalid!\nPlease provide the valid password for your account.");
		}
		;
		keyManager.setKeyrelation(sum.getKeyRelation());
		return true;

	}

	/**
	 * Return false if a failure occured
	 * 
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
		//rename existing keyfile
		checkExistingDTLAFiles(Messages.getApplicationpath(), Messages.KeyFile);
		
		keyManager.addKeyRelation(Messages.getIdSize(), password);
		if(keyManager.saveKeyFile(Messages.getApplicationpath(), password) != KeyFileError.NONE){
			
			return CANCEL;
		}
		IDatabase database = new XMLDatabase();
		database.createDatabase(password, "", DBSituation.LOCAL_DATABASE);
		database.openDatabase(password, "", Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		//WIthout CloudAccount
		if(!withSharing) return SUCCESS;
		//init cloudaccount
		database.openTransaction("", DBSituation.LOCAL_DATABASE);
		if(database.createElement(cloudaccount) == null){
			database.closeTransaction("", Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
			return CANCEL;
		}
		//Everything OK
		database.closeTransaction("", Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		return SUCCESS;
		
	}

	private void checkExistingDTLAFiles(String keyFilePath, String keyFileName)
	{
		// TODO Optimise the naming 
		File dataBaseFile = new File(Configuration.getPropertieDropBoxPath("", true) + Messages.getIdSize(), Messages.getIdSize() + ".tilllate");
		if (dataBaseFile.exists()) {
			dataBaseFile.delete();
		}
		File keyFile = new File(keyFilePath, keyFileName);
		try
		{
			if (keyFile.exists())
			{
				Date date =  new Date();
				File newKeyFileName = new File(keyFilePath, keyFileName + new SimpleDateFormat("_yyyy-mm-dd_hh-mm").format(date));
				keyFile.renameTo(newKeyFileName);
			}
		} catch (Exception e)
		{
			keyFile.delete();
		}
	}

	public boolean checkExitError()
	{
		return SUCCESS;
	}

	/**
	 * Return true if Dropbox/100000.tilllate/100000.xml exist
	 * 
	 * @return
	 */
	public boolean checkIfFileStructureAvailable()
	{
		File file = new File(Configuration.getPropertieDropBoxPath("", true) + Messages.getIdSize() + OSValidator.getSlash()
				+ XMLConstruct.IdXMLContainer + "." + Constants.CONTAINER_EXTENTION);
		if (file.exists())
		{
			return true;
		} else
		{
			this.newUser = true;
			return false;
		}
	}

	public boolean setProperties(String dropboxPath, String tempPath, String dropTillLateFolderName)
	{
		if (dropTillLateFolderName == null)
		{

		} else
		{
			dropboxPath = dropboxPath + OSValidator.getSlash() + dropTillLateFolderName;
		}
		try
		{
			// TODO check for valid dropbox path (no eding slashes)
			Configuration.setPropertieDropBoxPath(dropboxPath, "");
			Configuration.setPropertieTempPath(tempPath, "");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean checkDropboxAccount(String username, String password)
	{
		// TODO Test if account correct
		ICloudProviderCom com = new CloudDropboxCom();
		CloudError status = com.testCloudAccount(username, password);
		cloudaccount = new CloudAccount(username, password);
		if (status != CloudError.NONE)
		{
			new ErrorMessage(shell, "Error", "Could not verifiy your Dropbox account\nError message: " + status.getMessage());
			return false;
		}
		new SuccessMessage(shell, "Success", "Your Dropbox Account is valid, please proceed!");

		return true;
	}

	private void createFolder()
	{
		File dir = new File(Configuration.getPropertieDropBoxPath("", true) + Messages.getIdSize());
		dir.mkdir();
	}

	private void openFileStructure()
	{
		KeyManager km = new KeyManager();

	}

	/**
	 * return true if the user use the applicatio for the first time
	 * 
	 * @return
	 */
	public Boolean isNewUser()
	{
		return newUser;
	}

	public boolean useExistingAccount(String password, String dropboxPath, String tempPath, String dropboxPassword)
	{
		if (!setProperties(dropboxPath, tempPath, null))
			return false;
		return true;
	}

}
