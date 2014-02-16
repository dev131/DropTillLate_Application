package ch.droptilllate.application.dao;

import java.util.ArrayList;
import java.util.List;

import ch.droptilllate.application.com.IXmlDatabase;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.query.GhostFolderQuery;

public class GhostFolderDao implements IXmlDatabase {

	private GhostFolderQuery folderquery;

	public List<GhostFolderDob> getFoldersInFolder(
			GhostFolderDob folder) {
		if (folderquery == null)
			folderquery = new GhostFolderQuery();
		List<GhostFolderDob> folders = new ArrayList<GhostFolderDob>();
		folders = folderquery.getFolderInFolder(folder);
		return folders;
	}

	@Override
	public Object newElement(Object obj) {
		if (folderquery == null)
			folderquery = new GhostFolderQuery();
		return  folderquery.newFolder((GhostFolderDob) obj);
	}

	@Override
	public Object getElementByID(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateElement(Object obj) {
		if (folderquery == null)
			folderquery = new GhostFolderQuery();		
		folderquery.updateFolder((GhostFolderDob) obj);
	}

	@Override
	public void deleteElement(Object obj) {	
		if (folderquery == null)
			folderquery = new GhostFolderQuery();
			folderquery.deleteFolder((List<GhostFolderDob>) obj);	
	}

	@Override
	public Object checkDatabase(Object obj) {
		if (folderquery == null)
			folderquery = new GhostFolderQuery();		
		return folderquery.checkDatabase((List<GhostFolderDob>) obj);
		
	}

}
