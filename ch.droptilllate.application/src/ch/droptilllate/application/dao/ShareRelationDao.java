package ch.droptilllate.application.dao;

import java.util.Iterator;
import java.util.List;

import ch.droptilllate.application.com.AbstractXmlDatabase;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.query.ShareFolderQuery;
import ch.droptilllate.application.query.ShareRelationQuery;

public class ShareRelationDao extends AbstractXmlDatabase {

	private ShareRelationQuery shareRelationQuery;

	@Override
	public Object newElement(Object obj, String key) {
		if (shareRelationQuery == null)
			shareRelationQuery = new ShareRelationQuery(key);		 
		 return shareRelationQuery.newShareRelation((ShareRelation) obj);
	}

	@Override
	public Object getElementByID(int id, String key) {
		if (shareRelationQuery == null)
			shareRelationQuery = new ShareRelationQuery(key);		
		return  shareRelationQuery.getShareRelations(id);
	}

	@Override
	public void updateElement(Object obj, String key) {
		if (shareRelationQuery == null)
			shareRelationQuery = new ShareRelationQuery(key);		
		shareRelationQuery.updateShareRelation((ShareRelation) obj);	
	}

	@Override
	public void deleteElement(Object obj, String key) {
		if (shareRelationQuery == null)
			shareRelationQuery = new ShareRelationQuery(key);
			shareRelationQuery.deleteShareFolder((ShareRelation) obj);
	}

	@Override
	public Object checkDatabase(Object obj, String key) {
		if (shareRelationQuery == null)
			shareRelationQuery = new ShareRelationQuery(key);		
		return shareRelationQuery.checkDatabase((List<ShareRelation>) obj);
	}

	@Override
	public Object getElementAll(String key) {
		if (shareRelationQuery == null)
			shareRelationQuery = new ShareRelationQuery(key);		
		return shareRelationQuery.getAll();
	}

}
