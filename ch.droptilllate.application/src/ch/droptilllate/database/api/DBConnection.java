package ch.droptilllate.database.api;

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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ch.droptilllate.application.com.FileSystemCom;
import ch.droptilllate.application.dnb.TillLateContainer;
import ch.droptilllate.application.dnb.ShareMember;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.exceptions.DatabaseException;
import ch.droptilllate.application.exceptions.DatabaseStatus;
import ch.droptilllate.application.handlers.FileHandler;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.StructureXmlDob;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.cloudprovider.api.IFileSystemCom;
import ch.droptilllate.keyfile.error.KeyFileException;

public class DBConnection {
	
	/**
	 * Create File
	 * @param path
	 * @param rootElement
	 * @throws DatabaseException 
	 * @throws  
	 */
	public void createFile(String path) throws DatabaseException {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = null;
			try {
				documentBuilder = documentFactory
						.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				 throw new DatabaseException(DatabaseStatus.DATABASE_NOT_CREATED, e.getMessage());
			}
			Document document = documentBuilder.newDocument();
			//CreateRoot Element
			Element collection = document.createElement(XMLConstruct.RootElement);
			document.appendChild(collection);
			//Create CloudAccount
			Element cloudAccount = document.createElement(XMLConstruct.RootElementCloudAccount);
			collection.appendChild(cloudAccount);
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
			Element shareRelations = document.createElement(XMLConstruct.RootElementShareMember);
			collection.appendChild(shareRelations);
			writeToXML(path, document);
			
	}

	/**
	 * Open Database
	 * @param path
	 * @return Document obj
	 * @throws DatabaseException 
	 */
	public synchronized Document getDatabase(String path) throws DatabaseException { 
		DocumentBuilderFactory factory;
	 	DocumentBuilder builder;
		Document document;
		factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		builder = null;
		document = null;
		
		try {
			builder = factory.newDocumentBuilder();
			String source = path;
			System.out.println(source);
			document = builder.parse(source);
		} catch (ParserConfigurationException e) {
			throw new DatabaseException(DatabaseStatus.CANNOT_OPEN_DATABASE, e.getMessage());
		} catch (SAXException e) {
			throw new DatabaseException(DatabaseStatus.CANNOT_OPEN_DATABASE, e.getMessage());
		} catch (IOException e) {
			throw new DatabaseException(DatabaseStatus.CANNOT_OPEN_DATABASE, e.getMessage());
		}
		return document;

	}

	/**
	 * Write changes to XML Database
	 * @param path
	 * @param document
	 * @throws DatabaseException 
	 */
	public synchronized void writeToXML(String path,Document document) throws DatabaseException {
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result1 = new StreamResult(new File(path));
			transformer.transform(source, result1);
		} catch (TransformerConfigurationException e) {
			throw new DatabaseException(DatabaseStatus.CANNOT_WRITE_TO_XML, e.getMessage());
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Execute Query in Database
	 * @param query
	 * @param document
	 * @return NodeList
	 * @throws DatabaseException
	 */
	public synchronized NodeList executeQuery(String query, Document document) throws DatabaseException {
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
			throw new DatabaseException(DatabaseStatus.WRONG_QUERY, e.getMessage());
		}
		// cast the result to a DOM NodeList
		nodes = (NodeList) result;
		return nodes;
	}

	/**
	 * Decrypt Database and set listener
	 * @param key
	 * @param Path
	 * @return
	 * @throws DatabaseException 
	 */
	public void decryptDatabase(String password, String path, DBSituation situation, Integer shareFolderID) throws DatabaseException {
		IFileSystemCom fileSystem = FileSystemCom.getInstance();	
		ShareRelation shareFolder = new ShareRelation(shareFolderID, password);
		if(fileSystem.decryptFile(shareFolder, situation)){
			StructureXmlDob sxml;
			File file = new File(path);	
			
			sxml = new StructureXmlDob(situation);
			//Register File Listener
			//TODO not useful
//			EncryptedFileDob dob = sxml.getEncryptedFileDob();
//			FileHandler fileHandler = new FileHandler();
//			fileHandler.setFileListener(file, dob);		
		}		
		else{
			throw new DatabaseException(DatabaseStatus.DATABASE_DECRYPTION_FAILED);
		}
	}
	
	/**
	 * Encrypt Database
	 * @param key
	 * @throws DatabaseException 
	 */
	public synchronized void encryptDatabase(String password, DBSituation situation, Integer shareRelationID) throws DatabaseException {
		IFileSystemCom fileSystem = FileSystemCom.getInstance();	
		ShareRelation shareRelation = new ShareRelation(shareRelationID, password);
		if(!fileSystem.encryptFile(shareRelation, situation)){
			throw new DatabaseException(DatabaseStatus.DATABASE_ENCRYPTION_FAILED);
		}
	}

	/**
	 * Delete temporary file
	 * @param path
	 * @throws DatabaseException
	 */
	public void deleteTempDB(String path) throws DatabaseException {		 
    		File file = new File(path);
    		FileHandler handler = new FileHandler();
    		try {
				handler.delete(file);
			} catch (IOException e) {
				throw new DatabaseException(DatabaseStatus.CANNOT_DELETE_TEMPFILE, e.getMessage());
			}
		
	}
	

	/**
	 * Get path from local or share database
	 * @param local
	 * @return
	 */
	public String getPath(DBSituation situation, String path) {
		if(situation == DBSituation.LOCAL_DATABASE){
			return Configuration.getPropertieTempPath(path,true) + XMLConstruct.NameLocalXML;
		} 
		else{
			return  Configuration.getPropertieTempPath(path,true) + XMLConstruct.NameShareXML; 
		}
		
	}
	
	
}
