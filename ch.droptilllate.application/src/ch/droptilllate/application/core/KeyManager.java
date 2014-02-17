package ch.droptilllate.application.core;


import ch.droptilllate.application.com.IXmlDatabase;
import ch.droptilllate.application.dao.ShareFolderDao;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.views.Messages;

public class KeyManager {

	/**
	 * Init MasterPassword
	 * @param password
	 */
	public void initPassword(String password, String salt){
		KeysGenerator kg = new KeysGenerator();
		IXmlDatabase dao = new ShareFolderDao();
		ShareFolder	folder = new ShareFolder(Integer.parseInt(Messages.getShareFolder0name()), Messages.getPathDropBox() + Messages.getShareFolder0name(), kg.getKey(password, salt));
		dao.newElement(folder);
	}
	
	/**
	 * Check if master password exist
	 * @return true if exist
	 */
	public boolean checkMasterPasswordExisting(){
		IXmlDatabase dao = new ShareFolderDao();
		ShareFolder folder = (ShareFolder) dao.getElementByID(0);
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
		KeysGenerator kg = new KeysGenerator();
		IXmlDatabase dao = new ShareFolderDao();
		ShareFolder folder = (ShareFolder) dao.getElementByID(shareID);
		if(folder.getKey().equals(kg.getKey(password, salt)))
			return true;
		else{
			return false;
		}			
	}
	
	public String generatePassword(String password, String salt){
		KeysGenerator kg = new KeysGenerator();
		return kg.getKey(password, salt);
	}
	

}
