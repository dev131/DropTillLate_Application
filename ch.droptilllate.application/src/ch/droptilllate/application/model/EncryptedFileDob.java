package ch.droptilllate.application.model;

import java.sql.Date;

import ch.droptilllate.application.dnb.DroppedElement;
import ch.droptilllate.application.views.Messages;
import ch.droptilllate.filesystem.commons.Constants;

public class EncryptedFileDob extends DroppedElement {

	// =========================================================================
	private String type;
	private Long size;
	private Integer containerId;

	/**
	 * EncryptedFileDob Contructor
	 * @param id
	 * @param name
	 * @param date
	 * @param path
	 * @param parent
	 * @param size
	 * @param containerId
	 */
	public EncryptedFileDob(Integer id, String name, Date date, String path,
			GhostFolderDob parent, Long size, Integer containerId) {
		super(id, name, date, path.replace("\\", "/"), parent);
		this.type = defineFileType(name);
		this.size = size;
		this.containerId = containerId;
		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Integer getContainerId() {
		return containerId;
	}

	public void setContainerId(Integer containerId) {
		this.containerId = containerId;
	}

	// =========================================================================

	public String defineFileType(String name) {
		String ext = "";
		if (this.name.lastIndexOf(".") > 0) {
			ext = this.name.substring(this.name.lastIndexOf(".") + 1,
					this.name.length());
		}

		return ext.toLowerCase();
	}
		
	/**
	 * Get File Path from Filesystem if it is the first Drop
	 * @return String /User/testuser/documents/example.txt
	 */
	public String getFileSystemPath(){
		return super.getPath();
	}
	/**
	 * Get ShareFolder Path
	 * @return String Dropbox/DropTillLate/sharedFolder0
	 */
	public String getShareRelationPath(){
		return super.getPath();
	}
	/**
	 * Get Container Path
	 * @return String Dropbox/DropTillLate/sharedFolder0/22222.tilllate
	 */
	public String getContainerPath(){
		return super.getPath() + this.containerId  + "." + Constants.CONTAINER_EXTENTION;
	}
	/**
	 * Get Full Container Path in EncryptedContainer
	 * @return String Dropbox/DropTillLate/sharedFolder0/22222.tilllate/1111.xml
	 */
	public String getFullPlainPath(){
		return super.getPath() + this.containerId  + "." + Constants.CONTAINER_EXTENTION +"/"+ super.getId() +"."+ this.getType();
	}
	
	/**
	 * Get Container Path
	 * @return String Dropbox/DropTillLate/TempFolder/1111.xml
	 */
	public String getTempPlainPath(){
		return Messages.getPathLocalTemp() + super.getId() +"."+ this.getType();
	}
	
	


}
