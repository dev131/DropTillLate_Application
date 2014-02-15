/**
 * Date: 01.11.2010
 * SteadyCrypt v2 Project by Joerg Harr and Marvin Hoffmann
 *
 */

package ch.droptilllate.application.model;

import java.sql.Date;

import ch.droptilllate.application.dnb.EncryptedFile;

public class EncryptedFileDob extends EncryptedFile {

	// =========================================================================

	/**
	 * Used when new file was added.
	 */
	public EncryptedFileDob(EncryptedFile encryptedFile) {
		super(encryptedFile.getId(),encryptedFile.getName(), encryptedFile.getType(), encryptedFile
				.getSize(), encryptedFile.getDate(), encryptedFile.getPath(),
				encryptedFile.getContainerID(), encryptedFile.getParent());
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
	public EncryptedFileDob(Integer id, String name, String type, long size,
			Date date, String path, int containerID) {
		super(id,name, type, size, date, path, containerID);
	}

	// =========================================================================

}
