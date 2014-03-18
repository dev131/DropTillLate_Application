package ch.droptilllate.application.dao;



import java.util.List;





import ch.droptilllate.application.dnb.ShareFolder;




import ch.droptilllate.application.query.ShareFolderQuery;
import ch.droptilllate.application.xml.AbstractXmlDatabase;

public class ShareFolderDao extends AbstractXmlDatabase {
	private ShareFolderQuery sharefolderQuery;

	@Override
	public Object newElement(Object obj, String key) {
		if (this.sharefolderQuery == null)
			this.sharefolderQuery = new ShareFolderQuery(key);		 
		 return this.sharefolderQuery.newShareFolder((ShareFolder) obj);
	}

	@Override
	public Object getElementByID(int id, String key) {
		if (this.sharefolderQuery == null)
			this.sharefolderQuery = new ShareFolderQuery(key);		
		return  this.sharefolderQuery.getShareFolder(id);
	}

	@Override
	public void updateElement(Object obj, String key) {
		if (this.sharefolderQuery == null)
			this.sharefolderQuery = new ShareFolderQuery(key);		
		this.sharefolderQuery.updateShareFolder((ShareFolder) obj);	
	}

	@Override
	public void deleteElement(Object obj, String key) {
		if (this.sharefolderQuery == null)
			this.sharefolderQuery = new ShareFolderQuery(key);
			this.sharefolderQuery.deleteShareFolder((List<ShareFolder>) obj);
	
	}

	@Override
	public Object checkDatabase(Object obj, String key) {
		if (this.sharefolderQuery == null)
			this.sharefolderQuery = new ShareFolderQuery(key);		
		return this.sharefolderQuery.checkDatabase((List<ShareFolder>) obj);
	}

	@Override
	public Object getElementAll(String key) {
		// TODO Auto-generated method stub
		return null;
	}
}
