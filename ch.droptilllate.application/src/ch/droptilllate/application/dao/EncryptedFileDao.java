package ch.droptilllate.application.dao;

import java.util.ArrayList;
import java.util.List;

import ch.droptilllate.application.com.IXmlDatabase;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.query.ContainerQuery;
import ch.droptilllate.application.query.FileQuery;


public class EncryptedFileDao implements IXmlDatabase {

	private FileQuery filequery;

	public List<EncryptedFileDob> getFilesInFolder(GhostFolderDob folder)
	{
		if (filequery == null)
			filequery = new FileQuery();
		List<EncryptedFileDob> files = new ArrayList<EncryptedFileDob>();
		files = filequery.getFiles(folder);
		return files;
	}

	@Override
	public Object newElement(Object obj) {
		if (filequery == null)
			filequery = new FileQuery();

		return filequery.newFile((EncryptedFileDob) obj);
	}

	@Override
	public Object getElementByID(int id) {
		EncryptedFileDob encryptedFileDob = null;
		if(filequery == null)
				filequery = new FileQuery();		
		 encryptedFileDob = filequery.getFile(id);
		return encryptedFileDob;
	}

	@Override
	public void updateElement(Object obj) {
		if(filequery == null)
			filequery = new FileQuery();	
		filequery.updateFile((EncryptedFileDob) obj);
	}

	@Override
	public void deleteElement(Object obj) {
		if (filequery == null)
			filequery = new FileQuery();
			filequery.deleteFile((List<EncryptedFileDob>) obj);
}

	@Override
	public Object checkDatabase(Object obj) {
		if (filequery == null)
			filequery = new FileQuery();		
		return filequery.checkDatabase((List<EncryptedFileDob>) obj);
	}
	
	public Object getFileIdsByContainerId(Integer id){
		if (filequery == null)
			filequery = new FileQuery();	
		return filequery.getFileIdsByContainerId(id);
	}

}
