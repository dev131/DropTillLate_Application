package ch.droptilllate.application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.droptilllate.application.com.IXmlDatabase;
import ch.droptilllate.application.dnb.EncryptedFile;
import ch.droptilllate.application.dnb.GhostFolder;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.query.FileQuery;
import ch.droptilllate.application.query.GhostFolderQuery;


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
		EncryptedFile tmp = filequery.newFile((EncryptedFile) obj);
		EncryptedFileDob dob = new EncryptedFileDob(tmp);
		return dob;
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

}
