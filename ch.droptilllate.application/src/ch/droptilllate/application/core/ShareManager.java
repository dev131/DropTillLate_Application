package ch.droptilllate.application.core;

import java.util.List;

import ch.droptilllate.application.com.FileSystemCom;
import ch.droptilllate.application.com.IFileSystemCom;
import ch.droptilllate.application.com.IXmlDatabase;
import ch.droptilllate.application.dao.ShareFolderDao;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.views.Messages;

public class ShareManager {

	public void newShareRelation(List<EncryptedFileDob> fileList, String password){
		KeyManager km = new KeyManager();
		String key = "";
		//Insert newShareFolder In DB
		IXmlDatabase shareDao = new ShareFolderDao();
		ShareFolder sharedFolder = new ShareFolder(0, "", "");
		sharedFolder = (ShareFolder) shareDao.newElement(sharedFolder);
		key = km.generatePassword(password, sharedFolder.getPath());
		sharedFolder.setPath(Messages.getLocalPathDropbox() + Integer.toString(sharedFolder.getID()));		 
		sharedFolder.setKey(key);
		shareDao.updateElement(sharedFolder);
		//TODO create new ShareFolder
		IFileSystemCom iFile = new FileSystemCom();
		iFile.moveFiles(fileList, sharedFolder);
		//TODO move files
		
		
	}
}
