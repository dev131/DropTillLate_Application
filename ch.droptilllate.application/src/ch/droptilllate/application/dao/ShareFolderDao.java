package ch.droptilllate.application.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.droptilllate.application.com.AbstractXmlDatabase;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.query.FileQuery;
import ch.droptilllate.application.query.GhostFolderQuery;
import ch.droptilllate.application.query.ShareFolderQuery;

public class ShareFolderDao extends AbstractXmlDatabase {
	private ShareFolderQuery sharefolderQuery;

	@Override
	public Object newElement(Object obj, String key) {
		if (sharefolderQuery == null)
			sharefolderQuery = new ShareFolderQuery(key);		 
		 return sharefolderQuery.newShareFolder((ShareFolder) obj);
	}

	@Override
	public Object getElementByID(int id, String key) {
		if (sharefolderQuery == null)
			sharefolderQuery = new ShareFolderQuery(key);		
		return  sharefolderQuery.getShareFolder(id);
	}

	@Override
	public void updateElement(Object obj, String key) {
		if (sharefolderQuery == null)
			sharefolderQuery = new ShareFolderQuery(key);		
		sharefolderQuery.updateShareFolder((ShareFolder) obj);	
	}

	@Override
	public void deleteElement(Object obj, String key) {
		if (sharefolderQuery == null)
			sharefolderQuery = new ShareFolderQuery(key);
			sharefolderQuery.deleteShareFolder((List<ShareFolder>) obj);
	
	}

	@Override
	public Object checkDatabase(Object obj, String key) {
		if (sharefolderQuery == null)
			sharefolderQuery = new ShareFolderQuery(key);		
		return sharefolderQuery.checkDatabase((List<ShareFolder>) obj);
	}

	@Override
	public Object getElementAll(String key) {
		// TODO Auto-generated method stub
		return null;
	}
}
