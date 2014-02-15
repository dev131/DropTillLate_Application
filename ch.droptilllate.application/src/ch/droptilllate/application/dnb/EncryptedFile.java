
package ch.droptilllate.application.dnb;

import java.io.File;
import java.sql.Date;

import ch.droptilllate.application.model.GhostFolderDob;

public class EncryptedFile extends DroppedElement {

	private String type;
	private Long size;
	private Integer containerID;
	protected Integer id;
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	/**
	 * Used when a new file was dropped.
	 * 
	 * @param id -> auto generate if it is null
	 * @param newFile
	 * @param parent
	 */
	public EncryptedFile(Integer id, File newFile, GhostFolderDob parent) {
		super(newFile.getName(), new Date(System.currentTimeMillis()), newFile
				.getPath().replace("\\", "/"), parent);
		this.type = defineFileType();
		this.size = newFile.length();
		this.id = id;
	}

	/**
	 * Used when the content table is being read.
	 *  
	 * @param id -> auto generate if it is null
	 * @param name
	 * @param type
	 * @param size
	 * @param date
	 * @param path
	 * @param scFileName
	 */
	public EncryptedFile(Integer id,String name, String type, long size, Date date,
			String path, Integer containerID) {
		super(name, date, path);
		this.type = type;
		this.size = size;
		this.containerID = containerID;
		this.id = id;
	}

	/**
	 * Used when the content table is being read.
	 * 
	 * @param id -> auto generate if it is null
	 * @param name
	 * @param type
	 * @param size
	 * @param date
	 * @param path
	 * @param containerID
	 * @param parent
	 */
	public EncryptedFile(Integer id, String name, String type, long size, Date date,
			String path, Integer containerID, GhostFolderDob parent) {
		super(name, date, path, parent);
		this.type = type;
		this.size = size;
		this.containerID = containerID;
		this.id = id;
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	public String getType() {
		return type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getContainerID() {
		return containerID;
	}

	public void setContainerID(Integer containerID) {
		this.containerID = containerID;
	}

	public String defineFileType() {
		String ext = "";
		if (this.name.lastIndexOf(".") > 0) {
			ext = this.name.substring(this.name.lastIndexOf(".") + 1,
					this.name.length());
		}

		return ext.toLowerCase();
	}

}
