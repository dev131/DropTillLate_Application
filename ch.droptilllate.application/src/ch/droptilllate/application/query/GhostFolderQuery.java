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
import ch.droptilllate.application.info.CRUDGhostFolderInfo;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.views.XMLConstruct;

public class GhostFolderQuery {
	private XmlConnection conn;

	public GhostFolderQuery(String key) {
		conn = new XmlConnection(true, key);
	}

	/**
	 * New Folder entry
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public GhostFolderDob newFolder(GhostFolderDob folderDob) {
		// TODO id generate
		if(folderDob.getId() == null){
			int id = (int) (Math.random() * 10000 + 1);
			// Check if it exist
			while (checkExist(id)) {
				id = (int) (Math.random() * 10000 + 1);
			}
			folderDob.setId(id);
		}		
		Document document = conn.getXML();
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.getRootElementGhostFolder());
		Node node = nodelist.item(0);
		// TODO Generate ID and Check if it exist
		Element folder = document.createElement(XMLConstruct.getChildElementGhostFolder());
		folder.setAttribute(XMLConstruct.getAttId(), Integer.toString(folderDob.getId()));
		int parentID = folderDob.getParent().getId();
		folder.setAttribute(XMLConstruct.getAttFolderName(), folderDob.getName());
		folder.setAttribute(XMLConstruct.getAttDate(), folderDob.getDate().toString());
		folder.setAttribute(XMLConstruct.getAttPath(), folderDob.getPath().toString());
		folder.setAttribute(XMLConstruct.getAttParentId(), Integer.toString(parentID));
		node.appendChild(folder);

		conn.writeToXML();
		return folderDob;
	}

	private boolean checkExist(int folderID) {
		Document document = conn.getXML();
		boolean result = false;
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery(XMLConstruct.getGhostFolderExpression()+"[@"+XMLConstruct.getAttId()+"='"
				+ folderID + "']");
		if(nodes.getLength()>0)
			result = true;
		return result;
	}

	/**
	 * get Folder in Folder
	 * 
	 * @param folder
	 * @return
	 */
	public List<GhostFolderDob> getFolderInFolder(GhostFolderDob folder) {
		List<GhostFolderDob> folders = new ArrayList<GhostFolderDob>();
		Document document = conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery(XMLConstruct.getGhostFolderExpression()+ "[@"+XMLConstruct.getAttParentId()+"='"
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
			//Integer id, String name, Date date, String path, GhostFolderDob parent
			GhostFolderDob dob = new GhostFolderDob(
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.getAttId()).getNodeValue()),
					nodes.item(i).getAttributes().getNamedItem(XMLConstruct.getAttFolderName()).getNodeValue(), 
					sqlDate, 
					nodes.item(i).getAttributes().getNamedItem(XMLConstruct.getAttPath()).getNodeValue(),
					folder);			
			folders.add(dob);
		}
		return folders;
	}

	/**
	 * UpdateFolder
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public void updateFolder(GhostFolderDob encryptedFolder) {
		int newParentID = encryptedFolder.getParent().getId();
		Document document = conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery(XMLConstruct.getGhostFolderExpression()+ "[@"+XMLConstruct.getAttId()+"='"
				+ encryptedFolder.getId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.getAttParentId())
					.setNodeValue(Integer.toString(newParentID));
		}
		System.out.println("Everything replaced.");
		// save xml file back
		conn.writeToXML();
	}

	/**
	 * DeleteFolder
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public void deleteFolder(List<GhostFolderDob> encryptedFolder) {
		Document document = conn.getXML();
		for(GhostFolderDob dob : encryptedFolder){
		NodeList nodes = conn.executeQuery(XMLConstruct.getGhostFolderExpression()+ "[@"+XMLConstruct.getAttId()+"='"
				+ dob.getId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getParentNode().removeChild(nodes.item(idx));
		}
		}
		conn.writeToXML();
	}
	/**
	 * CheckDatabase ErrorList=not exist, SuccessList= exist
	 * @param folderDob
	 * @return FolderCRUDResults 
	 */
	public CRUDGhostFolderInfo checkDatabase(List<GhostFolderDob> folderDob){
		Document document = conn.getXML();
		List<GhostFolderDob> folderSuccessList = new ArrayList<GhostFolderDob>();
		List<GhostFolderDob> folderErrorList = new ArrayList<GhostFolderDob>();
		Iterator<GhostFolderDob> folderInfoListErrorIterator = folderDob.iterator();		
		while (folderInfoListErrorIterator.hasNext()) {
			NodeList nodes = conn.executeQuery(XMLConstruct.getGhostFolderExpression()+ "[@"+XMLConstruct.getAttId()+"='"
					+ folderInfoListErrorIterator.next().getId() + "']");
			if(nodes.getLength()>0){
				folderSuccessList.add(folderInfoListErrorIterator.next());
			}
			else{
				folderErrorList.add(folderInfoListErrorIterator.next());
			}
		}
		CRUDGhostFolderInfo result = new CRUDGhostFolderInfo();
		result.setEncryptedFolderListError(folderErrorList);
		result.setEncryptedFolderListSuccess(folderSuccessList);
		return result;
	}
}
