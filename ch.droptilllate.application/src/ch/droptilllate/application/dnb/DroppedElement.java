package ch.droptilllate.application.dnb;
import java.sql.Date;

import ch.droptilllate.application.listener.DeltaEvent;
import ch.droptilllate.application.listener.IDeltaListener;
import ch.droptilllate.application.listener.NullDeltaListener;
import ch.droptilllate.application.model.GhostFolderDob;

public abstract class DroppedElement {
	protected IDeltaListener listener = NullDeltaListener.getSoleInstance();
	private GhostFolderDob parent;

	protected String name;
	protected Date date;
	protected String path;
	protected Integer id;
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	
	public DroppedElement(Integer id, String name, Date date, String path, GhostFolderDob parent) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.path = path;
		this.parent = parent;
	}
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	public void addListener(IDeltaListener listener) {
		this.listener = listener;
	}
	
	public void removeListener(IDeltaListener listener) {
		if(this.listener.equals(listener)) {
			this.listener = NullDeltaListener.getSoleInstance();
		}
	}

	protected void fireAdd(Object added) {
		listener.add(new DeltaEvent(added));
	}

	protected void fireRemove(Object removed) {
		listener.remove(new DeltaEvent(removed));
	}
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	
	public GhostFolderDob getParent() {
		return parent;
	}

	public void setParent(GhostFolderDob parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

}
