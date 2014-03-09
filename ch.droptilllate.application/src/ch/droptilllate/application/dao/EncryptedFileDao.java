package ch.droptilllate.application.dao;

import java.util.ArrayList;
import java.util.List;

import ch.droptilllate.application.com.AbstractXmlDatabase;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.query.ContainerQuery;
import ch.droptilllate.application.query.FileQuery;


public class EncryptedFileDao extends AbstractXmlDatabase {

	private FileQuery filequery;

	public List<EncryptedFileDob> getFilesInFolder(GhostFolderDob folder, String key)
	{
		if (filequery == null)
			filequery = new FileQuery(key);
		List<EncryptedFileDob> files = new ArrayList<EncryptedFileDob>();
		files = filequery.getFiles(folder);
		return files;
	}

	@Override
	public Object newElement(Object obj, String key) {
		if (filequery == null)
			filequery = new FileQuery(key);

		return filequery.newFile((EncryptedFileDob) obj);
	}

	@Override
	public Object getElementByID(int id, String key) {
		EncryptedFileDob encryptedFileDob = null;
		if(filequery == null)
				filequery = new FileQuery(key);		
		 encryptedFileDob = filequery.getFile(id);
		return encryptedFileDob;
	}

	@Override
	public void updateElement(Object obj, String key) {
		if(filequery == null)
			filequery = new FileQuery(key);	
		filequery.updateFile((EncryptedFileDob) obj);
	}

	@Override
	public void deleteElement(Object obj, String key) {
		if (filequery == null)
			filequery = new FileQuery(key);
			filequery.deleteFile((List<EncryptedFileDob>) obj);
}

	@Override
	public Object checkDatabase(Object obj, String key) {
		if (filequery == null)
			filequery = new FileQuery(key);		
		return filequery.checkDatabase((List<EncryptedFileDob>) obj);
	}
	
	public Object getFileIdsByContainerId(Integer id, String key){
		if (filequery == null)
			filequery = new FileQuery(key);	
		return filequery.getFileIdsByContainerId(id);
	}
	
	public List<EncryptedFileDob> getFileByContainerId(Integer containerid, String key){
		if (filequery == null)
			filequery = new FileQuery(key);	
		return filequery.getFileByContainerId(containerid);
	}

	@Override
	public Object getElementAll(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
