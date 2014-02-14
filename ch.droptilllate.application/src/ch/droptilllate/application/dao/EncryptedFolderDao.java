package ch.droptilllate.application.dao;


import java.util.ArrayList;
import java.util.List;

import ch.droptilllate.application.com.IXmlDatabase;
import ch.droptilllate.application.dnb.EncryptedFolder;
import ch.droptilllate.application.model.EncryptedFolderDob;
import ch.droptilllate.application.query.FolderQuery;

public class EncryptedFolderDao implements IXmlDatabase {

	private FolderQuery folderquery;

	public List<EncryptedFolderDob> getFoldersForFolder(
			EncryptedFolderDob folder) {
		if (folderquery == null)
			folderquery = new FolderQuery();

		List<EncryptedFolderDob> folders = new ArrayList<EncryptedFolderDob>();
		folders = folderquery.getFolder(folder);

		return folders;
	}

	@Override
	public Object newElement(Object obj) {
		EncryptedFolderDob encryptedFolderDob = null;
		if (folderquery == null)
			folderquery = new FolderQuery();
		int id = folderquery.newFolder((EncryptedFolder) obj);
		encryptedFolderDob = new EncryptedFolderDob(id,(EncryptedFolder) obj);
		return encryptedFolderDob;
	}

	@Override
	public Object getElementByID(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateElement(Object obj) {
		boolean successful = false;
		if (folderquery == null)
			folderquery = new FolderQuery();
		successful = folderquery.updateFolder((EncryptedFolderDob) obj);
		return successful;
	}

	@Override
	public boolean deleteElement(Object obj) {
		List<EncryptedFolderDob> folderList = (List<EncryptedFolderDob>) obj;
		boolean successful = false;
		if (folderquery == null)
			folderquery = new FolderQuery();
		for (int i = 0; i < folderList.size(); i++) {
			successful = folderquery.deleteFolder(folderList.get(i));
		}
		return successful;
	}

}
