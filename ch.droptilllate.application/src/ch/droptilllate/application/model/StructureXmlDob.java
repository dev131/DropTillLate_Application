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
				this.fileName = XMLConstruct.NameLocalXML;
				this.fileId = Integer.parseInt(XMLConstruct.IdLocalXMLFiles);
			}
			else{
				this.fileName = XMLConstruct.NameShareXML;
				this.fileId = Integer.parseInt(XMLConstruct.IdShareXMLFiles);
			}
		this.encryptedFileDob = new EncryptedFileDob(this.fileId, 
				this.fileName, 
				new Date(System.currentTimeMillis()), 
				null, 
				null,  
				0L, 
				Integer.parseInt(XMLConstruct.IdXMLContainer));
		this.shareRelation = new ShareRelation(Messages.getIdSize(), Messages.OwnerMail);
		this.encryptedContainer = new EncryptedContainer(Integer.parseInt(XMLConstruct.IdXMLContainer), Messages.getIdSize());
		
	}

	public EncryptedFileDob getEncryptedFileDob() {
		return this.encryptedFileDob;
	}

	public ShareRelation getShareRelation() {
		return this.shareRelation;
	}

	public EncryptedContainer getEncryptedContainer() {
		return this.encryptedContainer;
	}


	
	
}
