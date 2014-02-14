package ch.droptilllate.application.dao;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.droptilllate.application.com.IXmlDatabase;
import ch.droptilllate.application.dnb.Container;
import ch.droptilllate.application.query.ContainerQuery;
import ch.droptilllate.application.query.ShareFolderQuery;

public class ContainerDao implements IXmlDatabase {
	ContainerQuery containerQuery;

	@Override
	public Object newElement(Object obj) {
		if (containerQuery == null)
			containerQuery = new ContainerQuery();
		containerQuery.newContainer((Container) obj);
		return (Container) obj;
		
	}

	@Override
	public Object getElementByID(int id) {
		if (containerQuery == null)
			containerQuery = new ContainerQuery();
		return containerQuery.getContainerByID(id);	
	}

	@Override
	public boolean updateElement(Object obj) {
		if (containerQuery == null)
			containerQuery = new ContainerQuery();
		return containerQuery.updateContainer((Container) obj);	
	}
	@Override
	public boolean deleteElement(Object obj) {
		if (containerQuery == null)
			containerQuery = new ContainerQuery();
		return containerQuery.deleteContainer((Container) obj);
	}

}
