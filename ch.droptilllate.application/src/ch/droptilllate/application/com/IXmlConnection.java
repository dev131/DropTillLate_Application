package ch.droptilllate.application.com;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public interface IXmlConnection {
	/**
	 * Get Document
	 * 
	 * @return Document
	 */
	public Document getXML();

	/**
	 * writeToXML
	 */
	public void writeToXML();

	/**
	 * Execute Query
	 * 
	 * @param query
	 *            String
	 * @return NodeList
	 */
	public NodeList executeQuery(String query);

}
