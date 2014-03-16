package ch.droptilllate.application.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.info.CRUDShareFolderInfo;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.application.xml.XmlConnection;

public class ShareFolderQuery {
	private XmlConnection conn;;

	public ShareFolderQuery(String key) {
		conn = new XmlConnection(true, key);
		
	}

	/**
	 * New ShareFolder entry
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public ShareFolder newShareFolder(ShareFolder sharefolder) {
		Document document = conn.getXML();
		if (sharefolder.getID() == null) {
			int id = (int) (Math.random() * Messages.getIdSize() + 1);
			// Check if it exist
			while (checkExist(id)) {
				id = (int) (Math.random() * Messages.getIdSize() + 1);
			}
			sharefolder.setID(id);
		}
		else{
			if(checkExist(sharefolder.getID())){
				return sharefolder;
			}
		}
		Node node = document.getFirstChild();
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.RootElementShareFolder);
		node = nodelist.item(0);
		Element folder = document.createElement(XMLConstruct.ChildElementShareFolder);
		folder.setAttribute(XMLConstruct.AttId, Integer.toString(sharefolder.getID()));
		folder.setAttribute(XMLConstruct.AttKey, sharefolder.getKey());
		node.appendChild(folder);
		conn.writeToXML();
		return sharefolder;
	}

	private boolean checkExist(int sharefolderID) {
		boolean result = false;
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery(XMLConstruct.getShareFolderExpression()+ "[@"+XMLConstruct.AttId+"='"
				+ sharefolderID + "']");
		if (nodes.getLength() > 0)
			result = true;
		return result;
	}

	public ShareFolder getShareFolder(int sharefolderID) {
		conn.getXML();
		NodeList nodes = conn.executeQuery(XMLConstruct.getShareFolderExpression()+ "[@"+XMLConstruct.AttId+"='"
				+ sharefolderID + "']");
		ShareFolder sharefolder = null;
		if (nodes.getLength() > 0) {
			sharefolder = new ShareFolder(sharefolderID, nodes.item(0).getAttributes()
					.getNamedItem(XMLConstruct.AttKey).getNodeValue().toString());
		}
		return sharefolder;
	}

	/**
	 * UpdateFolder
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public boolean updateShareFolder(ShareFolder sharefolder) {
		conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery(XMLConstruct.getShareFolderExpression()+ "[@"+XMLConstruct.AttId+"='"
				+ sharefolder.getID() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttKey)
					.setNodeValue(sharefolder.getKey());
		}
		System.out.println("Everything updated.");
		// save xml file back
		conn.writeToXML();
		return true;
	}

	/**
	 * DeleteFolder
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public boolean deleteShareFolder(List<ShareFolder> sharefolder) {
		conn.getXML();
		for(ShareFolder shareFolder : sharefolder){
		NodeList nodes = conn.executeQuery(XMLConstruct.getShareFolderExpression()+ "[@"+XMLConstruct.AttId+"='"
				+ shareFolder.getID() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getParentNode().removeChild(nodes.item(idx));
		}
		}
		conn.writeToXML();
		return true;
	}

	public CRUDShareFolderInfo checkDatabase(List<ShareFolder> shareFolderList) {
		conn.getXML();
		List<ShareFolder> shareFolderSuccessList = new ArrayList<ShareFolder>();
		List<ShareFolder> shareFolderErrorList = new ArrayList<ShareFolder>();
		Iterator<ShareFolder> shareFolderInfoListIterator = shareFolderList
				.iterator();
		while (shareFolderInfoListIterator.hasNext()) {
			NodeList nodes = conn.executeQuery(XMLConstruct.getShareFolderExpression()+ "[@"+XMLConstruct.AttId+"='"
					+ shareFolderInfoListIterator.next().getID() + "']");
			if (nodes.getLength() > 0) {
				shareFolderSuccessList.add(shareFolderInfoListIterator.next());
			} else {
				shareFolderErrorList.add(shareFolderInfoListIterator.next());
			}
		}
		CRUDShareFolderInfo result = new CRUDShareFolderInfo();
		result.setShareFolderListError(shareFolderErrorList);
		result.setShareFolderListSuccess(shareFolderSuccessList);
		return result;

	}

}
