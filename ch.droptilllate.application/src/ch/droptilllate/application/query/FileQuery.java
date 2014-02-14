package ch.droptilllate.application.query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.droptilllate.application.com.IXmlConnection;
import ch.droptilllate.application.com.XmlConnection;
import ch.droptilllate.application.dnb.EncryptedFile;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.EncryptedFolderDob;
import ch.droptilllate.application.views.Messages;

public class FileQuery {
	private Document document;
	private IXmlConnection conn;
	private String rootElement = "collection";
	private String childElement = "file";

	public FileQuery() {
		conn = new XmlConnection(Messages.getFilesXMLpath(), rootElement);
	}

	/**
	 * New File Entry
	 * 
	 * @param encryptedFile
	 * @return
	 */
	public int newFile(EncryptedFile encryptedFile) {
		// TODO id generate
		int id = (int) (Math.random() * 10000 + 1);
		// Check if it exist
		while (!checkExist(id)) {
			id = (int) (Math.random() * 10000 + 1);
		}
		document = conn.getXML();
		Node node = document.getFirstChild();
		Element file = document.createElement(childElement);
		// Generate xml entry with ID
		file.setAttribute("id", Integer.toString(id));
		file.setIdAttribute("id", true);
		int parentID = encryptedFile.getParent().getId();
		file.setAttribute("name", encryptedFile.getName());
		file.setAttribute("date", encryptedFile.getDate().toString());
		file.setAttribute("size", encryptedFile.getSize().toString());
		file.setAttribute("type", encryptedFile.getType());
		file.setAttribute("path", encryptedFile.getPath());
		file.setAttribute("parentID", Integer.toString(parentID));
		file.setAttribute("containerID", "0");
		node.appendChild(file);
		conn.writeToXML();
		return id;
	}

	/**
	 * Check if it already exist
	 * 
	 * @param fileID
	 * @return
	 */
	private boolean checkExist(int fileID) {
		document = conn.getXML();
		boolean result = true;
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery("//" + childElement + "[@id='"
				+ fileID + "']");
		if (nodes == null)
			result = false;
		return result;
	}

	/**
	 * Get files from folder
	 * 
	 * @param folder
	 * @return
	 */
	public List<EncryptedFileDob> getFile(EncryptedFolderDob folder) {
		List<EncryptedFileDob> files = new ArrayList<EncryptedFileDob>();
		document = conn.getXML();
		NodeList nodes = conn.executeQuery("//" + childElement + "[@parentID='"
				+ folder.getId() + "']");
		for (int i = 0; i < nodes.getLength(); i++) {
			String date1 = nodes.item(i).getAttributes().getNamedItem("date")
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
					.getNamedItem("size").getNodeValue());
			files.add(new EncryptedFileDob(Integer.parseInt(nodes.item(i)
					.getAttributes().getNamedItem("id").getNodeValue()), nodes
					.item(i).getAttributes().getNamedItem("name")
					.getNodeValue(), nodes.item(i).getAttributes()
					.getNamedItem("type").getNodeValue(), size, sqlDate, nodes
					.item(i).getAttributes().getNamedItem("path")
					.getNodeValue(), Integer
					.parseInt(nodes.item(i).getAttributes()
							.getNamedItem("containerID").getNodeValue())));

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
		document = conn.getXML();
		NodeList nodes = conn.executeQuery("//" + childElement + "[@id='"
				+ encryptedFile.getId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			if (encryptedFile.getName() != null)
				nodes.item(idx).getAttributes().getNamedItem("name")
						.setNodeValue(encryptedFile.getName());
			if (encryptedFile.getDate() != null)
				nodes.item(idx).getAttributes().getNamedItem("date")
						.setNodeValue(encryptedFile.getDate().toString());
			if (encryptedFile.getSize() != null)
				nodes.item(idx).getAttributes().getNamedItem("size")
						.setNodeValue(encryptedFile.getSize().toString());
			if (encryptedFile.getType() != null)
				nodes.item(idx).getAttributes().getNamedItem("type")
						.setNodeValue(encryptedFile.getType());
			if (encryptedFile.getPath() != null)
				nodes.item(idx).getAttributes().getNamedItem("path")
						.setNodeValue(encryptedFile.getPath());
			if (encryptedFile.getContainerID() >= 0)
				nodes.item(idx)
						.getAttributes()
						.getNamedItem("containerID")
						.setNodeValue(
								Integer.toString(encryptedFile.getContainerID()));
			if (encryptedFile.getParent() != null)
				nodes.item(idx)
						.getAttributes()
						.getNamedItem("parentID")
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
		EncryptedFileDob encryptedFileDob = null;
		document = conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery("//" + childElement + "[@id='" + id
				+ "']");
		String date1 = nodes.item(0).getAttributes().getNamedItem("date")
				.getNodeValue().toString();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date parsed = null;
		try {
			parsed = format.parse(date1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
		encryptedFileDob = new EncryptedFileDob(Integer.parseInt(nodes.item(0)
				.getAttributes().getNamedItem("id").getNodeValue()), nodes
				.item(0).getAttributes().getNamedItem("name").getNodeValue(),
				nodes.item(0).getAttributes().getNamedItem("type")
						.getNodeValue(), Long.parseLong(nodes.item(0)
						.getAttributes().getNamedItem("size").getNodeValue()),
				sqlDate, nodes.item(0).getAttributes().getNamedItem("path")
						.getNodeValue(), Integer.parseInt(nodes.item(0)
						.getAttributes().getNamedItem("containerID")
						.getNodeValue()));
		System.out.println("Everything replaced.");
		// save xml file back
		// writeToXML();

		return encryptedFileDob;
	}

	/**
	 * Delete Files
	 * 
	 * @param encryptedFile
	 * @return
	 */
	public boolean deleteFile(EncryptedFileDob encryptedFile) {
		document = conn.getXML();
		// cast the result to a DOM NodeList
		NodeList nodes = conn.executeQuery("//" + childElement + "[@id='"
				+ encryptedFile.getId() + "']");
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			nodes.item(idx).getParentNode().removeChild(nodes.item(idx));
		}
		System.out.println("Everything replaced.");
		// save xml file back
		conn.writeToXML();

		return true;
	}
}
