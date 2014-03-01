package ch.droptilllate.application.core;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import ch.droptilllate.application.com.IXmlConnection;
import ch.droptilllate.application.com.XmlConnection;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.views.Messages;

public class UpdateXMLGenerator {
	private String rootElement = "collection";
	private String childElementFile = "file";
	private String childElementFolder = "folder";
	private String childElementContainer= "container";
	private String childElementShareRelation = "share";
	
	public void createFileUpdateXML(String shareFolderPath, List<EncryptedFileDob> dobList){
		IXmlConnection conn = new XmlConnection(shareFolderPath +"/"+Messages.getFileUpdateXML(), rootElement);
		Document document = conn.getXML();
		Node node = document.getFirstChild();		
		// Generate xml entry with ID
		for(EncryptedFileDob encryptedFileDob : dobList){
		Element file = document.createElement(childElementFile);
		file.setAttribute("id", Integer.toString(encryptedFileDob.getId()));
		file.setIdAttribute("id", true);
		int parentID = 0;
		file.setAttribute("name", encryptedFileDob.getName());
		file.setAttribute("date", encryptedFileDob.getDate().toString());
		file.setAttribute("size", encryptedFileDob.getSize().toString());
		file.setAttribute("type", encryptedFileDob.getType());
		file.setAttribute("path", encryptedFileDob.getPath());
		file.setAttribute("parentID", Integer.toString(parentID));
		file.setAttribute("containerID", Integer.toString(encryptedFileDob.getContainerId()));
		node.appendChild(file);			
	}
		conn.writeToXML();	
	}
	
	public void createFolderUpdateXML(String shareFolderPath, List<GhostFolderDob> dobList){
		IXmlConnection conn = new XmlConnection(shareFolderPath +"/"+Messages.getFolderUpdateXML(), rootElement);
		Document document = conn.getXML();
		Node node = document.getFirstChild();
		// TODO Generate ID and Check if it exist
		for(GhostFolderDob folderDob : dobList){
			Element folder = document.createElement(childElementFolder);
			folder.setAttribute("id", Integer.toString(folderDob.getId()));
			folder.setIdAttribute("id", true);
			int parentID = folderDob.getParent().getId();
			folder.setAttribute("name", folderDob.getName());
			folder.setAttribute("date", folderDob.getDate().toString());
			folder.setAttribute("path", folderDob.getPath().toString());
			folder.setAttribute("parentID", Integer.toString(parentID));
			node.appendChild(folder);
		}		
		conn.writeToXML();
	}
	
	public void createContainerUpdateXML(String shareFolderPath, List<EncryptedContainer> shareList){
		IXmlConnection conn = new XmlConnection(shareFolderPath +"/"+Messages.getContainerUpdateXML(), rootElement);
		Document document = conn.getXML();
		Node node = document.getFirstChild();
		for(EncryptedContainer container : shareList){
			Element element = document.createElement(childElementContainer);
			element.setAttribute("id", Integer.toString(container.getId()));
			element.setIdAttribute("id", true);
			element.setAttribute("shareFolderID",
					Integer.toString(container.getShareFolderId()));
			node.appendChild(element);
		}		
		conn.writeToXML();
	}
	
	public void creatShareRelationUpdateXML(String shareFolderPath, List<ShareRelation> shareList){
		IXmlConnection conn = new XmlConnection(shareFolderPath +"/"+Messages.getShareRelationUpdateXML(), rootElement);
		Document document = conn.getXML();
		Node node = document.getFirstChild();
		for(ShareRelation shareRelation : shareList){
			Element folder = document.createElement(childElementShareRelation);
			folder.setAttribute("sharefolderId",
					Integer.toString(shareRelation.getSharefolderId()));
			folder.setIdAttribute("sharefolderId", true);
			folder.setAttribute("mail", shareRelation.getMail());
			node.appendChild(folder);
		}	
		conn.writeToXML();

	}
}
