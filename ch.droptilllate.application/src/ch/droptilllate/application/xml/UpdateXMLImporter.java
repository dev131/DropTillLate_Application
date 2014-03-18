package ch.droptilllate.application.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.properties.XMLConstruct;

public class UpdateXMLImporter {
	private XmlConnection conn;
	private Document document;
	
	public UpdateXMLImporter(String key){
		this.conn = new XmlConnection(false, key);
		this.document = this.conn.getXML();
	}
	public List<EncryptedFileDob> getFileUpdateXML(){
		List<EncryptedFileDob> files = new ArrayList<EncryptedFileDob>();	
		//String xpath= "/collection/file";

		NodeList nodelist = this.document.getElementsByTagName(XMLConstruct.ChildElementFile);
		for (int i = 0; i < nodelist.getLength(); i++) {
			String date1 = nodelist.item(i).getAttributes().getNamedItem(XMLConstruct.AttDate)
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
					.getNamedItem(XMLConstruct.AttSize).getNodeValue());
			//Integer id, String name, Date date, String path, GhostFolderDob parent, String type, Long size, Integer containerId
			EncryptedFileDob fileDob = new EncryptedFileDob(
					Integer.parseInt(nodelist.item(i).getAttributes().getNamedItem(XMLConstruct.AttId).getNodeValue()), 
					nodelist.item(i).getAttributes().getNamedItem(XMLConstruct.AttFileName).getNodeValue(), 
					sqlDate, 
					"", 
					null, 
					size, 
					Integer.parseInt(nodelist.item(i).getAttributes().getNamedItem(XMLConstruct.AttContainerId).getNodeValue()));
			files.add(fileDob);
		}
		return files;
	}
	
	public List<GhostFolderDob> getFolderUpdateXML(){
		List<GhostFolderDob> folders = new ArrayList<GhostFolderDob>();
		NodeList nodelist = this.document.getElementsByTagName(XMLConstruct.ChildElementGhostFolder);
		// cast the result to a DOM NodeList
		for (int i = 0; i < nodelist.getLength(); i++) {
			//Integer id, String name, GhostFolderDob parent
			GhostFolderDob dob = new GhostFolderDob(
					Integer.parseInt(nodelist.item(i).getAttributes().getNamedItem(XMLConstruct.AttId).getNodeValue()),
					nodelist.item(i).getAttributes().getNamedItem(XMLConstruct.AttFolderName).getNodeValue(), 
					null);			
			folders.add(dob);
		}
		return folders;
	}
	
	public List<EncryptedContainer> getContainerUpdateXML(){
		NodeList nodelist = this.document.getElementsByTagName(XMLConstruct.ChildElementContainer);
		// cast the result to a DOM NodeList
		List<EncryptedContainer> containerList = new ArrayList<EncryptedContainer>();
		for (int idx = 0; idx < nodelist.getLength(); idx++) {
			EncryptedContainer container = new EncryptedContainer(
					Integer.parseInt(nodelist.item(idx).getAttributes().getNamedItem(XMLConstruct.AttId).getNodeValue()),
					Integer.parseInt(nodelist.item(idx).getAttributes().getNamedItem(XMLConstruct.AttShareFolderId).getNodeValue())
					);
			containerList.add(container);
		}
		return containerList;
	}
	
	public List<ShareRelation> getShareRelationUpdateXML(){
		NodeList nodelist = this.document.getElementsByTagName(XMLConstruct.ChildElementShareRelation);
		List<ShareRelation> shareList = new ArrayList<ShareRelation>();
		for (int idx = 0; idx < nodelist.getLength(); idx++) {
			ShareRelation tmp = new ShareRelation(
					Integer.parseInt(nodelist.item(idx).getAttributes().getNamedItem(XMLConstruct.AttShareFolderId).getNodeValue()),
					nodelist.item(idx).getAttributes().getNamedItem(XMLConstruct.AttMail).getNodeValue()
					);
			shareList.add(tmp);
		}
		return shareList;
	}
}
