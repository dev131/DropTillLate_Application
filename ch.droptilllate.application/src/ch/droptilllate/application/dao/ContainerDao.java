package ch.droptilllate.application.dao;

import java.util.List;
import ch.droptilllate.application.com.AbstractXmlDatabase;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.query.ContainerQuery;


public class ContainerDao extends AbstractXmlDatabase {
	ContainerQuery containerQuery;

	@Override
	public Object newElement(Object obj, String key) {
		if (containerQuery == null)
			containerQuery = new ContainerQuery(key);
		containerQuery.newContainer((EncryptedContainer) obj);
		return (EncryptedContainer) obj;
		
	}

	@Override
	public Object getElementByID(int id, String key) {
		if (containerQuery == null)
			containerQuery = new ContainerQuery(key);
		return containerQuery.getContainerByID(id);	
	}

	@Override
	public void updateElement(Object obj, String key) {
		if (containerQuery == null)
			containerQuery = new ContainerQuery(key);
		containerQuery.updateContainer((EncryptedContainer) obj);	
	}
	@Override
	public void deleteElement(Object obj, String key) {
		if (containerQuery == null)
			containerQuery = new ContainerQuery(key);
			containerQuery.deleteContainer((List<EncryptedContainer>) obj);

	}

	@Override
	public Object checkDatabase(Object obj, String key) {
		if (containerQuery == null)
			containerQuery = new ContainerQuery(key);		
		return containerQuery.checkDatabase((List<EncryptedContainer>) obj);
	}
	public Object getContainerBySharedFolderId(Integer id, String key){
		if (containerQuery == null)
			containerQuery = new ContainerQuery(key);		
		return containerQuery.getContainerBySharedFolderId(id);
	}

}
