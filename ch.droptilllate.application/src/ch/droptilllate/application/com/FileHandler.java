package ch.droptilllate.application.com;

import java.util.List;

import ch.droptilllate.application.dao.ContainerDao;
import ch.droptilllate.application.dao.EncryptedFileDao;
import ch.droptilllate.application.dao.EncryptedFolderDao;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.EncryptedFile;
import ch.droptilllate.application.dnb.GhostFolder;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;

public class FileHandler {

	public GhostFolderDob newFolderDBEntry(GhostFolder newfolder){
		IXmlDatabase folderDao = new EncryptedFolderDao();
		return (GhostFolderDob) folderDao.newElement(newfolder);
	}
	
	public EncryptedFileDob newFileDBEntry(EncryptedFile newFile){
		IXmlDatabase fileDao = new EncryptedFileDao();
		return (EncryptedFileDob) fileDao.newElement(newFile);
	}
	
	public CRUDCryptedFileResult decryptFiles(List<EncryptedFileDob> fileList, String path){
		FileSystemCom ifileSystem = new FileSystemCom();
		return ifileSystem.decryptFile(fileList, path);
	}
	
	public CRUDCryptedFileResult encryptFiles(List<EncryptedFileDob> fileList, String containerpath){
		FileSystemCom ifileSystem = new FileSystemCom();
		return ifileSystem.encryptFile(fileList, containerpath);
	}
	
	public CRUDCryptedFileResult deleteFilesOnFileSystem(List<EncryptedFileDob> fileList){
		FileSystemCom ifileSystem = new FileSystemCom();
		return ifileSystem.deleteFile(fileList);
	} 
	
	public void deleteFilesOnDatabase(List<EncryptedFileDob> fileList){
		// Send to Dao
		IXmlDatabase fileDao = new EncryptedFileDao();
		fileDao.deleteElement(fileList);	
	}
	
	public void deleteFolderOnDatabase(List<GhostFolderDob> folderList){
		// Send to Dao
		IXmlDatabase folderDao = new EncryptedFolderDao();
		folderDao.deleteElement(folderList);
	}
	
	public void updateFileOnDatabase(EncryptedFileDob fileDob){
		IXmlDatabase fileDao = new EncryptedFileDao();
		fileDao.updateElement(fileDob);
	}
	
	public void newContainer(EncryptedContainer container){
		IXmlDatabase containerDao = new ContainerDao();
		containerDao.newElement(container);
	}
}
