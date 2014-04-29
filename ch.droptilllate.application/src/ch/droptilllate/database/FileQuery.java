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

import ch.droptilllate.application.dnb.ShareMember;
import ch.droptilllate.application.exceptions.DatabaseStatus;
import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.application.xml.XmlConnection;

public class FileQuery {

	/**
	 * Create element into DOM object
	 * @param encryptedFile
	 * @return document
	 */
	public Document createElement(List<EncryptedFileDob> fileDobList, Document document) {
		for(EncryptedFileDob encryptedFileDob : fileDobList){
			NodeList nodelist = document.getElementsByTagName(XMLConstruct.RootElementFile);
			Node node = nodelist.item(0);
			Element file = document.createElement(XMLConstruct.ChildElementFile);
			// Generate xml entry with ID
			file.setAttribute(XMLConstruct.AttId, Integer.toString(encryptedFileDob.getId()));
			String parentID = "0";
			if(encryptedFileDob.getParent() != null){
			parentID = Integer.toString(encryptedFileDob.getParent().getId());
			}
			String containerID ="0";
			if(encryptedFileDob.getContainerId() != null){
				containerID = encryptedFileDob.getContainerId().toString();
			}
			file.setAttribute(XMLConstruct.AttFileName, encryptedFileDob.getName());
			file.setAttribute(XMLConstruct.AttDate, encryptedFileDob.getDate().toString());
			file.setAttribute(XMLConstruct.AttSize, encryptedFileDob.getSize().toString());
			file.setAttribute(XMLConstruct.AttType, encryptedFileDob.getType());
			file.setAttribute(XMLConstruct.AttParentId, parentID);
			file.setAttribute(XMLConstruct.AttContainerId, containerID);
			node.appendChild(file);
		}	
		return document;
	}

	/**
	 * Delete files from DOM object with id
	 * @param fileDobList
	 * @param document
	 * @return document
	 */
	public Document deleteElement(List<EncryptedFileDob> fileDobList, Document document){
		for(EncryptedFileDob fileDob : fileDobList){
		// cast the result to a DOM NodeList
		NodeList nodes = executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.AttId+"='"
				+ fileDob.getId() + "']", document);
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
		NodeList nodes = document.getElementsByTagName(XMLConstruct.ChildElementFile);
		for (int i = 0; i < nodes.getLength(); i++) {
			if(Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttId).getNodeValue()) != Messages.getIdSize()){
				nodes.item(i).getParentNode().removeChild(nodes.item(i));	
			}			
		}
		return document;
	}
	
	/**
	 * UpdateElement in DOM object
	 * @param fileDobList
	 * @param document
	 * @return document
	 */
	public Document updateElement(List<EncryptedFileDob> fileDobList, Document document){
		for(EncryptedFileDob dob : fileDobList){
			NodeList nodes = executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.AttId+"='"
					+ dob.getId() + "']", document);
			for (int idx = 0; idx < nodes.getLength(); idx++) {
				if (dob.getName() != null)
					nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttFileName)
							.setNodeValue(dob.getName());
				if (dob.getDate() != null)
					nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttDate)
							.setNodeValue(dob.getDate().toString());
				if (dob.getSize() != null)
					nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttSize)
							.setNodeValue(dob.getSize().toString());
				if (dob.getType() != null)
					nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttType)
							.setNodeValue(dob.getType());
				if (dob.getContainerId() >= 0)
					nodes.item(idx)
							.getAttributes()
							.getNamedItem(XMLConstruct.AttContainerId)
							.setNodeValue(
									Integer.toString(dob.getContainerId()));
				if (dob.getParent() != null)
					nodes.item(idx)
							.getAttributes()
							.getNamedItem(XMLConstruct.AttParentId)
							.setNodeValue(
									Integer.toString(dob.getParent()
											.getId()));
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
	public List<EncryptedFileDob> getElement(String argument, String value, Document document){
		List<EncryptedFileDob> files = new ArrayList<EncryptedFileDob>();
		NodeList nodes = executeQuery(XMLConstruct.getFileExpression()+ "[@"+argument+"='"
				+ value + "']", document);
		for (int i = 0; i < nodes.getLength(); i++) {
			String date1 = nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttDate)
					.getNodeValue().toString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parsed = null;
			try {
				parsed = format.parse(date1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
			long size = Long.parseLong(nodes.item(i).getAttributes()
					.getNamedItem(XMLConstruct.AttSize).getNodeValue());
			//Integer id, String name, Date date, String path, GhostFolderDob parent, String type, Long size, Integer containerId
			EncryptedFileDob fileDob = new EncryptedFileDob(
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttId).getNodeValue()), 
					nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttFileName).getNodeValue(), 
					sqlDate, 
					"", 
					null,   
					size, 
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttContainerId).getNodeValue()));
			files.add(fileDob);
		}
		return files;
	}
	

	/**
	 * Get Elements from DOM object by parent ID
	 * @param folderdob
	 * @param document
	 * @return
	 */
	public List<EncryptedFileDob> getElementByParent(GhostFolderDob folderdob, Document document){
		List<EncryptedFileDob> files = new ArrayList<EncryptedFileDob>();
		NodeList nodes = executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.AttParentId+"='"
				+ folderdob.getId() + "']", document);
		for (int i = 0; i < nodes.getLength(); i++) {
			String date1 = nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttDate)
					.getNodeValue().toString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parsed = null;
			try {
				parsed = format.parse(date1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
			long size = Long.parseLong(nodes.item(i).getAttributes()
					.getNamedItem(XMLConstruct.AttSize).getNodeValue());
			//Integer id, String name, Date date, String path, GhostFolderDob parent, String type, Long size, Integer containerId
			EncryptedFileDob fileDob = new EncryptedFileDob(
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttId).getNodeValue()), 
					nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttFileName).getNodeValue(), 
					sqlDate, 
					"", 
					folderdob,   
					size, 
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttContainerId).getNodeValue()));
			files.add(fileDob);
		}
		return files;
	}
	
	
	/**
	 * Get All Element from DOM object, Parent will be empty
	 * @param document
	 * @return
	 */
	public List<EncryptedFileDob> getElementAll(Document document) {
		NodeList nodes = document.getElementsByTagName(XMLConstruct.ChildElementFile);
		List<EncryptedFileDob> encrytpedFileDobList = new ArrayList<EncryptedFileDob>();
		for (int i = 0; i < nodes.getLength(); i++) {
			String date1 = nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttDate)
					.getNodeValue().toString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parsed = null;
			try {
				parsed = format.parse(date1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
			long size = Long.parseLong(nodes.item(i).getAttributes()
					.getNamedItem(XMLConstruct.AttSize).getNodeValue());
			//Integer id, String name, Date date, String path, GhostFolderDob parent, String type, Long size, Integer containerId
			EncryptedFileDob fileDob = new EncryptedFileDob(
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttId).getNodeValue()), 
					nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttFileName).getNodeValue(), 
					sqlDate, 
					"", 
					null, 
					size, 
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttContainerId).getNodeValue()));
			encrytpedFileDobList.add(fileDob);
		}
		return encrytpedFileDobList;
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
