package ch.droptilllate.application.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;







import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.info.CRUDContainerInfo;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.application.xml.XmlConnection;

public class ContainerQuery {
	private XmlConnection conn;
	public ContainerQuery(String key) {
		this.conn = new XmlConnection(true, key);
	}

	/**
	 * New Container entry
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public void newContainer(EncryptedContainer container) {		
		
		if (container.getId() == null) {
			int id = (int) (Math.random() * Messages.getIdSize() + 1);
			// Check if it exist
			while (checkExist(id)) {
				id = (int) (Math.random() * Messages.getIdSize() + 1);
			}
			// if now container ID -> Alert! it have to be one
			container.setId(id);
		}
		else{
			if(getContainerByID(container.getId())!= null){
				return;
			}
		}
		Document document = this.conn.getXML();
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.RootElementContainer);
		Node node = nodelist.item(0);
		//GetNodeList by name
		for(int i=0; i<nodelist.getLength(); i++){
			  Node childNode = nodelist.item(i);
			  if (childNode.getNodeName().equals(XMLConstruct.RootElementContainer)) {
			     node = nodelist.item(i);
			  }
			}
		Element element = document.createElement(XMLConstruct.ChildElementContainer);
		element.setAttribute(XMLConstruct.AttId, Integer.toString(container.getId()));
		element.setIdAttribute(XMLConstruct.AttId, true);
		element.setAttribute(XMLConstruct.AttShareFolderId,
				Integer.toString(container.getShareFolderId()));
		node.appendChild(element);
		this.conn.writeToXML();
	}
	private boolean checkExist(int containerId) {
		this.conn.getXML();
		boolean result = false;
		// cast the result to a DOM NodeList
		NodeList nodes = this.conn.executeQuery(XMLConstruct.getContainerExpression()+ "[@"+XMLConstruct.AttId+"='"
				+ containerId + "']");
		if (nodes.getLength() > 0)
			result = true;
		return result;
	}

	public EncryptedContainer getContainerByID(int id) {
		this.conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = this.conn.executeQuery(XMLConstruct.getContainerExpression()+ "[@"+XMLConstruct.AttId+"='" + id
				+ "']");
		EncryptedContainer container = null;
		if (nodes.getLength() > 0) {
			container = new EncryptedContainer(id, Integer.parseInt(nodes.item(0)
					.getAttributes().getNamedItem(XMLConstruct.AttShareFolderId)
					.getNodeValue()));
		}
		return container;
	}

	/**
	 * UpdateFolder
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public boolean updateContainer(EncryptedContainer container) {
		this.conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = this.conn.executeQuery(XMLConstruct.getContainerExpression() + "[@"+XMLConstruct.AttId+"='"
				+ container.getId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx)
					.getAttributes()
					.getNamedItem(XMLConstruct.AttShareFolderId)
					.setNodeValue(
							Integer.toString(container.getShareFolderId()));
		}
		System.out.println("Everything updated.");
		// save xml file back
		this.conn.writeToXML();
		return true;
	}

	/**
	 * DeleteFolder
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public boolean deleteContainer(List<EncryptedContainer> container) {
		Document document = this.conn.getXML();
		for(EncryptedContainer container1 : container){
		NodeList nodes = this.conn.executeQuery(XMLConstruct.getContainerExpression() + "[@"+XMLConstruct.AttId+"='"
				+ container1.getId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getParentNode().removeChild(nodes.item(idx));
		}
		}
		this.conn.writeToXML();
		return true;
	}

	public CRUDContainerInfo checkDatabase(List<EncryptedContainer> containerList) {
		Document document = conn.getXML();
		List<EncryptedContainer> encryptedContainerListSuccess = new ArrayList<EncryptedContainer>();
		List<EncryptedContainer> encryptedContainerListError = new ArrayList<EncryptedContainer>();
		Iterator<EncryptedContainer> ContainerInfoListErrorIterator = containerList.iterator();
		while (ContainerInfoListErrorIterator.hasNext()) {
			NodeList nodes = conn.executeQuery(XMLConstruct.getContainerExpression()+ "[@"+XMLConstruct.AttId+"='"
					+ ContainerInfoListErrorIterator.next().getId() + "']");
			if (nodes.getLength() > 0) {
				encryptedContainerListSuccess.add(ContainerInfoListErrorIterator.next());
			} else {
				encryptedContainerListError.add(ContainerInfoListErrorIterator.next());
			}
		}
		CRUDContainerInfo result = new CRUDContainerInfo();
		result.setEncryptedContainerListError(encryptedContainerListError);
		result.setEncryptedContainerListSuccess(encryptedContainerListSuccess);
		return result;
	}

	public Object getContainerBySharedFolderId(Integer id) {
		conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery(XMLConstruct.getContainerExpression()+ "[@"+XMLConstruct.AttShareFolderId+"='" + id
				+ "']");
		ArrayList<EncryptedContainer> containerList = new ArrayList<EncryptedContainer>();
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			EncryptedContainer container = new EncryptedContainer(
					Integer.parseInt(nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttId).getNodeValue()),
					Integer.parseInt(nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttShareFolderId).getNodeValue())
					);
			containerList.add(container);
		}
		return containerList;
	}

}
