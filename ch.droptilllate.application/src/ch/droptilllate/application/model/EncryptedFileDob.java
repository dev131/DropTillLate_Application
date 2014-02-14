/**
 * Date: 01.11.2010
 * SteadyCrypt v2 Project by Joerg Harr and Marvin Hoffmann
 *
 */

package ch.droptilllate.application.model;

import java.sql.Date;

import ch.droptilllate.application.dnb.EncryptedFile;

public class EncryptedFileDob extends EncryptedFile {

	private int id;

	// =========================================================================

	/**
	 * Used when new file was added.
	 */
	public EncryptedFileDob(int id, EncryptedFile encryptedFile) {
		super(encryptedFile.getName(), encryptedFile.getType(), encryptedFile
				.getSize(), encryptedFile.getDate(), encryptedFile.getPath(),
				encryptedFile.getContainerID(), encryptedFile.getParent());
		this.id = id;
	}

	/**
	 * Used when the content table is being read.
	 * 
	 * @param id
	 * @param name
	 * @param type
	 * @param size
	 * @param date
	 * @param path
	 * @param file
	 */
	public EncryptedFileDob(int id, String name, String type, long size,
			Date date, String path, int containerID) {
		super(name, type, size, date, path, containerID);
		this.id = id;
	}

	// =========================================================================

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
