package ch.droptilllate.application.share;


import java.io.File;

import ch.droptilllate.application.dao.ShareFolderDao;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.lifecycle.OSValidator;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.filesystem.preferences.Constants;

public class KeyManager {

	/**
	 * Init MasterPassword
	 * @param password
	 */
	public void initPassword(String password){
		ShareFolderDao dao = new ShareFolderDao();
		ShareFolder	folder = new ShareFolder(Messages.getIdSize(),password );
		dao.newElement(folder, password);
	}
	
	/**
	 * Check if master password exist
	 * @return true if exist
	 */
	public boolean checkMasterPasswordExisting(){
		ShareFolderDao dao = new ShareFolderDao();
		ShareFolder folder = (ShareFolder) dao.getElementByID(Messages.getIdSize(), null);
		if(folder == null ){
			return false;
		}
		return true;
	}
	/**
	 * Check existing password, true when it match
	 * @param password
	 * @return true if it match
	 */
	public boolean checkPassword(String password,String salt, int shareID){
		Boolean exist = false;
		ShareFolderDao dao = new ShareFolderDao();
		ShareFolder folder = (ShareFolder) dao.getElementByID(shareID, password);
		if(folder != null){
			if(folder.getKey().equals(password))exist= true;	
		}	
		return exist;
	}
	
	/**
	 * Return true if Dropbox/100000.xml exist
	 * @return
	 */
	public boolean checkIfStructureFileExist() {
		File file = new File(Configuration.getPropertieDropBoxPath(true) + Messages.getIdSize()+ OSValidator.getSlash()+ XMLConstruct.IdXMLContainer+"."+ Constants.CONTAINER_EXTENTION);
		return file.exists();
	}
	

}
