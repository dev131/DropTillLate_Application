package ch.droptilllate.application.dao;

import ch.droptilllate.application.dnb.CloudAccount;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.query.CloudAccountQuery;
import ch.droptilllate.application.query.ContainerQuery;
import ch.droptilllate.application.xml.AbstractXmlDatabase;

public class CloudAccountDao extends AbstractXmlDatabase {
	private CloudAccountQuery cloudAccountQuery;
	@Override
	public Object newElement(Object obj, String key) {
		if (this.cloudAccountQuery == null)
			this.cloudAccountQuery = new CloudAccountQuery(key);
		return this.cloudAccountQuery.newCloudAccount((CloudAccount) obj);
	}

	@Override
	public Object getElementByID(int id, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getElementAll(String key) {
		if (this.cloudAccountQuery == null)
			this.cloudAccountQuery = new CloudAccountQuery(key);
		return this.cloudAccountQuery.getAccount();
	}

	@Override
	public void updateElement(Object obj, String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteElement(Object obj, String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object checkDatabase(Object obj, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteElementAll(String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getElementbyName(String name, String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
