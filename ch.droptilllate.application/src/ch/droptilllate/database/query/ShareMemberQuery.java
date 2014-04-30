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


import ch.droptilllate.application.dnb.ShareMember;

import ch.droptilllate.application.properties.XMLConstruct;

public class ShareMemberQuery {
	public Document document;
	/**
	 * Create element into DOM object
	 * @param encryptedFile
	 * @return document
	 */
	public List<ShareMember> createElement(List<ShareMember> list, Document document) {
		this.document = document;
		for(ShareMember element : list){
			NodeList nodelist = document.getElementsByTagName(XMLConstruct.RootElementShareMember);
			Node node = nodelist.item(0);
			Element shareMember = document.createElement(XMLConstruct.ChildElementShareMember);
			// Generate xml entry with ID
			shareMember.setAttribute(XMLConstruct.AttMail, element.getMail());
			shareMember.setAttribute(XMLConstruct.AttShareRelationID, element.getShareRelationId().toString());
			node.appendChild(shareMember);
		}	
		return list;
	}

	/**
	 * Delete files from DOM object
	 * @param fileDobList
	 * @param document
	 * @return document
	 */
	public Document deleteElement(List<ShareMember> list, Document document){
		for(ShareMember element : list){
		// cast the result to a DOM NodeList
		NodeList nodes = executeQuery(XMLConstruct.getShareMemberExpression()+ "[@"+XMLConstruct.AttShareRelationID+"='"
				+ element.getShareRelationId() + "']", document);
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
		NodeList nodes = document.getElementsByTagName(XMLConstruct.ChildElementShareMember);
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
	public Document updateElement(List<ShareMember> list, Document document){
		for(ShareMember element : list){
			NodeList nodes = executeQuery(XMLConstruct.getShareMemberExpression()+ "[@"+XMLConstruct.AttMail+"='"
					+ element.getMail() + "']", document);
			for (int idx = 0; idx < nodes.getLength(); idx++) {
				if (element.getShareRelationId() != null)
					nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttShareRelationID)
							.setNodeValue(element.getShareRelationId().toString());
			}
		}
			return document;
	}
	
	/**
	 * Get Element from DOM object, Parent will be empty
	 * @param argument
	 * @param value
	 * @return List<EncryptedFileDob>
	 */
	public List<ShareMember> getElement(String argument, String value, Document document){
		List<ShareMember> list = new ArrayList<ShareMember>();
		NodeList nodes = executeQuery(XMLConstruct.getShareMemberExpression()+ "[@"+argument+"='"
				+ value + "']", document);
		for (int i = 0; i < nodes.getLength(); i++) {
			ShareMember sharemember = new ShareMember(Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttShareRelationID)
							.getNodeValue()), nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttMail)
							.getNodeValue());
			list.add(sharemember);
		}
		return list;
	}
	
	
	/**
	 * Get All Element from DOM object, Parent will be empty
	 * @param document
	 * @return
	 */
	public List<ShareMember> getElementAll(Document document) {
		NodeList nodes = document.getElementsByTagName(XMLConstruct.ChildElementShareMember);
		List<ShareMember> list = new ArrayList<ShareMember>();
		for (int i = 0; i < nodes.getLength(); i++) {
			ShareMember sharemember = new ShareMember(Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttShareRelationID)
					.getNodeValue()), nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttMail)
					.getNodeValue());
				list.add(sharemember);
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
