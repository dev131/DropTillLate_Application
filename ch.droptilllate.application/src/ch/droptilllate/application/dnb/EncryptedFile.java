/**
 * Date: 26.10.2010
 * SteadyCrypt v2 Project by Joerg Harr and Marvin Hoffmann
 *
 */

package ch.droptilllate.application.dnb;

import java.io.File;
import java.sql.Date;

import ch.droptilllate.application.model.EncryptedFolderDob;

public class EncryptedFile extends DroppedElement {

	private String type;
	private Long size;
	private int containerID;

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	/**
	 * Used when a new file was dropped.
	 * 
	 * @param newFile
	 * @param parent
	 */
	public EncryptedFile(File newFile, EncryptedFolderDob parent) {
		super(newFile.getName(), new Date(System.currentTimeMillis()), newFile
				.getPath().replace("\\", "/"), parent);
		this.type = defineFileType();
		this.size = newFile.length();
	}

	/**
	 * Used when the content table is being read.
	 * 
	 * @param name
	 * @param type
	 * @param size
	 * @param date
	 * @param path
	 * @param scFileName
	 */
	public EncryptedFile(String name, String type, long size, Date date,
			String path, int containerID) {
		super(name, date, path);
		this.type = type;
		this.size = size;
		this.containerID = containerID;
	}

	/**
	 * Used when the content table is being read.
	 * 
	 * @param name
	 * @param type
	 * @param size
	 * @param date
	 * @param path
	 * @param containerID
	 * @param parent
	 */
	public EncryptedFile(String name, String type, long size, Date date,
			String path, int containerID, EncryptedFolderDob parent) {
		super(name, date, path, parent);
		this.type = type;
		this.size = size;
		this.containerID = containerID;
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

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

	public int getContainerID() {
		return containerID;
	}

	public void setContainerID(int containerID) {
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
