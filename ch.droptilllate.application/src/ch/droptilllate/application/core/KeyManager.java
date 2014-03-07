package ch.droptilllate.application.core;


import java.io.File;

import ch.droptilllate.application.com.AbstractXmlDatabase;
import ch.droptilllate.application.dao.ShareFolderDao;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.views.Messages;
import ch.droptilllate.application.views.XMLConstruct;
import ch.droptilllate.filesystem.commons.Constants;

public class KeyManager {

	/**
	 * Init MasterPassword
	 * @param password
	 */
	public void initPassword(String password, String salt){
		KeysGenerator kg = new KeysGenerator();
		ShareFolderDao dao = new ShareFolderDao();
		String key  = kg.getKey(password, salt);
		ShareFolder	folder = new ShareFolder(Integer.parseInt(Messages.getShareFolder0name()), Messages.getPathDropBox(),key );
		dao.newElement(folder, key);
	}
	
	/**
	 * Check if master password exist
	 * @return true if exist
	 */
	public boolean checkMasterPasswordExisting(){
		ShareFolderDao dao = new ShareFolderDao();
		ShareFolder folder = (ShareFolder) dao.getElementByID(Integer.parseInt(Messages.getShareFolder0name()), null);
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
	public boolean checkPassword(String password, String salt, int shareID){
		Boolean exist = false;
		KeysGenerator kg = new KeysGenerator();
		ShareFolderDao dao = new ShareFolderDao();
		ShareFolder folder = (ShareFolder) dao.getElementByID(shareID, kg.getKey(password, salt));
		if(folder != null){
			if(folder.getKey().equals(kg.getKey(password, salt)))exist= true;	
		}	
		return exist;
	}
	
	/**
	 * 
	 * @param password
	 * @param salt
	 * @return key
	 */
	public String generatePassword(String password, String salt){
		KeysGenerator kg = new KeysGenerator();
		return kg.getKey(password, salt);
	}

	/**
	 * Return true if Dropbox/100000.xml exist
	 * @return
	 */
	public boolean checkIfStructureFileExist() {
		File file = new File(Messages.getPathDropBox() + Messages.getShareFolder0name()+Messages.getSlash()+ XMLConstruct.getIdXMLContainer()+"."+ Constants.CONTAINER_EXTENTION);
		if(file.exists()){
			return true;
		}
		else{
			return false;
		}
	}
	

}
