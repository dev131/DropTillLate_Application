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
		if (this.folderquery == null)
			this.folderquery = new GhostFolderQuery(key);
		List<GhostFolderDob> folders = new ArrayList<GhostFolderDob>();
		folders = this.folderquery.getFolderInFolder(folder);
		return folders;
	}

	@Override
	public Object newElement(Object obj, String key) {
		if (this.folderquery == null)
			this.folderquery = new GhostFolderQuery(key);
		return  this.folderquery.newFolder((GhostFolderDob) obj);
	}

	@Override
	public Object getElementByID(int id, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateElement(Object obj, String key) {
		if (this.folderquery == null)
			this.folderquery = new GhostFolderQuery(key);		
		this.folderquery.updateFolder((GhostFolderDob) obj);
	}

	@Override
	public void deleteElement(Object obj, String key) {	
		if (this.folderquery == null)
			this.folderquery = new GhostFolderQuery(key);
			this.folderquery.deleteFolder((List<GhostFolderDob>) obj);	
	}

	@Override
	public Object checkDatabase(Object obj, String key) {
		if (this.folderquery == null)
			this.folderquery = new GhostFolderQuery(key);		
		return this.folderquery.checkDatabase((List<GhostFolderDob>) obj);
		
	}

	@Override
	public Object getElementAll(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
