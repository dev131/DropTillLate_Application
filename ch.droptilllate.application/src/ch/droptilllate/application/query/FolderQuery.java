package ch.droptilllate.application.query;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import ch.droptilllate.application.com.IXmlConnection;
import ch.droptilllate.application.com.XmlConnection;
import ch.droptilllate.application.dnb.EncryptedFolder;
import ch.droptilllate.application.model.EncryptedFolderDob;
import ch.droptilllate.application.views.Messages;

public class FolderQuery {
	private Document document;
	private IXmlConnection conn;
	private String rootElement = "collection";
	private String childElement = "folder";

	public FolderQuery() {
		conn = new XmlConnection(Messages.getFolderXMLpath(), rootElement);
	}

	/**
	 * New Folder entry
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public int newFolder(EncryptedFolder encryptedFolder) {
		// TODO id generate
		int id = (int) (Math.random() * 10000 + 1);
		// Check if it exist
		while (checkExist(id)) {
			id = (int) (Math.random() * 10000 + 1);
		}
		document = conn.getXML();
		Node node = document.getFirstChild();
		// TODO Generate ID and Check if it exist
		Element folder = document.createElement(childElement);
		folder.setAttribute("id", Integer.toString(id));
		folder.setIdAttribute("id", true);
		int parentID = encryptedFolder.getParent().getId();
		folder.setAttribute("name", encryptedFolder.getName());
		folder.setAttribute("date", encryptedFolder.getDate().toString());
		folder.setAttribute("path", encryptedFolder.getPath().toString());
		folder.setAttribute("parentID", Integer.toString(parentID));
		node.appendChild(folder);

		conn.writeToXML();
		return id;
	}

	private boolean checkExist(int sharefolderID) {
		document = conn.getXML();
		boolean result = false;
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery("//" + childElement + "[@id='"
				+ sharefolderID + "']");
		if(nodes.getLength()>0)
			result = true;
		return result;
	}

	/**
	 * get Folder in Folder
	 * 
	 * @param folder
	 * @return
	 */
	public List<EncryptedFolderDob> getFolder(EncryptedFolderDob folder) {
		List<EncryptedFolderDob> folders = new ArrayList<EncryptedFolderDob>();
		document = conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery("//" + childElement + "[@parentID='"
				+ folder.getId() + "']");
		for (int i = 0; i < nodes.getLength(); i++) {
			String date1 = nodes.item(i).getAttributes().getNamedItem("date")
					.getNodeValue().toString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parsed = null;
			try {
				parsed = format.parse(date1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
			folders.add(new EncryptedFolderDob(Integer.parseInt(nodes.item(i)
					.getAttributes().getNamedItem("id").getNodeValue()), nodes
					.item(i).getAttributes().getNamedItem("name")
					.getNodeValue(), sqlDate, nodes.item(i).getAttributes()
					.getNamedItem("path").getNodeValue()));

		}
		return folders;
	}

	/**
	 * UpdateFolder
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public boolean updateFolder(EncryptedFolderDob encryptedFolder) {
		int newParentID = encryptedFolder.getParent().getId();
		document = conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery("//" + childElement + "[@id='"
				+ encryptedFolder.getId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getAttributes().getNamedItem("parentID")
					.setNodeValue(Integer.toString(newParentID));
		}
		System.out.println("Everything replaced.");

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
	public boolean deleteFolder(EncryptedFolderDob encryptedFolder) {
		document = conn.getXML();
		NodeList nodes = conn.executeQuery("//" + childElement + "[@id='"
				+ encryptedFolder.getId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getParentNode().removeChild(nodes.item(idx));
		}
		conn.writeToXML();
		return true;
	}
}
