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

import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.exceptions.DatabaseStatus;
import ch.droptilllate.application.info.CRUDGhostFolderInfo;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.application.xml.XmlConnection;

public class GhostFolderQuery {
	/**
	 * Create element into DOM object
	 * @param encryptedFile
	 * @return document
	 */
	public Document createElement(List<GhostFolderDob> list, Document document) {
		for(GhostFolderDob element : list){
			NodeList nodelist = document.getElementsByTagName(XMLConstruct.RootElementGhostFolder);
			Node node = nodelist.item(0);
			Element ghostfolder = document.createElement(XMLConstruct.ChildElementGhostFolder);
			// Generate xml entry with ID
			ghostfolder.setAttribute(XMLConstruct.AttId, element.getId().toString());
			ghostfolder.setAttribute(XMLConstruct.AttParentId, element.getParent().getId().toString());
			ghostfolder.setAttribute(XMLConstruct.AttFolderName, element.getName());
			node.appendChild(ghostfolder);
		}	
		return document;
	}

	/**
	 * Delete files from DOM object
	 * @param fileDobList
	 * @param document
	 * @return document
	 */
	public Document deleteElement(List<GhostFolderDob> list, Document document){
		for(GhostFolderDob element : list){
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
		NodeList nodes = document.getElementsByTagName(XMLConstruct.ChildElementGhostFolder);
		for (int i = 0; i < nodes.getLength(); i++) {			
				nodes.item(i).getParentNode().removeChild(nodes.item(i));		
		}
		return document;
	}
	
	/**
	 * UpdateElement in DOM object
	 * @param fileDobList
	 * @param document
	 * @return document
	 */
	public Document updateElement(List<GhostFolderDob> list, Document document){
		for(GhostFolderDob element : list){
			NodeList nodes = executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.AttId+"='"
					+ element.getId() + "']", document);
			for (int idx = 0; idx < nodes.getLength(); idx++) {
				if (element.getParent() != null){	
				nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttFolderName)
							.setNodeValue(element.getName().toString());
		}}
		}
			return document;
	}
	
	/**
	 * Get Element from DOM object, Parent will be empty
	 * @param argument
	 * @param value
	 * @return List<EncryptedFileDob>
	 */
	public List<GhostFolderDob> getElement(String argument, String value, Document document){
		List<GhostFolderDob> list = new ArrayList<GhostFolderDob>();
		NodeList nodes = executeQuery(XMLConstruct.getFileExpression()+ "[@"+argument+"='"
				+ value + "']", document);
		for (int i = 0; i < nodes.getLength(); i++) {
			GhostFolderDob dob = new GhostFolderDob(
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttId)
							.getNodeValue()), 
							nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttFolderName)
							.getNodeValue(), 
							null);
			list.add(dob);
		}
		return list;
	}
	
	
	/**
	 * Get All Element from DOM object, Parent will be empty
	 * @param document
	 * @return
	 */
	public List<GhostFolderDob> getElementAll(Document document) {
		NodeList nodes = document.getElementsByTagName(XMLConstruct.ChildElementContainer);
		List<GhostFolderDob> list = new ArrayList<GhostFolderDob>();
		for (int i = 0; i < nodes.getLength(); i++) {
			GhostFolderDob dob = new GhostFolderDob(
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttId)
							.getNodeValue()), 
							nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttFolderName)
							.getNodeValue(), 
							null);
			list.add(dob);
		}
		return list;
	}

	/**
	 * Get Elements from DOM object by parent ID
	 * @param folderdob
	 * @param document
	 * @return
	 */
	public List<GhostFolderDob> getElementByParent(GhostFolderDob folderdob, Document document){
		List<GhostFolderDob> folders = new ArrayList<GhostFolderDob>();
		// cast the result to a DOM NodeList
		NodeList nodes = executeQuery(XMLConstruct.getGhostFolderExpression()+ "[@"+XMLConstruct.AttParentId+"='"
				+ folderdob.getId() + "']", document);
		for (int i = 0; i < nodes.getLength(); i++) {
			//Integer id, String name, GhostFolderDob parent
			GhostFolderDob dob = new GhostFolderDob(
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttId).getNodeValue()),
					nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttFolderName).getNodeValue(), 
					folderdob);			
			folders.add(dob);
		}
		return folders;
	}
	
	/**
	 * Execute query
	 * @param query
	 * @param document
	 * @return NodeList
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
