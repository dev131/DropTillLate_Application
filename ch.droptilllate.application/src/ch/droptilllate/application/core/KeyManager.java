package ch.droptilllate.application.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

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
		ShareFolderDao dao = new ShareFolderDao();
		ShareFolder	folder = new ShareFolder(0, Messages.getLocalPathDropBoxMaster(), kg.getKey(password, salt));
		dao.newElement(folder);
	}
	
	/**
	 * Check if master password exist
	 * @return true if exist
	 */
	public boolean checkMasterPasswordExisting(){
		ShareFolderDao dao = new ShareFolderDao();
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
	public boolean checkPassword(String password, String salt){
		KeysGenerator kg = new KeysGenerator();
		ShareFolderDao dao = new ShareFolderDao();
		ShareFolder folder = (ShareFolder) dao.getElementByID(0);
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
