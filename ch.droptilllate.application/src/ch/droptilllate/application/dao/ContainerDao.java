package ch.droptilllate.application.dao;


import ch.droptilllate.application.com.IXmlDatabase;
import ch.droptilllate.application.dnb.Container;
import ch.droptilllate.application.query.ContainerQuery;


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
