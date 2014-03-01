package ch.droptilllate.application.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.NodeList;

import ch.droptilllate.application.com.IXmlConnection;
import ch.droptilllate.application.com.XmlConnection;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;

public class UpdateXMLImporter {

	private String rootElement = "collection";
	private String childElementFile = "file";
	private String childElementFolder = "folder";
	private String childElementContainer= "container";
	private String childElementShareRelation = "share";
	
	public List<EncryptedFileDob> getFileUpdateXML(String xmlPath){
		List<EncryptedFileDob> files = new ArrayList<EncryptedFileDob>();
		IXmlConnection conn = new XmlConnection(xmlPath, rootElement);
		//String xpath= "/collection/file";
		conn.getXML();
		NodeList nodes = conn.executeQuery("//" + childElementFile);
		for (int i = 0; i < nodes.getLength(); i++) {
			String date1 = nodes.item(i).getAttributes().getNamedItem("date")
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
					.getNamedItem("size").getNodeValue());
			//Integer id, String name, Date date, String path, GhostFolderDob parent, String type, Long size, Integer containerId
			EncryptedFileDob fileDob = new EncryptedFileDob(
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem("id").getNodeValue()), 
					nodes.item(i).getAttributes().getNamedItem("name").getNodeValue(), 
					sqlDate, 
					nodes.item(i).getAttributes().getNamedItem("path").getNodeValue(), 
					null, 
					nodes.item(i).getAttributes().getNamedItem("type").getNodeValue(),  
					size, 
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem("containerID").getNodeValue()));
			files.add(fileDob);
		}
		return files;
	}
	
	public List<GhostFolderDob> getFolderUpdateXML(String xmlPath){
		List<GhostFolderDob> folders = new ArrayList<GhostFolderDob>();
		IXmlConnection conn = new XmlConnection(xmlPath, rootElement);
		conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery("//" + childElementFolder);
		for (int i = 0; i < nodes.getLength(); i++) {
			String date1 = nodes.item(i).getAttributes().getNamedItem("date")
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
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem("id").getNodeValue()),
					nodes.item(i).getAttributes().getNamedItem("name").getNodeValue(), 
					sqlDate, 
					nodes.item(i).getAttributes().getNamedItem("path").getNodeValue(),
					null);			
			folders.add(dob);
		}
		return folders;
	}
	
	public List<EncryptedContainer> getContainerUpdateXML(String xmlPath){
		IXmlConnection conn = new XmlConnection(xmlPath, rootElement);
		conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery("//" + childElementContainer);
		List<EncryptedContainer> containerList = new ArrayList<EncryptedContainer>();
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			EncryptedContainer container = new EncryptedContainer(
					Integer.parseInt(nodes.item(idx).getAttributes().getNamedItem("id").getNodeValue()),
					Integer.parseInt(nodes.item(idx).getAttributes().getNamedItem("shareFolderID").getNodeValue())
					);
			containerList.add(container);
		}
		return containerList;
	}
	
	public List<ShareRelation> getShareRelationUpdateXML(String xmlPath){
		IXmlConnection conn = new XmlConnection(xmlPath, rootElement);
		conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery("//" + childElementShareRelation);
		List<ShareRelation> shareList = new ArrayList<ShareRelation>();
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			ShareRelation tmp = new ShareRelation(
					Integer.parseInt(nodes.item(idx).getAttributes().getNamedItem("sharefolderId").getNodeValue()),
					nodes.item(idx).getAttributes().getNamedItem("mail").getNodeValue()
					);
			shareList.add(tmp);
		}
		return shareList;
	}
}
