package ch.droptilllate.application.key;


import java.io.File;

import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.os.OSValidator;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.filesystem.preferences.Constants;
import ch.droptilllate.keyfile.api.IKeyFile;
import ch.droptilllate.keyfile.api.KeyFileHandler;
import ch.droptilllate.keyfile.api.KeyFileHandlingSummary;
import ch.droptilllate.keyfile.error.KeyFileError;
import ch.droptilllate.security.commons.KeyRelation;

public class KeyManager {
	
	public KeyRelation keyrelation;
	private static KeyManager instance = null;

	public KeyManager(){
	}
	
	 public static KeyManager getInstance() {
	      if(instance == null) {
	         instance = new KeyManager();
	      }
	      return instance;
	   }
	
	/**
	 * Open the KeyFile and fill the KeyRelation Object
	 * @param keyFilePath
	 * @param passphrase
	 * @return false if key is wrong
	 */
	public KeyFileHandlingSummary loadKeyFile(String keyFilePath,String passphrase){
		keyFilePath = keyFilePath + OSValidator.getSlash()+Messages.KeyFile;
		KeysGenerator gen = new KeysGenerator();
		String key = gen.getKey(passphrase, Messages.getIdSize().toString());
		IKeyFile iKeyFile = new KeyFileHandler();
		return iKeyFile.loadKeyFile(keyFilePath, key);
	}

	
	/**
	 * Save key file
	 * @param keyFilePath
	 * @param passphrase
	 * @return true = no error
	 */
	public KeyFileError saveKeyFile(String keyFilePath, String passphrase){
		keyFilePath = keyFilePath + OSValidator.getSlash()+Messages.KeyFile;
		KeysGenerator gen = new KeysGenerator();
		String key = gen.getKey(passphrase, Messages.getIdSize().toString());
		IKeyFile iKeyFile = new KeyFileHandler();	
		return iKeyFile.storeKeyFile(keyFilePath, key, keyrelation);
	}
	
	private String getKeyByShareRelationID(Integer shareRelationId){	
		KeysGenerator gen = new KeysGenerator();
		return gen.getKey(keyrelation.getKeyOfShareRelation(shareRelationId), shareRelationId.toString());
	}
	
	/**
	 * Return ShareRelation with key as a hash if true/ without if false
	 * @param shareRelationID
	 * @return ShareRelation
	 */
	public ShareRelation getShareRelation(Integer ShareRelationID, boolean withhash){
		ShareRelation shareRelation;
		if(withhash){
		KeysGenerator gen = new KeysGenerator();
		 shareRelation = new ShareRelation(ShareRelationID, gen.getKey(keyrelation.getKeyOfShareRelation(ShareRelationID), ShareRelationID.toString()));
		}
		else{
		shareRelation = new ShareRelation(ShareRelationID, keyrelation.getKeyOfShareRelation(ShareRelationID));
		}
		return shareRelation;
	}
	
	/**
	 * Create new ShareRelation
	 * @param passphrase
	 * @param shareRelation if ID is set create specified shareRelation
	 * @return ShareRelation
	 */
	public ShareRelation newShareRelation(String passphrase, Integer shareRelationID){
		if (shareRelationID == null) {
			shareRelationID = (int) (Math.random() * Messages.getIdSize() + 1);
			// Check if it exist
			while (checkIfShareFolderExist(shareRelationID)) {
			shareRelationID = (int) (Math.random() * Messages.getIdSize() + 1);
			}
		}
		ShareRelation shareRelation = new ShareRelation(shareRelationID, passphrase);
		addKeyRelation(shareRelation.getID(), shareRelation.getKey());
		return shareRelation;
	}
	
	private boolean checkIfShareFolderExist(int shareRelationID) {
		String i = keyrelation.getKeyOfShareRelation(shareRelationID);
		if(i == null){
			return false;
		}
		return true;
	}

	/**
	 * Add keyrelation with plain password (not hash)
	 * @param shareRelationID
	 * @param passphrase
	 */
	public void addKeyRelation(Integer shareRelationID, String passphrase){	
		if(keyrelation == null){
			keyrelation = new KeyRelation();
		}
		keyrelation.addKeyOfShareRelation(shareRelationID, passphrase);
	}

	public KeyRelation getKeyrelation() {
		return keyrelation;
	}
	
	public KeyRelation getKeyrelationWithHash(){
		KeyRelation hashKeyrelation = new KeyRelation();
		for(Integer i : keyrelation.getKeyShareMap().keySet()){
			ShareRelation shareRelation = getShareRelation(i, true);
			hashKeyrelation.addKeyOfShareRelation(shareRelation.getID(), shareRelation.getKey());
		}
		return hashKeyrelation;	
	}
	

	public void setKeyrelation(KeyRelation keyrelation) {
		this.keyrelation = keyrelation;
	}
	
	

}
