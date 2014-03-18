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

import ch.droptilllate.application.info.CRUDGhostFolderInfo;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.application.xml.XmlConnection;

public class GhostFolderQuery {
	private XmlConnection conn;

	public GhostFolderQuery(String key) {
		this.conn = new XmlConnection(true, key);
	}

	/**
	 * New Folder entry
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public GhostFolderDob newFolder(GhostFolderDob folderDob) {
		// id generate
		if(folderDob.getId() == null){
			int id = (int) (Math.random() * Messages.getIdSize() + 1);
			// Check if it exist
			while (checkExist(id)) {
				id = (int) (Math.random() * Messages.getIdSize() + 1);
			}
			folderDob.setId(id);
		}		
		Document document = this.conn.getXML();
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.RootElementGhostFolder);
		Node node = nodelist.item(0);
		// TODO Generate ID and Check if it exist
		Element folder = document.createElement(XMLConstruct.ChildElementGhostFolder);
		folder.setAttribute(XMLConstruct.AttId, Integer.toString(folderDob.getId()));
		int parentID = folderDob.getParent().getId();
		folder.setAttribute(XMLConstruct.AttFolderName, folderDob.getName());
		folder.setAttribute(XMLConstruct.AttParentId, Integer.toString(parentID));
		node.appendChild(folder);

		this.conn.writeToXML();
		return folderDob;
	}

	private boolean checkExist(int folderID) {
		this.conn.getXML();
		boolean result = false;
		// cast the result to a DOM NodeList
		NodeList nodes = this.conn.executeQuery(XMLConstruct.getGhostFolderExpression()+"[@"+XMLConstruct.AttId+"='"
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
		this.conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = this.conn.executeQuery(XMLConstruct.getGhostFolderExpression()+ "[@"+XMLConstruct.AttParentId+"='"
				+ folder.getId() + "']");
		for (int i = 0; i < nodes.getLength(); i++) {
			//Integer id, String name, GhostFolderDob parent
			GhostFolderDob dob = new GhostFolderDob(
					Integer.parseInt(nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttId).getNodeValue()),
					nodes.item(i).getAttributes().getNamedItem(XMLConstruct.AttFolderName).getNodeValue(), 
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
		this.conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = this.conn.executeQuery(XMLConstruct.getGhostFolderExpression()+ "[@"+XMLConstruct.AttId+"='"
				+ encryptedFolder.getId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttParentId)
					.setNodeValue(Integer.toString(newParentID));
		}
		System.out.println("Everything replaced.");
		// save xml file back
		this.conn.writeToXML();
	}

	/**
	 * DeleteFolder
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public void deleteFolder(List<GhostFolderDob> encryptedFolder) {
		this.conn.getXML();
		for(GhostFolderDob dob : encryptedFolder){
		NodeList nodes = this.conn.executeQuery(XMLConstruct.getGhostFolderExpression()+ "[@"+XMLConstruct.AttId+"='"
				+ dob.getId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getParentNode().removeChild(nodes.item(idx));
		}
		}
		this.conn.writeToXML();
	}
	/**
	 * CheckDatabase ErrorList=not exist, SuccessList= exist
	 * @param folderDob
	 * @return FolderCRUDResults 
	 */
	public CRUDGhostFolderInfo checkDatabase(List<GhostFolderDob> folderDob){
		this.conn.getXML();
		List<GhostFolderDob> folderSuccessList = new ArrayList<GhostFolderDob>();
		List<GhostFolderDob> folderErrorList = new ArrayList<GhostFolderDob>();
		Iterator<GhostFolderDob> folderInfoListErrorIterator = folderDob.iterator();		
		while (folderInfoListErrorIterator.hasNext()) {
			NodeList nodes = this.conn.executeQuery(XMLConstruct.getGhostFolderExpression()+ "[@"+XMLConstruct.AttId+"='"
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
