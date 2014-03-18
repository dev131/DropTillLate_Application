package ch.droptilllate.application.dao;


import java.util.List;


import ch.droptilllate.application.dnb.ShareRelation;

import ch.droptilllate.application.query.ShareRelationQuery;
import ch.droptilllate.application.xml.AbstractXmlDatabase;

public class ShareRelationDao extends AbstractXmlDatabase {

	private ShareRelationQuery shareRelationQuery;

	@Override
	public Object newElement(Object obj, String key) {
		if (this.shareRelationQuery == null)
			this.shareRelationQuery = new ShareRelationQuery(key);		 
		 return this.shareRelationQuery.newShareRelation((ShareRelation) obj);
	}

	@Override
	public Object getElementByID(int id, String key) {
		if (this.shareRelationQuery == null)
			this.shareRelationQuery = new ShareRelationQuery(key);		
		return  this.shareRelationQuery.getShareRelations(id);
	}

	@Override
	public void updateElement(Object obj, String key) {
		if (this.shareRelationQuery == null)
			this.shareRelationQuery = new ShareRelationQuery(key);		
		this.shareRelationQuery.updateShareRelation((ShareRelation) obj);	
	}

	@Override
	public void deleteElement(Object obj, String key) {
		if (this.shareRelationQuery == null)
			this.shareRelationQuery = new ShareRelationQuery(key);
			this.shareRelationQuery.deleteShareFolder((ShareRelation) obj);
	}

	@Override
	public Object checkDatabase(Object obj, String key) {
		if (this.shareRelationQuery == null)
			this.shareRelationQuery = new ShareRelationQuery(key);		
		return this.shareRelationQuery.checkDatabase((List<ShareRelation>) obj);
	}

	@Override
	public Object getElementAll(String key) {
		if (this.shareRelationQuery == null)
			this.shareRelationQuery = new ShareRelationQuery(key);		
		return this.shareRelationQuery.getAll();
	}

}
