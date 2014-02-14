package ch.droptilllate.application.dao;


import java.util.ArrayList;
import java.util.List;

import ch.droptilllate.application.com.IXmlDatabase;
import ch.droptilllate.application.dnb.EncryptedFile;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.EncryptedFolderDob;
import ch.droptilllate.application.query.FileQuery;


public class EncryptedFileDao implements IXmlDatabase {

	private FileQuery filequery;

	public List<EncryptedFileDob> getFilesForFolder(EncryptedFolderDob folder)
	{
		if (filequery == null)
			filequery = new FileQuery();
		List<EncryptedFileDob> files = new ArrayList<EncryptedFileDob>();
		files = filequery.getFile(folder);
		return files;
	}

	@Override
	public Object newElement(Object obj) {
		EncryptedFileDob encryptedFileDob = null;
		if (filequery == null)
			filequery = new FileQuery();
		int id = filequery.newFile((EncryptedFile) obj);

		encryptedFileDob = new EncryptedFileDob(id, (EncryptedFile) obj);
		return encryptedFileDob;
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
	public boolean updateElement(Object obj) {
		boolean successful = false;
		if(filequery == null)
			filequery = new FileQuery();	
		successful = filequery.updateFile((EncryptedFileDob) obj);
		return successful;
	}

	@Override
	public boolean deleteElement(Object obj) {
		List<EncryptedFileDob> fileList = (List<EncryptedFileDob>) obj;
		boolean successful = false;
		if(filequery == null)
			filequery = new FileQuery();
		for(int i=0; i< fileList.size(); i++){
			successful = filequery.deleteFile(fileList.get(i));
		}		
		return successful;
	}

}
