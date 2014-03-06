package ch.droptilllate.application.com;

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

import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.handlers.FileHandler;
import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.listener.FileChangeListener;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.views.Messages;
import ch.droptilllate.application.views.XMLConstruct;

public class XmlConnection {

	private String path;
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private Document document;

	
	public XmlConnection(Boolean local, String key) {
		if(local){
			path = Messages.getPathLocalTemp() + XMLConstruct.getNameLocalXML();
			//TODO tillate expression
			File f = new File(Messages.getPathLocalTemp() + XMLConstruct.getIdXMLContainer()+ ".tilllate");
			//IF exist create new register to listener ELSE decrypt and register to listener
			if (!f.exists()){
				createFile(path);
				encryptFile(key);
				deleteFile(path);
				decryptFile(key);
			}
			else{
				File f1 = new File(path);
				//If not decrypted
				if(!f1.exists())decryptFile(key);
			}
				
		}
		else{
			path = Messages.getPathLocalTemp() + XMLConstruct.getNameShareXML(); 
			File f = new File(path);
			if (!f.exists())
				createFile(path);
		}
	}

	private void decryptFile(String key) {
		IFileSystemCom fileSystem = new FileSystemCom();
		ShareFolder shareFolder = new ShareFolder(Integer.parseInt(Messages.ShareFolder0name), Messages.getPathDropBox(), key);
		if(fileSystem.decryptFile(shareFolder, true)){
			//TODO Successfull
		};
		File file = new File(path);	
		//Integer id, String name, Date date, String path, GhostFolderDob parent, String type, Long size, Integer containerId)
		EncryptedFileDob dob = new EncryptedFileDob(
				Integer.parseInt(XMLConstruct.getIdXMLFiles()), 
				file.getName(), 
				new Date(System.currentTimeMillis()), 
				file.getPath(), 
				null, 
				file.getName(), 
				file.length(), 
				Integer.parseInt(XMLConstruct.getIdXMLContainer()));
			FileHandler fileHanlder = new FileHandler();
			fileHanlder.setFileListener(file, dob);
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
		IFileSystemCom fileSystem = new FileSystemCom();
		ShareFolder shareFolder = new ShareFolder(Integer.parseInt(Messages.ShareFolder0name), Messages.getPathDropBox(), key);
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
			Element collection = document.createElement(XMLConstruct.getRootElement());
			document.appendChild(collection);
			//Create SubContainerElement
			Element containers = document.createElement(XMLConstruct.getRootElementContainer());
			collection.appendChild(containers);
			//Create SubFileElement
			Element files = document.createElement(XMLConstruct.getRootElementFile());
			collection.appendChild(files);
			//Create SubFolderElement
			Element folders = document.createElement(XMLConstruct.getRootElementGhostFolder());
			collection.appendChild(folders);
			//Create SubShareRelationElement
			Element shareRelations = document.createElement(XMLConstruct.getRootElementShareRelation());
			collection.appendChild(shareRelations);
			//Create SubShareFolderElement
			Element shareFolders = document.createElement(XMLConstruct.getRootElementShareFolder());
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

	public Document getXML() {
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

	public void writeToXML() {
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

	public NodeList executeQuery(String query) {
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
