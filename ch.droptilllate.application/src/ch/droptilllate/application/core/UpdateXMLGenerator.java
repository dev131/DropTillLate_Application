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
import ch.droptilllate.application.views.XMLConstruct;

public class UpdateXMLGenerator {
	private XmlConnection conn;
	private Document document;
	public UpdateXMLGenerator(String key){
		this.conn = new XmlConnection(false, key);	
		document = conn.getXML();
	}
	
	public void createFileUpdateXML(List<EncryptedFileDob> dobList){
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.getRootElementFile());
		Node node = nodelist.item(0);
		for(EncryptedFileDob encryptedFileDob : dobList){
		Element file = document.createElement(XMLConstruct.getChildElementFile());
		file.setAttribute(XMLConstruct.getAttId(), Integer.toString(encryptedFileDob.getId()));
		int parentID = 0;
		file.setAttribute(XMLConstruct.getAttFileName(), encryptedFileDob.getName());
		file.setAttribute(XMLConstruct.getAttDate(), encryptedFileDob.getDate().toString());
		file.setAttribute(XMLConstruct.getAttSize(), encryptedFileDob.getSize().toString());
		file.setAttribute(XMLConstruct.getAttType(), encryptedFileDob.getType());
		file.setAttribute(XMLConstruct.getAttPath(), encryptedFileDob.getPath());
		file.setAttribute(XMLConstruct.getAttParentId(), Integer.toString(parentID));
		file.setAttribute(XMLConstruct.getAttContainerId(), Integer.toString(encryptedFileDob.getContainerId()));
		node.appendChild(file);			
	}
		conn.writeToXML();	
	}
	
	public void createGhostFolderUpdateXML(List<GhostFolderDob> dobList){
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.getRootElementGhostFolder());
		Node node = nodelist.item(0);
		// TODO Generate ID and Check if it exist
		for(GhostFolderDob folderDob : dobList){
			Element folder = document.createElement(XMLConstruct.getChildElementGhostFolder());
			folder.setAttribute(XMLConstruct.getAttId(), Integer.toString(folderDob.getId()));
			int parentID = folderDob.getParent().getId();
			folder.setAttribute(XMLConstruct.getAttFileName(), folderDob.getName());
			folder.setAttribute(XMLConstruct.getAttDate(), folderDob.getDate().toString());
			folder.setAttribute(XMLConstruct.getAttPath(), folderDob.getPath().toString());
			folder.setAttribute(XMLConstruct.getAttParentId(), Integer.toString(parentID));
			node.appendChild(folder);
		}		
		conn.writeToXML();
	}
	
	public void createContainerUpdateXML(List<EncryptedContainer> shareList){
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.getRootElementContainer());
		Node node = nodelist.item(0);
		for(EncryptedContainer container : shareList){
			Element element = document.createElement(XMLConstruct.getChildElementContainer());
			element.setAttribute(XMLConstruct.getAttId(), Integer.toString(container.getId()));
			element.setAttribute(XMLConstruct.getAttShareFolderId(),
					Integer.toString(container.getShareFolderId()));
			node.appendChild(element);
		}		
		conn.writeToXML();
	}
	
	public void creatShareRelationUpdateXML(List<ShareRelation> shareList){
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.getRootElementShareRelation());
		Node node = nodelist.item(0);
		for(ShareRelation shareRelation : shareList){
			Element folder = document.createElement(XMLConstruct.getChildElementShareRelation());
			folder.setAttribute(XMLConstruct.getAttShareFolderId(),
					Integer.toString(shareRelation.getSharefolderId()));
			folder.setIdAttribute(XMLConstruct.getAttShareFolderId(), true);
			folder.setAttribute(XMLConstruct.getAttMail(), shareRelation.getMail());
			node.appendChild(folder);
		}	
		conn.writeToXML();

	}
}
