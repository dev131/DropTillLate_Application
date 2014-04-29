package ch.droptilllate.application.model;

import java.io.File;
import java.sql.Date;

import ch.droptilllate.application.dnb.TillLateContainer;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.dnb.ShareMember;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.database.api.DBSituation;

public class StructureXmlDob {
	
	private EncryptedFileDob encryptedFileDob;
	private ShareMember shareMember;
	private TillLateContainer encryptedContainer;
	private String fileName;
	private Integer fileId;
	
	/**
	 * Create all Dob's for Local/ShareXml
	 * @param path
	 * @param key
	 * @param local (if set id and name from local xml)
	 */
	public StructureXmlDob(DBSituation situation){
			if(situation == DBSituation.LOCAL_DATABASE){
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
		this.shareMember = new ShareMember(Messages.getIdSize(), Messages.OwnerMail);
		this.encryptedContainer = new TillLateContainer(Integer.parseInt(XMLConstruct.IdXMLContainer), Messages.getIdSize());
		
	}

	public EncryptedFileDob getEncryptedFileDob() {
		return this.encryptedFileDob;
	}

	public ShareMember getShareMember() {
		return this.shareMember;
	}

	public TillLateContainer getEncryptedContainer() {
		return this.encryptedContainer;
	}


	
	
}
