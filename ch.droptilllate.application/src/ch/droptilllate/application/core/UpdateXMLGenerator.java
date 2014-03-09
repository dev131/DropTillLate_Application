package ch.droptilllate.application.core;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.droptilllate.application.com.XmlConnection;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.properties.XMLConstruct;

public class UpdateXMLGenerator {
	private XmlConnection conn;
	private Document document;
	public UpdateXMLGenerator(String key){
		this.conn = new XmlConnection(false, key);	
		document = conn.getXML();
	}
	
	public void createFileUpdateXML(List<EncryptedFileDob> dobList){
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.RootElementFile);
		Node node = nodelist.item(0);
		for(EncryptedFileDob encryptedFileDob : dobList){
		Element file = document.createElement(XMLConstruct.ChildElementFile);
		file.setAttribute(XMLConstruct.AttId, Integer.toString(encryptedFileDob.getId()));
		int parentID = 0;
		file.setAttribute(XMLConstruct.AttFileName, encryptedFileDob.getName());
		file.setAttribute(XMLConstruct.AttDate, encryptedFileDob.getDate().toString());
		file.setAttribute(XMLConstruct.AttSize, encryptedFileDob.getSize().toString());
		file.setAttribute(XMLConstruct.AttType, encryptedFileDob.getType());
		file.setAttribute(XMLConstruct.AttParentId, Integer.toString(parentID));
		file.setAttribute(XMLConstruct.AttContainerId, Integer.toString(encryptedFileDob.getContainerId()));
		node.appendChild(file);			
	}
		conn.writeToXML();	
	}
	
	public void createGhostFolderUpdateXML(List<GhostFolderDob> dobList){
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.RootElementGhostFolder);
		Node node = nodelist.item(0);
		// TODO Generate ID and Check if it exist
		for(GhostFolderDob folderDob : dobList){
			Element folder = document.createElement(XMLConstruct.ChildElementGhostFolder);
			folder.setAttribute(XMLConstruct.AttId, Integer.toString(folderDob.getId()));
			int parentID = folderDob.getParent().getId();
			folder.setAttribute(XMLConstruct.AttFileName, folderDob.getName());
			folder.setAttribute(XMLConstruct.AttParentId, Integer.toString(parentID));
			node.appendChild(folder);
		}		
		conn.writeToXML();
	}
	
	public void createContainerUpdateXML(List<EncryptedContainer> shareList){
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.RootElementContainer);
		Node node = nodelist.item(0);
		for(EncryptedContainer container : shareList){
			Element element = document.createElement(XMLConstruct.ChildElementContainer);
			element.setAttribute(XMLConstruct.AttId, Integer.toString(container.getId()));
			element.setAttribute(XMLConstruct.AttShareFolderId,
					Integer.toString(container.getShareFolderId()));
			node.appendChild(element);
		}		
		conn.writeToXML();
	}
	
	public void creatShareRelationUpdateXML(List<ShareRelation> shareList){
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.RootElementShareRelation);
		Node node = nodelist.item(0);
		for(ShareRelation shareRelation : shareList){
			Element folder = document.createElement(XMLConstruct.ChildElementShareRelation);
			folder.setAttribute(XMLConstruct.AttShareFolderId,
					Integer.toString(shareRelation.getSharefolderId()));
			folder.setIdAttribute(XMLConstruct.AttShareFolderId, true);
			folder.setAttribute(XMLConstruct.AttMail, shareRelation.getMail());
			node.appendChild(folder);
		}	
		conn.writeToXML();

	}
}
