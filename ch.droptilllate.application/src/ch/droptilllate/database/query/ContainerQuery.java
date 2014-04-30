package ch.droptilllate.database.query;

import java.util.ArrayList;
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

import ch.droptilllate.application.dnb.TillLateContainer;
import ch.droptilllate.application.properties.XMLConstruct;

public class ContainerQuery {
	public Document document;
	/**
	 * Create element into DOM object
	 * @param TillLateContainer
	 * @return document
	 */
	public List<TillLateContainer> createElement(List<TillLateContainer> list, Document document) {
		this.document = document;
		for(TillLateContainer element : list){
			NodeList nodelist = document.getElementsByTagName(XMLConstruct.RootElementContainer);
			Node node = nodelist.item(0);
			Element account = document.createElement(XMLConstruct.ChildElementContainer);
			// Generate xml entry with ID
			account.setAttribute(XMLConstruct.AttId, element.getId().toString());
			account.setAttribute(XMLConstruct.AttShareRelationID, element.getShareRelationId().toString());
			node.appendChild(account);
		}	
		return list;
	}

	/**
	 * Delete files from DOM object
	 * @param EncryptedContainerlist
	 * @param document
	 * @return document
	 */
	public Document deleteElement(List<TillLateContainer> list, Document document){
		for(TillLateContainer element : list){
		// cast the result to a DOM NodeList
		NodeList nodes = executeQuery(XMLConstruct.getContainerExpression()+ "[@"+XMLConstruct.AttId+"='"
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
	public Document updateElement(List<TillLateContainer> list, Document document){
		for(TillLateContainer element : list){
			NodeList nodes = executeQuery(XMLConstruct.getContainerExpression()+ "[@"+XMLConstruct.AttId+"='"
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
	public List<TillLateContainer> getElement(String argument, String value, Document document){
		List<TillLateContainer> list = new ArrayList<TillLateContainer>();
		NodeList nodes = executeQuery(XMLConstruct.getContainerExpression()+ "[@"+argument+"='"
				+ value + "']", document);
		for (int i = 0; i < nodes.getLength(); i++) {
			TillLateContainer container = new TillLateContainer(
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
	public List<TillLateContainer> getElementAll(Document document) {
		NodeList nodes = document.getElementsByTagName(XMLConstruct.ChildElementContainer);
		List<TillLateContainer> list = new ArrayList<TillLateContainer>();
		for (int i = 0; i < nodes.getLength(); i++) {
			TillLateContainer container = new TillLateContainer(
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

	public Document getDocument() {
		return document;
	}
	

}
