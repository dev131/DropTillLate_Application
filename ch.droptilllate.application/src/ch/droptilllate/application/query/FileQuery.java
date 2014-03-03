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

import ch.droptilllate.application.com.XmlConnection;
import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.views.XMLConstruct;

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
		//TODO Id generate
		if (encryptedFileDob.getId() == null) {
			int id = (int) (Math.random() * 10000 + 1);
			// Check if it exist
			while (checkExist(id)) {
				id = (int) (Math.random() * 10000 + 1);
			}
			encryptedFileDob.setId(id);
		}
		Document document = conn.getXML();
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.getRootElementFile());
		Node node = nodelist.item(0);
		Element file = document.createElement(XMLConstruct.getChildElementFile());
		// Generate xml entry with ID
		file.setAttribute(XMLConstruct.getAttId(), Integer.toString(encryptedFileDob.getId()));
		int parentID = encryptedFileDob.getParent().getId();
		file.setAttribute(XMLConstruct.getAttFileName(), encryptedFileDob.getName());
		file.setAttribute(XMLConstruct.getAttDate(), encryptedFileDob.getDate().toString());
		file.setAttribute(XMLConstruct.getAttSize(), encryptedFileDob.getSize().toString());
		file.setAttribute(XMLConstruct.getAttType(), encryptedFileDob.getType());
		file.setAttribute(XMLConstruct.getAttPath(), encryptedFileDob.getPath());
		file.setAttribute(XMLConstruct.getAttParentId(), Integer.toString(parentID));
		file.setAttribute(XMLConstruct.getAttContainerId(), "0");
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
		NodeList nodes = conn.executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.getAttId()+"='"
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
		NodeList nodes = conn.executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.getAttParentId()+"='"
				+ folder.getId() + "']");
		for (int i = 0; i < nodes.getLength(); i++) {
			String date1 = nodes.item(i).getAttributes().getNamedItem(XMLConstruct.getAttDate())
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
					.getNamedItem(XMLConstruct.getAttSize()).getNodeValue());
			//Integer id, String name, Date date, String path, GhostFolderDob parent, String type, Long size, Integer containerId
			EncryptedFileDob fileDob = new EncryptedFileDob(
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.getAttId()).getNodeValue()), 
					nodes.item(i).getAttributes().getNamedItem(XMLConstruct.getAttFileName()).getNodeValue(), 
					sqlDate, 
					nodes.item(i).getAttributes().getNamedItem(XMLConstruct.getAttPath()).getNodeValue(), 
					folder, 
					nodes.item(i).getAttributes().getNamedItem(XMLConstruct.getAttType()).getNodeValue(),  
					size, 
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.getAttContainerId()).getNodeValue()));
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
		NodeList nodes = conn.executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.getAttId()+"='"
				+ encryptedFile.getId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			if (encryptedFile.getName() != null)
				nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.getAttFileName())
						.setNodeValue(encryptedFile.getName());
			if (encryptedFile.getDate() != null)
				nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.getAttDate())
						.setNodeValue(encryptedFile.getDate().toString());
			if (encryptedFile.getSize() != null)
				nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.getAttSize())
						.setNodeValue(encryptedFile.getSize().toString());
			if (encryptedFile.getType() != null)
				nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.getAttType())
						.setNodeValue(encryptedFile.getType());
			if (encryptedFile.getPath() != null)
				nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.getAttPath())
						.setNodeValue(encryptedFile.getPath());
			if (encryptedFile.getContainerId() >= 0)
				nodes.item(idx)
						.getAttributes()
						.getNamedItem(XMLConstruct.getAttContainerId())
						.setNodeValue(
								Integer.toString(encryptedFile.getContainerId()));
			if (encryptedFile.getParent() != null)
				nodes.item(idx)
						.getAttributes()
						.getNamedItem(XMLConstruct.getAttParentId())
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
		NodeList nodes = conn.executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.getAttId()+"='" + id
				+ "']");
		String date1 = nodes.item(0).getAttributes().getNamedItem(XMLConstruct.getAttDate())
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
				Integer.parseInt(nodes.item(0).getAttributes().getNamedItem(XMLConstruct.getAttId()).getNodeValue()), 
				nodes.item(0).getAttributes().getNamedItem(XMLConstruct.getAttFileName()).getNodeValue(), 
				sqlDate, 
				nodes.item(0).getAttributes().getNamedItem(XMLConstruct.getAttPath()).getNodeValue(), 
				null, 
				nodes.item(0).getAttributes().getNamedItem(XMLConstruct.getAttType()).getNodeValue(),  
				Long.parseLong(nodes.item(0).getAttributes().getNamedItem(XMLConstruct.getAttSize()).getNodeValue()),
				Integer.parseInt(nodes.item(0).getAttributes().getNamedItem(XMLConstruct.getAttContainerId()).getNodeValue()));
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
		NodeList nodes = conn.executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.getAttId()+"='"
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
			NodeList nodes = conn.executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.getAttId()+"='"
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
		NodeList nodes = conn.executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.getAttContainerId()+"='"
				+ containerid + "']");
		for (int i = 0; i < nodes.getLength(); i++) {					 
			ids.add(Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.getAttId()).getNodeValue()));
		}
		return ids;
	}
	
	public List<EncryptedFileDob> getFileByContainerId(Integer containerid){
		List<EncryptedFileDob> files = new ArrayList<EncryptedFileDob>();
		Document document = conn.getXML();
		NodeList nodes = conn.executeQuery(XMLConstruct.getFileExpression()+ "[@"+XMLConstruct.getAttContainerId()+"='"
				+ containerid + "']");
		for (int i = 0; i < nodes.getLength(); i++) {
			String date1 = nodes.item(i).getAttributes().getNamedItem(XMLConstruct.getAttDate())
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
					.getNamedItem(XMLConstruct.getAttSize()).getNodeValue());
			//Integer id, String name, Date date, String path, GhostFolderDob parent, String type, Long size, Integer containerId
			EncryptedFileDob fileDob = new EncryptedFileDob(
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.getAttId()).getNodeValue()), 
					nodes.item(i).getAttributes().getNamedItem(XMLConstruct.getAttFileName()).getNodeValue(), 
					sqlDate, 
					nodes.item(i).getAttributes().getNamedItem(XMLConstruct.getAttPath()).getNodeValue(), 
					null, 
					nodes.item(i).getAttributes().getNamedItem(XMLConstruct.getAttType()).getNodeValue(),  
					size, 
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.getAttContainerId()).getNodeValue()));
			files.add(fileDob);
		}
		return files;		
	}
}
