package ch.droptilllate.application.query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.application.xml.XmlConnection;

public class FileQuery {
	private XmlConnection conn;

	public FileQuery(String key) {
		conn = new XmlConnection(true, key);
	}

	/**
	 * New File Entry
	 * 
	 * @param encryptedFile
	 * @return
	 */
	public EncryptedFileDob newFile(EncryptedFileDob encryptedFileDob) {
		// Id generate
		String parentID = "";
		if (encryptedFileDob.getId() == null) {
			int id = (int) (Math.random() * Messages.getIdSize() + 1);
			// Check if it exist
			while (checkExist(id)) {
				id = (int) (Math.random() * Messages.getIdSize() + 1);
			}
			encryptedFileDob.setId(id);
		}
		Document document = conn.getXML();
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.RootElementFile);
		Node node = nodelist.item(0);
		Element file = document.createElement(XMLConstruct.ChildElementFile);
		// Generate xml entry with ID
		file.setAttribute(XMLConstruct.AttId, Integer.toString(encryptedFileDob.getId()));
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
		conn.writeToXML();
		return encryptedFileDob;
	}

	/**
	 * Check if it already exist
	 * 
	 * @param fileID
	 * @return
	 */
	private boolean checkExist(int fileID) {
		Document document = conn.getXML();
		boolean result = false;
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.AttId+"='"
				+ fileID + "']");
		int i = nodes.getLength();
		if (nodes.getLength() > 0)
			result = true;
		return result;
	}

	/**
	 * Get files from folder
	 * 
	 * @param folder
	 * @return
	 */
	public List<EncryptedFileDob> getFiles(GhostFolderDob folder) {
		List<EncryptedFileDob> files = new ArrayList<EncryptedFileDob>();
		Document document = conn.getXML();
		NodeList nodes = conn.executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.AttParentId+"='"
				+ folder.getId() + "']");
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
					folder,   
					size, 
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttContainerId).getNodeValue()));
			files.add(fileDob);
		}
		return files;
	}

	/**
	 * update Fileinfos
	 * 
	 * @param encryptedFile
	 * @return
	 */
	public boolean updateFile(EncryptedFileDob encryptedFile) {
		Document document = conn.getXML();
		NodeList nodes = conn.executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.AttId+"='"
				+ encryptedFile.getId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			if (encryptedFile.getName() != null)
				nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttFileName)
						.setNodeValue(encryptedFile.getName());
			if (encryptedFile.getDate() != null)
				nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttDate)
						.setNodeValue(encryptedFile.getDate().toString());
			if (encryptedFile.getSize() != null)
				nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttSize)
						.setNodeValue(encryptedFile.getSize().toString());
			if (encryptedFile.getType() != null)
				nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttType)
						.setNodeValue(encryptedFile.getType());
			if (encryptedFile.getContainerId() >= 0)
				nodes.item(idx)
						.getAttributes()
						.getNamedItem(XMLConstruct.AttContainerId)
						.setNodeValue(
								Integer.toString(encryptedFile.getContainerId()));
			if (encryptedFile.getParent() != null)
				nodes.item(idx)
						.getAttributes()
						.getNamedItem(XMLConstruct.AttParentId)
						.setNodeValue(
								Integer.toString(encryptedFile.getParent()
										.getId()));
		}
		System.out.println("Everything updated.");
		// save xml file back
		conn.writeToXML();
		return true;
	}

	public EncryptedFileDob getFile(int id) {
		Document document = conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.AttId+"='" + id
				+ "']");
		String date1 = nodes.item(0).getAttributes().getNamedItem(XMLConstruct.AttDate)
				.getNodeValue().toString();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date parsed = null;
		try {
			parsed = format.parse(date1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
		//Integer id, String name, Date date, String path, GhostFolderDob parent, String type, Long size, Integer containerId
		EncryptedFileDob fileDob = new EncryptedFileDob(
				Integer.parseInt(nodes.item(0).getAttributes().getNamedItem(XMLConstruct.AttId).getNodeValue()), 
				nodes.item(0).getAttributes().getNamedItem(XMLConstruct.AttFileName).getNodeValue(), 
				sqlDate, 
				"", 
				null, 
				Long.parseLong(nodes.item(0).getAttributes().getNamedItem(XMLConstruct.AttSize).getNodeValue()),
				Integer.parseInt(nodes.item(0).getAttributes().getNamedItem(XMLConstruct.AttContainerId).getNodeValue()));
		System.out.println("Everything replaced.");
		// save xml file back
		// writeToXML();
		return fileDob;
	}

	/**
	 * Delete Files
	 * 
	 * @param encryptedFile
	 * @return
	 */
	public boolean deleteFile(List<EncryptedFileDob> encryptedFile) {
		Document document = conn.getXML();
		for(EncryptedFileDob fileDob : encryptedFile){
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.AttId+"='"
				+ fileDob.getId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getParentNode().removeChild(nodes.item(idx));
		}
		System.out.println("Everything replaced.");
		// save xml file back
		}
		conn.writeToXML();
		return true;
	}

	public CRUDCryptedFileInfo checkDatabase(
			List<EncryptedFileDob> encryptedFileDob) {
		Document document = conn.getXML();
		List<EncryptedFileDob> fileSuccessList = new ArrayList<EncryptedFileDob>();
		List<EncryptedFileDob> fileErrorList = new ArrayList<EncryptedFileDob>();
		Iterator<EncryptedFileDob> fileInfoListIterator = encryptedFileDob
				.iterator();
		while (fileInfoListIterator.hasNext()) {
			NodeList nodes = conn.executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.AttId+"='"
					+ fileInfoListIterator.next().getId() + "']");
			if (nodes.getLength() > 0) {
				fileSuccessList.add(fileInfoListIterator.next());
			} else {
				fileErrorList.add(fileInfoListIterator.next());
			}
		}
		CRUDCryptedFileInfo result = new CRUDCryptedFileInfo();
		result.setEncryptedFileListError(fileErrorList);
		result.setEncryptedFileListSuccess(fileSuccessList);
		return result;
	}

	public Object getFileIdsByContainerId(Integer containerid) {
		List<Integer> ids = new ArrayList<Integer>();
		Document document = conn.getXML();
		NodeList nodes = conn.executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.AttContainerId+"='"
				+ containerid + "']");
		for (int i = 0; i < nodes.getLength(); i++) {					 
			ids.add(Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttId).getNodeValue()));
		}
		return ids;
	}
	
	public List<EncryptedFileDob> getFileByContainerId(Integer containerid){
		List<EncryptedFileDob> files = new ArrayList<EncryptedFileDob>();
		Document document = conn.getXML();
		NodeList nodes = conn.executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.AttContainerId+"='"
				+ containerid + "']");
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
}
