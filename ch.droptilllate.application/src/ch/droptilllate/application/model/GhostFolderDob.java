package ch.droptilllate.application.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import ch.droptilllate.application.dnb.DroppedElement;
import ch.droptilllate.application.listener.NullDeltaListener;

public class GhostFolderDob extends DroppedElement{

	private List<GhostFolderDob> folders;
	private List<EncryptedFileDob> files;

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	/**
	 * GhostFolderDob Constructor
	 * @param id
	 * @param name
	 * @param date
	 * @param path
	 * @param parent
	 */
	public GhostFolderDob(Integer id, String name, Date date, String path,
			GhostFolderDob parent) {
		super(id, name, date, path.replace("\\", "/"), parent);
		this.folders = new ArrayList<GhostFolderDob>();
		this.files = new ArrayList<EncryptedFileDob>();
	}	

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	public void addFolder(GhostFolderDob folder) {
		this.folders.add(folder);
		folder.setParent(this);
		fireAdd(folder);
	}

	public void addFolders(List<GhostFolderDob> folders) {
		for (GhostFolderDob folder : folders) {
			this.folders.add(folder);
			folder.setParent(this);
			fireAdd(folder);
		}
	}

	public List<GhostFolderDob> getFolders() {
		return folders;
	}

	public void removeFolder(GhostFolderDob folder) {
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