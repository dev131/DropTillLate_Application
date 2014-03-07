package ch.droptilllate.application.model;

import java.io.File;
import java.sql.Date;

import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.views.Messages;
import ch.droptilllate.application.views.XMLConstruct;

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
	 * @param encrypt (if set example path = TempFolder/10000.xml if not path = path)
	 */
	public StructureXmlDob(String path, String key, boolean local, boolean encrypt){
		if(encrypt){
			if(local){
				fileName = XMLConstruct.getNameLocalXML();
				this.path = path + fileName;
				fileId = Integer.parseInt(XMLConstruct.getIdLocalXMLFiles());
			}
			else{
				fileName = XMLConstruct.getNameShareXML();
				this.path = path + fileName;
				fileId = Integer.parseInt(XMLConstruct.getIdShareXMLFiles());
			}
		}
		else{
			if(local){
				this.path = path;
				fileName = XMLConstruct.getNameLocalXML();
				fileId = Integer.parseInt(XMLConstruct.getIdLocalXMLFiles());
			}
			else{
				this.path = path;
				fileName = XMLConstruct.getNameShareXML();
				fileId = Integer.parseInt(XMLConstruct.getIdShareXMLFiles());
			}
		}
		encryptedFileDob = new EncryptedFileDob(fileId, 
				fileName, 
				new Date(System.currentTimeMillis()), 
				this.path, 
				null,  
				0l, 
				Integer.parseInt(XMLConstruct.getIdXMLContainer()));
		
		shareFolder = new ShareFolder(Integer.parseInt(Messages.getShareFolder0name()), Messages.getPathDropBox(), key);
		shareRelation = new ShareRelation(Integer.parseInt(Messages.getShareFolder0name()), Messages.getOwnerMail());
		encryptedContainer = new EncryptedContainer(Integer.parseInt(XMLConstruct.getIdXMLContainer()), Integer.parseInt(Messages.getShareFolder0name()));
		
	}

	public EncryptedFileDob getEncryptedFileDob() {
		return encryptedFileDob;
	}

	public ShareFolder getShareFolder() {
		return shareFolder;
	}

	public ShareRelation getShareRelation() {
		return shareRelation;
	}

	public EncryptedContainer getEncryptedContainer() {
		return encryptedContainer;
	}


	
	
}
