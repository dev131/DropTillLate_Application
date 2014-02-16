package ch.droptilllate.application.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import ch.droptilllate.application.com.IXmlDatabase;
import ch.droptilllate.application.dnb.GhostFolder;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.query.GhostFolderQuery;

public class GhostFolderDao implements IXmlDatabase {

	private GhostFolderQuery folderquery;

	public List<GhostFolderDob> getFoldersInFolder(
			GhostFolderDob folder) {
		if (folderquery == null)
			folderquery = new GhostFolderQuery();
		List<GhostFolderDob> folders = new ArrayList<GhostFolderDob>();
		folders = folderquery.getFolderInFolder(folder);
		return folders;
	}

	@Override
	public Object newElement(Object obj) {
		if (folderquery == null)
			folderquery = new GhostFolderQuery();
		GhostFolder tmp = folderquery.newFolder((GhostFolder) obj);
		GhostFolderDob dob = new GhostFolderDob(tmp);
		return  dob;
	}

	@Override
	public Object getElementByID(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateElement(Object obj) {
		if (folderquery == null)
			folderquery = new GhostFolderQuery();		
		folderquery.updateFolder((GhostFolderDob) obj);
	}

	@Override
	public void deleteElement(Object obj) {	
		if (folderquery == null)
			folderquery = new GhostFolderQuery();
			folderquery.deleteFolder((List<GhostFolderDob>) obj);	
	}

	@Override
	public Object checkDatabase(Object obj) {
		if (folderquery == null)
			folderquery = new GhostFolderQuery();		
		return folderquery.checkDatabase((List<GhostFolderDob>) obj);
		
	}

}
