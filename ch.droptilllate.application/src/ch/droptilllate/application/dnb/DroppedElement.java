package ch.droptilllate.application.dnb;
import ch.droptilllate.application.listener.DeltaEvent;
import ch.droptilllate.application.listener.IDeltaListener;
import ch.droptilllate.application.listener.NullDeltaListener;
import ch.droptilllate.application.model.GhostFolderDob;

public abstract class DroppedElement {
	protected IDeltaListener listener = NullDeltaListener.getSoleInstance();
	
	private GhostFolderDob parent;
	protected String name;
	protected Integer id;
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

}
