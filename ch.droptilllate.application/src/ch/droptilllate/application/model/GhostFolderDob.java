package ch.droptilllate.application.model;

import java.util.ArrayList;
import java.util.List;

import ch.droptilllate.application.dnb.DroppedElement;
import ch.droptilllate.application.listener.NullDeltaListener;
import ch.droptilllate.application.properties.Messages;

public class GhostFolderDob extends DroppedElement{

	private List<GhostFolderDob> folders;
	private List<EncryptedFileDob> files;

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	/**
	 * GhostFolderDob Constructor
	 * @param id
	 * @param name
	 * @param parent
	 */
	public GhostFolderDob(Integer id, String name,
			GhostFolderDob parent) {
		super(id, name, parent);
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
		return this.folders;
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
		return this.files;
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
