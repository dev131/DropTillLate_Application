package ch.droptilllate.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import ch.droptilllate.application.dnb.ShareMember;
import ch.droptilllate.application.exceptions.DatabaseStatus;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.application.xml.XmlConnection;

public class CloudAccountQuery {

		/**
		 * Create element into DOM object
		 * @param encryptedFile
		 * @return document
		 */
		public Document createElement(List<CloudAccount> list, Document document) {
			for(CloudAccount element : list){
				NodeList nodelist = document.getElementsByTagName(XMLConstruct.RootElementCloudAccount);
				Node node = nodelist.item(0);
				Element account = document.createElement(XMLConstruct.ChildElementCloudAccount);
				// Generate xml entry with ID
				account.setAttribute(XMLConstruct.AttCloudUsername, element.getUsername());
				account.setAttribute(XMLConstruct.AttCloudPassword, element.getPassword());
				node.appendChild(account);
			}	
			return document;
		}

		/**
		 * Delete files from DOM object
		 * @param fileDobList
		 * @param document
		 * @return document
		 */
		public Document deleteElement(List<CloudAccount> list, Document document){
			for(CloudAccount element : list){
			// cast the result to a DOM NodeList
			NodeList nodes = executeQuery(XMLConstruct.getCloudAccountExpression()+ "[@"+XMLConstruct.AttCloudUsername+"='"
					+ element.getUsername() + "']", document);
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
			NodeList nodes = document.getElementsByTagName(XMLConstruct.ChildElementCloudAccount);
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
		public Document updateElement(List<CloudAccount> list, Document document){
			for(CloudAccount element : list){
				NodeList nodes = executeQuery(XMLConstruct.getCloudAccountExpression()+ "[@"+XMLConstruct.AttCloudUsername+"='"
						+ element.getUsername() + "']", document);
				for (int idx = 0; idx < nodes.getLength(); idx++) {
					if (element.getPassword() != null)
						nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttCloudPassword)
								.setNodeValue(element.getPassword());
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
		public List<CloudAccount> getElement(String argument, String value, Document document){
			List<CloudAccount> list = new ArrayList<CloudAccount>();
			NodeList nodes = executeQuery(XMLConstruct.getCloudAccountExpression()+ "[@"+argument+"='"
					+ value + "']", document);
			for (int i = 0; i < nodes.getLength(); i++) {
				CloudAccount account = new CloudAccount(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttCloudUsername)
						.getNodeValue().toString(), nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttCloudPassword)
						.getNodeValue().toString());
				list.add(account);
			}
			return list;
		}
		
		
		/**
		 * Get All Element from DOM object, Parent will be empty
		 * @param document
		 * @return
		 */
		public List<CloudAccount> getElementAll(Document document) {
			NodeList nodes = document.getElementsByTagName(XMLConstruct.ChildElementCloudAccount);
			List<CloudAccount> list = new ArrayList<CloudAccount>();
			for (int i = 0; i < nodes.getLength(); i++) {
				CloudAccount account = new CloudAccount(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttCloudUsername)
						.getNodeValue().toString(), nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttCloudPassword)
						.getNodeValue().toString());
				list.add(account);
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
