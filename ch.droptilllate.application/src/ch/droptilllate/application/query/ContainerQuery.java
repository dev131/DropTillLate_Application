package ch.droptilllate.application.query;

import java.io.File;
import java.io.IOException;

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

import ch.droptilllate.application.com.FileSystemCom;
import ch.droptilllate.application.com.IFileSystemCom;
import ch.droptilllate.application.com.IXmlConnection;
import ch.droptilllate.application.com.XmlConnection;
import ch.droptilllate.application.dnb.Container;
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
		Container container = null;
		if(nodes.getLength()>0){
		container = new Container(id, Integer.parseInt(nodes.item(0).getAttributes().getNamedItem("shareFolderID").getNodeValue()));
		}
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
/**
 * Create New XML File
 * @param path
 */
	private void createFile(String path) {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentFactory
					.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			Element root = document.createElement("containers");
			document.appendChild(root);
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(path));
			transformer.transform(domSource, streamResult);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

}
