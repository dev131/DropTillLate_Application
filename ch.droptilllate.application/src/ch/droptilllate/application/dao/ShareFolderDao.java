package ch.droptilllate.application.dao;

import ch.droptilllate.application.com.IXmlDatabase;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.query.ShareFolderQuery;

public class ShareFolderDao implements IXmlDatabase {
	private ShareFolderQuery sharefolderQuery;

	@Override
	public Object newElement(Object obj) {
		if (sharefolderQuery == null)
			sharefolderQuery = new ShareFolderQuery();
		 sharefolderQuery.newShareFolder((ShareFolder) obj);
		 return (ShareFolder) obj;
	}

	@Override
	public Object getElementByID(int id) {
		if (sharefolderQuery == null)
			sharefolderQuery = new ShareFolderQuery();		
		return  sharefolderQuery.getShareFolder(id);
	}

	@Override
	public boolean updateElement(Object obj) {
		if (sharefolderQuery == null)
			sharefolderQuery = new ShareFolderQuery();		
		return  sharefolderQuery.updateShareFolder((ShareFolder) obj);	
	}

	@Override
	public boolean deleteElement(Object obj) {
		if (sharefolderQuery == null)
			sharefolderQuery = new ShareFolderQuery();		
		return  sharefolderQuery.deleteShareFolder((ShareFolder) obj);	
	}
}
