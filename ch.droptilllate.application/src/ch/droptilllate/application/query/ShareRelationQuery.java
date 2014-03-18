package ch.droptilllate.application.query;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.info.CRUDShareRelationInfo;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.application.xml.XmlConnection;

public class ShareRelationQuery {
	private Document document;
	private XmlConnection conn;

	public ShareRelationQuery(String key) {
		this.conn = new XmlConnection(true, key);	
	}

	/**
	 * New ShareFolder entry
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public ShareRelation newShareRelation(ShareRelation shareRelation) {
		this.document = this.conn.getXML();
		NodeList nodelist = this.document.getElementsByTagName(XMLConstruct.RootElementShareRelation);
		Node node = nodelist.item(0);
		Element folder = this.document.createElement(XMLConstruct.ChildElementShareRelation);
		folder.setAttribute(XMLConstruct.AttShareFolderId,
				Integer.toString(shareRelation.getSharefolderId()));
		folder.setAttribute(XMLConstruct.AttMail, shareRelation.getMail());
		node.appendChild(folder);
		this.conn.writeToXML();
		return shareRelation;
	}

	public List<ShareRelation> getShareRelations(int shareRelationId) {
		List<ShareRelation> shareRelations = new ArrayList<ShareRelation>();
		this.document = this.conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = this.conn.executeQuery(XMLConstruct.getShareRelationExpression()+"[@"
				+ XMLConstruct.AttShareFolderId + "='" + shareRelationId + "']");
		for (int i = 0; i < nodes.getLength(); i++) {
			//Integer sharefolderId, String mail
			ShareRelation tmp = new ShareRelation(Integer.parseInt(nodes.item(i).getAttributes()
					.getNamedItem(XMLConstruct.AttShareFolderId).getNodeValue()),
					nodes.item(i).getAttributes()
					.getNamedItem(XMLConstruct.AttMail).getNodeValue()
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
		this.document = this.conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = this.conn.executeQuery(XMLConstruct.getShareRelationExpression()+ "[@"
				+ XMLConstruct.AttShareFolderId + "='"
				+ shareRelation.getSharefolderId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttMail)
					.setNodeValue(shareRelation.getMail());
			nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttShareFolderId)
					.setNodeValue(shareRelation.getSharefolderId().toString());
		}
		System.out.println("Everything updated.");
		// save xml file back
		this.conn.writeToXML();
		return true;
	}

	/**
	 * DeleteFolder
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public boolean deleteShareFolder(ShareRelation shareRelation) {
		this.document = this.conn.getXML();
		NodeList nodes = this.conn.executeQuery(XMLConstruct.getShareRelationExpression()+ "[@"
				+ XMLConstruct.AttShareFolderId + "='"
				+ shareRelation.getSharefolderId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getParentNode().removeChild(nodes.item(idx));
		}
		this.conn.writeToXML();
		return true;
	}

	public CRUDShareRelationInfo checkDatabase(
			List<ShareRelation> shareRelationList) {
		this.document = this.conn.getXML();
		List<ShareRelation> shareRelationSuccessList = new ArrayList<ShareRelation>();
		List<ShareRelation> shareRelationErrorList = new ArrayList<ShareRelation>();
		for (ShareRelation relation : shareRelationList) {
			NodeList nodes = this.conn.executeQuery(XMLConstruct.getShareRelationExpression()+ "[@"
					+ XMLConstruct.AttShareFolderId + "='"
					+ relation.getSharefolderId() + "']");
			if (nodes.getLength() > 0) {
				for (int i = 0; i < nodes.getLength(); i++) {
					if (nodes.item(i).getAttributes()
							.getNamedItem(XMLConstruct.AttMail).getNodeValue()
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

	public List<ShareRelation> getAll() {
		document = conn.getXML();
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.ChildElementShareRelation);
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
