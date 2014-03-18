package ch.droptilllate.application.provider;

import java.util.Iterator;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import ch.droptilllate.application.dnb.DroppedElement;
import ch.droptilllate.application.listener.DeltaEvent;
import ch.droptilllate.application.listener.IDeltaListener;
import ch.droptilllate.application.model.GhostFolderDob;

public class DropTillLateContentProvider implements ITreeContentProvider,
		IDeltaListener {

	private static Object[] EMPTY_ARRAY = new Object[0];
	private TreeViewer viewer;

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer) viewer;
		if (oldInput != null) {
			removeListenerFrom((GhostFolderDob) oldInput);
		}
		if (newInput != null) {
			addListenerTo((GhostFolderDob) newInput);
		}
	}

	/**
	 * Because the domain model does not have a richer listener model,
	 * recursively remove this listener from each child box of the given box.
	 */
	protected void removeListenerFrom(GhostFolderDob folder) {
		folder.removeListener(this);
		for (Iterator iterator = folder.getFolders().iterator(); iterator
				.hasNext();) {
			GhostFolderDob aFolder = (GhostFolderDob) iterator.next();
			removeListenerFrom(aFolder);
		}
	}

	/**
	 * Because the domain model does not have a richer listener model,
	 * recursively add this listener to each child box of the given box.
	 */
	protected void addListenerTo(GhostFolderDob folder) {
		folder.addListener(this);
		for (Iterator iterator = folder.getFolders().iterator(); iterator
				.hasNext();) {
			GhostFolderDob aFolder = (GhostFolderDob) iterator.next();
			addListenerTo(aFolder);
		}
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof GhostFolderDob) {
			GhostFolderDob encryptedFolder = (GhostFolderDob) parentElement;
			return concat(encryptedFolder.getFolders().toArray(),
					encryptedFolder.getFiles().toArray());
		}
		return EMPTY_ARRAY;
	}

	protected Object[] concat(Object[] folders, Object[] files) {
		Object[] both = new Object[folders.length + files.length];
		System.arraycopy(folders, 0, both, 0, folders.length);
		System.arraycopy(files, 0, both, folders.length, files.length);
		return both;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof DroppedElement) {
			return ((DroppedElement) element).getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	@Override
	public void add(DeltaEvent event) {
		Object folder = ((DroppedElement) event.receiver()).getParent();
		this.viewer.refresh(folder, false);
	}

	@Override
	public void remove(DeltaEvent event) {
		add(event);
	}
}
