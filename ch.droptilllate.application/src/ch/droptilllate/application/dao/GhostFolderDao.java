package ch.droptilllate.application.dao;

import java.util.ArrayList;
import java.util.List;

import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.query.GhostFolderQuery;
import ch.droptilllate.application.xml.AbstractXmlDatabase;

public class GhostFolderDao extends AbstractXmlDatabase {

	private GhostFolderQuery folderquery;

	public List<GhostFolderDob> getFoldersInFolder(
			GhostFolderDob folder, String key) {
		if (folderquery == null)
			folderquery = new GhostFolderQuery(key);
		List<GhostFolderDob> folders = new ArrayList<GhostFolderDob>();
		folders = folderquery.getFolderInFolder(folder);
		return folders;
	}

	@Override
	public Object newElement(Object obj, String key) {
		if (folderquery == null)
			folderquery = new GhostFolderQuery(key);
		return  folderquery.newFolder((GhostFolderDob) obj);
	}

	@Override
	public Object getElementByID(int id, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateElement(Object obj, String key) {
		if (folderquery == null)
			folderquery = new GhostFolderQuery(key);		
		folderquery.updateFolder((GhostFolderDob) obj);
	}

	@Override
	public void deleteElement(Object obj, String key) {	
		if (folderquery == null)
			folderquery = new GhostFolderQuery(key);
			folderquery.deleteFolder((List<GhostFolderDob>) obj);	
	}

	@Override
	public Object checkDatabase(Object obj, String key) {
		if (folderquery == null)
			folderquery = new GhostFolderQuery(key);		
		return folderquery.checkDatabase((List<GhostFolderDob>) obj);
		
	}

	@Override
	public Object getElementAll(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
