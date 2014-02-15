
package ch.droptilllate.application.dnb;

import java.io.File;
import java.sql.Date;

import ch.droptilllate.application.model.GhostFolderDob;

public class GhostFolder extends DroppedElement {

	private Integer id;
	/**
	 * Used when a new folder was dropped.
	 * 
	 * @param id -> auto generate if it is null
	 * @param newFile
	 * @param parent
	 */
	public GhostFolder(Integer id, File newFile, GhostFolderDob parent) {
		super(newFile.getName(), new Date(System.currentTimeMillis()), newFile
				.getPath().replace("\\", "/"), parent);
		this.id = id;
	}

	/**
	 * Used when the content table is being read.
	 * 
	 * @param name
	 * @param date
	 * @param path
	 */
	public GhostFolder(Integer id, String name, Date date, String path) {
		super(name, date, path);
		this.id = id;
	}

	/**
	 * Used when the content table is being read.
	 * 
	 * @param name
	 * @param date
	 * @param path
	 * @param parent
	 */
	public GhostFolder(Integer id, String name, Date date, String path,
			GhostFolderDob parent) {
		super(name, date, path, parent);
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

}
