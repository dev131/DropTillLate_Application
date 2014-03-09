package ch.droptilllate.application.model;

import java.sql.Date;

import ch.droptilllate.application.dnb.DroppedElement;
import ch.droptilllate.application.properties.Messages;

public class EncryptedFileDob extends DroppedElement {

	// =========================================================================
	private String type;
	private Long size;
	private Integer containerId;
	private String fullPlainpath;
	private Date date;
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
	public EncryptedFileDob(Integer id, String name, Date date, String fullPlainpath,
			GhostFolderDob parent, Long size, Integer containerId) {
		super(id, name, parent);
		this.type = defineFileType(name);
		this.size = size;
		this.containerId = containerId;
		this.fullPlainpath = fullPlainpath;
		this.date = date;
		
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
	
	public String getPath() {
		return fullPlainpath;
	}

	public void setPath(String path) {
		this.fullPlainpath = path;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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
}
