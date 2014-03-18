package ch.droptilllate.application.dnb;
import ch.droptilllate.application.listener.DeltaEvent;
import ch.droptilllate.application.listener.IDeltaListener;
import ch.droptilllate.application.listener.NullDeltaListener;
import ch.droptilllate.application.model.GhostFolderDob;

public abstract class DroppedElement {
	private IDeltaListener listener = NullDeltaListener.getSoleInstance();
	
	private GhostFolderDob parent;
	private String name;
	private Integer id;
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	
	public DroppedElement(Integer id, String name, GhostFolderDob parent) {
		this.id = id;
		this.name = name;
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
		this.listener.add(new DeltaEvent(added));
	}

	protected void fireRemove(Object removed) {
		this.listener.remove(new DeltaEvent(removed));
	}
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	
	public GhostFolderDob getParent() {
		return this.parent;
	}

	public void setParent(GhostFolderDob parent) {
		this.parent = parent;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

}
