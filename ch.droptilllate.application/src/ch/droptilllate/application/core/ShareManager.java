package ch.droptilllate.application.core;

import java.util.List;

import ch.droptilllate.application.com.FileSystemCom;
import ch.droptilllate.application.com.IFileSystemCom;
import ch.droptilllate.application.com.IXmlDatabase;
import ch.droptilllate.application.dao.EncryptedFileDao;
import ch.droptilllate.application.dao.ShareFolderDao;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.info.CRUDCryptedFileResult;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.views.Messages;
import ch.droptilllate.application.views.Status;

public class ShareManager {

	/**
	 * Create new ShareFolder and Move fileList into it
	 * @param fileList
	 * @param password
	 */
	public void newShareRelation(List<EncryptedFileDob> fileList, String password){
		KeyManager km = new KeyManager();
		String key = null;
		//Check if they are All Files from Shared Folder 0
		
		//Check if they are All Files from one Shared Folder x no files left
		
		//Check if not All Files from same SharedFolder but not all
		
		//Check if files from different shareFolder
		
		//Create and insert newShareFolder in DB and create Id
		IXmlDatabase shareDao = new ShareFolderDao();
		ShareFolder sharedFolder = new ShareFolder(null, Messages.getPathDropBox(), null);		
		sharedFolder = (ShareFolder) shareDao.newElement(sharedFolder);		
		key = km.generatePassword(password, sharedFolder.getPath());		 
		sharedFolder.setKey(key);
		shareDao.updateElement(sharedFolder);
		//Move Files
		IFileSystemCom iFile = new FileSystemCom();
		CRUDCryptedFileResult result = iFile.moveFiles(fileList, sharedFolder);
		//Handle Error
		for(EncryptedFileDob fileDob : result.getEncryptedFileListError()){
			Status status = Status.getInstance();
			status.setMessage(fileDob.getName() + " -> sharing not worked");
		}
		//Update Database
		for(EncryptedFileDob fileDob : result.getEncryptedFileListSuccess()){
			IXmlDatabase fileDB = new EncryptedFileDao();
			fileDB.updateElement(fileDob);
		}
		//
		
	}
}
