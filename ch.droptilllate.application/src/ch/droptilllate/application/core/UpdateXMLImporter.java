package ch.droptilllate.application.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import ch.droptilllate.application.com.XmlConnection;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.views.XMLConstruct;

public class UpdateXMLImporter {
	private XmlConnection conn;
	private Document document;
	
	public UpdateXMLImporter(String key){
		conn = new XmlConnection(false, key);
		document = conn.getXML();
	}
	public List<EncryptedFileDob> getFileUpdateXML(){
		List<EncryptedFileDob> files = new ArrayList<EncryptedFileDob>();	
		//String xpath= "/collection/file";

		NodeList nodelist = document.getElementsByTagName(XMLConstruct.getChildElementFile());
		for (int i = 0; i < nodelist.getLength(); i++) {
			String date1 = nodelist.item(i).getAttributes().getNamedItem(XMLConstruct.getAttDate())
					.getNodeValue().toString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parsed = null;
			try {
				parsed = format.parse(date1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
			long size = Long.parseLong(nodelist.item(i).getAttributes()
					.getNamedItem(XMLConstruct.getAttSize()).getNodeValue());
			//Integer id, String name, Date date, String path, GhostFolderDob parent, String type, Long size, Integer containerId
			EncryptedFileDob fileDob = new EncryptedFileDob(
					Integer.parseInt(nodelist.item(i).getAttributes().getNamedItem(XMLConstruct.getAttId()).getNodeValue()), 
					nodelist.item(i).getAttributes().getNamedItem(XMLConstruct.getAttFileName()).getNodeValue(), 
					sqlDate, 
					nodelist.item(i).getAttributes().getNamedItem(XMLConstruct.getAttPath()).getNodeValue(), 
					null, 
					nodelist.item(i).getAttributes().getNamedItem(XMLConstruct.getAttType()).getNodeValue(),  
					size, 
					Integer.parseInt(nodelist.item(i).getAttributes().getNamedItem(XMLConstruct.getAttContainerId()).getNodeValue()));
			files.add(fileDob);
		}
		return files;
	}
	
	public List<GhostFolderDob> getFolderUpdateXML(){
		List<GhostFolderDob> folders = new ArrayList<GhostFolderDob>();
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.getChildElementGhostFolder());
		// cast the result to a DOM NodeList
		for (int i = 0; i < nodelist.getLength(); i++) {
			String date1 = nodelist.item(i).getAttributes().getNamedItem(XMLConstruct.getAttDate())
					.getNodeValue().toString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parsed = null;
			try {
				parsed = format.parse(date1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
			//Integer id, String name, Date date, String path, GhostFolderDob parent
			GhostFolderDob dob = new GhostFolderDob(
					Integer.parseInt(nodelist.item(i).getAttributes().getNamedItem(XMLConstruct.getAttId()).getNodeValue()),
					nodelist.item(i).getAttributes().getNamedItem(XMLConstruct.getAttFolderName()).getNodeValue(), 
					sqlDate, 
					nodelist.item(i).getAttributes().getNamedItem(XMLConstruct.getAttPath()).getNodeValue(),
					null);			
			folders.add(dob);
		}
		return folders;
	}
	
	public List<EncryptedContainer> getContainerUpdateXML(){
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.getChildElementContainer());
		// cast the result to a DOM NodeList
		List<EncryptedContainer> containerList = new ArrayList<EncryptedContainer>();
		for (int idx = 0; idx < nodelist.getLength(); idx++) {
			EncryptedContainer container = new EncryptedContainer(
					Integer.parseInt(nodelist.item(idx).getAttributes().getNamedItem(XMLConstruct.getAttId()).getNodeValue()),
					Integer.parseInt(nodelist.item(idx).getAttributes().getNamedItem(XMLConstruct.getAttShareFolderId()).getNodeValue())
					);
			containerList.add(container);
		}
		return containerList;
	}
	
	public List<ShareRelation> getShareRelationUpdateXML(){
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.getChildElementShareRelation());
		List<ShareRelation> shareList = new ArrayList<ShareRelation>();
		for (int idx = 0; idx < nodelist.getLength(); idx++) {
			ShareRelation tmp = new ShareRelation(
					Integer.parseInt(nodelist.item(idx).getAttributes().getNamedItem(XMLConstruct.getAttShareFolderId()).getNodeValue()),
					nodelist.item(idx).getAttributes().getNamedItem(XMLConstruct.getAttMail()).getNodeValue()
					);
			shareList.add(tmp);
		}
		return shareList;
	}
}
