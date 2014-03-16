package ch.droptilllate.application.xml;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

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

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ch.droptilllate.application.com.FileSystemCom;
import ch.droptilllate.application.dao.ContainerDao;
import ch.droptilllate.application.dao.EncryptedFileDao;
import ch.droptilllate.application.dao.ShareFolderDao;
import ch.droptilllate.application.dao.ShareRelationDao;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.handlers.FileHandler;
import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.lifecycle.OSValidator;
import ch.droptilllate.application.listener.FileChangeListener;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.StructureXmlDob;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.couldprovider.api.IFileSystemCom;
import ch.droptilllate.filesystem.preferences.Constants;

public class XmlConnection {

	private String path;
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private Document document;

	
	public XmlConnection(Boolean local, String key) {
		if(local){
			path = Configuration.getPropertieTempPath(true) + XMLConstruct.NameLocalXML;

			File f = new File(Configuration.getPropertieDropBoxPath(true)+Messages.getIdSize()+ OSValidator.getSlash()
					+ XMLConstruct.IdXMLContainer+ "."+ Constants.CONTAINER_EXTENTION);
			//IF exist create new register to listener ELSE decrypt and register to listener
			if (!f.exists()){
				createFile(path);
				encryptFile(key);
				deleteFile(path);
				decryptFile(key, true);
				insertDBEntry(key);
				
			}
			else{
				File f1 = new File(path);
				//If not decrypted
				if(!f1.exists()){
					decryptFile(key, true);
				}
			}
				
		}
		else{
			path = Configuration.getPropertieTempPath(true) + XMLConstruct.NameShareXML; 
			File f = new File(path);
			if (!f.exists())
				createFile(path);
		}
	}

	private void insertDBEntry(String key) {
		ShareFolder shareFolder = new ShareFolder(Messages.getIdSize(), key);
		StructureXmlDob sxml = new StructureXmlDob(shareFolder, true);
		EncryptedFileDob filedob = sxml.getEncryptedFileDob();
		ShareRelation shareRelation= sxml.getShareRelation();
		EncryptedContainer encryptedContainer = sxml.getEncryptedContainer();
		EncryptedFileDao fDao = new EncryptedFileDao();
		ShareFolderDao sfDao = new ShareFolderDao();
		ShareRelationDao srDao = new ShareRelationDao();
		ContainerDao cDao = new ContainerDao();
		fDao.newElement(filedob, null);
		sfDao.newElement(shareFolder, null);
		srDao.newElement(shareRelation, null);
		cDao.newElement(encryptedContainer, null);
	
	}

	private boolean decryptFile(String key, boolean local) {
		boolean status = false;
		IFileSystemCom fileSystem = FileSystemCom.getInstance();	
		ShareFolder shareFolder = new ShareFolder(Messages.getIdSize(), key);
		if(fileSystem.decryptFile(shareFolder, true)){
			//TODO Successfull
			status = true;
		};
		StructureXmlDob sxml;
		File file = new File(path);	
		if(!local){
			sxml = new StructureXmlDob(shareFolder, false);
		}
		else{
			sxml = new StructureXmlDob(shareFolder, true);			
		}		
		EncryptedFileDob dob = sxml.getEncryptedFileDob();
		//Integer id, String name, Date date, String path, GhostFolderDob parent, String type, Long size, Integer containerId)
			FileHandler fileHandler = new FileHandler();
			fileHandler.setFileListener(file, dob);
		
		return status;
	}

	private void deleteFile(String path2) {
		try{			 
    		File file = new File(path2);
 
    		if(file.delete()){
    			System.out.println(file.getName() + " is deleted!");
    		}else{
    			System.out.println("Delete operation is failed.");
    		}
 
    	}catch(Exception e){
 
    		e.printStackTrace();
 
    	}
		
	}

	private void encryptFile(String key) {
		// TODO Auto-generated method stub
		IFileSystemCom fileSystem = FileSystemCom.getInstance();	
		ShareFolder shareFolder = new ShareFolder(Messages.getIdSize(), key);
		fileSystem.encryptFile(shareFolder, true);
	}

	/**
	 * Create File
	 * 
	 * @param path
	 * @param rootElement
	 */
	private void createFile(String path) {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentFactory
					.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			//CreateRoot Element
			Element collection = document.createElement(XMLConstruct.RootElement);
			document.appendChild(collection);
			//Create SubContainerElement
			Element containers = document.createElement(XMLConstruct.RootElementContainer);
			collection.appendChild(containers);
			//Create SubFileElement
			Element files = document.createElement(XMLConstruct.RootElementFile);
			collection.appendChild(files);
			//Create SubFolderElement
			Element folders = document.createElement(XMLConstruct.RootElementGhostFolder);
			collection.appendChild(folders);
			//Create SubShareRelationElement
			Element shareRelations = document.createElement(XMLConstruct.RootElementShareRelation);
			collection.appendChild(shareRelations);
			//Create SubShareFolderElement
			Element shareFolders = document.createElement(XMLConstruct.RootElementShareFolder);
			collection.appendChild(shareFolders);
			
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

	public synchronized Document getXML() { 
		factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		builder = null;
		document = null;
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(path);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;

	}

	public synchronized void writeToXML() {
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result1 = new StreamResult(new File(path));
			transformer.transform(source, result1);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	

	}

	public synchronized NodeList executeQuery(String query) {
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
