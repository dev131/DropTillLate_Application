package ch.droptilllate.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;










import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.droptilllate.application.dnb.CloudAccount;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.exceptions.DatabaseStatus;
import ch.droptilllate.application.info.CRUDContainerInfo;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.application.xml.XmlConnection;

public class ContainerQuery {
	/**
	 * Create element into DOM object
	 * @param EncryptedContainer
	 * @return document
	 */
	public Document createElement(List<EncryptedContainer> list, Document document) {
		for(EncryptedContainer element : list){
			NodeList nodelist = document.getElementsByTagName(XMLConstruct.RootElementContainer);
			Node node = nodelist.item(0);
			Element account = document.createElement(XMLConstruct.ChildElementContainer);
			// Generate xml entry with ID
			account.setAttribute(XMLConstruct.AttId, element.getId().toString());
			account.setAttribute(XMLConstruct.AttShareRelationID, element.getShareRelationId().toString());
			node.appendChild(account);
		}	
		return document;
	}

	/**
	 * Delete files from DOM object
	 * @param EncryptedContainerlist
	 * @param document
	 * @return document
	 */
	public Document deleteElement(List<EncryptedContainer> list, Document document){
		for(EncryptedContainer element : list){
		// cast the result to a DOM NodeList
		NodeList nodes = executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.AttId+"='"
				+ element.getId() + "']", document);
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getParentNode().removeChild(nodes.item(idx));
		}
		}	
		return document;
	}
	
	/**
	 * Delete All child elements
	 * @param document
	 */
	public Document deleteAllElementAll(Document document) {
		NodeList nodes = document.getElementsByTagName(XMLConstruct.ChildElementContainer);
		for (int i = 0; i < nodes.getLength(); i++) {			
				nodes.item(i).getParentNode().removeChild(nodes.item(i));		
		}
		return document;
	}
	
	/**
	 * UpdateElement in DOM object
	 * @param EncryptedContainerlist
	 * @param document
	 * @return document
	 */
	public Document updateElement(List<EncryptedContainer> list, Document document){
		for(EncryptedContainer element : list){
			NodeList nodes = executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.AttId+"='"
					+ element.getId() + "']", document);
			for (int idx = 0; idx < nodes.getLength(); idx++) {
				if (element.getShareRelationId() != null){	
				nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttShareRelationID)
							.setNodeValue(element.getShareRelationId().toString());
		}}
		}
			return document;
	}
	
	/**
	 * Get Element from DOM object, Parent will be empty
	 * @param argument
	 * @param value
	 * @return EncryptedContainerlist
	 */
	public List<EncryptedContainer> getElement(String argument, String value, Document document){
		List<EncryptedContainer> list = new ArrayList<EncryptedContainer>();
		NodeList nodes = executeQuery(XMLConstruct.getFileExpression()+ "[@"+argument+"='"
				+ value + "']", document);
		for (int i = 0; i < nodes.getLength(); i++) {
			EncryptedContainer container = new EncryptedContainer(
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttId)
					.getNodeValue()), 
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttShareRelationID)
							.getNodeValue())
					);
			list.add(container);
		}
		return list;
	}
	
	
	/**
	 * Get All Element from DOM object, Parent will be empty
	 * @param document
	 * @return EncryptedContainerlist
	 */
	public List<EncryptedContainer> getElementAll(Document document) {
		NodeList nodes = document.getElementsByTagName(XMLConstruct.ChildElementContainer);
		List<EncryptedContainer> list = new ArrayList<EncryptedContainer>();
		for (int i = 0; i < nodes.getLength(); i++) {
			EncryptedContainer container = new EncryptedContainer(
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttId)
					.getNodeValue()), 
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttShareRelationID)
							.getNodeValue())
					);
			list.add(container);
		}
		return list;
	}

	/**
	 * Execute query
	 * @param query
	 * @param document
	 * @return NoeList
	 */
	private synchronized NodeList executeQuery(String query,Document document) {
		NodeList nodes = null;
		XPathExpression expr = null;
		// create an XPathFactory
		XPathFactory xFactory = XPathFactory.newInstance();
		// create an XPath object
		XPath xpath = xFactory.newXPath();
		// compile the XPath expression
		// run the query and get a nodeset
		Object result = null;
		try {
			expr = xpath.compile(query);

			result = expr.evaluate(document.getDocumentElement(),
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		// cast the result to a DOM NodeList
		nodes = (NodeList) result;
		return nodes;
	}
	

}
