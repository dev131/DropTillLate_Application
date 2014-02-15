package ch.droptilllate.application.query;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ch.droptilllate.application.com.CRUDContainerResult;
import ch.droptilllate.application.com.FileSystemCom;
import ch.droptilllate.application.com.CRUDGhostFolderResult;
import ch.droptilllate.application.com.IFileSystemCom;
import ch.droptilllate.application.com.IXmlConnection;
import ch.droptilllate.application.com.XmlConnection;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.views.Messages;

public class ContainerQuery {
	private Document document;
	private IXmlConnection conn;
	private String rootElement = "collection";
	private String childElement = "container";

	public ContainerQuery() {
		conn = new XmlConnection(Messages.getContainerXMLpath(), rootElement);
	}

	/**
	 * New Container entry
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public void newContainer(EncryptedContainer container) {
		
		if (container.getId() == null) {
			int id = (int) (Math.random() * 10000 + 1);
			// Check if it exist
			while (checkExist(id)) {
				id = (int) (Math.random() * 10000 + 1);
			}
			//TODO if now container ID -> Alert! it have to be one
			container.setId(id);
		}
		document = conn.getXML();
		Node node = document.getFirstChild();
		Element element = document.createElement(childElement);
		element.setAttribute("id", Integer.toString(container.getId()));
		element.setIdAttribute("id", true);
		element.setAttribute("shareFolderID",
				Integer.toString(container.getShareFolderId()));
		node.appendChild(element);
		conn.writeToXML();
	}
	private boolean checkExist(int containerId) {
		document = conn.getXML();
		boolean result = false;
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery("//" + childElement + "[@id='"
				+ containerId + "']");
		if (nodes.getLength() > 0)
			result = true;
		return result;
	}

	public EncryptedContainer getContainerByID(int id) {
		document = conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery("//" + childElement + "[@id='" + id
				+ "']");
		EncryptedContainer container = null;
		if (nodes.getLength() > 0) {
			container = new EncryptedContainer(id, Integer.parseInt(nodes.item(0)
					.getAttributes().getNamedItem("shareFolderID")
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
		document = conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery("//" + childElement + "[@id='"
				+ container.getId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx)
					.getAttributes()
					.getNamedItem("shareFolderID")
					.setNodeValue(
							Integer.toString(container.getShareFolderId()));
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
	public boolean deleteContainer(EncryptedContainer container) {
		document = conn.getXML();
		NodeList nodes = conn.executeQuery("//" + childElement + "[@id='"
				+ container.getId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getParentNode().removeChild(nodes.item(idx));
		}
		conn.writeToXML();
		return true;
	}

	public CRUDContainerResult checkDatabase(List<EncryptedContainer> containerList) {
		document = conn.getXML();
		List<EncryptedContainer> encryptedContainerListSuccess = new ArrayList<EncryptedContainer>();
		List<EncryptedContainer> encryptedContainerListError = new ArrayList<EncryptedContainer>();
		Iterator<EncryptedContainer> ContainerInfoListErrorIterator = containerList.iterator();
		while (ContainerInfoListErrorIterator.hasNext()) {
			NodeList nodes = conn.executeQuery("//" + childElement + "[@id='"
					+ ContainerInfoListErrorIterator.next().getId() + "']");
			if (nodes.getLength() > 0) {
				encryptedContainerListSuccess.add(ContainerInfoListErrorIterator.next());
			} else {
				encryptedContainerListError.add(ContainerInfoListErrorIterator.next());
			}
		}
		CRUDContainerResult result = new CRUDContainerResult();
		result.setEncryptedContainerListError(encryptedContainerListError);
		result.setEncryptedContainerListSuccess(encryptedContainerListSuccess);
		return result;
	}

}
