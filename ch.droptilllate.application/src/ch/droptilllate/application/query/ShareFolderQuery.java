package ch.droptilllate.application.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.droptilllate.application.com.IXmlConnection;
import ch.droptilllate.application.com.XmlConnection;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.info.CRUDCryptedFileResult;
import ch.droptilllate.application.info.CRUDShareFolderResult;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.views.Messages;

public class ShareFolderQuery {
	private Document document;
	private IXmlConnection conn;
	private String rootElement = "collection";
	private String childElement = "sharefolder";

	public ShareFolderQuery() {
		conn = new XmlConnection(Messages.getPathShareFolderXML(), rootElement);

	}

	/**
	 * New ShareFolder entry
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public ShareFolder newShareFolder(ShareFolder sharefolder) {
		document = conn.getXML();
		if (sharefolder.getID() == null) {
			int id = (int) (Math.random() * 10000 + 1);
			// Check if it exist
			while (checkExist(id)) {
				id = (int) (Math.random() * 10000 + 1);
			}
			sharefolder.setID(id);
		}	
		Node node = document.getFirstChild();
		Element folder = document.createElement(childElement);
		folder.setAttribute("id", Integer.toString(sharefolder.getID()));
		folder.setIdAttribute("id", true);
		folder.setAttribute("key", sharefolder.getKey());
		folder.setAttribute("path", sharefolder.getPath());
		node.appendChild(folder);
		conn.writeToXML();
		return sharefolder;
	}

	private boolean checkExist(int sharefolderID) {
		boolean result = false;
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery("//" + childElement + "[@id='"
				+ sharefolderID + "']");
		if (nodes.getLength() > 0)
			result = true;
		return result;
	}

	public ShareFolder getShareFolder(int sharefolderID) {
		conn.getXML();
		// cast the result to a DOM NodeList

		NodeList nodes = conn.executeQuery("//" + childElement + "[@id='"
				+ sharefolderID + "']");
		ShareFolder sharefolder = null;
		if (nodes.getLength() > 0) {
			sharefolder = new ShareFolder(sharefolderID, nodes.item(0)
					.getAttributes().getNamedItem("path").getNodeValue()
					.toString(), nodes.item(0).getAttributes()
					.getNamedItem("key").getNodeValue().toString());
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
		NodeList nodes = conn.executeQuery("//" + childElement + "[@id='"
				+ sharefolder.getID() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getAttributes().getNamedItem("key")
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
		NodeList nodes = conn.executeQuery("//" + childElement + "[@id='"
				+ shareFolder.getID() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getParentNode().removeChild(nodes.item(idx));
		}
		}
		conn.writeToXML();
		return true;
	}

	public CRUDShareFolderResult checkDatabase(List<ShareFolder> shareFolderList) {
		conn.getXML();
		List<ShareFolder> shareFolderSuccessList = new ArrayList<ShareFolder>();
		List<ShareFolder> shareFolderErrorList = new ArrayList<ShareFolder>();
		Iterator<ShareFolder> shareFolderInfoListIterator = shareFolderList
				.iterator();
		while (shareFolderInfoListIterator.hasNext()) {
			NodeList nodes = conn.executeQuery("//" + childElement + "[@id='"
					+ shareFolderInfoListIterator.next().getID() + "']");
			if (nodes.getLength() > 0) {
				shareFolderSuccessList.add(shareFolderInfoListIterator.next());
			} else {
				shareFolderErrorList.add(shareFolderInfoListIterator.next());
			}
		}
		CRUDShareFolderResult result = new CRUDShareFolderResult();
		result.setShareFolderListError(shareFolderErrorList);
		result.setShareFolderListSuccess(shareFolderSuccessList);
		return result;

	}

}
