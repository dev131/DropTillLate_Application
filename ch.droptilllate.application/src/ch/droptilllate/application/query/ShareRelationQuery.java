package ch.droptilllate.application.query;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.droptilllate.application.com.IXmlConnection;
import ch.droptilllate.application.com.XmlConnection;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.info.CRUDShareRelationInfo;
import ch.droptilllate.application.views.Messages;

public class ShareRelationQuery {

	private Document document;
	private IXmlConnection conn;
	private String rootElement = "collection";
	private String childElement = "share";
	private String shareFolderIdAttribut = "sharefolderId";
	private String mailAttribut = "mail";

	public ShareRelationQuery() {
		conn = new XmlConnection(Messages.getPathShareRealtionXML(),
				rootElement);

	}

	/**
	 * New ShareFolder entry
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public ShareRelation newShareFolder(ShareRelation shareRelation) {
		document = conn.getXML();
		Node node = document.getFirstChild();
		Element folder = document.createElement(childElement);
		folder.setAttribute(shareFolderIdAttribut,
				Integer.toString(shareRelation.getSharefolderId()));
		folder.setIdAttribute(shareFolderIdAttribut, true);
		folder.setAttribute(mailAttribut, shareRelation.getMail());
		node.appendChild(folder);
		conn.writeToXML();
		return shareRelation;
	}

	public List<ShareRelation> getShareRelations(int shareRelationId) {
		List<ShareRelation> shareRelations = new ArrayList<ShareRelation>();
		document = conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery("//" + childElement + "[@"
				+ shareFolderIdAttribut + "='" + shareRelationId + "']");
		for (int i = 0; i < nodes.getLength(); i++) {
			//Integer sharefolderId, String mail
			ShareRelation tmp = new ShareRelation(Integer.parseInt(nodes.item(i).getAttributes()
					.getNamedItem(shareFolderIdAttribut).getNodeValue()),
					nodes.item(i).getAttributes()
					.getNamedItem(mailAttribut).getNodeValue()
					);
			shareRelations.add(tmp);
		}
		return shareRelations;
	}

	/**
	 * UpdateFolder
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public boolean updateShareRelation(ShareRelation shareRelation) {
		document = conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery("//" + childElement + "[@"
				+ shareFolderIdAttribut + "='"
				+ shareRelation.getSharefolderId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getAttributes().getNamedItem(mailAttribut)
					.setNodeValue(shareRelation.getMail());
			nodes.item(idx).getAttributes().getNamedItem(shareFolderIdAttribut)
					.setNodeValue(shareRelation.getSharefolderId().toString());
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
	public boolean deleteShareFolder(ShareRelation shareRelation) {
		document = conn.getXML();
		NodeList nodes = conn.executeQuery("//" + childElement + "[@"
				+ shareFolderIdAttribut + "='"
				+ shareRelation.getSharefolderId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getParentNode().removeChild(nodes.item(idx));
		}
		conn.writeToXML();
		return true;
	}

	public CRUDShareRelationInfo checkDatabase(
			List<ShareRelation> shareRelationList) {
		document = conn.getXML();
		List<ShareRelation> shareRelationSuccessList = new ArrayList<ShareRelation>();
		List<ShareRelation> shareRelationErrorList = new ArrayList<ShareRelation>();
		for (ShareRelation relation : shareRelationList) {
			NodeList nodes = conn.executeQuery("//" + childElement + "[@"
					+ shareFolderIdAttribut + "='"
					+ relation.getSharefolderId() + "']");
			if (nodes.getLength() > 0) {
				for (int i = 0; i < nodes.getLength(); i++) {
					if (nodes.item(i).getAttributes()
							.getNamedItem(mailAttribut).getNodeValue()
							.equals(relation.getMail())) {
						shareRelationSuccessList.add(relation);
					} else {
						shareRelationErrorList.add(relation);
					}
				}
			} else {
				shareRelationErrorList.add(relation);
			}
		}
		CRUDShareRelationInfo result = new CRUDShareRelationInfo();
		result.setShareRelationListError(shareRelationErrorList);
		result.setShareRelationListSuccess(shareRelationSuccessList);
		return result;

	}

}
