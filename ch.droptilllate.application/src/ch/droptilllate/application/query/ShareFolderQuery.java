package ch.droptilllate.application.query;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.droptilllate.application.com.IXmlConnection;
import ch.droptilllate.application.com.XmlConnection;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.views.Messages;

public class ShareFolderQuery {
	private Document document;
	private IXmlConnection conn;
	private String rootElement = "collection";
	private String childElement = "sharefolder";

	public ShareFolderQuery() {
		conn = new XmlConnection(Messages.getShareFolderXMLpath(), rootElement);

	}

	/**
	 * New ShareFolder entry
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public void newShareFolder(ShareFolder sharefolder) {
		document = conn.getXML();
		Node node = document.getFirstChild();
		Element folder = document.createElement(childElement);
		folder.setAttribute("id", Integer.toString(sharefolder.getID()));
		folder.setIdAttribute("id", true);
		folder.setAttribute("key", sharefolder.getKey());
		folder.setAttribute("path", sharefolder.getPath());
		node.appendChild(folder);
		conn.writeToXML();
	}

	public ShareFolder getShareFolder(int sharefolderID) {
		document = conn.getXML();
		// cast the result to a DOM NodeList
		
		NodeList nodes = conn.executeQuery("//" + childElement + "[@id='"
				+ sharefolderID + "']");
		ShareFolder sharefolder = null;
		if(nodes.getLength()>0){
			sharefolder= new ShareFolder(sharefolderID,
				nodes.item(0).getAttributes().getNamedItem("path")
						.getNodeValue().toString(), nodes.item(0)
						.getAttributes().getNamedItem("key").getNodeValue()
						.toString());
		}
		return sharefolder;
	}

	/**
	 * UpdateFolder
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public boolean updateShareFolder(ShareFolder sharefolder) {
		document = conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery("//" + childElement + "[@id='"
				+ sharefolder.getID() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getAttributes().getNamedItem("key")
					.setNodeValue(sharefolder.getKey());
		}
		System.out.println("Everything updated.");
		// save xml file back
		conn.writeToXML();
		return true;
	}

	/**
	 * DeleteFolder
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public boolean deleteShareFolder(ShareFolder sharefolder) {
		document = conn.getXML();
		NodeList nodes = conn.executeQuery("//" + childElement + "[@id='"
				+ sharefolder.getID() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getParentNode().removeChild(nodes.item(idx));
		}
		conn.writeToXML();
		return true;
	}

}
