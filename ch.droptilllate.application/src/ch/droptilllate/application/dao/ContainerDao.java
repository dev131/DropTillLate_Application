package ch.droptilllate.application.dao;

import java.util.List;

import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.query.ContainerQuery;
import ch.droptilllate.application.xml.AbstractXmlDatabase;


public class ContainerDao extends AbstractXmlDatabase {
	private ContainerQuery containerQuery;

	@Override
	public Object newElement(Object obj, String key) {
		if (this.containerQuery == null)
			this.containerQuery = new ContainerQuery(key);
		this.containerQuery.newContainer((EncryptedContainer) obj);
		return obj;
		
	}

	@Override
	public Object getElementByID(int id, String key) {
		if (this.containerQuery == null)
			this.containerQuery = new ContainerQuery(key);
		return this.containerQuery.getContainerByID(id);	
	}

	@Override
	public void updateElement(Object obj, String key) {
		if (this.containerQuery == null)
			this.containerQuery = new ContainerQuery(key);
		this.containerQuery.updateContainer((EncryptedContainer) obj);	
	}
	@Override
	public void deleteElement(Object obj, String key) {
		if (this.containerQuery == null)
			this.containerQuery = new ContainerQuery(key);
			this.containerQuery.deleteContainer((List<EncryptedContainer>) obj);

	}

	@Override
	public Object checkDatabase(Object obj, String key) {
		if (this.containerQuery == null)
			this.containerQuery = new ContainerQuery(key);		
		return this.containerQuery.checkDatabase((List<EncryptedContainer>) obj);
	}
	public Object getContainerByShareRelationId(Integer shareRelationId, String key){
		if (this.containerQuery == null)
			this.containerQuery = new ContainerQuery(key);		
		return this.containerQuery.getContainerBySharedRelationId(shareRelationId);
	}

	@Override
	public Object getElementAll(String key) {
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
