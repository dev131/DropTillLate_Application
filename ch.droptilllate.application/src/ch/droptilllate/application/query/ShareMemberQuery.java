package ch.droptilllate.application.query;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.droptilllate.application.dnb.ShareMember;
import ch.droptilllate.application.info.CRUDShareRelationInfo;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.application.xml.XmlConnection;

public class ShareMemberQuery {
	private Document document;
	private XmlConnection conn;

	public ShareMemberQuery(String key) {
		this.conn = new XmlConnection(true, key);	
	}

	/**
	 * New ShareRelation entry
	 * 
	 * @param encryptedFolder
	 * @return
	 */
	public ShareMember newShareMembers(ShareMember shareRelationMember) {
		this.document = this.conn.getXML();
		NodeList nodelist = this.document.getElementsByTagName(XMLConstruct.RootElementShareMember);
		Node node = nodelist.item(0);
		Element folder = this.document.createElement(XMLConstruct.ChildElementShareMember);
		folder.setAttribute(XMLConstruct.AttShareRelationID,
				Integer.toString(shareRelationMember.getShareRelationId()));
		folder.setAttribute(XMLConstruct.AttMail, shareRelationMember.getMail());
		node.appendChild(folder);
		this.conn.writeToXML();
		return shareRelationMember;
	}

	public List<ShareMember> getShareRelations(int shareRelationId) {
		List<ShareMember> shareRelations = new ArrayList<ShareMember>();
		this.document = this.conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = this.conn.executeQuery(XMLConstruct.getShareMemberExpression()+"[@"
				+ XMLConstruct.AttShareRelationID + "='" + shareRelationId + "']");
		for (int i = 0; i < nodes.getLength(); i++) {
			//Integer sharefolderId, String mail
			ShareMember tmp = new ShareMember(Integer.parseInt(nodes.item(i).getAttributes()
					.getNamedItem(XMLConstruct.AttShareRelationID).getNodeValue()),
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
	public boolean updateShareRelationMember(ShareMember shareMember) {
		this.document = this.conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = this.conn.executeQuery(XMLConstruct.getShareMemberExpression()+ "[@"
				+ XMLConstruct.AttShareRelationID + "='"
				+ shareMember.getShareRelationId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttMail)
					.setNodeValue(shareMember.getMail());
			nodes.item(idx).getAttributes().getNamedItem(XMLConstruct.AttShareRelationID)
					.setNodeValue(shareMember.getShareRelationId().toString());
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
	public boolean deleteShareFolder(ShareMember shareRelation) {
		this.document = this.conn.getXML();
		NodeList nodes = this.conn.executeQuery(XMLConstruct.getShareMemberExpression()+ "[@"
				+ XMLConstruct.AttShareRelationID + "='"
				+ shareRelation.getShareRelationId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getParentNode().removeChild(nodes.item(idx));
		}
		this.conn.writeToXML();
		return true;
	}

	public CRUDShareRelationInfo checkDatabase(
			List<ShareMember> shareRelationList) {
		this.document = this.conn.getXML();
		List<ShareMember> shareRelationSuccessList = new ArrayList<ShareMember>();
		List<ShareMember> shareRelationErrorList = new ArrayList<ShareMember>();
		for (ShareMember relation : shareRelationList) {
			NodeList nodes = this.conn.executeQuery(XMLConstruct.getShareMemberExpression()+ "[@"
					+ XMLConstruct.AttShareRelationID + "='"
					+ relation.getShareRelationId()+ "']");
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

	public List<ShareMember> getAll() {
		document = conn.getXML();
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.ChildElementShareMember);
		List<ShareMember> shareList = new ArrayList<ShareMember>();
		for (int idx = 0; idx < nodelist.getLength(); idx++) {
			ShareMember tmp = new ShareMember(
					Integer.parseInt(nodelist.item(idx).getAttributes().getNamedItem(XMLConstruct.AttShareRelationID).getNodeValue()),
					nodelist.item(idx).getAttributes().getNamedItem(XMLConstruct.AttMail).getNodeValue()
					);
			shareList.add(tmp);
		}
		return shareList;
	}

	public Object getElementByName(String email) {
		List<ShareMember> shareRelations = new ArrayList<ShareMember>();
		this.document = this.conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = this.conn.executeQuery(XMLConstruct.getShareMemberExpression()+"[@"
				+ XMLConstruct.AttMail + "='" + email + "']");
		for (int i = 0; i < nodes.getLength(); i++) {
			//Integer sharefolderId, String mail
			ShareMember tmp = new ShareMember(Integer.parseInt(nodes.item(i).getAttributes()
					.getNamedItem(XMLConstruct.AttShareRelationID).getNodeValue()),
					nodes.item(i).getAttributes()
					.getNamedItem(XMLConstruct.AttMail).getNodeValue()
					);
			shareRelations.add(tmp);
		}
		return shareRelations;
	}

}
