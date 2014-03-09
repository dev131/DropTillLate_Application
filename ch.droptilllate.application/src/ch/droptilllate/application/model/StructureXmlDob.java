package ch.droptilllate.application.model;

import java.io.File;
import java.sql.Date;

import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;

public class StructureXmlDob {
	
	private EncryptedFileDob encryptedFileDob;
	private ShareFolder shareFolder;
	private ShareRelation shareRelation;
	private EncryptedContainer encryptedContainer;
	private String path;
	private String fileName;
	private Integer fileId;
	
	/**
	 * Create all Dob's for Local/ShareXml
	 * @param path
	 * @param key
	 * @param local (if set id and name from local xml)
	 */
	public StructureXmlDob(ShareFolder sharefolder, boolean local){
			if(local){
				fileName = XMLConstruct.NameLocalXML;
				fileId = Integer.parseInt(XMLConstruct.IdLocalXMLFiles);
			}
			else{
				fileName = XMLConstruct.NameShareXML;
				fileId = Integer.parseInt(XMLConstruct.IdShareXMLFiles);
			}
		encryptedFileDob = new EncryptedFileDob(fileId, 
				fileName, 
				new Date(System.currentTimeMillis()), 
				null, 
				null,  
				0l, 
				Integer.parseInt(XMLConstruct.IdXMLContainer));
		shareRelation = new ShareRelation(Messages.getIdSize(), Messages.OwnerMail);
		encryptedContainer = new EncryptedContainer(Integer.parseInt(XMLConstruct.IdXMLContainer), Messages.getIdSize());
		
	}

	public EncryptedFileDob getEncryptedFileDob() {
		return encryptedFileDob;
	}

	public ShareRelation getShareRelation() {
		return shareRelation;
	}

	public EncryptedContainer getEncryptedContainer() {
		return encryptedContainer;
	}


	
	
}
