package ch.droptilllate.application.dao;

import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.droptilllate.application.com.AbstractXmlDatabase;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.query.ContainerQuery;
import ch.droptilllate.application.query.GhostFolderQuery;
import ch.droptilllate.application.query.ShareFolderQuery;

public class ContainerDao extends AbstractXmlDatabase {
	ContainerQuery containerQuery;

	@Override
	public Object newElement(Object obj) {
		if (containerQuery == null)
			containerQuery = new ContainerQuery();
		containerQuery.newContainer((EncryptedContainer) obj);
		return (EncryptedContainer) obj;
		
	}

	@Override
	public Object getElementByID(int id) {
		if (containerQuery == null)
			containerQuery = new ContainerQuery();
		return containerQuery.getContainerByID(id);	
	}

	@Override
	public void updateElement(Object obj) {
		if (containerQuery == null)
			containerQuery = new ContainerQuery();
		containerQuery.updateContainer((EncryptedContainer) obj);	
	}
	@Override
	public void deleteElement(Object obj) {
		if (containerQuery == null)
			containerQuery = new ContainerQuery();
			containerQuery.deleteContainer((List<EncryptedContainer>) obj);

	}

	@Override
	public Object checkDatabase(Object obj) {
		if (containerQuery == null)
			containerQuery = new ContainerQuery();		
		return containerQuery.checkDatabase((List<EncryptedContainer>) obj);
	}
	public Object getContainerBySharedFolderId(Integer id){
		if (containerQuery == null)
			containerQuery = new ContainerQuery();		
		return containerQuery.getContainerBySharedFolderId(id);
	}

}
