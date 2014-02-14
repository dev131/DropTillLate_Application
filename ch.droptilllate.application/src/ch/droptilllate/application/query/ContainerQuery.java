package ch.droptilllate.application.query;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.droptilllate.application.com.IXmlConnection;
import ch.droptilllate.application.com.XmlConnection;
import ch.droptilllate.application.dnb.Container;
import ch.droptilllate.application.views.Messages;

public class ContainerQuery {
	private Document document;
	private IXmlConnection conn;
	private String rootElement = "collection";
	private String childElement = "file";
	public ContainerQuery() {
		conn = new XmlConnection(Messages.getContainerXMLpath(), rootElement);		
	}
	/**
	 * New Container entry
	 * @param encryptedFolder
	 * @return
	 */
	public void newContainer(Container container) {
		document = conn.getXML();
		Node node = document.getFirstChild();
		Element element = document.createElement(childElement);
		element.setAttribute("id", Integer.toString(container.getID()));
		element.setIdAttribute("id", true);
		element.setAttribute("shareFolderID", Integer.toString(container.getShareFolderID()));
		node.appendChild(element);
		conn.writeToXML();
	}

	public Container getContainerByID(int id){
		document = conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery("//"+childElement+"[@id='" + id + "']");
		Container container = new Container(id, Integer.parseInt(nodes.item(0).getAttributes().getNamedItem("shareFolderID").getNodeValue()));
		return container;
	}
/**
 * UpdateFolder
 * @param encryptedFolder
 * @return
 */
	public boolean updateContainer(Container container) {
		document = conn.getXML();
			// cast the result to a DOM NodeList
			NodeList nodes = conn.executeQuery("//"+childElement+"[@id='" + container.getID()
					+ "']");
			for (int idx = 0; idx < nodes.getLength(); idx++) {
				nodes.item(idx).getAttributes().getNamedItem("shareFolderID")
						.setNodeValue(Integer.toString(container.getShareFolderID()));
			}
			System.out.println("Everything updated.");
			// save xml file back
			conn.writeToXML();
		return true;
	}
/**
 * DeleteFolder
 * @param encryptedFolder
 * @return
 */
	public boolean deleteContainer(Container container) {
		document = conn.getXML();
			NodeList nodes = conn.executeQuery("//"+childElement+"[@id='" + container.getID()
					+ "']");
			for (int idx = 0; idx < nodes.getLength(); idx++) {
				nodes.item(idx).getParentNode().removeChild(nodes.item(idx));
			}
			conn.writeToXML();
		return true;
	}
}
