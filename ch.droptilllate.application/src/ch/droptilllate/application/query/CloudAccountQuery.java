package ch.droptilllate.application.query;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.droptilllate.application.dnb.CloudAccount;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.application.xml.XmlConnection;

public class CloudAccountQuery {

	
	private XmlConnection conn;
	
	public CloudAccountQuery(String key) {
		this.conn = new XmlConnection(true, key);
	}
	
	/**
	 * Insert new CloudAccount
	 * @param username
	 * @param password
	 */
	public CloudAccount newCloudAccount(CloudAccount account){
		
		Document document = this.conn.getXML();
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.RootElementCloudAccount);
		Node node = nodelist.item(0);
		//GetNodeList by name
		for(int i=0; i<nodelist.getLength(); i++){
			  Node childNode = nodelist.item(i);
			  if (childNode.getNodeName().equals(XMLConstruct.RootElementContainer)) {
			     node = nodelist.item(i);
			  }
			}
		Element element = document.createElement(XMLConstruct.ChildElementCloudAccount);
		element.setAttribute(XMLConstruct.AttCloudUsername, account.getUsername());
		element.setAttribute(XMLConstruct.AttCloudPassword,account.getPassword());
		node.appendChild(element);
		this.conn.writeToXML();
		return account;
	}
	
	public CloudAccount getAccount(){		
		CloudAccount account = null;
		Document document = conn.getXML();
		NodeList nodelist = document.getElementsByTagName(XMLConstruct.ChildElementCloudAccount);
		for (int idx = 0; idx < nodelist.getLength(); idx++) {
			 account = new CloudAccount(
					nodelist.item(idx).getAttributes().getNamedItem(XMLConstruct.AttCloudUsername).getNodeValue(),
					nodelist.item(idx).getAttributes().getNamedItem(XMLConstruct.AttCloudPassword).getNodeValue()
					);			
		}
		return account;
	}

}
