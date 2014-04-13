package ch.droptilllate.application.dao;


import java.util.List;

import ch.droptilllate.application.dnb.ShareMember;
import ch.droptilllate.application.query.ShareMemberQuery;
import ch.droptilllate.application.xml.AbstractXmlDatabase;

public class ShareMembersDao extends AbstractXmlDatabase {

	private ShareMemberQuery shareMemberQuery;

	@Override
	public Object newElement(Object obj, String key) {
		if (this.shareMemberQuery == null)
			this.shareMemberQuery = new ShareMemberQuery(key);			
		 return this.shareMemberQuery.newShareMembers((ShareMember) obj);
	}

	@Override
	public Object getElementByID(int id, String key) {
		if (this.shareMemberQuery == null)
			this.shareMemberQuery = new ShareMemberQuery(key);		
		return  this.shareMemberQuery.getShareRelations(id);
	}

	@Override
	public void updateElement(Object obj, String key) {
		if (this.shareMemberQuery == null)
			this.shareMemberQuery = new ShareMemberQuery(key);			
		this.shareMemberQuery.updateShareRelationMember((ShareMember) obj);	
	}

	@Override
	public void deleteElement(Object obj, String key) {
		if (this.shareMemberQuery == null)
			this.shareMemberQuery = new ShareMemberQuery(key);
			this.shareMemberQuery.deleteShareFolder((ShareMember) obj);
	}

	@Override
	public Object checkDatabase(Object obj, String key) {
		if (this.shareMemberQuery == null)
			this.shareMemberQuery = new ShareMemberQuery(key);		
		return this.shareMemberQuery.checkDatabase((List<ShareMember>) obj);
	}

	@Override
	public Object getElementAll(String key) {
		if (this.shareMemberQuery == null)
			this.shareMemberQuery = new ShareMemberQuery(key);		
		return this.shareMemberQuery.getAll();
	}

	@Override
	public void deleteElementAll(String key) {
		// TODO Auto-generated method stub
		
	}

}
