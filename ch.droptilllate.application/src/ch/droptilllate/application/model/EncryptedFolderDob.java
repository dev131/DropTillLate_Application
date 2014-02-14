package ch.droptilllate.application.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import ch.droptilllate.application.dnb.EncryptedFolder;
import ch.droptilllate.application.listener.NullDeltaListener;

public class EncryptedFolderDob extends EncryptedFolder {

	private List<EncryptedFolderDob> folders;
	private List<EncryptedFileDob> files;

	private int id;

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	/**
	 * Used when new folder was added.
	 */
	public EncryptedFolderDob(int id, EncryptedFolder encryptedFolder) {
		super(encryptedFolder.getName(), encryptedFolder.getDate(),
				encryptedFolder.getPath(), encryptedFolder.getParent());
		this.id = id;
		this.folders = new ArrayList<EncryptedFolderDob>();
		this.files = new ArrayList<EncryptedFileDob>();
	}

	/**
	 * Used when the content table is being read.
	 * 
	 * @param id
	 * @param name
	 * @param date
	 * @param path
	 */
	public EncryptedFolderDob(int id, String name, Date date, String path) {
		super(name, date, path);
		this.id = id;
		this.folders = new ArrayList<EncryptedFolderDob>();
		this.files = new ArrayList<EncryptedFileDob>();
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	public void addFolder(EncryptedFolderDob folder) {
		this.folders.add(folder);
		folder.setParent(this);
		fireAdd(folder);
	}

	public void addFolders(List<EncryptedFolderDob> folders) {
		for (EncryptedFolderDob folder : folders) {
			this.folders.add(folder);
			folder.setParent(this);
			fireAdd(folder);
		}
	}

	public List<EncryptedFolderDob> getFolders() {
		return folders;
	}

	public void removeFolder(EncryptedFolderDob folder) {
		this.folders.remove(folder);
		folder.addListener(NullDeltaListener.getSoleInstance());
		fireRemove(folder);
	}

	public void addFile(EncryptedFileDob file) {
		this.files.add(file);
		file.setParent(this);
		fireAdd(file);
	}

	public void addFiles(List<EncryptedFileDob> files) {
		for (EncryptedFileDob file : files) {
			this.files.add(file);
			file.setParent(this);
			fireAdd(file);
		}
	}

	public List<EncryptedFileDob> getFiles() {
		return files;
	}

	public void removeFile(EncryptedFileDob file) {
		this.files.remove(file);
		file.addListener(NullDeltaListener.getSoleInstance());
		fireRemove(file);
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	public int size() {
		return getFolders().size() + getFiles().size();
	}

}
